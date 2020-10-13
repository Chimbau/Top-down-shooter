package javagame;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;

public class Conectar extends BasicGameState {

    DatagramPacket receivePacket;
    private boolean mouseLeft = false;
    private boolean conectou = false;
 
    public Conectar(int state) {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.drawString("Rodar o codigo do enry", 330, 110);
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
        
        if (conectou){
                //g.drawString("ALOOOO", 100, 100);            
                g.drawString(new String(receivePacket.getData()), 500, 500); //Linha que ta dando problema
                
            }

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        int xpos = Mouse.getX();
        int ypos = Mouse.getY();
        Input input = gc.getInput();
        
        int TIMEOUT = 2000;
        int MAXTRIES = 5;

        //botão de play
        if ((xpos > 330 && xpos < 534) && (ypos > 606 && ypos < 668)) {
            if (input.isMousePressed(0)) {
                conectou = true;
                 //UDP
                 String endereco = "255.255.255.255";
                 InetAddress broadcast = null;
                 try {
                     broadcast = InetAddress.getByName(endereco);
                 } catch (UnknownHostException ex) {
                     Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 byte[] bytesToSend = "Qual porta?".getBytes();
                 DatagramSocket disc_socket = null;
                 try {
                     disc_socket = new DatagramSocket();
                 } catch (SocketException ex) {
                     Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 try {
                     disc_socket.setBroadcast(true);
                 } catch (SocketException ex) {
                     Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 try {
                     disc_socket.setSoTimeout(TIMEOUT);
                 } catch (SocketException ex) {
                     Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, broadcast, 8888);
                 receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

                 int tries = 0;
                 boolean receivedResponse = false;
                 do {
                     try {
                         disc_socket.send(sendPacket);
                     } catch (IOException ex) {
                         Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     try {
                         //System.out.println("Enviando pergunta broadcast: " + new String(sendPacket.getData()));
                         disc_socket.receive(receivePacket);
                         receivedResponse = true;
                     } catch (InterruptedIOException e) {
                         tries += 1;
                         //System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
                     } catch (IOException ex) {
                         Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 } while ((!receivedResponse) && (tries < MAXTRIES));

                 
                 
                 
                 
             }

                
       
        //mouse = "Mouse position x: " + xpos + " y : " + ypos;*/
            
        }
    }

    @Override
    public int getID() {
        return 2;
    
    }
}
