package javagame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;

public class Menu extends BasicGameState{
    
    public String mouse = "No input";
    
    Image PlayNow;
    Image exitGame;
    
    Image PlayNow2;
    Image exitGame2;
    
    java.awt.Font fontInstrucao;
    TrueTypeFont ttfInstrucao;
    
    
    
    
    
    public Menu(int state){
        
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        PlayNow = new Image("res/Play2.png");
        exitGame = new Image("res/Sair.png");
        
       PlayNow2 = new Image("res/Play3.png");
       exitGame2 = new Image("res/Sair2.png");
       
        fontInstrucao = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        ttfInstrucao = new TrueTypeFont(fontInstrucao, true);    
        
        
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        int xpos = Mouse.getX();
        int ypos = Mouse.getY();
        
        g.drawString("Zombie YZ Mega Survival Battle Royale Special 2 (early access)", 250, 50);
        PlayNow.draw(350, 100);
        exitGame.draw(350, 220);
        ttfInstrucao.drawString(420, 400, "INSTRUÇÕES ");
        ttfInstrucao.drawString(100 , 460, "Andar : W,A,S,D");
        ttfInstrucao.drawString(100 , 510, "Mirar  : Botão direito do Mouse");
        ttfInstrucao.drawString(100 , 560, "Atirar : Botão esquerdo do mouse enquanto estiver mirando");
        ttfInstrucao.drawString(100, 610, "Menu  : Esc");
        
       //g.drawString("Conectar", 330, 280); //Botões não usados por enquanto
        //g.drawString("Hostear", 330, 320);
        
        //play
        if((xpos > 350 && xpos < 650)&&(ypos > 590 && ypos < 670)){
           PlayNow2.draw(350,100);
           
           
        }
        
        if((xpos > 350 && xpos < 650)&&(ypos > 470 && ypos < 550)){
            exitGame2.draw(350, 220);
        }
        
        
        
        //Coordenadas do mouse
        //g.drawString(mouse, 10, 30);
        
    }
    
    
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        int xpos = Mouse.getX();
        int ypos = Mouse.getY();
        
        //botão de play
        if((xpos > 350 && xpos < 650)&&(ypos > 590 && ypos < 670)){
            if(Mouse.isButtonDown(0)){
                sbg.enterState(1);
            }
        }
        
        //botão de sair
        if((xpos > 350 && xpos < 650)&&(ypos > 470 && ypos < 550)){
            if(Mouse.isButtonDown(0)){
                System.exit(0);
            }
        }
        
        /*if((xpos > 330 && xpos < 532)&&(ypos > 460 && ypos < 488)){
            if(Mouse.isButtonDown(0)){
                sbg.enterState(2);
            }
        }
       
        if((xpos > 330 && xpos < 532)&&(ypos > 426 && ypos < 455)){
            if(Mouse.isButtonDown(0)){
                sbg.enterState(3);
            }
        }*/
        
        mouse = "Mouse position x: " + xpos + " y : " + ypos;
        
    }
    
    public int getID(){
        return 0;
    }
    
}
