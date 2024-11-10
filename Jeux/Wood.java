public class Wood extends Ressource {

    public Wood(int quantity) {
        super("Wood", quantity, false);

    }
    public Wood clone() {
        return new Wood(this.getQuantity());
    }

}
