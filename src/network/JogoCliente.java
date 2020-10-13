package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javagame.Game;
import javagame.Play;
import javagame.Player;
import javagame.PlayerMP;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import packets.Packet;
import packets.Packet00Login;
import packets.Packet01Disconnect;
import packets.Packet02Move;
import packets.Packet03Mirar;
import packets.Packet04Inimigo;
import packets.Packet05CriarInimigo;


public class JogoCliente extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;
    private Play play;
    
    public JogoCliente(Play play, String ipAddress) throws SocketException, UnknownHostException{
        this.play = play;
        this.socket = new DatagramSocket();
        this.ipAddress = InetAddress.getByName(ipAddress);
    }
    
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
                //System.out.println("Server > "+new String(packet.getData()));
                this.decodificarPacote(packet.getData(), packet.getAddress(), packet.getPort());
            } catch (IOException ex) {
                Logger.getLogger(JogoCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SlickException ex) {
                Logger.getLogger(JogoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void sendData(byte[] data) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        socket.send(packet);
    }
    
    private void decodificarPacote(byte[] data, InetAddress address, int port) throws IOException, SlickException {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPackets(message.substring(0, 2));
        Packet packet = null;
       
        switch(type){
            default:
            case INVALID:
                break;
            
            case LOGIN:
                packet = new Packet00Login(data);
                ArrumarLogin((Packet00Login)packet, address, port);
              
                break;
                
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress()+":"+port+"]"+((Packet01Disconnect)packet).getUsername()+" saiu ");
                play.removePlayerMP(((Packet01Disconnect)packet).getUsername());
                break;
                
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move)packet);
                break;
                
            case MIRAR:
                packet = new Packet03Mirar(data);
                ArrumarMira((Packet03Mirar)packet);             
                break;
                
            case INIMIGO:
                packet = new Packet04Inimigo(data);
                this.ArrumarInimigo((Packet04Inimigo) packet);
                break;
            
            case CriarInimigo:
                packet = new Packet05CriarInimigo(data);
                this.CriarInimigosAux((Packet05CriarInimigo)packet);
        }
    }
    
    private void ArrumarLogin(Packet00Login packet, InetAddress address, int port) throws SlickException{
         System.out.println("["+address.getHostAddress()+":"+port+"]"+((Packet00Login)packet).getUsername()+" entrou ");
         PlayerMP player = new PlayerMP(address, port, packet.getUsername(), packet.getX() , packet.getY());               
         play.adicionarPlayer(player);
    }

    private void handleMove(Packet02Move packet) {
        this.play.movePlayer(packet.getUsername(), packet.getX(), packet.getY());
    }
    
     private void ArrumarMira(Packet03Mirar packet){
         this.play.MoverMira(packet.getUsername(), packet.getAngulo(), packet.getAtirou(), packet.getAnguloTiro());
       
    }

    private void ArrumarInimigo(Packet04Inimigo packet) {
         this.play.MoverInimigo(packet.getVida(), packet.getDelta(), packet.getInimigoX(), packet.getInimigoY(), packet.getInimigo(), packet.getAngulo());
    }

    private void CriarInimigosAux(Packet05CriarInimigo packet) throws SlickException {
        this.play.CriarInimigo(packet.getCriado());
    }
}

   





                



