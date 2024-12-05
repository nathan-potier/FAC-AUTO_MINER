public class Iron extends Ressource {

    public Iron(int quantity) {
        super("Iron", quantity, true);
    }

    public Iron clone() {
        return new Iron(this.getQuantity());
    }

}
