import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Ressource> ressources;

    public Inventory() {
        ressources = new ArrayList<>();
    }

    public boolean addResource(Ressource ressource) {
        for (Ressource r : ressources) {
            if (r.getName().equals(ressource.getName())) {
                r.addQuantity(ressource.getQuantity());
                return true;
            }
        }


        if (ressources.size() < 10){
            ressources.add(ressource);
            return true;
        }

        return false;
        
    }

    public void removeResource(Ressource ressource, int quantity) {
        for (Ressource r : ressources) {
            if (r.getName().equals(ressource.getName())) {
                r.removeQuantity(quantity);
                return;
            }
        }
    }

    public void displayInventory() {
        for (Ressource resource : ressources) {
            System.out.println(resource.getName() + ": " + resource.getQuantity());

        }
        
    }


}
