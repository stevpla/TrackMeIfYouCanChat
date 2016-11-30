
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.i2p.I2PException;
import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocket;
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
public class Chats_Accepted implements Runnable
{
      //Improve threads handling
      ExecutorService Bob_Executor = Executors.newCachedThreadPool ( ) ;
      private static final boolean LISTENING = true ;
    
    
    public void run ( )
    {
        System.out.println ( " Ready to accept Chat Requests " ) ;
        while ( LISTENING )
        {
            try
            {
               I2PSocket Socket = I2P_Connection.Get_I2PServerSocket ( ).accept ( ) ;   //Waits for the next socket connecting. 
               Runnable R = new Chats_Accepted_Begin ( Socket ) ;
               Bob_Executor.execute ( R ) ;
            }
            catch ( I2PException i2pException )
            {
                System.out.println ( " I2PException " ) ;
            }
            catch ( ConnectException CE )
            {
                
            }
            catch ( SocketTimeoutException STE )
            {
                
            }
        }
    }
}
    
    

