package Modele.Resource;

public class BlocDeFer extends Resource{

    public BlocDeFer(int quantity) {
        super("BlocDeFer", quantity);
    }

    public BlocDeFer clone() {
        return new BlocDeFer(this.getQuantity());
    }   
}
