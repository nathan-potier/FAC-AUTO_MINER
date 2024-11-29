package Modele.Ressource;

import java.io.Serializable;

public class Pickaxe extends Ressource implements Serializable{

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity);
    }

    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }

}
