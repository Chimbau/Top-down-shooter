package packets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.JogoCliente;
import network.JogoServer;


public class Packet03Mirar extends Packet {
    
    private String username;
    private float angulo;
    private double anguloTiro;
    private int atirou;
    
    
    public Packet03Mirar(byte[] data){
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.angulo = Float.parseFloat(dataArray[1]);
        this.atirou = Integer.parseInt(dataArray[2]);
        this.anguloTiro = Double.parseDouble(dataArray[3]);
        
        
    }
    
    public Packet03Mirar(String username, float a, int b, double c){
        super(03);
        this.username = username;
        this.angulo = a;
        this.atirou = b;
        this.anguloTiro = c;
        
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
       return ("03"+this.username + "," +this.angulo+ "," + this.atirou + "," + this.anguloTiro).getBytes();
    }

    public String getUsername() {
       return username;
    }
    
    public float getAngulo(){
        return this.angulo;
    }
    
    public int getAtirou(){
        return this.atirou;
    }
    
    public double getAnguloTiro(){
        return this.anguloTiro;
    }
    
   
}
