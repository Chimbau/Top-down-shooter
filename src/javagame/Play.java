package javagame;

import java.io.IOException;
import static java.lang.Math.sqrt;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import network.JogoCliente;
import network.JogoServer;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.*;
import packets.Packet00Login;
import packets.Packet01Disconnect;
import packets.Packet04Inimigo;
import packets.Packet05CriarInimigo;

public class Play extends BasicGameState{
    
    //Objetos
    private Bullet[] bullets;
    List<Inimigo> inimigos;
    List<Player> players = new ArrayList<>();
    public PlayerMP personagem1;
    public Player personagem2;
    
    boolean personagem1Morto = false;
    
    public static Play play;
    
    //Cliente e servidor
    public JogoCliente socketCliente;
    public JogoServer socketServer;
    Packet00Login login;
    
    
    private Rectangle PlayerRectangle;
    
    //Sons
    private Sound pew;
    
     
    //Imagens
    Image Map;
    Image personagem;
    Image Mira;
    Image Morte;
    Image Corpo;
    
    //Variaveis  
    private boolean IniciarServidor;
    boolean quit = false;
    boolean collision = false;
    private String ip;
    
    private static int FIRE_RATE = 250;
    private int t =0;
    private int current = 0;
    int[] duration = {200, 200};
    float MapX = 0;
    float MapY = 0;
    float velocidade = .3f;
    int meioHorizontal, meioVertical;
    public int server = 0;
    public String usuario;
    float velocidadePlayers = 0.3f;
    private int numeroDeInimigos = 0;
    private boolean inicioDoJogo = false;
    private boolean jaApertou = false;
    private int levaAtual = 1;
    private int levaMaxima = 4;
    private int tempoEntreLevasMaximo = 3000;
    private int tempoEntreLevasAtual = 0;
    private int numeroRandomX;
    private int numeroRandomAux;
    private int numeroRandomY;
    private int tempoEntreBalas = 0;
   
    //Fontes
    java.awt.Font fontLeva;
    TrueTypeFont ttfLeva;
   
    java.awt.Font fontInicio;
    TrueTypeFont ttfInicio;
    
    java.awt.Font fontVida;
    TrueTypeFont ttfVida;
    
