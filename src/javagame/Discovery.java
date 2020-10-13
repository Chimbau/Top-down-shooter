/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enry_
 */
public class Discovery implements Runnable  {
    private static final int ECHOMAX = 255;
    private static final List<String> players = new ArrayList<>();
    private DatagramSocket socket;
    private int porta;
    private String servidor_origem;
    private DatagramPacket packet;
    
    public Discovery(String servidor_origem,int porta){
        this.porta = porta;
        this.servidor_origem = servidor_origem;
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public void run(){
        
        try {
            socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
        } catch (SocketException ex) {
            Logger.getLogger(Discovery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Discovery.class.getName()).log(Level.SEVERE, null, ex);
        }
        packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        
        while(true){
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(Discovery.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Handling client at "+packet.getAddress().getHostAddress()+" on port "+packet.getPort());
            //Enviar packet com infos
            try {
                String resp = ""+porta;
                byte[] respb = resp.getBytes();
                DatagramPacket returnPacket = new DatagramPacket(respb, respb.length, packet.getAddress(), packet.getPort());
                socket.send(returnPacket);
            } catch (IOException ex) {
                Logger.getLogger(Discovery.class.getName()).log(Level.SEVERE, null, ex);
            }
            packet.setLength(ECHOMAX);
            String novo = ""+packet.getAddress().getHostAddress()+" "+packet.getPort();
            for (String player : players) {
                if(novo == player){
                    break;
                }
                players.add(novo);
            }
        }
        
    }
}
