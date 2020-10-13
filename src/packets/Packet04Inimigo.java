package packets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.JogoCliente;
import network.JogoServer;
import org.newdawn.slick.geom.Vector2f;


public class Packet04Inimigo extends Packet {
    
    private int vida;
    
    private int delta;
    private float posPersonagemX;
    private float posPersonagemY;
    private float posInimigoX;
    private float posInimigoY;
    private int id;
    private float angulo;
   
    
   
    
    
    public Packet04Inimigo(byte[] data){
        super(04);
        String[] dataArray = readData(data).split(",");
        this.vida = Integer.parseInt(dataArray[0]);
        this.delta = Integer.parseInt(dataArray[1]);  
        //this.posPersonagemX = Float.parseFloat(dataArray[2]);
        //this.posPersonagemY = Float.parseFloat(dataArray[3]);
        this.posInimigoX = Float.parseFloat(dataArray[2]);
        this.posInimigoY = Float.parseFloat(dataArray[3]);
        this.id = Integer.parseInt(dataArray[4]);
        this.angulo = Float.parseFloat(dataArray[5]);
       
        
        
    }
    
    public Packet04Inimigo(int v, int t, float px, float py, int id, float angulo){
        super(04);
        this.vida = v;
        this.delta = t;
        //this.posPersonagemX = px;
        //this.posPersonagemY = py;
        this.posInimigoX = px;
        this.posInimigoY = py;
        this.id = id;
        this.angulo = angulo;
        
        
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
       return ("04"+this.vida+","+this.delta+","+this.posInimigoX+","+this.posInimigoY+","+this.id+","+this.angulo).getBytes();
    }

    public int getVida(){
        return this.vida;
    }
    
    public int getDelta(){
        return this.delta;
    }
     
    public float getPersonagemX(){
        return this.posPersonagemX;
    }
      
     public float getPesonagemY(){
        return this.posPersonagemY;
    }
     
    public float getInimigoX(){
        return this.posInimigoX;
        
    }
    
    public float getInimigoY(){
       return this.posInimigoY;
        
    }
     
     public int getInimigo(){
         return this.id;
     }
     
    public float getAngulo(){
        return this.angulo;
    }
}

