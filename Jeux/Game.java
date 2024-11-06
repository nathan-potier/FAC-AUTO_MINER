import java.awt.event.KeyEvent;

public class Game implements KeyPressListener {
    private Map gameMap;
    private Player player;
    private Market market;
    private Spawn spawn;
    private int width, height;

    public void start() {

        
        

        gameMap = new Map(width, height, player, spawn);

        Affichage affichage = new Affichage(this);
        AffichageTemporaire.affiche(this.getMap().getGrid());
        this.onKeyPress(height);

    }

    @Override
    public void onKeyPress(int keyCode) {
        // Gère les touches pressées
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                gameMap.déplacer(player, Direction.UP);
                AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_DOWN:
            gameMap.déplacer(player, Direction.DOWN);
            AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_LEFT:
            gameMap.déplacer(player, Direction.LEFT);
            AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_RIGHT:
            gameMap.déplacer(player, Direction.RIGHT);
            AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_ESCAPE:
                System.out.println("Échap pressée - Sortie.");
                System.exit(0);
                break;
            default:
                System.out.println("Autre touche pressée.");
        }
    }

    
    

    public Game(String pseudo){
        width = 8;
        height = 5;
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, spawn.getPos());


    }
    public Map getMap(){return gameMap;}
}






