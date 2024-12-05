package Modele;
import java.util.ArrayList;


import Modele.Machine.Machine;
import Modele.Resource.Resource;

public class Inventory {
    private ArrayList<Resource> resources;
    private Machine machine;

    public Inventory() {
        resources = new ArrayList<>();
    }
    public Inventory(ArrayList<Resource> resources){
        this.resources = resources;
    }

    public ArrayList<Resource> getResources() {
        return resources;
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

    public boolean addResource(Resource ressource) {
        for (Resource r : resources) {
            if (r.getName().equals(ressource.getName())) {
                return r.addQuantity(ressource.getQuantity());

            }
        }

        if (resources.size() < 10){
            resources.add(ressource.clone());
            return true;
        }

        return false;
        
    }

    public void removeResource(Resource ressource, int quantity) {
        for (Resource r : resources) {

            if (r.getName().equals(ressource.getName())) {
                r.removeQuantity(quantity);
                
                if (r.getQuantity() <= 0){

                    resources.remove(ressource);
                }

                return;
            }
        }
    }

    public void displayInventory() {
        for (Resource resource : resources) {
            System.out.println(resource.getName() + ": " + resource.getQuantity());
        }
    }

    public String[] getResInvName(){
        String [] res = new String[resources.size()];
        for (int i = 0; i < resources.size(); i++){
            res[i] = resources.get(i).getName();
        }
        return res;
    }

    public int[] getResInvQuant(){
        int [] res = new int[resources.size()];
        for (int i = 0; i < resources.size(); i++){
            res[i] = resources.get(i).getQuantity();
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory:\n");
    
        sb.append("Resources:\n");
        for (Resource resource : resources) {
            sb.append(" - ").append(resource.getName()).append(": ").append(resource.getQuantity()).append("\n");
        }
        if (resources.isEmpty()) {
            sb.append(" - No resources\n");
        }
    
        sb.append("Machine:\n");
        if (machine != null) {
            sb.append(" - ").append(machine.getName()).append("\n");
        } else {
            sb.append(" - No machine\n");
        }
    
        return sb.toString();
    }
    

}
