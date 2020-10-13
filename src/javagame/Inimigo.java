package javagame;


import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.*;
import packets.Packet04Inimigo;


public class Inimigo {

    
    private int id;
    private Image inimigo;
    public Rectangle InimigoRectangle;
    private boolean vivo = true;
    private boolean colisao;
    private int vida = 200;
    private float velocidadeInimigo = 250;
    public Vector2f pos;
    public Vector2f speed;
    public float targetAng;
    public Player alvo;
    
    
    
    public Inimigo(Vector2f pos, int id, Player p){
         this.pos = pos;
         this.id = id;
         InimigoRectangle = new Rectangle(pos.getX(),pos.getY(),45,45);
         this.alvo = p;
    }
    
    public Inimigo(){
        
    }
    
    
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
      inimigo = new Image("res/zumbi_1.png");
      //InimigoRectangle = new Rectangle(pos.getX(),pos.getY(),50,50);
      
    }
    
    public void update (int t, Vector2f posP){
        speed =  posP.sub(pos).getNormal().scale(velocidadeInimigo);
        pos.add(speed.copy().scale(t/3000.0f));
        InimigoRectangle.setX(pos.getX());
        InimigoRectangle.setY(pos.getY());
        
       
        
        
        //Criando e mandando pacotes com a coordenada dos inimigos
       if(Play.play.server == 1){ 
            Packet04Inimigo packetInimigo = new Packet04Inimigo(150, t , pos.getX(), pos.getY(), id, targetAng);
            packetInimigo.writeData(Play.play.socketServer);
       }
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g, Vector2f posP) throws SlickException{
      
      
      //Rotação na direção do personagem
     inimigo = new Image("res/zumbi_8.png");
     if (Play.play.server == 1){
         targetAng = (float) getTargetAngle(pos.getX(), pos.getY(), posP.getX(), posP.getY());
     }
      int tx = (inimigo.getWidth() / 2);
      int ty = (inimigo.getHeight() / 2);
      
      g.rotate(pos.getX() + tx, pos.getY() + ty, targetAng);
      g.drawImage(inimigo, pos.getX(), pos.getY());
      g.rotate(pos.getX() + tx, pos.getY() + ty, -targetAng);
        
    }
    
    public Rectangle getRectangle(){
        return InimigoRectangle;
    }
    
    public boolean getVivo(){
        return vivo;
    }
    
    public void setVivo(boolean vida){
        vivo = vida;
        
    }
    
    public boolean getColisao(){
        return colisao;
    }
    
    public void setColisao(boolean colidiu){
        this.colisao = colidiu;
    }
    
    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
    
    public int getId(){
        return id;
    }
    
  
    public float getTargetAngle(float startX, float startY, float targetX, float targetY) {
        float dx = targetX - startX;
        float dy = targetY - startY;
        return (float)Math.toDegrees(Math.atan2(dy, dx));
    
    }
    
    
}
