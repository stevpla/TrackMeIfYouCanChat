
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Display_Incoming_Messages implements Runnable
{
    private static ObjectInputStream OIS ;
    private static ObjectOutputStream OOS ;
    private Message message = null ;
    private String NAME ;
    
    
    public Display_Incoming_Messages ( ObjectInputStream ois, ObjectOutputStream oos, String nm )
    {
       NAME = nm ;
       OOS = oos ;
       OIS = ois ;
    }
    
    
    public void run ( )
    {
        do
        {
            try
            {
                message = ( Message ) OIS.readObject ( ) ;
                Chat_Window.Refresh_Area ( message.Get_Message ( ), NAME ) ;
            }
            catch ( ClassNotFoundException as )
            {
                
            }
            catch ( IOException ioe )
            {
                
            }
        } while ( ! message.Get_Message ( ).equals ( "EXIT" ) ) ;
        //out when EXIT, so
        try
        {
           OOS.close ( ) ;
           OIS.close ( ) ;
           Chat_Window.Refresh_Area ( "Closing Chat.." ,NAME ) ;
        }
        catch ( IOException ioe )
        {
            
        }
        
    }
    
    
    
}
