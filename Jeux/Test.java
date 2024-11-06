public class Test {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addResource("Wood", 10);
        inventory.addResource("Iron", 5);
        inventory.addResource("Wood", 15); 

        inventory.displayInventory();

        //MainMenu mainMenu = new MainMenu();

        Map test = new Map(5, 5);
        AffichageTemporaire.affiche(test.getGrid());
    }
}
