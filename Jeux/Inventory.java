import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable{
    private List<Ressource> ressources;
    private Machine machine;

    public Inventory() {
        ressources = new ArrayList<>();
    }

    public List<Ressource> getRessources() {
        return ressources;
    }
    public boolean addMachine(Machine m){
        if (machine == null){
            machine = m;
            return true;
        }
        return false;
        
    }
    public Machine getMachine(){return machine;}
    public boolean hasMachine(Machine m){return machine.getName().equals(m.getName());}
    public void removeMachine(){machine = null;}

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
