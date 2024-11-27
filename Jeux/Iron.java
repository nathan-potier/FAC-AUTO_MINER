import java.io.Serializable;

public class Iron extends Ressource implements Serializable{

    public Iron(int quantity) {
        super("Iron", quantity, true, 3);
    }

    public Iron clone() {
        return new Iron(this.getQuantity());
    }

}
