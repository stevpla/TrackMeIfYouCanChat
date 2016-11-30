
import java.util.concurrent.ScheduledFuture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Scheduled_Future_With_Name 
{
    private ScheduledFuture SF ;
    private String Name ;
    
    public Scheduled_Future_With_Name ( ScheduledFuture SF, String Name )
    {
        this.SF =SF ;
        this.Name = Name ;
    }
    
    public String Get_Name ( )
    {
        return this.Name ;
    }
    
    public ScheduledFuture Get_SF ( )
    {
        return this.SF ;
    }
    
}
