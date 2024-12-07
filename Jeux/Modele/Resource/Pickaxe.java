package Modele.Resource;

public class Pickaxe extends Resource {

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity);
    }

    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }
    public Pickaxe cloneOne(){return new Pickaxe(1);}
}
