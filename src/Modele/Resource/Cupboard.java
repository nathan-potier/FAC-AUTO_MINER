package Modele.Resource;

import Modele.*;

public class Cupboard extends Resource {
    private static final int prix = Prix.PRIX_ARMORIE;

    public Cupboard(int quantity) {
        super("Cupboard", quantity, true, prix);
    }

    public Cupboard(int quantity, Position pos) {
        super("Cupboard", quantity, true, prix, pos);
    }

    public Cupboard clone() {
        return new Cupboard(this.getQuantity());
    }

    public Cupboard cloneOne() {
        return new Cupboard(1);
    }
}
