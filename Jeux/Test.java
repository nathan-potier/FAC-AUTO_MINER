public class Test {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Wood wood = new Wood(10);
        Iron iron = new Iron(60);
        inventory.addResource(wood);
        inventory.addResource(iron);

        Iron moreIron = new Iron(4);
        inventory.addResource(moreIron);

        inventory.displayInventory();

        


        
        Game game = new Game("nathan");
        Affichage affichage = new Affichage(game);
        
        
 
       
    }
}
