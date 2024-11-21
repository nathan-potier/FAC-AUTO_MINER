import java.awt.event.KeyEvent;

public class Game implements Controle {
    private Map gameMap;
    private Player player;
    private Spawn spawn;
    private int width, height;
    

    public void start() {
        gameMap = new Map(width, height, player, spawn);
        gameMap.generateRessource(player);
        player.collectResource(new Wood(3)); 
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
}