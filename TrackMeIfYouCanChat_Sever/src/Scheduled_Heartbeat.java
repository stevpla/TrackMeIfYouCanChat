
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Scheduled_Heartbeat implements Runnable
{
    private String tmp = null ;
    

    public Scheduled_Heartbeat ( String N )
    {
        tmp = N ;
    }
    
    @Override
    public void run ( ) 
    {
        try 
        {
            Message_Thread.Delete_Client_List ( tmp ) ;
            //static method
        } 
        catch ( IOException ex ) 
        {
            Logger.getLogger ( Scheduled_Heartbeat.class.getName ( ) ) .log(Level.SEVERE, null, ex ) ;
        }
    }
    
}