    Color color;
    
    
    
    
    
    
    public Play(int state) throws SlickException{
        
        play = this;
        
        if (JOptionPane.showConfirmDialog(null ,"Voce deseja hostear um servidor?", "", JOptionPane.YES_NO_OPTION) == 0){
            server = 1;
        }
        else{
            ip = JOptionPane.showInputDialog("Digite o IP do servidor");
        }

        usuario = JOptionPane.showInputDialog("Digite um nome de usuario");
        if (usuario == null){
            usuario = "Anonimo";
        }
        
        /*System.out.println("Voce quer rodar o servidor?");
        Scanner s = new Scanner(System.in);
        Scanner l = new Scanner(System.in);
        server = s.nextInt();
        System.out.println("Digite o nome do usuario : ");
        usuario = l.next();*/

        
        //Iniciar Servidor
        if (server == 1){
           try {
               socketServer = new JogoServer(this);
           } catch (SocketException ex) {
               Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
           } catch (UnknownHostException ex) {
               Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
           }
           socketServer.start();
        }
              
         
        
        personagem1 = new PlayerMP(null, -1, usuario, 500, 500);
        players.add(personagem1);
        
        inimigos = new ArrayList<>();
        /*for(int i = 0; i < 5; i++){
            inimigos.add(new Inimigo(new Vector2f(i*300, 500), numeroDeInimigos));
            numeroDeInimigos++;
        }*/
        
        
        
        
        
        //Iniciar Cliente
        try {
            socketCliente = new JogoCliente(this, ip);
        } catch (SocketException ex) {
            Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
        }
        socketCliente.start();
        
        login = new Packet00Login(personagem1.getUsername(), personagem1.PersonagemPosX, personagem1.PersonagemPosY);
        
        if (socketServer != null){
            try {
                socketServer.addConection((PlayerMP)personagem1, login);
            } catch (IOException ex) {
                Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        login.writeData(socketCliente);
        
       
        
        
        
       
    
    }
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
        
      
        fontLeva = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        ttfLeva = new TrueTypeFont(fontLeva, true);   
        
        fontInicio = new java.awt.Font("Verdana", java.awt.Font.BOLD, 30);
        ttfInicio = new TrueTypeFont(fontInicio, true);    
        
        fontVida = new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);
        ttfVida = new TrueTypeFont(fontVida, true);                 
        
        Map = new Image("res/map3.jpg");
                          
        Mira = new Image("res/crosshair.png");
        Morte = new Image("res/game_over.png");
        
        Corpo = new Image("res/ded.png");
        
       
       // Image crosshair = new Image("res/crosshair.png");
        //gc.setMouseCursor(crosshair, 0, 0);
       //pew = new Sound("res/Pew.wav");
        meioHorizontal = gc.getWidth() / 2;
        meioVertical = gc.getHeight() / 2;
        
        //Retangulo de colisao do personagem
         PlayerRectangle = new Rectangle(personagem1.getPersonagemPosX(),personagem1.getPersonagemPosY(),50,50);
        
        
        //balas
        /*bullets = new Bullet[8];
        for(int i = 0; i < bullets.length ; i++){
            bullets[i] = new Bullet();                    
        }*/
                              
        
        //Lista de inimigos 
        /*inimigos = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            inimigos.add(new Inimigo(new Vector2f(i*300, 500), numeroDeInimigos));
            numeroDeInimigos++;
        }
        for(Inimigo i : inimigos){
            i.init(gc, sbg);
        }*/
        
        personagem1.init();
        
        
        
        
     
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
       
        //Mapa
        Map.draw(MapX,MapY);
        //g.drawString("Personagem X: "+personagem1.getPersonagemPosX() +"\nPersonagem Y: " +personagem1.getPersonagemPosY(), 800, 50);
        
        //Leva
        
        
        ttfLeva.drawString(900,5, "Leva "+(levaAtual-1)+"/"+levaMaxima, Color.black);
        if (personagem1.getVida() == 3){
            color = Color.green;
        }
        if (personagem1.getVida() == 2){
            color = Color.orange;
        }
         if (personagem1.getVida() <= 1){
            color = Color.red;
        }
        ttfVida.drawString(650,5, "Vidas Restantes : "+personagem1.getVida(), color);
        
        
        //Imagem do persongaem e rotação          
        for (Player p : new ArrayList<>(players)){
            p.render(gc, sbg, g);
        }
       
        
        //Comentado para parar de encher o saco
        //Lista de inimigos 
        
        for(Inimigo i : new ArrayList<>(inimigos)){
            i.render(gc, sbg, g, new Vector2f(i.alvo.PersonagemPosX, i.alvo.PersonagemPosY));
        }
        
        
        //Menu do Esc
        if (quit == true){
            g.setColor(Color.black);
            g.drawString("Voltar a jogar (R)", meioHorizontal , meioVertical - 100);
            //g.drawString("Menu Princioal(M)", meioHorizontal , meioVertical - 50);
            g.drawString("Fechar o jogo (Q) ", meioHorizontal , meioVertical -50);
            if (quit == false){
                g.clear();             
            }
            
        }
        
        //Inicio do Jogo
        
        if (server == 1){
            if (!inicioDoJogo){
                //g.drawString("Aperta I para inciar a partida", meioHorizontal, meioVertical+200);
                ttfInicio.drawString(270, 100, "Aperte I para inciar a partida", Color.black);
                
            }
            if(inicioDoJogo){
                 ttfInicio.drawString(0, 0, "");
            }
            
            
        }
        
         if (personagem1Morto == true) {
            Morte.draw(100, 300);
            
        }
        
         
        
        
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        Input input = gc.getInput();
       
        t =delta;
        tempoEntreBalas += delta;
        
         System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
        
        
        //for(Inimigo i : inimigos){
          //  i.init(gc, sbg);
        //}
        
       //Comentado pq n tava funcionando, iniciação das imagens foi passada pro render do player
        //for (Player p : new ArrayList<>(players)){
        //    p.init();
       // }
        
        
        //Lista de personagens
        for (Player p : players){
            p.update(gc, sbg, delta);
        }
        
        //Retangulo de colisão do personagem
        PlayerRectangle.setX(personagem1.getPersonagemPosX());
        PlayerRectangle.setY(personagem1.getPersonagemPosY());
        
        
       //Bala
       
       double angulo = Math.atan2(input.getMouseX() - personagem1.getPersonagemPosX(), input.getMouseY() - personagem1.getPersonagemPosY());
       float velocidadeBala = 3000;
       double VelocidadeX = velocidadeBala * Math.cos(angulo);
       double VelocidadeY = velocidadeBala * Math.sin(angulo);
        
       //Comentado para parar de encher o saco
       
       
       //Lista de inimigos    
       for(Inimigo i : new ArrayList<>(inimigos)){
            boolean impedido = false;
            float auxX = 0;
            float auxY = 0;
           if (server == 1){
               for (Inimigo i2 :  new ArrayList<>(inimigos)) {
                    if (i != i2) {
                        if (overlaps(i.getRectangle(),i2.getRectangle())) {
                            //auxX = i2.pos.getX()+12;
                            //auxY = i2.pos.getY()+12;
                            impedido = true;
                            //Vector2f vaux = i.pos;
                            Vector2f vaux = new Vector2f(i2.getRectangle().getX(),i2.getRectangle().getY());
                            Vector2f vaux2 = new Vector2f(i.getRectangle().getX(),i.getRectangle().getY());
                            if(vaux.getTheta()>vaux2.getTheta()){
                                vaux.add(3.141592/6);
                                vaux2.sub(3.141592/6);
                            }else{
                                vaux2.add(3.141592/6);
                                vaux.sub(3.141592/6);
                            }
                            i.update(-delta, vaux);
                            i2.update(-delta,vaux2);
                        }
                    }
                }
                for (Player p : players) {
                    Rectangle raux = new Rectangle(p.getPersonagemPosX(),p.getPersonagemPosY(),40,40);
                    if (overlaps(i.getRectangle(),raux)) {
                        i.update(-delta, new Vector2f(personagem1.getPersonagemPosX(), personagem1.getPersonagemPosY()));
                    }
                }
                i.update(delta, new Vector2f(i.alvo.PersonagemPosX, i.alvo.PersonagemPosY));
                
           }
        }
       
        Sound dano = new Sound("res/oof.wav");
        Sound morte = new Sound("res/death.wav");
       
         for (Player p : new ArrayList<>(players)) {
            Rectangle raux = new Rectangle(p.getPersonagemPosX(),p.getPersonagemPosY(),40,40);
            for (Inimigo i : new ArrayList<>(inimigos)) {
                if (overlaps(i.getRectangle(),raux)) {
                    int aux = p.contarDano();
                    if (aux != -1) {
                        if (aux > 0) {
                            dano.play();
                        } else {
                            morte.play();
                            if (p.getUsername() == this.usuario){
                                personagem1Morto = true;
                               
                            }
                            
                            players.remove(p);
                            
                            
                        }
                    }
                }
            }
        }
         
       
       
       //Colisão
       collision = false;
       for (Inimigo i : inimigos){
           i.setColisao(false);
       }
       
     
       
       for (Player p: players){
           for(Bullet b : new ArrayList<>(p.balas)){
                if (b.isAktiv()){
                    for(Inimigo i2 : inimigos){
                         if(i2.getRectangle().intersects(b.getBulletRectangle())){
                             collision = true;
                             i2.setColisao(true);
                             b.setAktiv(false);
                         }
                    }
                }     
           }
       }
       
           
       if (collision){
          RemoverInimigo();
       }

       
      
        
        //Esc Menu
        if(input.isKeyDown(Input.KEY_ESCAPE)){
            quit = true;
        }
        
        //Esc Menu aberto
        if (quit == true){
            if(input.isKeyDown(Input.KEY_R)){
                quit = false;
            }
            if(input.isKeyDown(Input.KEY_M)){
                sbg.enterState(0);
            }
            if(input.isKeyDown(Input.KEY_Q)){
                Packet01Disconnect packet = new Packet01Disconnect(this.personagem1.getUsername());
                packet.writeData(this.socketCliente);              
                gc.exit();
               
            }
            
        }
        
        //Começar o Jogo
        if (server == 1){
            if (input.isKeyPressed(Input.KEY_I) && jaApertou == false){
                inicioDoJogo = true;
                Packet05CriarInimigo packet = new Packet05CriarInimigo(1);
                packet.writeData(this.socketServer);   
                jaApertou = true;
            }
          
        }
        
        //Funcionamento do sistema de levas
        if (inimigos.isEmpty() && jaApertou && levaAtual <= levaMaxima){
            
            tempoEntreLevasAtual += delta;
            if (tempoEntreLevasAtual >= tempoEntreLevasMaximo){
                Packet05CriarInimigo packet = new Packet05CriarInimigo(1);
                packet.writeData(this.socketServer);
                tempoEntreLevasAtual = 0;
                
            }
            
        }
        
        // System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
        
    }
    
