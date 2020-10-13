package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javagame.Game;
import javagame.Play;
import javagame.Player;
import javagame.PlayerMP;
import javax.swing.JOptionPane;
import org.newdawn.slick.SlickException;
import packets.Packet;
import packets.Packet.PacketTypes;
import packets.Packet00Login;
import packets.Packet01Disconnect;
import packets.Packet02Move;
import packets.Packet03Mirar;
import packets.Packet04Inimigo;

/**
 *
 * @author Pichau
 */
public class JogoServer extends Thread {
    
    private DatagramSocket socket;
    private Game game;
    private Play play;
    private List<PlayerMP> PlayerConectados = new ArrayList<PlayerMP>();
    
    public JogoServer(Play play) throws SocketException, UnknownHostException{
        this.play = play;
        this.socket = new DatagramSocket(1331);
    }
    
    @Override
    public void run(){
        while(true){
            byte[] data = new byte[1024]; 
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(JogoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
                this.decodificarPacote(packet.getData(), packet.getAddress(), packet.getPort());
                
                /*String message = new String(packet.getData());
                System.out.println("Cliente > "+message);
                if (message.trim().equalsIgnoreCase("ping")){
                try {
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
                } catch (IOException ex) {
                Logger.getLogger(JogoServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                }*/
            } catch (IOException ex) {
                Logger.getLogger(JogoServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SlickException ex) {
                Logger.getLogger(JogoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void sendData(byte[] data, InetAddress ipAddress, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        this.socket.send(packet);
    }

    public void sendDataToAllClients(byte[] data) throws IOException {
        for(PlayerMP p : PlayerConectados){
            sendData(data, p.ipAddress, p.port);
        }
    }

    private void decodificarPacote(byte[] data, InetAddress address, int port) throws IOException, SlickException {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPackets(message.substring(0, 2));
        Packet packet = null;
       
        switch(type){
            default:
            case INVALID:
                break;
            
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]"+((Packet00Login)packet).getUsername()+" se conectou ");
                PlayerMP player = new PlayerMP(address, port, ((Packet00Login)packet).getUsername(), 200, 200);
                this.addConection(player, ((Packet00Login)packet));
                break;
                
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]"+((Packet01Disconnect)packet).getUsername()+" se desconectou ");
                this.removeConection((Packet01Disconnect) packet);
                break;
                
            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove(((Packet02Move)packet));
                break;
            
            case MIRAR:
                packet = new Packet03Mirar(data);
                this.ArrumarMira((Packet03Mirar)packet);
                break;
            
         
                
                            
        }
    }

    public void addConection(PlayerMP player, Packet00Login packet) throws IOException {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.PlayerConectados){
            if (player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.ipAddress == null){
                    p.ipAddress = player.ipAddress;
                }
                
                if (p.port == -1){
                    p.port = player.port;
                }
                alreadyConnected = true;
            }
            else{
                sendData(packet.getData(), p.ipAddress, p.port);
                
                Packet00Login packetNew = new Packet00Login(p.getUsername(), p.PersonagemPosX, p.PersonagemPosY);
                sendData(packetNew.getData(), player.ipAddress, player.port);
            }
            
        }
        if (!alreadyConnected){
                this.PlayerConectados.add(player);
         
         }
    }

    public void removeConection(Packet01Disconnect packet01Disconnect) {      
        this.PlayerConectados.remove(getPlayerMPIndex(packet01Disconnect.getUsername()));
        packet01Disconnect.writeData(this);
    }
    
    public PlayerMP getPlayerMP(String username){
        for(PlayerMP p : this.PlayerConectados){
            if(p.getUsername().equals(username)){
                return p;
            }
           
        }
        return null;
    }
    
     public int getPlayerMPIndex(String username){
        int index = 0;
        for(PlayerMP p : this.PlayerConectados){
            if(p.getUsername().equals(username)){
                break;
            }
            index++;
           
        }
        return index;
    }

    private void handleMove(Packet02Move packet) {
        if(getPlayerMP(packet.getUsername()) != null){
            int index = getPlayerMPIndex(packet.getUsername());
            this.PlayerConectados.get(index).PersonagemPosX = packet.getX();
            this.PlayerConectados.get(index).PersonagemPosY = packet.getY();
            packet.writeData(this);
            
        }
        
    }
    
    private void ArrumarMira(Packet03Mirar packet){
        if(getPlayerMP(packet.getUsername()) != null){
            int index = getPlayerMPIndex(packet.getUsername());
            this.PlayerConectados.get(index).targetAng = packet.getAngulo();
            this.PlayerConectados.get(index).atirou = packet.getAtirou();
            this.PlayerConectados.get(index).anguloTiro = packet.getAnguloTiro();
            
            packet.writeData(this);
            
        }
    }
    
   
    
}
