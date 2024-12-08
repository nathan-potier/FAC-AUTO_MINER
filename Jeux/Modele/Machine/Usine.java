package Modele.Machine;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Modele.Resource.*;
import Modele.*;

public class Usine extends Machine {
    private Inventory stockResources;
    private List<Recette> recettesDisponibles;
    private Timer timer;  // Timer pour la production périodique
    private String recetteEnCours;  // Nom de la recette en cours


    public Usine(Inventory stockResources, Position position, String option, Inventory stock, Player player) {
        super("Usine", position, option, stock, player);
        this.stockResources = stockResources;
        this.recettesDisponibles = Recette.initialiserRecettes();
        this.timer = new Timer();
    }

    public void setRecetteEnCours(String recetteNom) {
        this.recetteEnCours = recetteNom;
        startProduction();
    }

    private void startProduction() {
        stopProduction(); // Arrête tout timer existant
        if (recetteEnCours == null) return;
    
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Recette recette : recettesDisponibles) {
                    if (recette.getNom().equals(recetteEnCours)) {
                        if (hasSufficientResourcesForRecipe(recette)) {
                            produireRecette(recette);
                        } else {
                            System.out.println("Pas assez de ressources, en attente...");
                        }
                        break;
                    }
                }
            }
        }, 5000, 5000); // Délai initial de 5 secondes, puis répétition toutes les 5 secondes
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
            for (int i = 0; i < getStock().getResources().size(); i++) {
                Resource ressource = getStock().getResources().get(i);
                for (int j = 0; j < getStock().getResources().get(i).getQuantity(); j++){
                    
                    if(getPlayer().collectQuantityResource(ressource, 1)){// Ajoute la ressource à l'inventaire
                        getStock().removeResource(ressource, 1);
                    } 
                }
            }
                

            
        }
    }

    public boolean ajouterRessource(Resource r){
        return stockResources.addQuantityResource(r, 1);
    }
    public void retirerRessource(Resource r){
        
        if (getPlayer().collectQuantityResource(r, 1)){
            stockResources.removeResource(r, 1);
        }
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

    private void stopProduction() {
        timer.cancel();
        timer = new Timer(); // Réinitialiser le timer
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

    @Override
    public void finalize() {
        stopProduction(); // Assure l'arrêt du timer lors de la destruction de l'objet
    }

    
}
