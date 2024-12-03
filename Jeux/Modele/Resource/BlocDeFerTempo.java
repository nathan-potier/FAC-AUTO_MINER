package Modele.Resource;

public class BlocDeFerTempo extends Resource{

    public BlocDeFerTempo(int quantity) {
        super("Bloc De Fer", quantity);
    }

    public BlocDeFerTempo clone() {
        return new BlocDeFerTempo(this.getQuantity());
    }   
}
