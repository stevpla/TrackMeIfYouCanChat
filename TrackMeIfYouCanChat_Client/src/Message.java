
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Message implements Serializable
{
    private String message ;
    
    
    public Message ( String m )
    {
        message = m ;
    }
    
    public String Get_Message ( )
    {
        return this.message ;
    }
    
}
