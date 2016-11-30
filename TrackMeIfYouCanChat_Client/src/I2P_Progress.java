
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class I2P_Progress extends JFrame implements Runnable
{
     
    private JLabel Image, Owner, MiniNet ;
    private JProgressBar ProgressBar ;
    private double Start_Time, End_Time, Result ;
    
    public I2P_Progress ( )
    {
        super ( ) ;
        getContentPane ( ).setBackground ( Color.WHITE ) ;
        setSize ( 695,390 ) ;
        
        setUndecorated ( true ) ;   //No x,- -_
        setLocationRelativeTo ( null ) ;  // Display Window in center of Screen
        setVisible ( true ) ;
        
        
        Owner = new JLabel ( "Connecting to i2p net ..." ) ;
        Owner.setForeground ( new Color ( 200, 20, 50 ) ) ;
        Owner.setFont  ( new Font ( "Courier New", Font.BOLD, 25 ) ) ;
        Owner.setBounds ( 150, 195, 700, 200 ) ;
        
        MiniNet = new JLabel ( "I2P Anonymous Network" ) ;
        MiniNet.setForeground ( Color.BLUE ) ;
        MiniNet.setFont  ( new Font ( "Courier New", Font.ITALIC, 50 ) ) ;
        MiniNet.setBounds ( 50, 255, 700, 200 ) ;
        
        
        Image = new JLabel ( ) ;
        Image.setIcon ( new ImageIcon ( this .getClass ( ). getResource ( "i2p.jpg" ) ) ) ;
        Image.setBounds ( 0, 0, 850, 280 ) ;
        
        ProgressBar = new JProgressBar ( ) ;
        ProgressBar.setMinimum ( 0 ) ;
        ProgressBar.setMaximum ( 100 ) ;
        ProgressBar.setStringPainted ( true ) ;
        ProgressBar.setBounds ( 10, 311, 670, 20 ) ;
        ProgressBar.setValue ( 0 ) ; 
        ProgressBar.setBackground ( Color.green ) ;
        
         
        
       //----------
        
        Container pane = getContentPane ( ) ;
        pane.setLayout ( null ) ;   //Deactivate Manager Layout
    
        
        
        //Pane ADDITIONS
        pane.add ( Image ) ;
        pane.add ( Owner ) ;
        pane.add ( MiniNet ) ;
        pane.add ( ProgressBar ) ;
        
        setContentPane ( pane ) ;
    }//End Constructror
    
    
    public void run ( )
    {
        //Now
        Start_Time = System.currentTimeMillis ( ) ;
        End_Time = System.currentTimeMillis ( ) ;
        Result = End_Time - Start_Time ;
        
        int i = 0 ;
        while ( Result <= 40000 ) 
        {   final int currentValue = i / 40000 ;  //May possibly change
            SwingUtilities.invokeLater ( new Runnable ( ) 
            {
                    public void run() 
                    {
                        ProgressBar.setValue( currentValue ) ;
                    }
            });
            End_Time = System.currentTimeMillis ( ) ;
            Result = End_Time - Start_Time ;
            i ++ ;
        }
        
        this.dispose ( ) ;
        //Hide
    }
}
