package Modele.Resource;
import java.io.Serializable;
import Modele.*;

public class Wood extends Resource {

    public Wood(int quantity) {
        super("Wood", quantity, false, 2);

    }
    public Wood(int quantity, Position pos){
        super("Wood", quantity, false, 2, pos);
    }
    public Wood clone() {
        return new Wood(this.getQuantity());
    }

}
