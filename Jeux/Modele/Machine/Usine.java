package Modele.Machine;

import java.util.List;
import java.util.Timer;

import Modele.Resource.*;
import Modele.*;

public class Usine extends Machine {
    private Inventory stockResources;
    private List<Recette> recettesDisponibles;
    private Timer timer;  // Timer pour la production périodique
    private String recetteEnCours;  // Nom de la recette en cours


    public Usine(Inventory stockResources, Position position, String option, Inventory stock, Inventory playerInv) {
        super("Usine", position, option, stock, playerInv);
        this.stockResources = stockResources;
        this.recettesDisponibles = Recette.initialiserRecettes();
    }

    private void startProduction() {
        System.out.println(stockResources);
        if (recetteEnCours != null) {

            for (Recette recette : recettesDisponibles) {
                System.out.println(recette.getNom().equals(recetteEnCours));
                System.out.println(hasSufficientResourcesForRecipe(recette));
                if (recette.getNom().equals(recetteEnCours) && hasSufficientResourcesForRecipe(recette)) {
                    produireRecette(recette);
                    
                    break;
                }
            }
        }

    }

    public String getResourcesSTR(){
        String res = "";
        for (Resource r : stockResources.getResources()){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;
    }


    private void produireRecette(Recette recette) {

    
        // Retirer les ingrédients de l'inventaire
        for (Resource ingredient : recette.getIngredients()) {
            stockResources.removeResource(ingredient, ingredient.getQuantity());
        }
    
        // Ajouter le produit final au stock de l'usine
        ajouterAustock(recette.getProduitFinal().clone());
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
        startProduction();
    }

    

    public void recuperer() {
        if (getStock().getResources().isEmpty()) {
            System.out.println("Il n'y a rien à récolter dans cette usine");
        } else {
            while (!getStock().getResources().isEmpty()) {
                Resource ressource = getStock().getResources().remove(0); // Supprime la ressource du stock de l'usine
                getPlayerInv().addResource(ressource); // Ajoute la ressource à l'inventaire
            }
        }
    }

    public void ajouterRessource(Resource r){
        stockResources.addQuantityResource(r, 1);
    }
    public void retirerRessource(Resource r){
        stockResources.removeResource(r, 1);
        getPlayerInv().addQuantityResource(r, 1);
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
            System.out.println(quantityInInventory);
            System.out.println(ingredient.getQuantity());
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

    public Inventory getStockResources() {
        return stockResources;
    }

    
}
