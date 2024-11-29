package Modele.Ressource;

public class BlocDeFerTempo extends Ressource{

    public BlocDeFerTempo(int quantity) {
        super("Bloc De Fer", quantity);
    }

    public BlocDeFerTempo clone() {
        return new BlocDeFerTempo(this.getQuantity());
    }   
}
