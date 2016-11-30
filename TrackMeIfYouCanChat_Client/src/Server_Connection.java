
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Server_Connection implements Runnable 
{
    private final String Password = "abc123" ,
                         JavaKeyStore = "JKS" ,
                         KMF_Algo = "SunX509" ,
                         TLS_Algo = "TLS" ,
                         SecRandom_Algo = "SHA1PRNG" ,
                         path_jks1 = "icsdjks1.jks" ,
                         path_jks2 = "icsdjks2.jks" ,
                         Server_Ip_Address = "127.0.0.1" ;  //na to allakso otan pao lab
    private KeyStore JKeyStore, JKeyStore2 ;
    private KeyManagerFactory KMF ;
    private TrustManagerFactory TMF ;
    private SSLContext context ;
    private SSLSocketFactory SSLSF ;
    private SSLSocket socket ;
    private final int port = 9898 ;
    private static String i2p_Destination ;
    private ExecutorService Executor0 = Executors.newCachedThreadPool ( ) ; 
    
    
    public static String Get_I2P_Destination_ ( )
    {
        return i2p_Destination ;
    }
    
    
    
    
    public void run ( )
    {
        i2p_Destination = new I2P_Connection ( ).Connect_I2P_Network_Return_Dest ( ) ;
        System.out.println ( " Connected to I2P Network. Destination -> " + i2p_Destination ) ;
        char [] Pass = Password.toCharArray ( ) ;
        try
        {
           JKeyStore = KeyStore.getInstance ( JavaKeyStore ) ;
           JKeyStore.load ( new FileInputStream ( path_jks2 ), Pass ) ;
           KMF = KeyManagerFactory.getInstance ( KMF_Algo ) ;
           KMF.init ( JKeyStore, Pass ) ;
        
           //2nd crt
           JKeyStore2 = KeyStore.getInstance ( JavaKeyStore ) ;
           JKeyStore2.load ( new FileInputStream ( path_jks1 ), Pass ) ; 
           TMF = TrustManagerFactory.getInstance ( KMF_Algo ) ;
           TMF.init ( JKeyStore2 ) ;
        
           context = SSLContext.getInstance( TLS_Algo ) ;
           context.init (  KMF.getKeyManagers ( ) , TMF.getTrustManagers ( ), SecureRandom.getInstance ( SecRandom_Algo ) ) ;
           SSLSF = context.getSocketFactory ( ) ;
           socket = ( SSLSocket ) SSLSF.createSocket ( Server_Ip_Address, port ) ;
           
           //now begin protocol
           ObjectOutputStream OOS = new ObjectOutputStream ( socket.getOutputStream ( ) ) ;
           ObjectInputStream OIS = new ObjectInputStream ( socket.getInputStream ( ) ) ;
           OOS.writeObject( new Client_Message ( Main_Window.Get_MyName ( ), i2p_Destination ) ) ;
            try 
            {
                ArrayList < Client_Message > My = new ArrayList < Client_Message > ( ) ;
                My = ( ArrayList < Client_Message > ) OIS.readObject ( ) ;
                //pass list to method. Method display Names from List onto JFrame ( JPanel )
                Main_Window.Refresh_Panel_With_Friends ( My ) ;
                System.out.println ( " Receive List from Registrar " ) ;
                //Then me as a client become a server. Able to listen clients that want to chat with me.
                Executor0.execute ( new Chats_Accepted ( ) ) ;
                //When Registrar sends me List and terminates the connection, then start measure time to send heartbeat
                Executor0.execute ( new Heartbeat_Send ( Main_Window.Get_MyName ( ), OOS, OIS ) ) ;
                My.clear ( ) ;
            } 
            catch ( ClassNotFoundException ex ) 
            {
                System.out.println ( "ClassNotFoundException..." + ex.getLocalizedMessage ( ) ) ;
            }
        }
        catch ( KeyStoreException KSE )
        {
            System.out.println ( "KeyStoreException..." + KSE.getLocalizedMessage ( ) ) ;
        }
        catch ( NoSuchAlgorithmException NSAE )
        {
            System.out.println ( "NoSuchAlgorithmException..." + NSAE.getLocalizedMessage ( ) ) ;
        }
        catch ( UnrecoverableKeyException UKE )
        {
            System.out.println ( "UnrecoverableKeyException..." + UKE.getLocalizedMessage ( ) ) ;
        }
        catch ( KeyManagementException KME )
        {
            System.out.println ( "KeyManagementException..." + KME.getLocalizedMessage ( ) ) ;
        }
        catch ( CertificateException CE )
        {
            System.out.println ( "CertificateException..." + CE.getLocalizedMessage ( ) ) ;
        }
        catch ( IOException IOE )
        {
            System.out.println ( "Error while trying to connect with server..." + IOE.getLocalizedMessage ( ) ) ;
            JOptionPane.showMessageDialog ( null , "IOException during connecting to i2p...\nLeave ChatRoom and Start again") ;
        }
    }
}
