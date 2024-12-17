package Modele.Resource;
import Modele.*;

public class Wood extends Resource {
    private static final int prix = Prix.PRIX_BOIS;
    public Wood(int quantity) {
        super("Wood", quantity, false, prix);

    }
    public Wood(int quantity, Position pos){
        super("Wood", quantity, false, prix, pos);
    }
    public Wood clone() {
        return new Wood(this.getQuantity());
    }
    public Wood cloneOne(){return new Wood(1);}

}
