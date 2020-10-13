package javagame;

import java.awt.Dialog;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import network.JogoCliente;
import network.JogoServer;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Game extends StateBasedGame {
    
    public static final String gamename = "Zombie YZ Mega Survival Battle Royale Special 2 (early access)";
    public static final int menu = 0;
    public static final int play = 1;
    public static final int conectar = 2;
    public static final int hostear = 3;
    private int server;
    
    //Teste de cliente servidor
    private JogoCliente socketCliente;
    private JogoServer socketServer;
    //
    
    
    public Game(String gamename) throws SocketException, UnknownHostException, SlickException{
        super(gamename);
        this.addState(new Menu(menu));
        this.addState(new Play(play));
        this.addState(new Conectar(conectar));
        this.addState(new Hostear(hostear));
        
        
        //Teste de cliente/servidor
        
        /*System.out.println("Voce quer rodar o servidor?");
        Scanner s = new Scanner(System.in);
        server = s.nextInt();
        
        if (server == 1){
            socketServer = new JogoServer(this);
            socketServer.start();
        }
        else{
            if (server == 0){
                socketCliente = new JogoCliente(this, "localhost");
                socketCliente.start();
            }
        }*/
    }
    
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException{
        
        // Teste de cliente servidor
        
        /*if (server == 0){
            try {
                socketCliente.sendData("ping".getBytes());
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
        //
        
        this.getState(menu).init(gc, this);
        this.getState(play).init(gc, this); //Iniciando duas vezes por algum motivo
        this.getState(conectar).init(gc, this);
        this.getState(hostear).init(gc,this);
        this.enterState(menu);
                
    }
            
    public static void main(String[] args) throws SocketException, UnknownHostException{
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer(new Game(gamename));
            appgc.setDisplayMode(1024, 768, false);
            appgc.setAlwaysRender(true);
            appgc.start();
                       
            
        }catch(SlickException e){
            e.printStackTrace();
        }
    }

  
    
    
}
