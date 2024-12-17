package Modele.Resource;
import Modele.*;

public class Pickaxe extends Resource {
    private static final int prix = 15;

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity, false, prix);
    }
    public Pickaxe(int quantity, Position pos){
        super("Pickaxe", quantity, false, prix, pos);
    }
    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }
    public Pickaxe cloneOne(){return new Pickaxe(1);}
}
