
public class Player {
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;

    public void move(Direction direction) {
        // Code pour déplacer le joueur sur la carte
    }
    
    public void collectResource(Ressource resource) {
        // Code pour la récolte des ressources
    }

    public Player(String pseudo, Position position){
        this.pseudo = pseudo;
        inventory = new Inventory();
        this.position = position;
    }
}