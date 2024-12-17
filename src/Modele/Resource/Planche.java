package Modele.Resource;

import Modele.*;

public class Planche extends Resource{
    private static final int prix = 5;

    public Planche(int quantity) {
        super("Planche", quantity, false, prix);
    }
    public Planche(int quantity, Position pos){
        super("Planche", quantity, false, prix, pos);
    }
    public Planche clone() {
        return new Planche(this.getQuantity());
    }   
    public Planche cloneOne(){return new Planche(1);}


}
