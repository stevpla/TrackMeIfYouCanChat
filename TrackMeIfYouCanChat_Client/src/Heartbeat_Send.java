
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
public class Heartbeat_Send implements Runnable
{
    private String Name ;
         
    private double Start_Time, End_Time, Result ;
    private ObjectOutputStream OOS;
    private ObjectInputStream OIS ;
    
    
    
    public Heartbeat_Send ( String name, ObjectOutputStream oos, ObjectInputStream ois )
    {
        Name = name ;
        OOS = oos ;
        OIS = ois ;
    }
    
    
    public void run ( )
    {
        while ( true )
        {
            Start_Time = System.currentTimeMillis ( ) ;
            End_Time = System.currentTimeMillis ( ) ;
            Result = End_Time - Start_Time ;
            while ( Result <= 80000 )   //1.3- mins little less
            {  
               End_Time = System.currentTimeMillis ( ) ;
               Result = End_Time - Start_Time ;
            }
            //2 minutes sendheartbeat
        try
        {
           OOS.writeObject ( new Heartbeat ( Name ) ) ;
           ArrayList < Client_Message > LIST = new ArrayList < Client_Message > ( ) ;
           LIST = ( ArrayList < Client_Message > ) OIS.readObject ( ) ;
           //pass list to refresh gui
           //FIRST
           Main_Window.Clear_JPanel_Heartbeat ( ) ;
           //
           //Then
           Main_Window.Refresh_Panel_With_Friends ( LIST ) ;
        }
        catch ( ClassNotFoundException CNFE )
        {
            System.out.println ( "KeyStoreException..." + CNFE.getLocalizedMessage ( ) ) ;
        }
        catch ( IOException IOE )
        {
            System.out.println ( "Error while trying to connect with server..." + IOE.getLocalizedMessage ( ) ) ;
         JOptionPane.showMessageDialog ( null , "IOException during connecting to i2p...\nLeave ChatRoom and Start again\nHeartbeat error./") ;
            break ;
        }
        }
    }
    
}
