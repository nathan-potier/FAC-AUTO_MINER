public class Pickaxe extends Ressource {

    public Pickaxe(int quantity) {
        super("Pickaxe", quantity);
    }

    public Pickaxe clone() {
        return new Pickaxe(this.getQuantity());
    }

}
