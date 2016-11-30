
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
public class Client_Message implements Serializable
{
    private String nickname ;
    private String I2P_Destination ;
    
    
    public Client_Message ( String nickname, String I2P_Destination )
    {
        this.nickname = nickname ;
        this.I2P_Destination = I2P_Destination ;
    }
    
    public String Get_Name ( )
    {
        return this.nickname ;
    }
    
    public String Get_I2P ( )
    {
        return this.I2P_Destination ;
    }
    
    @Override
    public String toString ( )
    {
        return "Client : " + this.Get_Name ( ) + ", I2P_Destination : " + this.Get_I2P ( ) ;
    }
}
