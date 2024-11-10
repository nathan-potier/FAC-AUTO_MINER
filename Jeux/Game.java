import java.awt.event.KeyEvent;

public class Game implements KeyPressListener {
    private Map gameMap;
    private Player player;
    private Market market;
    private Spawn spawn;
    private int width, height;
    

    public void start() {
        

        gameMap = new Map(width, height, player, spawn);
        gameMap.generateRessource();    


        player.collectResource(new Wood(3));
        player.collectResource(new Wood(3));
        player.collectResource(new Iron(100));
        
        //AffichageTemporaire.affiche(this.getMap().getGrid());
        
        Usine usine = new Usine(player.getInv());

        //System.out.println("\nLancement de la production d'une pioche...");
        usine.produire("Pickaxe");

        // Affichage du stock de l'usine
        usine.afficherStock();

        //System.out.println("\nLancement de la production d'un bloc de fer...");
        usine.produire("Bloc de Fer");

        // Affichage du stock de l'usine
        usine.afficherStock();

        usine.recuperer();

       
        

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
        width = 8;
        height = 5;
        spawn = new Spawn(new Position( width - (width - 2), height - 1));
        player = new Player(pseudo, Spawn.getPos());


    }
    public Map getMap(){return gameMap;}

    public int getHeight(){return height;}
    public int getWidth(){return width;}
    public Player getPlayer(){return player;}
}






