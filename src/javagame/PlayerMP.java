package javagame;

import java.net.InetAddress;
import org.newdawn.slick.SlickException;


public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(InetAddress ipAddress, int port, String username, float x, float y) throws SlickException{
        super(username, x, y);
        this.ipAddress = ipAddress;
        this.port = port;
    }

}
