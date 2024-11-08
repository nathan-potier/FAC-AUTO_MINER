import java.util.ArrayList;
import java.util.List;

class Inventory {
    private List<Ressource> ressources;

    public Inventory() {
        ressources = new ArrayList<>();
    }

    public void addResource(String name, int quantity) {
        for (Ressource resource : ressources) {
            if (resource.getName().equals(name)) {
                resource.addQuantity(quantity);
                return;
            }
        }
        ressources.add(new Ressource(name, quantity));
    }

    public void removeResource(String name, int quantity) {
        for (Ressource resource : ressources) {
            if (resource.getName().equals(name)) {
                resource.removeQuantity(quantity);
                break;
            }
        }
    }

    public void displayInventory() {
        for (Ressource resource : ressources) {
            System.out.println(resource.getName() + ": " + resource.getQuantity());

        }
        
    }


}

