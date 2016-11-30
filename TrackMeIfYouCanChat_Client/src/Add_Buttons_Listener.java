
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Add_Buttons_Listener implements Runnable, ActionListener
{
    private ArrayList < JButton > MYLM ;
    private ArrayList < String > Dests_ ;
    
    
    public Add_Buttons_Listener ( ArrayList < JButton > LB, ArrayList < String > I2P_DESTINATIONS )
    {
        MYLM = new ArrayList < JButton > ( ) ;
        MYLM= LB; 
        Dests_ = new ArrayList < String > ( ) ;
        Dests_ = I2P_DESTINATIONS ;
    }
    
    
    
    public void run ( )
    {
         for ( int k = 0 ;  k < MYLM.size ( ) ;  k ++ )
         {
               MYLM.get ( k ).addActionListener ((ActionListener) this) ;
         }
    }
    
    
    
         public void actionPerformed ( ActionEvent e ) 
         {
            Object ect = e.getSource ( ) ;
             
            ExecutorService Executoror = Executors.newCachedThreadPool ( ) ;
           // your handle button click code
             for ( int i = 0 ;  i < MYLM.size ( ) ;   i ++  )
             {
                 if ( MYLM.get ( i ) == ect )
                 {
                     //start chat with him, find i2p and then talk
                     String dest = Dests_.get ( i ) ;
                     //start a thread that starts a i2p socket to server target
                     System.out.println ( " thelo na ksekiniso me " + MYLM.get ( i ).getText ( ) + " chat" ) ;
                     Runnable ru = new Start_Chat ( dest, MYLM.get ( i ).getText ( )  ) ;
                     Executoror.execute ( ru ) ;
                 }
             }
         }
    
    
}
    
    

