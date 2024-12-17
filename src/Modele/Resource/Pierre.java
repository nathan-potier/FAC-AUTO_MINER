package Modele.Resource;

import Modele.*;

public class Pierre extends Resource {
    private static final int prix = Prix.PRIX_PIERRE;

    public Pierre(int quantity) {
        super("Pierre", quantity, true, prix);
    }

    public Pierre(int quantity, Position pos) {
        super("Pierre", quantity, true, prix, pos);
    }

    public Pierre clone() {
        return new Pierre(this.getQuantity());
    }

    public Pierre cloneOne() {
        return new Pierre(1);
    }
}
