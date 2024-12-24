package Modele.Resource;

import Modele.*;

public class Rock extends Resource {
    private static final int prix = Prix.PRIX_PIERRE;

    public Rock(int quantity) {
        super("Rock", quantity, true, prix);
    }

    public Rock(int quantity, Position pos) {
        super("Rock", quantity, true, prix, pos);
    }

    public Rock clone() {
        return new Rock(this.getQuantity());
    }

    public Rock cloneOne() {
        return new Rock(1);
    }
}
