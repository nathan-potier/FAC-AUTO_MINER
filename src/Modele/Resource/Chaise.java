package Modele.Resource;

import Modele.*;

public class Chaise extends Resource {
    private static final int prix = Prix.PRIX_CHAISE;

    public Chaise(int quantity) {
        super("Chaise", quantity, true, prix);
    }

    public Chaise(int quantity, Position pos) {
        super("Chaise", quantity, true, prix, pos);
    }

    public Chaise clone() {
        return new Chaise(this.getQuantity());
    }

    public Chaise cloneOne() {
        return new Chaise(1);
    }
}
