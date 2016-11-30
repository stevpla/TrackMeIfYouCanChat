
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
public class Heartbeat implements Serializable
{
    private String nickname; //einai monadiko theroume gia kathe pelati
    
    public Heartbeat ( String nickname )
    {
        this.nickname = nickname ;
    }
    
    
    public String Get_nick ( )
    {
        return this.nickname ;
    }
    
}
