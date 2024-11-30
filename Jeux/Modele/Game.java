package Modele;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


import Vue.VueGenerale;
import Vue.GameListener;
import Modele.Ressource.*;

public class Game implements Controle, Serializable  {
    private Map gameMap;
    private Player player;
    private Spawn spawn;
    private int width, height;
    private GameListener listener ;
    private Market market;


    public void start() {

        gameMap = new Map(width, height, player, spawn, market, this);
        gameMap.generateRessource(player);
        player.collectResource(new Wood(3)); 
        updateAffichage(true);

        
    }



    public void addGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    private void notifyMove(Object content, Position start, Position target, String dir) {
        
        listener.onMove(content, start, target, dir);
        

    }

    public void start(Map m) {
        

        gameMap = m;



    }



    @Override
    public void onKeyPress(int keyCode) {
        Position startPos = player.getPos();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (gameMap.déplacer(player, Direction.UP) ){
                    notifyMove(player, startPos, player.getPos(), "up");
                    updateAffichage(false);
                }
                break; 
            case KeyEvent.VK_DOWN:
                if (gameMap.déplacer(player, Direction.DOWN) ){
                    notifyMove(player, startPos, player.getPos(), "down");
                    updateAffichage(false);
                }
                break; 
            case KeyEvent.VK_LEFT:
                if (gameMap.déplacer(player, Direction.LEFT) ){
                    notifyMove(player, startPos, player.getPos(), "left");
                    updateAffichage(false);
                }
                break; 
            case KeyEvent.VK_RIGHT:
                if (gameMap.déplacer(player, Direction.RIGHT) ){
                    notifyMove(player, startPos, player.getPos(), "right");
                    updateAffichage(false);
                }
                break;  
            default:
                System.out.println("Autre touche pressée.");
                break;  
        }
    }
    

    public void updateAffichage(boolean b){
        ArrayList<Object> obj = new ArrayList<>();
        ArrayList<Position> pos = new ArrayList<>();
        for (Cell[] cells : gameMap.getGrid()){
            for (Cell c: cells){
                if(c != null && (!(c.getContent().getClass().equals(player.getClass())) || b)  ){

                    
                    obj.add(c.getContent());
                    pos.add(c.getPosition());
                    

                }
            }
        }

        listener.updateObjects(obj, pos);
    }

    public Game(String pseudo){
        width = 13;
        height = 10;
        market = new Market(new Position(width - 1, (int)(height / 2)));
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, Spawn.getPos());
    }
    public Map getMap(){return gameMap;}
    public int getHeight(){return height;}
    public int getWidth(){return width;}
    public Player getPlayer(){return player;}
    public Market getMarket() {return market;}
    public void initVue(VueGenerale v){gameMap.setVue(v);}
    

    public Object getObjectAtPosition(int x, int y) {
        // Retourne l'objet à la position (x, y) de la gameMap
        Cell c = gameMap.getGrid()[y][x];
        if (c != null){
            return c.getContent();
        }
        return c;
    }
    


    public void sauvegarderCarte(String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(this);  // Sérialise l'objet Game complet
            System.out.println("Carte sauvegardée dans " + nomFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    public static Game chargerCarte(String nomFichier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            return (Game) ois.readObject();  // Charge l'objet Game complet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();  // Affiche la pile d'erreur complète
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void clickOnCase(Position pos) {
        gameMap.ActionByClick(player, pos);
        
    }

    public void CallAnnimationRespawn(Position pos, int tempsMS){
        listener.attenteAnimation(pos, tempsMS);
    }


}




