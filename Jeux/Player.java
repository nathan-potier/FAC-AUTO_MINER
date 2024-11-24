import java.io.Serializable;

public class Player implements Serializable{
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;
    private boolean ecoute = true;
    private int money;

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
    public boolean addMachine(Machine machine) {
        return inventory.addMachine(machine);
    }

    public boolean hasMachine(Machine m){
        return inventory.hasMachine(m);
    }
    public void removeMachine(){
        inventory.removeMachine();
    }


    public Player(String pseudo, Position position){
        this.pseudo = pseudo;
        inventory = new Inventory();
        this.position = position;
        money = 10;
    }

    public boolean hasResource(Ressource ressource){
        for (Ressource r : inventory.getRessources()){
            if (r.getName().equals(ressource.getName())){
                return true;
            }
        }
        return false;
    }

    public void removeResource(Ressource r){
        inventory.removeResource(r, 1);
    }

    public void setPos(Position p){
        this.position = p;
    }
    public Position getPos(){return position;}

    public Inventory getInv() {return inventory;}
    public String getPseudo(){return pseudo;}
    public void setMoney(int m){money = m;}
    public int getMoney(){return money;}
    
    
}