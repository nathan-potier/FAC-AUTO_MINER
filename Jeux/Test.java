public class Test {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addResource("Wood", 10);
        inventory.addResource("Iron", 5);
        inventory.addResource("Wood", 15); 

        inventory.displayInventory();

        


        
        Game game = new Game("nathan");
        Affichage affichage = new Affichage(game);
        
        
 
       
    }
}
