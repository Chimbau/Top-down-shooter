package packets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.JogoCliente;
import network.JogoServer;


public class Packet01Disconnect extends Packet {
    
    private String username;
    
    public Packet01Disconnect(byte[] data){
        super(01);
        this.username = readData(data);
    }
    
    public Packet01Disconnect(String username){
        super(01);
        this.username = username;
    }

    @Override
    public void writeData(JogoCliente cliente) {
        try {
            cliente.sendData(getData());
        } catch (IOException ex) {
            Logger.getLogger(Packet01Disconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeData(JogoServer server) {
        try {
            server.sendDataToAllClients(getData());
        } catch (IOException ex) {
            Logger.getLogger(Packet01Disconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] getData() {
       return ("01" + this.username).getBytes();
    }

    public String getUsername() {
       return username;
    }
}
