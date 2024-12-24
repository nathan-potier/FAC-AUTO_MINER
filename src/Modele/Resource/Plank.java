package Modele.Resource;

import Modele.*;

public class Plank extends Resource{
    private static final int prix = Prix.PRIX_PLANCHE;

    public Plank(int quantity) {
        super("Plank", quantity, false, prix);
    }
    
    public Plank(int quantity, Position pos){
        super("Plank", quantity, false, prix, pos);
    }

    public Plank clone() {
        return new Plank(this.getQuantity());
    }  

    public Plank cloneOne(){return new Plank(1);}


}
