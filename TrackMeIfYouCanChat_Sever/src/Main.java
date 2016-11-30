
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyManagementException
    {
         ExecutorService Executor = Executors.newCachedThreadPool ( ) ;
         Executor.execute ( new Message_Accept ( ) ) ;
         System.out.println ( "Server starts on !!.." ) ;
    }
    
}
