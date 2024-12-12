package Modele.Resource;
import Modele.*;

public class Pickaxe extends Resource {

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity, false, 30);
    }
    public Pickaxe(int quantity, Position pos){
        super("Pickaxe", quantity, false, 20, pos);
    }
    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }
    public Pickaxe cloneOne(){return new Pickaxe(1);}
}
