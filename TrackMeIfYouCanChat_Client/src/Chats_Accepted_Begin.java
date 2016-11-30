
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Chats_Accepted_Begin implements Runnable
{
    
    private I2PSocket sock ;
    private String dest ;
    private I2PSocketManager Socket_Manager = null ;
    private Destination Server_Destination = null ;
    private I2PSocket i2pSocket = null ;
    private String realName = null ;
    
    
    
    public Chats_Accepted_Begin ( I2PSocket sock )
    {
        this.sock = sock ;
    }
    
    
    
    
    public void run ( )
    {
        try
        {
           ObjectOutputStream OOS = new ObjectOutputStream ( this.sock.getOutputStream ( ) ) ;
           ObjectInputStream OIS = new ObjectInputStream ( this.sock.getInputStream ( ) ) ;
           
           Message MessageSpecial = ( Message ) OIS.readObject ( ) ; 
           //SOS 2 OBJCETS gia na ksexoriso an thelei na milisei mazi mou kai na erxetai os client se mena piso
         if ( MessageSpecial.Get_Message ( ).equals ( "SomeBodyWannaChat" ) )
         {
           //wait and read first message  from friend who want to chat with you
           Message m1 = ( Message ) OIS.readObject ( ) ; //your name 
           System.out.println ( "Client with " +m1.Get_Message ( ) + ", wanna talk with me " ) ;
           String name = m1.Get_Message ( ) ;
           //split to find name
           String [ ] realname = name.split ( "_" ) ;
           realName = realname [ 0 ] ;
           int Answer = JOptionPane.showConfirmDialog ( null,  "Accept chat with <" + realName + "> ?", "Friend want to chat..", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) ;
           if ( Answer == JOptionPane.YES_OPTION )
           {  
               //send him an ack
               OOS.writeObject ( new Message ( "AcceptChat" ) ) ; 
               //then read i2p dest from friend who wants to chat with you
               Message mi2pdest = ( Message ) OIS.readObject ( ) ;  //read destination
               //AFTER HERE EVERY READOBJECT IS FOR THE CHAR MESSAGES. so....oti m stelnei oti kano read dld se mia while apla to kano display sti Lista
               
               //-------------------------------------------------------------------------------------------------------------------------------------
               //-------------------------------------------------------------------------------------------------------------------------------------
               //-------------------------------------------------------------------------------------------------------------------------------------
               //now i become client and  establish connection with friend who wants to chat with me. he was client. No me client
               //and him is a server side find server target friend serversocket keep channel open
               Socket_Manager = I2PSocketManagerFactory.createManager ( ) ;
               try 
               {
                  //Destination - Defines an end point in the I2P network 
                  Server_Destination = new Destination ( mi2pdest.Get_Message ( ) ) ;  //String of server
               } 
               catch ( DataFormatException ex ) 
               {
                  System.out.println ( "Destination string incorrectly formatted." ) ;
               }
                //
               try 
               {
                  //connect  Create a new connected i2p socket (block until the socket is created) Parameters: Destination to connect to
                  //I2PSocket - Interaface . Streaming socket returned by I2PSocketManager.connect(Destination).
                  i2pSocket = Socket_Manager.connect ( Server_Destination ) ;
                  //Channel  open channel ,to send you messages
                  ObjectOutputStream OOS0 = new ObjectOutputStream ( i2pSocket.getOutputStream ( ) ) ;
                  ObjectInputStream OIS0 = new ObjectInputStream ( i2pSocket.getInputStream ( ) ) ;
                  //Just send a message for saperate operation
                  OOS0.writeObject ( new Message ( "NowMeClientYouServer_" + Main_Window.Get_MyName ( ) ) ) ;   //kane parse kanonika ayto kai vale to sto 
                  //open gui to write and send messages
                  Main_Window.Set_Chat_GUI ( new Chat_Window ( realName, OOS0, OIS0, i2pSocket ) ) ;    //write
                 //open thread to write and display messages
                  ExecutorService Executor = Executors.newCachedThreadPool ( ) ;  //to onoma tou pelati pou thelo na miliso eksarxis
                  Executor.execute ( new Display_Incoming_Messages ( OIS, OOS, realName ) ) ;  //read
               }
               catch ( I2PException I2P )
               {
                   
               }
               catch ( IOException IOE )
               {
             
               }
           }
           else
           {
               OOS.writeObject ( new Message ( "DeclineChat" ) ) ; 
               this.sock.close ( ) ;
               OOS.close ( ) ;
               OIS.close ( ) ;  
           }
         }//End IF
         else 
         {
             String [ ] realname2 = MessageSpecial.Get_Message ( ).split ( "_" ) ;
             realName = realname2 [ 1 ] ;
             //Keep Socket Open so now we have 2 separate channels . Alice as Client & Bos as a Server       AND       Alice as a sServer and Bob as a Client
             //open thread and messages that  friend sends to you display in the
             ExecutorService Executor1 = Executors.newCachedThreadPool ( ) ;
             Executor1.execute ( new Display_Incoming_Messages ( OIS, OOS, realName ) ) ;  //read
         }
        }
        catch ( ClassNotFoundException CNFE )
        {
            
        }
        catch ( IOException IOE )
        {
            
        }
    }
    
    
    
    
}
