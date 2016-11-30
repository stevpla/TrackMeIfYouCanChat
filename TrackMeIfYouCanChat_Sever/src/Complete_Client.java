
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.net.ssl.SSLSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Complete_Client 
{
   private String Name, I2P_Dest ;
   private SSLSocket socket ;
   private ObjectOutputStream OOS ;
   private ObjectInputStream OIS ;
   
   public Complete_Client ( String Name, String I2P_Dest, SSLSocket socket, ObjectOutputStream OOS, ObjectInputStream OIS )
   {
       this.Name = Name ;
       this.I2P_Dest = I2P_Dest ;
       this.socket = socket ;
       this.OIS = OIS ;
       this.OOS = OOS ;
   }
   
   public String Get_Client_Name ( )
   {
       return this.Name ;
   }
   
   @Override
   public String toString ( )
   {
       return " Client : " + Get_Client_Name ( ) + ", IP2_Destination : " + this.I2P_Dest  ;
   }
   
    
}
