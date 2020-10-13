package javagame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javagame.Discovery;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;

public class Hostear extends BasicGameState {

    private List<String> players;
    private boolean mouseLeft = false;
    private boolean hosteou = false;
    private boolean vez;
    

    public Hostear(int state) {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        vez = true;
        
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawString("Ja rodou.", 330, 110);
        int xpos = Mouse.getX();
        int ypos = Mouse.getY();


        /*if ((xpos > 330 && xpos < 534) && (ypos > 606 && ypos < 668)) {
            
            if (Mouse.isButtonDown(0)) {
                mouseLeft = true;                                     
            } 
            
            if (mouseLeft) {
                //conectou = true;             
                mouseLeft = false;
            }
                 
        }*/
        if (hosteou) {
            int ydraw = 400;
            for (String player : players) {
                g.drawString(player, 330, ydraw);
                ydraw += 40;
            }
        }
        
        

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        int xpos = Mouse.getX();
        int ypos = Mouse.getY();

        
        if (vez) {
            int servPort = ThreadLocalRandom.current().nextInt(7000, 9999);
            String servidor_nome = "Servidor de >NOME<"; //vai ter entrada de nickname pra geral né?

            //UDP
            Discovery disc = new Discovery(servidor_nome, servPort);
            Thread udp_discovery = new Thread(disc);
            udp_discovery.start();
            players = disc.getPlayers();
            vez = false;
            hosteou = true;
                        
        }
    }

    @Override
    public int getID() {
        return 3;

    }
}
