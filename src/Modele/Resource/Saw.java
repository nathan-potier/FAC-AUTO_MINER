package Modele.Resource;

import Modele.*;

public class Saw extends Resource {
    private static final int prix = Prix.PRIX_SCIE;

    public Saw(int quantity) {
        super("Saw", quantity, false, prix);
    }

    public Saw(int quantity, Position pos) {
        super("Saw", quantity, false, prix, pos);
    }

    public Saw clone() {
        return new Saw(this.getQuantity());
    }

    public Saw cloneOne() {
        return new Saw(1);
    }
}
