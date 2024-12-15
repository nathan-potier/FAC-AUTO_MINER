package Modele;
import java.util.ArrayList;

import Modele.Machine.Machine;
import Modele.Resource.Recette;
import Modele.Resource.Resource;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        
        resources.add(ressource.clone());
        return true;
        


        
    }

    public boolean addQuantityResource(Resource ressource, int quantity){
        for (Resource r : resources) {
            if (r.getName().equals(ressource.getName())) {
                return r.addQuantity(1);

            }
        }

        if (resources.size() < 10){
            resources.add(ressource.cloneOne());
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
    
    public void produire(Recette recette) {
        // Vérification de la disponibilité des ingrédients
        for (Resource ingredient : recette.getIngredients()) {
            boolean found = false;
            for (Resource ressource : resources) {
                if (ressource.getName().equals(ingredient.getName()) && ressource.getQuantity() >= ingredient.getQuantity()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Pas assez de " + ingredient.getName() + " pour produire " + recette.getNom());
                return;
            }
        }

        if (recette.isTempo()) {
            // Production temporisée avec délai de 30 secondes
            System.out.println("Production de " + recette.getNom() + " en cours... Veuillez patienter 30 secondes.");
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                effectuerProduction(recette);
                scheduler.shutdown();
            }, 30, TimeUnit.SECONDS);
        } else {
            effectuerProduction(recette);
        }
    }

    // Méthode interne pour gérer la production réelle
    private void effectuerProduction(Recette recette) {
        for (Resource ingredient : recette.getIngredients()) {
            removeResource(ingredient, ingredient.getQuantity());
        }

        addResource(recette.getProduitFinal().clone());
        System.out.println("Production réussie : " + recette.getNom());
    }

    
    

}