    //Outras funções
    
    public int getID(){
        return 1;
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
    
    public void adicionarPlayer(Player player) throws SlickException{
        players.add(player);
       // player.init();
    }

    public void removePlayerMP(String username) {
        int index = 0;
        for (Player p : players){
            if (p instanceof PlayerMP && ((PlayerMP)p).getUsername().equals(username)){
                break;                
            }
            index++;
        }
        if (this.players.size() > index){
            this.players.remove(index);
        }
    }
    
    private int getPlayerMPIndex(String username){
        int index = 0;
        for (Player p : players){
            if(p instanceof PlayerMP && ((PlayerMP)p).getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }
    
    public void movePlayer(String username, float x, float y){
        int index = getPlayerMPIndex(username);
        this.players.get(index).PersonagemPosX = x;
        this.players.get(index).PersonagemPosY = y;
    }
    
    public void MoverMira(String username, float angulo, int atirou, double anguloTiro){
        int index = getPlayerMPIndex(username);
        float velocidadeBala = 3000;
        double VelocidadeX = velocidadeBala * Math.cos(anguloTiro);
        double VelocidadeY = velocidadeBala * Math.sin(anguloTiro);
        
        this.players.get(index).targetAng = angulo;
        if ((atirou == 1)){
            
            this.players.get(index).balas.add(this.players.get(index).current, new Bullet(new Vector2f(this.players.get(index).getPersonagemPosX(),
                    this.players.get(index).getPersonagemPosY()), new Vector2f((float) VelocidadeY,(float) VelocidadeX)));
            this.players.get(index).current++;
            
            
            
            if (this.players.get(index).current>= 10){
               this.players.get(index).current = 0;        
           }
        }
        
    }
    
    private int getInimigoIndex(int id){
        int index = 0;
        if (!inimigos.isEmpty()){
            for (Inimigo i : new ArrayList<>(inimigos)){
                if(i instanceof Inimigo && i.getId() == id){
                    break;
                }
                index++;
            }
            return index;
        }
        else{
            return -1;
        }
    }
    
    public synchronized void MoverInimigo(int vida, int delta, float inimigoX, float inimigoY, int id, float angulo){
        //Vector2f posP = new Vector2f(personagemX, personagemY); 
        int index = getInimigoIndex(id);
       
        if (index != -1){
            if (index < inimigos.size()){
                /*float posX = inimigos.get(index).pos.getX();
                float posY = inimigos.get(index).pos.getY();
                Vector2f speed;
                Vector2f pos = new Vector2f(posX, posY);
                float velocidadeInimigo = 200;
                float angulo = (float) getTargetAngle(pos.getX(), pos.getY(), posP.getX(), posP.getY());
                this.inimigos.get(index).targetAng = angulo;
                speed =  posP.sub(pos).getNormal().scale(velocidadeInimigo);
                inimigos.get(index).pos.add(speed.copy().scale(delta/3000.0f));*/
                if (inimigos.get(index) != null){
                    inimigos.get(index).InimigoRectangle.setX(inimigoX);
                    inimigos.get(index).InimigoRectangle.setY(inimigoY);
                    inimigos.get(index).pos = new Vector2f(inimigoX, inimigoY);
                    inimigos.get(index).targetAng = angulo;
                }
                
            }
        }
             
    }
    
    public void CriarCoordenadaRandom(){
        Random rand = new Random(); 
        numeroRandomAux = rand.nextInt(4)+1;;
        switch(numeroRandomAux){
            case 1:
                numeroRandomX = rand.nextInt(1000);
                numeroRandomY = rand.nextInt(100)-200;
                break;
            case 2:
                numeroRandomX = rand.nextInt(100)+1050;
                numeroRandomY = rand.nextInt(750);
                break;
            case 3:
                numeroRandomX = rand.nextInt(1000);
                numeroRandomY = rand.nextInt(100)+800;
                break;
            case 4:
                numeroRandomX = rand.nextInt(100)-200;
                numeroRandomY = rand.nextInt(900);
                break;
        }
     
        
    }

    public Player DistanciaMinima(int nx, int ny) throws SlickException{
        int distancia = 10000;
        float x, y;
        Player minimo = new Player("exemplo", 2000,2000);
        for (Player p : players){
            x = (nx - p.PersonagemPosX);
            y = (ny - p.PersonagemPosY);
            if (sqrt(x*x + y*y) < distancia){
                distancia = (int) sqrt(x*x + y*y);
                minimo = p;
            }
           
        }
        return minimo;
    }

    public void CriarInimigo(int criado) throws SlickException {
        if (criado == 1){
                
             for(int i = 0; i < (levaAtual*3+2); i++){
                    CriarCoordenadaRandom();
                    Player p = DistanciaMinima(numeroRandomX, numeroRandomY);
                    inimigos.add(new Inimigo(new Vector2f(numeroRandomX, numeroRandomY), numeroDeInimigos, p));
                    numeroDeInimigos++;
             }
             levaAtual++;
              
        }
    }
    
    private synchronized void RemoverInimigo(){
         for (int i = 0; i <inimigos.size(); i++){
               if (inimigos.get(i).getColisao()){
                   inimigos.get(i).setVida(inimigos.get(i).getVida() - 60);
                   if (inimigos.get(i).getVida() < 0){
                       inimigos.remove(i);
                       i--;
                   }
               }
           }
    }
    
    public boolean overlaps(Rectangle r, Rectangle r2) {
        return r2.getX() < r.getX() + r.getWidth() && r2.getX() + r2.getWidth() > r.getX() && r2.getY() < r.getY() + r.getHeight() && r2.getY() + r2.getHeight() > r.getY();
    }
    
    
       
}
    


    

