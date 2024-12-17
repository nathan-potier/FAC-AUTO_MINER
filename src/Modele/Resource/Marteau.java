package Modele.Resource;

import Modele.*;

public class Marteau extends Resource {
    private static final int prix = Prix.PRIX_MARTEAU;

    public Marteau(int quantity) {
        super("Marteau", quantity, false, prix);
    }

    public Marteau(int quantity, Position pos) {
        super("Marteau", quantity, false, prix, pos);
    }

    public Marteau clone() {
        return new Marteau(this.getQuantity());
    }

    public Marteau cloneOne() {
        return new Marteau(1);
    }
}
