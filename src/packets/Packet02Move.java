package packets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.JogoCliente;
import network.JogoServer;


public class Packet02Move extends Packet {
    
    private String username;
    private float x,y;
    
    public Packet02Move(byte[] data){
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        
    }
    
    public Packet02Move(String username, float x, float y){
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    @Override
    public void writeData(JogoCliente cliente) {
        try {
            cliente.sendData(getData());
        } catch (IOException ex) {
            Logger.getLogger(Packet02Move.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeData(JogoServer server) {
        try {
            server.sendDataToAllClients(getData());
        } catch (IOException ex) {
            Logger.getLogger(Packet02Move.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] getData() {
       return ("02"+this.username+","+this.x+","+this.y).getBytes();
    }

    public String getUsername() {
       return username;
    }
    
    public float getX(){
        return this.x;
    }
    
    public float getY(){
        return this.y;
    }
}
