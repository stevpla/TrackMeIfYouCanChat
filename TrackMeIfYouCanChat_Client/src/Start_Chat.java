
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
public class Start_Chat extends JFrame implements Runnable 
{
    private String dest ;
    private I2PSocketManager Socket_Manager = null ;
    private Destination Server_Destination = null ;
    private I2PSocket i2pSocket = null ;
    private String Name ; 
    
    
    
    public Start_Chat ( String dest, String name )
    {
       this.dest = dest ;
       Name = name ;
       System.out.println ( "i2p that you want to talk is -> " + this.dest ) ;
    }
    
    
    
    
    public void run ( )
    {
         //I2PSocketManagerFactory - Simplify the creation of I2PSession and transient I2P Destination objects if necessary to create a socket manager
         //createManager - Create a socket manager using a brand new destination connected to the I2CP router on the local machine on the default port (7654).
         //I2PSocketManager - Centralize the coordination and multiplexing of the local client's streaming. There should be one I2PSocketManager for each I2PSession
         Socket_Manager = I2PSocketManagerFactory.createManager ( ) ;
         try 
         {
             //Destination - Defines an end point in the I2P network 
            Server_Destination = new Destination ( this.dest ) ;  //String of server
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
            //Begin Protocol
            //-------------
            
            //Channel
            ObjectOutputStream OOS = new ObjectOutputStream ( i2pSocket.getOutputStream ( ) ) ;
            ObjectInputStream OIS = new ObjectInputStream ( i2pSocket.getInputStream ( ) ) ;
            
            //
            OOS.writeObject ( new Message ( "SomeBodyWannaChat" ) ) ;
            //
            //Write to Target friend who want to messahe-chat
            OOS.writeObject ( new Message ( Main_Window.Get_MyName ( ) + "_WannaChat" ) ) ; //hello message
            Message mssg = ( Message ) OIS.readObject ( ) ;
            
            if ( mssg.Get_Message ( ).equals ( "AcceptChat" ) )
            {
                Message mssg1 = new Message ( Server_Connection.Get_I2P_Destination_ ( ) ) ;
                OOS.writeObject ( mssg1 ) ;
                //Open jpanel chat window jtextarea thread open to write, 
                //pass parameter Name Stefanos
                Main_Window.Set_Chat_GUI ( new Chat_Window ( this.Name, OOS, OIS, i2pSocket ) ) ;  //open thread to write and display messages
                //then i can write to my friend. he establish his channel with me as a server
                //but have to display incoming messages
            }
            else
            {
                JOptionPane.showMessageDialog ( this, "Srry ," + Name + " doesnt accept your chat." ) ;
                i2pSocket.close ( ) ;
                OOS.close ( ) ;
                OIS.close ( ) ;
            }
         } 
         catch ( ClassNotFoundException CNFE )
         {
             System.out.println ( "ClassNotFoundException" + CNFE.getLocalizedMessage ( ) ) ;
         }
         catch ( I2PException ex ) 
         {
            System.out.println ( "General I2P exception occurred!" ) ;
         }
         catch ( ConnectException ex ) 
         {
            System.out.println ( "Failed to connect!" ) ;
         } 
         catch ( NoRouteToHostException ex ) 
         {
             System.out.println ( "Couldn't find host!" ) ;
         }
         catch ( InterruptedIOException ex ) 
         {
             System.out.println ( "Sending/receiving was interrupted!" ) ;
         } 
         catch ( IOException IOE )
         {
             System.out.println ( "IOExceprion " + IOE.getLocalizedMessage ( ) ) ;
         }
    }
    
}
