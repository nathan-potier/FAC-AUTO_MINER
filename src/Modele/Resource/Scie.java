package Modele.Resource;

import Modele.*;

public class Scie extends Resource {
    private static final int prix = Prix.PRIX_SCIE;

    public Scie(int quantity) {
        super("Scie", quantity, false, prix);
    }

    public Scie(int quantity, Position pos) {
        super("Scie", quantity, false, prix, pos);
    }

    public Scie clone() {
        return new Scie(this.getQuantity());
    }

    public Scie cloneOne() {
        return new Scie(1);
    }
}
