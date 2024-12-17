package Modele.Resource;

import Modele.*;

public class Armoire extends Resource {
    private static final int prix = 200;

    public Armoire(int quantity) {
        super("Armoire", quantity, true, prix);
    }

    public Armoire(int quantity, Position pos) {
        super("Armoire", quantity, true, prix, pos);
    }

    public Armoire clone() {
        return new Armoire(this.getQuantity());
    }

    public Armoire cloneOne() {
        return new Armoire(1);
    }
}
