

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Main_Window extends JFrame implements ActionListener
{
    private static Container pane = null ;
    private JButton Exit, Start_Chat ;
    private JLabel Photo , On ;
    private static JPanel P ;
    private static ArrayList < JButton > Buttons_Names = new ArrayList < JButton > ( ) ;
    private static ArrayList < String > I2P_Dests = new ArrayList < String > ( ) ; 
    private ExecutorService Executor = Executors.newCachedThreadPool ( ) ;
    private static int Temp_Size = 0 ;
    private static JLabel myname = new JLabel ( ) ;
    private static String GlobalName ;
    private static int Counter_X_New_Chat = 650 ;  //place chat window a bit left
    private JTextArea ta ;
    private JRadioButton button1 ;
    
    public static String Get_MyName ( )
    {
        return GlobalName ;
    }
    
    
    
    public static void Set_Chat_GUI ( JPanel op )
    {
        op.setBackground ( Color.GRAY ) ;
        op.setBounds ( Counter_X_New_Chat, 450, 250, 300 ) ;
        op.setBackground ( Color.GRAY ) ;
        pane.add ( op ) ;
        Counter_X_New_Chat = Counter_X_New_Chat - 150 ;
        //
        
    }
    
     
    public static void Clear_JPanel_Heartbeat ( )
    {
        P.removeAll ( ) ;
        P.revalidate ( ) ;
        P.repaint ( ) ;
    }
    
    
    
    public Main_Window ( )
    {
        super ( "TrackMeIfYouCanChat" ) ;   //First call Constructor of JFrame
        this.setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE ) ;  //We do not want our application to terminate when a JFrame is closed
        this.addWindowListener ( new java.awt.event.WindowAdapter ( ) 
        {
             @Override
             public void windowClosing ( java.awt.event.WindowEvent windowEvent ) 
             {
                    int Answer = JOptionPane.showConfirmDialog ( rootPane,  "Exit Application ? ", "Exit ? ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) ;
                    if ( Answer == JOptionPane.YES_OPTION )
                    {  
                        System.exit ( 1 ) ;
                    }
                    else
                    {
                        setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE ) ;
                    }
             }
        } ) ;  
        //
        this.setSize ( 1250, 850 ) ;       // Size of Window
        this.setLocationRelativeTo ( null ) ;  // Display Window in center of Screen
        this.setVisible ( true ) ;
        this.getContentPane ( ) .setBackground ( Color.LIGHT_GRAY ) ;
        
        ta = new JTextArea ( "Your NickName" ) ;
        ta.setBounds ( 20, 250, 250, 30 ) ;
        ta.setForeground ( Color.BLUE ) ;
        ta.addMouseListener ( new MouseAdapter ( ) 
        {
            @Override
            public void mouseClicked ( MouseEvent e )
            {
                ta.setText ( "" ) ;
                ta.setForeground ( Color.BLACK ) ;
            }
        });
        //------------------------
        myname.setBounds ( 400,70, 200, 250 ) ;
        myname.setFont ( new Font ( "Courier New", Font.CENTER_BASELINE, 40 ) ) ;
        //----------------------
        button1 = new JRadioButton ( "Then Press here" ) ;
        button1.setBounds ( 40, 300, 120, 40 ) ;
        button1.addActionListener ( new ActionListener ( )  
        {
           @Override public void actionPerformed ( ActionEvent X ) 
           {
             if ( button1.isSelected ( ) ) 
             {
                 button1.setEnabled ( false ) ;
                 myname.setText ( ta.getText ( ) ) ;
                 GlobalName = ta.getText ( ) ;
                 pane.add ( myname ) ;
                 pane.validate ( ) ;
             }  
           }
         } ) ;
        
                
        Photo = new JLabel ( ) ;
        Photo.setIcon ( new ImageIcon ( this .getClass ( ). getResource ( "jv.jpg" ) ) ) ;
        Photo.setBounds ( 15, 20, 350, 180 ) ;
        
        On = new JLabel ( "Online Friends:" ) ;
        On.setForeground ( Color.blue ) ;
        On.setFont ( new Font ( "Courier New", Font.BOLD, 25 ) ) ;
        On.setBounds ( 950, 10, 240, 60 ) ;
        
        P = new JPanel ( ) ;
        P.setBackground ( Color.GRAY ) ;
        P.setBounds ( 950, 50, 250, 650 ) ;
        P.setBackground ( Color.GRAY ) ;
        P.setLayout ( new GridLayout ( 20, 2 ) ) ; //The big JPanel fixed size 20 friends
        
        Start_Chat = new JButton ( "Start Chat" ) ;
        Start_Chat.setForeground ( Color.black ) ;
        Start_Chat.setFont ( new Font ( "Courier New", Font.BOLD, 20 ) ) ;
        Start_Chat.setBounds ( 320, 50, 200, 40 ) ;
        
        Exit = new JButton ( "Leave ChatRoom" ) ;
        Exit.setForeground ( Color.black ) ;
        Exit.setFont ( new Font ( "Courier New", Font.BOLD, 24 ) ) ;
        Exit.setBounds ( 530, 50, 230, 40 ) ;
       
        pane = getContentPane ( ) ;  //Manager
        pane.setLayout ( null ) ;   // Deactivate Manager Layout
        pane.add ( Photo ) ;
        pane.add ( P ) ;
        pane.add ( Exit ) ;
        pane.add ( Start_Chat ) ;
        pane.add ( On ) ;
        pane.add ( ta ) ;
        pane.add ( button1 ) ;
        Exit.addActionListener ( this ) ;
        Start_Chat.addActionListener ( this ) ;
        setContentPane ( pane ) ;
    }
    
    
  

    
    //List from Server with Names. These Names insert into JButtons and then JButtons ( each Name-JButton ), Appear in JPanel
    public static void Refresh_Panel_With_Friends ( ArrayList < Client_Message > M )
    {
      Buttons_Names.clear ( ) ;
      I2P_Dests.clear ( ) ;
      //After clear Lists then load
      if ( ! M.isEmpty ( ) )
      { Temp_Size = M.size ( ) ;  //holds amount of people online
        for ( int i = 0 ;   i < M.size ( ) ;   i ++ )
        {
            //extract also list of I2P Destinations if you wanna chat with somebody
            I2P_Dests.add ( M.get ( i ).Get_I2P ( ) ) ;
            JPanel p = new JPanel ( new GridLayout ( 1,1 ) ) ;
            //kane se ayto to panel add to koympaki me onoma
            JButton bo = new JButton ( M.get ( i ).Get_Name ( ) ) ;
            //bo.addActionListener ( Main_Window. ) ;
            Buttons_Names.add ( bo ) ;
            p.add ( bo ) ;
            P.add ( p ) ;
        }
        ExecutorService My_Exec = Executors.newCachedThreadPool ( ) ;
        My_Exec.execute ( new Add_Buttons_Listener ( Buttons_Names, I2P_Dests ) ) ; 
        P.updateUI ( ) ;
        P.validate ( ) ;
      }
      else
      {
         JPanel p = new JPanel ( new GridLayout ( 1,1 ) ) ;
         JLabel l = new JLabel ( "No one online now .. " ) ;
         p.add ( l ) ;
         P.add ( p ) ;
         P.updateUI ( ) ;
         P.validate ( ) ;
      }
    }
    
    
    
    
    public void actionPerformed ( ActionEvent evt )
    {
        Object source = evt.getSource ( ) ;
        
        if ( Start_Chat == source )
        {
            Start_Chat.setEnabled ( false ) ;
            Exit.setEnabled ( true ) ;
            ExecutorService Executoro = Executors.newCachedThreadPool ( ) ;
            Executoro.execute ( new I2P_Progress ( ) ) ;   //call I2P Progress Bar
            Runnable Run = new Server_Connection ( ) ;     //Then start connection with Registrar
            Executor.execute ( Run ) ;                     //Execute run in Server_Connecetion Class
        }
        //-------------------
        if ( Exit == source )
        {
            Exit.setEnabled ( false ) ;
            Start_Chat.setEnabled ( true ) ;
            ta.setText ( "Your Nickname" ) ;
            button1.setEnabled ( true ) ;
            button1.setSelected ( false ) ;
            P.removeAll ( ) ; 
            // refresh the panel.
            P.updateUI ( ) ;
            this.repaint ( ) ;
            //clear all arraylists deallocate memory
            Buttons_Names.clear ( ) ;
            I2P_Dests.clear ( ) ;
        }
    }
}
