package Modele;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Modele.Machine.*;
import Modele.Resource.*;
import Vue.VueGenerale;
import Vue.GameListener;


public class Game implements Controle{
    private Carte gameMap;
    private static Player player;
    private Spawn spawn;
    private int width = 15, height = 12;
    private GameListener listener ;
    private Market market;


    public void create() {
        gameMap = new Carte(width, height, player, spawn, market, this);
        gameMap.generateMap(player);
        gameMap.generateRessource(player);
        player.collectResource(new Wood(3)); 
        updateAffichage(true);
      
    }

    public void start(){updateAffichage(true);}

    public void setMap(Carte map){this.gameMap = map;}



    public void addGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    public void notifyMove(Object content, Position start, Position target, String dir) {

        listener.onMove(content, start, target, dir);
        

    }




    @Override
    public void onKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                gameMap.deplacer(player, Direction.UP) ;
                break; 
            case KeyEvent.VK_DOWN:
                gameMap.deplacer(player, Direction.DOWN);
      
                break; 
            case KeyEvent.VK_LEFT:
                gameMap.deplacer(player, Direction.LEFT);
                break; 
            case KeyEvent.VK_RIGHT:
                gameMap.deplacer(player, Direction.RIGHT);
                break;  
            case -1 :
                gameMap.placerMachine(player);
                break;
                
                
            default:
                break;  
        }
    }
    

    public void updateAffichage(boolean b){
        ArrayList<Object> obj = new ArrayList<>();
        ArrayList<Position> pos = new ArrayList<>();
        for (Cell<?>[] cells : gameMap.getGrid()){
            for (Cell<?> c: cells){
                if(c != null && (!(c.getContent().getClass().equals(player.getClass())) || b)  ){

                    
                    obj.add(c.getContent());
                    pos.add(c.getPosition());
                    

                }
            }
        }

        listener.updateObjects(obj, pos);
    }

    public Game(String pseudo){ 

        
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, Spawn.getPos());
        market = new Market(null);
    }

    public Game(Market market, Spawn spawn, Player player){

        this.market = market;
        this.spawn = spawn;
        Game.player = player;


        //this.gameMap = map;
    }
    public Carte getMap(){return gameMap;}
    public int getHeight(){return height;}
    public int getWidth(){return width;}
    public static Player getPlayer(){return player;}
    public Market getMarket() {return market;}
    public void initVue(VueGenerale v){gameMap.setVue(v);}
    public Spawn getSpawn(){return spawn;}
    

    @Override
    public void clickOnCase(Position pos) {
        gameMap.ActionByClick(player, pos);
        if(gameMap.posIsValid(pos) && GameStates.isMoveOn()){
            gameMap.deplacerVersPosition(player, pos);
        }
    }

    public void CallAnnimationRespawn(Position pos, int tempsMS){
        listener.attenteAnimation(pos, tempsMS);

    }

    public ArrayList<Savable> getSavableObject(){
        return gameMap.getSavableObject();
    }

    public ArrayList<Machine> getMachines(){
        return gameMap.getMachines();
    }


    public void stopGame() {
        gameMap.stopUpdateTimer();
        // Arrêter les threads ou timers des récolteuses
        for (Machine machine : getMachines()) {
           
            machine.stopWorking(); // Exemple : méthode qui arrête le thread
        }
        // Autres actions pour arrêter le jeu proprement
    }
    

}




