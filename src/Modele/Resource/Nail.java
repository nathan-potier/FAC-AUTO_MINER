package Modele.Resource;

import Modele.*;

public class Nail extends Resource {
    private static final int prix = Prix.PRIX_CLOU;

    public Nail(int quantity) {
        super("Nail", quantity, false, prix);
    }

    public Nail(int quantity, Position pos) {
        super("Nail", quantity, false, prix, pos);
    }

    public Nail clone() {
        return new Nail(this.getQuantity());
    }

    public Nail cloneOne() {
        return new Nail(1);
    }
}
