
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Message_Accept implements Runnable
{
    private boolean LISTENING = true ;
    final String Password = "abc123" ,
                     JavaKeyStore = "JKS" ,
                     KMF_Algo = "SunX509" ,
                     TLS_Algo = "TLS" ,
                     SecRandom_Algo = "SHA1PRNG" ,
                     path_jks1 = "icsdjks1.jks" ,
                     path_jks2 ="icsdjks2.jks" ;
    private char [ ] Pass = null ;
    private final int Server_Port = 9898 ;
    private KeyStore JKeyStore = null, JKeyStore2 = null ;
    private KeyManagerFactory KMF = null ;
    private TrustManagerFactory TMF = null ;
    private SSLContext context = null ;
    private SSLServerSocketFactory SSLSSF = null ;
    private SSLServerSocket sslserversocket = null ;
    private ExecutorService Server1_Executor = Executors.newCachedThreadPool ( ) ;
    
    
    public Message_Accept ( ) throws IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyManagementException
    {
        Pass = Password.toCharArray ( ) ;
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
        SSLSSF = ( SSLServerSocketFactory ) context.getServerSocketFactory ( ) ;
        sslserversocket = ( SSLServerSocket ) SSLSSF.createServerSocket ( Server_Port ) ;
        System.out.println ( "SSL Server Socket just created..: " + sslserversocket.toString ( ) ) ;
    }
    
    
    
    public void run ( )
    {
        int i = 0 ;
        System.out.println ( "listening for Clients...." ) ;
        System.out.println ( ) ;
        System.out.println ( ) ;
        System.out.println ( ) ;
        while ( LISTENING )
        {
            try 
            {
                SSLSocket mysock = ( SSLSocket )  sslserversocket.accept ( ) ;
                Runnable r = new Message_Thread ( mysock ) ;
                Server1_Executor.execute ( r ) ;
                System.out.println ( ) ;
                System.out.println ( ) ;
                System.out.println ( ) ;
            } 
            catch (IOException ex) 
            {
                System.out.println ( "Error during Connection with Client ... " + ex.getLocalizedMessage ( ) ) ;
            }
        }
    }
    
    
    
}
