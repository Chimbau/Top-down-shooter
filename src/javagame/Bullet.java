/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;

/**
 *
 * @author Pichau
 */
public class Bullet {

    
    
    private Rectangle bulletRectangle;
   
    private Vector2f pos;
    private Vector2f speed;
    private int lived = 0;
    private float targetAng;
    
    
    private boolean aktiv = true;
    
    private static int MAX_LIFETIME = 2000;
    
    public Bullet(Vector2f pos, Vector2f speed){
        this.pos = pos;
        this.speed = speed;
        bulletRectangle = new Rectangle(this.pos.getX(), this.pos.getY(), 10, 10);
    }
    
    public Bullet(){
        aktiv = false;
       
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
       
    }
    
    public void update (int t){
        if (isAktiv()){
            pos.add(speed.copy().scale(t/1000.0f));
             bulletRectangle = new Rectangle(pos.getX(), pos.getY(), 10, 10);
            
            lived += t;
            if(lived > MAX_LIFETIME) {
                setAktiv(false);
            }
        }
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        //Input input = gc.getInput();
        //Usando um circulo amarela por enquanto
        if (isAktiv()){
            //targetAng = (float) getTargetAngle(pos.getX(), pos.getY(), input.getMouseX(), input.getMouseY());
            g.setColor(Color.red);
            //g.rotate(pos.getX(), pos.getY(), targetAng);
            g.fillOval(pos.getX()+20, pos.getY()+20, 10, 10);
            //g.rotate(pos.getX(), pos.getY(), -targetAng);
           
            
            g.setColor(Color.white);
        }
        
    }
    
    public boolean isAktiv(){
        return aktiv;
    }

    public Rectangle getBulletRectangle() {
        return bulletRectangle;
    }
    
    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
    
    public void Desativar(){
        this.setAktiv(false);
    }
    
     public float getTargetAngle(float startX, float startY, float targetX, float targetY) {
        float dx = targetX - startX;
        float dy = targetY - startY;
        return (float)Math.toDegrees(Math.atan2(dy, dx));
    
    }
    
    
}
