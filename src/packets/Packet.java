/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import network.JogoCliente;
import network.JogoServer;

/**
 *
 * @author Pichau
 */
public abstract class Packet {
    public static enum PacketTypes{
        INVALID(-1),
        LOGIN(00),
        DISCONNECT(01),
        MOVE(02),
        MIRAR(03),
        INIMIGO(04),
        CriarInimigo(05);
        
        private int packetId;
        private PacketTypes(int packetId){
            this.packetId = packetId;
        }
        
        public int getId(){
            return packetId;
        }
        
    }
    
    public byte packetId;
    
    public Packet(int packetId){
        this.packetId = (byte) packetId;
    }
    
    public abstract void writeData(JogoCliente cliente);
    
    public abstract void writeData(JogoServer server);
    
    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2);
    }
    
    public abstract byte[] getData();
    
    public static PacketTypes lookupPackets(String packetId){
        try{
            return lookupPackets(Integer.parseInt(packetId));
        
        }catch(NumberFormatException e){
            return PacketTypes.INVALID;
        }
        
    }
        

    public static PacketTypes lookupPackets(int id){
        for (PacketTypes p : PacketTypes.values()){
            if (p.getId() == id){
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
    
        
  
}
