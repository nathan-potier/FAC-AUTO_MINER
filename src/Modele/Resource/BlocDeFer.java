package Modele.Resource;
import Modele.*;
public class BlocDeFer extends Resource{

    public BlocDeFer(int quantity) {
        super("BlocDeFer", quantity, false, 20);
    }
    public BlocDeFer(int quantity, Position pos){
        super("BlocDeFer", quantity, false, 20, pos);
    }
    public BlocDeFer clone() {
        return new BlocDeFer(this.getQuantity());
    }   
    public BlocDeFer cloneOne(){return new BlocDeFer(1);}
}
