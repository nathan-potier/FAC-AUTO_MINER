import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game {
    private Map gameMap;
    private Player player;
    private Market market;
    private Spawn spawn;
    private int width, height;

    public void start() {

        
        

        gameMap = new Map(width, height, player);

        Affichage affichage = new Affichage();
        AffichageTemporaire.affiche(this.getMap().getGrid());


    }
    

    public Game(String pseudo){
        width = 8;
        height = 5;
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, spawn.getPos());


    }
    public Map getMap(){return gameMap;}
}






