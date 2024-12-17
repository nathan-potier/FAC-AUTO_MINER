package Modele.Resource;

import Modele.*;

public class Table extends Resource {
    private static final int prix = 80;

    public Table(int quantity) {
        super("Table", quantity, true, prix);
    }

    public Table(int quantity, Position pos) {
        super("Table", quantity, true, prix, pos);
    }

    public Table clone() {
        return new Table(this.getQuantity());
    }

    public Table cloneOne() {
        return new Table(1);
    }
}
