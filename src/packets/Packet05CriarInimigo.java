package packets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.JogoCliente;
import network.JogoServer;
import org.newdawn.slick.geom.Vector2f;


public class Packet05CriarInimigo extends Packet {
    
    private int criado;
    
    
    
    public Packet05CriarInimigo(byte[] data){
        super(05);
        String[] dataArray = readData(data).split(",");
        this.criado = Integer.parseInt(dataArray[0]);
        
     
    }
    
    public Packet05CriarInimigo(int c){
        super(05);
        this.criado = c;
           
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
       return ("05"+this.criado).getBytes();
    }

    public int getCriado(){
        return this.criado;
    }
    
    
}
