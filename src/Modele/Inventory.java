package Modele;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Modele.Machine.Machine;
import Modele.Resource.Recette;
import Modele.Resource.Resource;
import Vue.UpdateInterface;

import javax.swing.Timer;


import java.awt.event.ActionEvent;





public class Inventory {
    private ArrayList<Resource> resources;
    private Machine machine;
    private UpdateInterface craft;


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

    public boolean addQuantityResource(Resource ressource){
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
                
                if (r.getQuantity() <= 0 && r.getName().equals(ressource.getName())){
                    
                    resources.remove(r);
                   

                    
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
    public void setUpdateCraft(UpdateInterface craft){this.craft = craft;}

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
        if (!Recette.hasSufficientResourcesForRecipe(this, recette)){
            craft.notificationUpdate();
            return;
        }

        Inventory temp = this;
        if (recette.isTempo()) {
            GameStates.setMoveOn(false);
            Timer timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameStates.setMoveOn(true);
                    if (!Recette.hasSufficientResourcesForRecipe(temp, recette)){
                        return;
                    }
                    effectuerProduction(recette);
                    craft.notificationUpdate();


                }
            });
            timer.setRepeats(false); // S'assure que le timer ne se répète pas
            timer.start();
        } else {
            effectuerProduction(recette);
            craft.notificationUpdate();
        }

    }

    // Méthode interne pour gérer la production réelle
    private void effectuerProduction(Recette recette) {
        if (addResource(recette.getProduitFinal().clone())){
            for (Resource ingredient : recette.getIngredients()) {
                removeResource(ingredient, ingredient.getQuantity());
            }
        }



        System.out.println("Production réussie : " + recette.getNom());
    }

    
    

}
