package javagame;

import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.geom.Vector2f;
import packets.Packet02Move;
import packets.Packet03Mirar;

public class Player{
    
    
    
    public float  PersonagemPosX = 0;
    public float PersonagemPosY = 0;
    private int PosicaoMouseVelhaX;
    private int PosicaoMouseVelhaY;
    public float targetAng;
    private float velocidade = 0;
    private float velocidadeMirando = 0.1f;
    private Image personagem;
    private String username;
    private boolean Movendo;
    private boolean MouseMoveu;
    public int atirou;
    private int MaxBalas = 10;
    public List<Bullet> balas = new ArrayList<>();
    private int tempoUpdate = 0;
    private static int FIRE_RATE = 300;
    public int current = 0;
    public double anguloTiro;
    private int tempoEntreBalas = 50;
    private  int tempoAtualEntreBalas;
    private int balasAtiradas = 0;
    
     //Vida
    private int vidaUpdate = 100;
    private static int VIDA_RATE = 1000;
    private int vida = 3;
    public int tomou;
    
    public Player(String u, float x, float y) throws SlickException{
        username = u;
        PersonagemPosX = x;
        PersonagemPosY = y;
        
    }
    
    public void init() throws SlickException{
        //personagem = new Image("res/personagem2.png");
        for(int i = 0; i < MaxBalas ; i++){
            balas.add(new Bullet());
        }
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        //Movimento e rotação do personagem (imagem)          
        personagem = new Image("res/personagem6.png");
        int tx = (personagem.getWidth() / 2);
        int ty = (personagem.getHeight() / 2);

        g.rotate(getPersonagemPosX() + tx, getPersonagemPosY() + ty, targetAng);
        g.drawImage(personagem, getPersonagemPosX(), getPersonagemPosY());
        g.rotate(getPersonagemPosX() + tx, getPersonagemPosY() + ty, -targetAng);
        
        g.setColor(Color.black);
        g.drawString(username, getPersonagemPosX(), getPersonagemPosY()-15);
        
        Movendo = false;
        
        //Tiros dado pelo personagem
         for(Bullet b : new ArrayList<>(balas)){
            b.render(gc, sbg, g);
        }
        
        
        
        
        
    }
    
    
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{    
         Input input = gc.getInput();
         MouseMoveu = false;
         
         
        
        
        //Atualizando a posição do personagem             
        Movendo = false;
        
        if (Mouse.isButtonDown(1)){
            this.velocidade = 0.05f;
        }
        else{
            this.velocidade = 0.19f;
        }
        
        if (this.getUsername().equals(Play.play.usuario)){
            
            if(input.isKeyDown(Input.KEY_W)){
                setPersonagemPosY((getPersonagemPosY() - delta * velocidade));
                Movendo = true;
                if(getPersonagemPosY() < -1.799){
                    setPersonagemPosY((int) (getPersonagemPosY() + delta *velocidade));
                }
            }
            if(input.isKeyDown(Input.KEY_S)){
                setPersonagemPosY((getPersonagemPosY() + delta * velocidade));
                Movendo = true;
                if(getPersonagemPosY() > 705.583){
                    setPersonagemPosY((getPersonagemPosY() - delta *velocidade));
                }
            }
            if(input.isKeyDown(Input.KEY_A)){
                setPersonagemPosX((getPersonagemPosX() - delta * velocidade));
                Movendo = true;
                
                if(getPersonagemPosX() < -11.4){
                    setPersonagemPosX((getPersonagemPosX() + delta *velocidade));
                }
            }
            if(input.isKeyDown(Input.KEY_D)){
                setPersonagemPosX((getPersonagemPosX() + delta * velocidade));
                Movendo = true;
               if(getPersonagemPosX() > 954.273){
                    setPersonagemPosX((getPersonagemPosX() - delta *velocidade));
                
            }
            }
        }
           
        
        //Se o personagem moveu enviar pacote para o server
        if (Movendo){
           Packet02Move packet = new Packet02Move(this.getUsername(), this.getPersonagemPosX(), this.getPersonagemPosY());
            packet.writeData(Play.play.socketCliente);
        }
        
        
        //Tiro dado pelo personagem
        
       anguloTiro = Math.atan2(input.getMouseX() - this.getPersonagemPosX(), input.getMouseY() - this.getPersonagemPosY());
       float velocidadeBala = 3000;
       double VelocidadeX = velocidadeBala * Math.cos(anguloTiro);
       double VelocidadeY = velocidadeBala * Math.sin(anguloTiro);
        
        
       tempoUpdate += delta; 
       this.atirou = 0;
        
       
       
       if (this.getUsername().equals(Play.play.usuario)){
            if (tempoUpdate > FIRE_RATE && Mouse.isButtonDown(0) && Mouse.isButtonDown(1)){
                this.atirou = 1;
                //Comentando essa linha as balas estão sendo criadas só nos pacotes, e não localmente
                //balas.add(current, new Bullet(new Vector2f(this.getPersonagemPosX(), this.getPersonagemPosY()), new Vector2f((float) VelocidadeY,(float) VelocidadeX)));
               // current++;
                //pew.play();
                //if (current >= MaxBalas){
                  //  current = 0;

               // }
                tempoUpdate = 0;
            }
            
        }
       
       for(Bullet b : new ArrayList<>(balas)){
                b.update(delta);
       }
       
       //Atualizando o angulo da imagem com o mouse e mandando pacote Mirar
        if (this.getUsername().equals(Play.play.usuario)){
            targetAng = (float) getTargetAngle(getPersonagemPosX(), getPersonagemPosY(), input.getMouseX(), input.getMouseY()); //mudei input

            if ((input.getMouseX() != PosicaoMouseVelhaX) || (input.getMouseY() != PosicaoMouseVelhaY) || (atirou == 1)){

                MouseMoveu = true;
                Packet03Mirar packet = new Packet03Mirar(this.getUsername(), this.targetAng, this.atirou, this.anguloTiro);
                packet.writeData(Play.play.socketCliente);
              
               
                

            }


            PosicaoMouseVelhaX = input.getMouseX();
            PosicaoMouseVelhaY = input.getMouseY();
        
        }
        
         //parde do dano
        vidaUpdate += delta;
        this.tomou = 0;
        
        
        
        
        
        
        
    }
    
    public float getTargetAngle(float startX, float startY, float targetX, float targetY) {
        float dx = targetX - startX;
        float dy = targetY - startY;
        return (float)Math.toDegrees(Math.atan2(dy, dx));
    
}

    public float getDistanceBetween(float startX, float startY, float endX, float endY) {
        float dx = endX - startX;
        float dy = endY - startY;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
    
   
    public float getPersonagemPosX() {
        return PersonagemPosX;
    }

    
    public void setPersonagemPosX(float PersonagemPosX) {
        this.PersonagemPosX = PersonagemPosX;
    }

   
    public float getPersonagemPosY() {
        return PersonagemPosY;
    }

   
    public void setPersonagemPosY(float PersonagemPosY) {
        this.PersonagemPosY = PersonagemPosY;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public int getVida(){
        return this.vida;
    }
    
    public int contarDano() {
        if (vidaUpdate > VIDA_RATE && vida > 0) {
            tomou = 1;
            vida--;
            vidaUpdate = 0;
            return vida;
        }else return -1; 
    }
    
   
}