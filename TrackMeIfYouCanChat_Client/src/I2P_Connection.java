
import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class I2P_Connection 
{
    private static I2PSocketManager Socket_Manager ;
    private static I2PServerSocket Server_Socket ;
    private static I2PSession Session ;
    
    
    public static I2PSocketManager Get_I2PSocketManager ( )
    {
        return Socket_Manager ;
    }
    
    public static I2PServerSocket Get_I2PServerSocket ( )
    {
        return Server_Socket ;
    }
    
    public static I2PSession Get_I2PSession ( )
    {
        return Session ;
    }
    
    public String Connect_I2P_Network_Return_Dest ( )
    {
        //I2PSocketManagerFactory - Simplify the creation of I2PSession and transient I2P Destination objects if necessary to create a socket manager. 
        //This class is most likely how classes will begin their use of the socket library.All createManager() methods are blocking and return null on error
        
        //createManager - Create a socket manager using a brand new destination connected to the I2CP router on the local machine on the default port (7654).
        //I2PSocketManager - Centralize the coordination and multiplexing of the local client's streaming. There should be one I2PSocketManager for each I2PSession
        Socket_Manager = I2PSocketManagerFactory.createManager ( ) ;
        //System.out.println ( "socket manager " + Socket_Manager.toString ( ) ) ;
        
        //getServerSocket - Returns non-null socket
        //I2PServerSocket - Streaming server socket returned by I2PSocketManager.getServerSocket(). Defines how to listen for streaming peer connections
        Server_Socket = Socket_Manager.getServerSocket ( ) ;
        //System.out.println ( "server socket " + Server_Socket.toString ( ) ) ;
    
        //I2PSession - Define the standard means of sending and receiving messages on the I2P network by using the I2CP (the client protocol). 
        //This is done over a bidirectional TCP socket
        Session = Socket_Manager.getSession ( ) ;
        //System.out.println ( "session " + Session.toString ( ) ) ;
        
        //Print the base64 string, the regular string would look like garbage.
        //GetMyDestination ( ).
        //Retrieve the Destination this session serves as the endpoint for. Returns null if no destination is available.
        return ( Session.getMyDestination ( ) .toBase64 ( ) ) ;
    }
    
}
