import java.io.Serializable;

public abstract class Ressource implements Serializable{
    private String name;
    private int quantity;
    private int prix;
    private boolean laborieuse = false;
    private boolean temporisée = false;

    public Ressource(String name, int quantity, Boolean laborieuse, int prix) {
        this.name = name;
        this.quantity = quantity;
        this.laborieuse = laborieuse;
        this.temporisée = !laborieuse;
        this.prix = prix;
    }

    public Ressource(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    public void removeQuantity(int amount) {
        quantity = Math.max(0, quantity - amount);
    }

    public boolean isLaborieuse() {
        return laborieuse;
    }

    public boolean isTemporisée() {
        return temporisée;
    }
    public int getPrix(){return prix;}


    public abstract Ressource clone();
}
