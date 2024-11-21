import java.io.Serializable;

public class Wood extends Ressource implements Serializable{

    public Wood(int quantity) {
        super("Wood", quantity, false, 20);

    }
    public Wood clone() {
        return new Wood(this.getQuantity());
    }

}
