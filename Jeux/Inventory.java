import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Ressource> ressources;

    public Inventory() {
        ressources = new ArrayList<>();
    }

    // Ajouter cette méthode pour accéder à la liste des ressources
    public List<Ressource> getRessources() {
        return ressources;
    }

    public boolean addResource(Ressource ressource) {
        for (Ressource r : ressources) {
            if (r.getName().equals(ressource.getName())) {
                r.addQuantity(ressource.getQuantity());
                return true;
            }
        }


        if (ressources.size() < 10){
            ressources.add(ressource.clone());
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

    public String[] getResInvName(){
        String [] res = new String[ressources.size()];
        for (int i = 0; i < ressources.size(); i++){
            res[i] = ressources.get(i).getName();
        }
        return res;
    }

    public int[] getResInvQuant(){
        int [] res = new int[ressources.size()];
        for (int i = 0; i < ressources.size(); i++){
            res[i] = ressources.get(i).getQuantity();
        }
        return res;
    }

}
