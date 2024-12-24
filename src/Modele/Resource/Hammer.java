package Modele.Resource;

import Modele.*;

public class Hammer extends Resource {
    private static final int prix = Prix.PRIX_MARTEAU;

    public Hammer(int quantity) {
        super("Hammer", quantity, false, prix);
    }

    public Hammer(int quantity, Position pos) {
        super("Hammer", quantity, false, prix, pos);
    }

    public Hammer clone() {
        return new Hammer(this.getQuantity());
    }

    public Hammer cloneOne() {
        return new Hammer(1);
    }
}
