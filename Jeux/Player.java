
public class Player {
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;

    public void move(Direction direction) {
        // Code pour déplacer le joueur sur la carte
    }
    
    public void collectResource(Ressource resource) {
        inventory.addResource(resource);
    }

    public Player(String pseudo, Position position){
        this.pseudo = pseudo;
        inventory = new Inventory();
        this.position = position;
    }

    public void setPos(Position p){
        this.position = p;
    }
    public Position getPos(){return position;}

    public Inventory getInv() {return inventory;}
    
    
}