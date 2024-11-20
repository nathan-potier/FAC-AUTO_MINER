import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Game implements Controle, Serializable  {
    private Map gameMap;
    private Player player;
    private Market market;
    private Spawn spawn;
    private int width, height;
    

    public void start() {
        

        gameMap = new Map(width, height, player, spawn);
        gameMap.generateRessource(player);
        player.collectResource(new Wood(3));


    }

    public void start(Map m) {
        

        gameMap = m;



    }



    @Override
    public void onKeyPress(int keyCode) {
        // Gère les touches pressées
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                gameMap.déplacer(player, Direction.UP);
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_DOWN:
                gameMap.déplacer(player, Direction.DOWN);
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_LEFT:
                gameMap.déplacer(player, Direction.LEFT);
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_RIGHT:
                gameMap.déplacer(player, Direction.RIGHT);
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_ESCAPE:
                //System.out.println("Échap pressée - Sortie.");
                System.exit(0);
                break;
            case KeyEvent.VK_SPACE:
                //System.out.println("Inventaire : ");
                //player.getInv().displayInventory();
                break;
            
            default:
                System.out.println("Autre touche pressée.");
        }
        
    }

    
    

    public Game(String pseudo){
        width = 10;
        height = 8;
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, Spawn.getPos());


    }
    public Map getMap(){return gameMap;}

    public int getHeight(){return height;}
    public int getWidth(){return width;}
    public Player getPlayer(){return player;}

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
}






