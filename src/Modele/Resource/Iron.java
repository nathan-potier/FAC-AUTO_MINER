package Modele.Resource;
import Modele.*;
public class Iron extends Resource {

    public Iron(int quantity) {
        super("Iron", quantity, true, 3);
    }
    public Iron(int quantity, Position pos){
        super("Iron", quantity, true, 3, pos);
    }

    public Iron clone() {
        return new Iron(this.getQuantity());
    }
    public Iron cloneOne(){return new Iron(1);}

}
