
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Message_Thread implements Runnable
{
    private SSLSocket sock;
    private final int Seconds_To_Execute = 120 ;
    private static int i = 0 ;
    private boolean Flag = false, Flag2 = false ;
    private Object Obj1 = null ;
    //to resource tou server
    private volatile static ArrayList < Complete_Client > List = new ArrayList < Complete_Client > ( ) ;
    private volatile static ArrayList < Client_Message > List0 = new ArrayList < Client_Message > ( ) ;
    private volatile static ArrayList < Scheduled_Future_With_Name > List_Tasks = new ArrayList < Scheduled_Future_With_Name > ( ) ;
    private ScheduledExecutorService executorFuture = Executors.newScheduledThreadPool ( 1 ) ;
    private ScheduledFuture sf ;
    private ObjectOutputStream OOS ;
    private ObjectInputStream OIS ;

    
    //In case a heartbeat doesnt arrive in the timeline <2min> for a Client
    public static void Delete_Client_List ( String t ) throws IOException  //t is a Client name
    {
        for ( int k = 0 ;  k < List.size ( ) ;  k ++ )
        {
            if ( ( List.get ( k ).Get_Client_Name ( ) ).equals ( t ) )
            {  //2 deletes, 2 lists. Only 1 returns to Client however
                String name0 = List.get ( k ) .Get_Client_Name ( ) ;
                List.remove ( List.get ( k ) ) ; //remove him from Client List, but also remove his socket, deallocate down/up streams
                System.out.println ( "\n\n\n\n\n *** Client doesnt send Heartbeat - Delete_Now : " + name0 + "***" ) ;
                System.out.println ( "Client : " + name0 + " just deleted !!" ) ;
                System.out.println ( "\n\n\n\n\n" ) ;
                //break maybe
            }
        }
        for ( int j = 0 ;  j < List0.size ( ) ;  j ++ )
        {
            if ( List0.get ( j ).Get_Name ( ).equals ( t ) )
            {
                List0.remove ( List0.get ( j ) ) ;
            } 
        }
    }
    
    
    //Constructor
    public Message_Thread ( SSLSocket sock )
    {
        this.sock = sock ;
    }
    
    
    //In Case a Heartbeat arrive
    public void Refresh_List ( Heartbeat Op , ObjectInputStream ois, ObjectOutputStream oos ) throws IOException
    {
       synchronized ( this )
       {
            while ( Flag2 == true )
            {
                try
                {
                  wait ( ) ;
                }
                catch ( Exception m )
                {
                    
                }
            }
            Flag2 = true ;
            //Search into List_Tasks to find the Name and in this index cancel the Delete_Action for this Client
            for ( int j = 0 ;   j < List_Tasks.size ( ) ;   j ++ )
            {
               if ( Op.Get_nick ( ).equals ( List_Tasks.get ( j ).Get_Name ( ) ) )
               {
                   ( List_Tasks.get ( j ).Get_SF ( ) ).cancel ( true ) ;
                   List_Tasks.remove ( List_Tasks.get ( j ) ) ;
               }
            }
            //When a heartbeat arrive, then cancel the delete, but schedule a new delete action in 2 min from now
            sf = executorFuture.schedule ( new Scheduled_Heartbeat ( Op.Get_nick ( )  ), Seconds_To_Execute, TimeUnit.SECONDS ) ;
            List_Tasks.add ( new Scheduled_Future_With_Name ( sf, Op.Get_nick ( )) ) ;  //add the delete action in Lists_Tasks
            //send current List to Client except from his Name
            ArrayList < Client_Message > List2 = new ArrayList < Client_Message > ( ) ;
            for ( int h = 0 ;    h < List0.size ( ) ;   h ++ )
            {
                if ( ! List0.get ( h ).Get_Name ( ).equals ( Op.Get_nick ( ) ) )
                {
                   List2.add ( List0.get ( h ) ) ;   
                }
            }
            oos.writeObject ( List2 ) ; //now send
            List2.clear ( ) ;
            Flag2 = false ;
            notifyAll ( ) ;
       }
    }
    
    
    
    //Client first time connests to server sending NickName and the I2P_Destination
    public void Register_To_List ( Client_Message Ob , SSLSocket socket0,  ObjectInputStream ois, ObjectOutputStream oos ) throws IOException
    {
        synchronized ( this )
        {
            while ( Flag == true )
            {
                try
                {
                  wait ( ) ;
                }
                catch ( Exception m )
                {
                    
                }
            }
            Flag = true ;
            oos.writeObject ( this.List0 ) ;
            //send list but not himself rest people
            //Do the register
            List.add ( new Complete_Client ( Ob.Get_Name( ), Ob.Get_I2P ( ), socket0, oos, ois ) ) ;  //to delete. Reason of this
            //List
            List0.add ( Ob ) ;
            Flag = false ;
            //And send List
            notifyAll ( ) ;
        }
    }
    
    
    
    
    public void run ( )
    {
        try
        {
            OOS = new ObjectOutputStream ( this.sock.getOutputStream ( ) ) ;
            OIS = new ObjectInputStream ( this.sock.getInputStream ( ) ) ;
           while ( true )
           {
            try
            {
                Obj1 = OIS.readObject ( ) ;  //diavase to antikeimeno pou sou esteile o pelatis gia na deis ti einai
                if ( Obj1 instanceof Heartbeat )  //an einai tupou klasis Heattbeat, tote tha kaleseis ti methodo poy einai armodia gia nakanei ayth th douleia
                {
                    System.out.println ( "Client Heartbeat -> " + ( ( Heartbeat ) Obj1 ).Get_nick ( ) ) ;
                    System.out.println ( "\n\n\n" ) ;
                    //System.out.println ( "\n\n\n\n" ) ;
                    Refresh_List ( ( ( Heartbeat ) Obj1 ), OIS, OOS ) ;
                }
                else if ( Obj1 instanceof Client_Message )   //allios o pelatis mou stlenei ena antikeimeno pou mesa exei ena string gia to i2p destination tou kai ena nickname
                {
                    System.out.println ( ( i + 1 ) + ". Client arrived."  ) ;
                    i ++ ;
                    System.out.println ( "Client registered -> " + ( ( Client_Message ) Obj1 ).toString ( ) )  ;
                    System.out.println ( "\n\n\n\n" )  ;
                    //System.out.println ( "\n\n\n\n" ) ;
                    Register_To_List ( ( Client_Message ) Obj1, sock, OIS, OOS ) ;   //me cast
                    //In 2 min from now i will schedule delete-action for this  client. Until a heartbeat arrived from same Client in timeline
                    sf = executorFuture.schedule ( new Scheduled_Heartbeat ( ( ( Client_Message ) Obj1 ).Get_Name ( ) ), Seconds_To_Execute, TimeUnit.SECONDS ) ;
                    //Returns a ScheduleFuture Object that we can manipulate, so we can cancel it if heartbeat arrive
                    //We add the ScheduleFuture in a list with the name of Client. This is happened in order to cancel a delete-action based on the Client Name
                    List_Tasks.add ( new Scheduled_Future_With_Name ( sf, ( ( Client_Message ) Obj1 ).Get_Name ( ) ) ) ;
                }
            }
            catch ( ClassNotFoundException CNFE )
            {
                System.out.println ( "ClassNotFoundException - " + CNFE.getLocalizedMessage ( ) ) ;
            }
            catch ( IOException IOE )
            {
                System.out.println ( "IOException1 - " + IOE.getLocalizedMessage ( ) ) ;
                System.out.println ( " Client Just Closed App.." + ( ( Heartbeat ) Obj1 ).Get_nick(  ) ) ;
                break ; //finish this thread
            }
           }//end of loop
        } 
        catch (IOException ex) 
        {
           System.out.println ( "IOException2 - " + ex.getLocalizedMessage ( ) ) ;
        }
      //SAME SOCKET FOR EVERY CLIENT. So 1 accept for 1 Client.
    }    
}
