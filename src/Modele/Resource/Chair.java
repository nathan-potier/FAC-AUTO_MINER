package Modele.Resource;

import Modele.*;

public class Chair extends Resource {
    private static final int prix = Prix.PRIX_CHAISE;

    public Chair(int quantity) {
        super("Chair", quantity, true, prix);
    }

    public Chair(int quantity, Position pos) {
        super("Chair", quantity, true, prix, pos);
    }

    public Chair clone() {
        return new Chair(this.getQuantity());
    }

    public Chair cloneOne() {
        return new Chair(1);
    }
}
