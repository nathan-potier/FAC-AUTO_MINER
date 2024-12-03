package Modele.Resource;

import java.io.Serializable;

public class Pickaxe extends Resource {

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity);
    }

    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }

}
