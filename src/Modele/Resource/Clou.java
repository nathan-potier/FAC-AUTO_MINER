package Modele.Resource;

import Modele.*;

public class Clou extends Resource {
    private static final int prix = Prix.PRIX_CLOU;

    public Clou(int quantity) {
        super("Clou", quantity, false, prix);
    }

    public Clou(int quantity, Position pos) {
        super("Clou", quantity, false, prix, pos);
    }

    public Clou clone() {
        return new Clou(this.getQuantity());
    }

    public Clou cloneOne() {
        return new Clou(1);
    }
}
