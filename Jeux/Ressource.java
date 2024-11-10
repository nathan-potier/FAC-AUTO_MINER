public abstract class Ressource {
    private String name;
    private int quantity;
    private boolean laborieuse;
    private boolean temporisée;

    public Ressource(String name, int quantity, Boolean laborieuse) {
        this.name = name;
        this.quantity = quantity;
        this.laborieuse = laborieuse;
        this.temporisée = !laborieuse;
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
}
