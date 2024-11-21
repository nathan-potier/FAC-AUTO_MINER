import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game implements Controle {
    private Map gameMap;
    private Player player;
    private Spawn spawn;
    private int width, height;
    private GameListener listener ;

    public void start() {
        gameMap = new Map(width, height, player, spawn);
        gameMap.generateRessource(player);
        player.collectResource(new Wood(3)); 
        updateAffichage(true);
        
    }

    public void addGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    private void notifyMove(Object content, Position start, Position target) {
        
        listener.onMove(content, start, target);
        
    }


    @Override
    public void onKeyPress(int keyCode) {
        // Gère les touches pressées
        Position startPos = player.getPos();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (gameMap.déplacer(player, Direction.UP)){
                    notifyMove(player,startPos, player.getPos());
                    updateAffichage(false);
                }else{updateAffichage(true);}
                
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_DOWN:
                if (gameMap.déplacer(player, Direction.DOWN)){
                    notifyMove(player,startPos, player.getPos());
                    updateAffichage(false);
                }else{updateAffichage(true);}
                
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_LEFT:
                if (gameMap.déplacer(player, Direction.LEFT)){
                    notifyMove(player,startPos, player.getPos());
                    updateAffichage(false);
                }else{updateAffichage(true);}
                //AffichageTemporaire.affiche(this.getMap().getGrid());
                break;
            case KeyEvent.VK_RIGHT:
                if (gameMap.déplacer(player, Direction.RIGHT)){
                    notifyMove(player,startPos, player.getPos());
                    updateAffichage(false);
                }else{updateAffichage(true);}
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

    public void updateAffichage(boolean b){
        ArrayList<Object> obj = new ArrayList<>();
        ArrayList<Position> pos = new ArrayList<>();
        for (Cell[] cells : gameMap.getGrid()){
            for (Cell c: cells){
                if(c != null && (!(c.getContent().getClass().equals(player.getClass())) || b) ){
                    
                    obj.add(c.getContent());
                    pos.add(c.getPosition());
                    

                }
            }
        }

        listener.updateObjects(obj, pos);
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
    public Object getObjectAtPosition(int x, int y) {
        // Retourne l'objet à la position (x, y) de la gameMap
        Cell c = gameMap.getGrid()[y][x];
        if (c != null){
            return c.getContent();
        }
        return c;
    }
    
}