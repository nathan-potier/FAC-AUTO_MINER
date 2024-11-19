
public class Player {
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;
    private boolean ecoute = true;

    public void setEcoute(boolean ecoute) {
        this.ecoute = ecoute;
    }
    public boolean isEcoute(){return this.ecoute;} 

    public void move(Direction direction) {
        // Code pour déplacer le joueur sur la carte
    }
    
    public boolean collectResource(Ressource resource) {
        return inventory.addResource(resource);
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