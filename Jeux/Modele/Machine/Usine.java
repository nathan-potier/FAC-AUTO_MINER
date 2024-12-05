package Modele.Machine;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import Modele.Resource.*;
import Modele.*;

public class Usine extends Machine {
    private Inventory stockResources;
    private List<Recette> recettesDisponibles;
    private Timer timer;  // Timer pour la production périodique
    private final long productionInterval = 5000; // Intervalle de 5 secondes pour la production (en millisecondes)
    private String recetteEnCours;  // Nom de la recette en cours


    public Usine(Inventory stockResources, Position position, String option, Inventory stock) {
        super("Usine", position, option, stock);
        this.stockResources = stockResources;
        this.recettesDisponibles = Recette.initialiserRecettes();
    }

    private void startProductionTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (recetteEnCours != null) {
                    for (Recette recette : recettesDisponibles) {
                        if (recette.getNom().equals(recetteEnCours) && hasSufficientResourcesForRecipe(recette)) {
                            produireRecette(recette);
                            break;
                        }
                    }
                }
            }
        }, 0, productionInterval);  // Démarrer immédiatement, répéter toutes les 5 secondes
    }

    public String getResourcesSTR(){
        String res = "";
        for (Resource r : stockResources.getResources()){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;
    }


    private void produireRecette(Recette recette) {
        // Vérifiez si l'inventaire contient suffisamment de chaque ingrédient
        for (Resource ingredient : recette.getIngredients()) {
            int quantityInInventory = 0;
            for (Resource ressource : stockResources.getResources()) {
                if (ressource.getName().equals(ingredient.getName())) {
                    quantityInInventory += ressource.getQuantity();
                }
            }
    
            if (quantityInInventory < ingredient.getQuantity()) {
                System.out.println("Ingrédients insuffisants pour produire : " + recette.getNom());
                return;
            }
        }
    
        // Retirer les ingrédients de l'inventaire
        for (Resource ingredient : recette.getIngredients()) {
            stockResources.removeResource(ingredient, ingredient.getQuantity());
        }
    
        // Ajouter le produit final au stock de l'usine
        ajouterRessource(recette.getProduitFinal().clone());
        System.out.println("Production réussie : " + recette.getNom());
    }
    

    // Méthode pour démarrer la production, appelée lorsque l'utilisateur appuie sur "produire"
    public void produire(String nomRecette) {
        if (recetteEnCours != null && !recetteEnCours.equals(nomRecette)) {
            // Si une autre recette est déjà en cours, arrêtez la production actuelle
            stopProduction();
        }
    
        recetteEnCours = nomRecette;
    
        // Démarrer la production périodique pour la recette choisie
        startProductionTimer();
    }

    

    public void recuperer() {
        if (getStock().isEmpty()) {
            System.out.println("Il n'y a rien à récolter dans cette usine");
        } else {
            while (!getStock().isEmpty()) {
                Resource ressource = getStock().remove(0); // Supprime la ressource du stock de l'usine
                stockResources.addResource(ressource); // Ajoute la ressource à l'inventaire
            }
        }
    }

    public void ajouterRessource(Resource r){
        stockResources.addResource(r);
    }

    private boolean hasSufficientResourcesForRecipe(Recette recette) {
        // Vérifier si le joueur a suffisamment de ressources pour chaque ingrédient
        for (Resource ingredient : recette.getIngredients()) {
            int quantityInInventory = 0;

            for (Resource ressource : stockResources.getResources()) {
                if (ressource.getName().equals(ingredient.getName())) {
                    quantityInInventory += ressource.getQuantity();
                }
            }

            if (quantityInInventory < ingredient.getQuantity()) {
                return false;  // Pas assez d'ingrédients
            }
        }
        return true;  // Le joueur a suffisamment d'ingrédients
    }

    public void stopProduction() {
        if (timer != null) {
            timer.cancel();  // Arrêter la production périodique
            timer = null;    // Réinitialiser le timer
        }
    }

    public List<Recette> getRecettesDisponibles() {
        return recettesDisponibles;
    }

    public String[] getTabRecettes(){
        String[] tab = new String[recettesDisponibles.size()];
        for(int i = 0; i < recettesDisponibles.size(); i++){
            tab[i] = recettesDisponibles.get(i).getNom();
        }
        return tab;
    }

    public void addInvToAlim(Resource r){
        
    }
    
}
