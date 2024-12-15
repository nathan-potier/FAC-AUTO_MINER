package Modele.Machine;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Modele.Resource.*;
import Modele.*;

public class Usine extends Machine {
    private Inventory stockResources;
    private List<Recette> recettesDisponibles;
    private Timer timer;  // Timer pour la production périodique
    //private String recetteEnCours;  // Nom de la recette en cours
    private Recette recetteEnCours = null;

    private boolean isCooking = false;


    public Usine(Inventory stockResources, Position position, String option, Inventory stock, Player player) {
        super("Usine", position, option, stock, player);
        this.stockResources = stockResources;
        this.recettesDisponibles = Recette.initialiserRecettes();
        this.timer = new Timer();
    }
    public Usine(String option, Player player) {
        super("Usine", new Position(), option, new Inventory(), player);
        this.stockResources = new Inventory();
        this.recettesDisponibles = Recette.initialiserRecettes();
        this.timer = new Timer();
    }

    public void setRecette(String recetteNom) {


        for (Recette recette : recettesDisponibles) {
            if (recette.getNom().equals(recetteNom)) {
                this.recetteEnCours = recette;
            }
        }
        startProduction();
    }

    public boolean isCooking(){return isCooking;}

    private void startProduction() {
        stopProduction(); // Arrête tout timer existant
        int tempFabriqMs = getTempFabriq() * 1000;
        isCooking = true;

 
        
        if (!Recette.hasSufficientResourcesForRecipe(stockResources, recetteEnCours)){stopProduction();}
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Recette.hasSufficientResourcesForRecipe(stockResources, recetteEnCours)) {
                    produireRecette(recetteEnCours);
                    Random random = new Random();
                    int randomNumber = random.nextInt(1); // Génère un entier entre 0 et 10 inclus
                    if (!getFonctionneBien() && randomNumber == 0){
                        stopProduction();
                        System.out.println("not good");
                    }
                } else {
                    stopProduction();
                    
                }
            }
        }, tempFabriqMs, tempFabriqMs); // Délai initial de 5 secondes, puis répétition toutes les 5 secondes
    }

    public String getResourcesSTR(){
        String res = "";
        for (Resource r : stockResources.getResources()){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;
    }


    private void produireRecette(Recette recette) {
        for (Resource r : getStock().getResources()){
            if (r.getName().equals(recette.getNom()) && r.getQuantity() >= getLimiteStock(this.getOption())){
                stopProduction();
                return;
            }
        }
    
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
        if (recetteEnCours != null && !recetteEnCours.getNom().equals(nomRecette)) {
            // Si une autre recette est déjà en cours, arrêtez la production actuelle
            stopProduction();
        }

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
                        if(ressource.getQuantity() == 0){break;}
                    } 
                }
            }
                

            
        }
    }

    public boolean ajouterRessource(Resource r){
        if (recetteEnCours != null && !isCooking){startProduction();}
        
        return stockResources.addQuantityResource(r, 1);
    }
    public void retirerRessource(Resource r){
        
        if (getPlayer().collectQuantityResource(r, 1)){
            stockResources.removeResource(r, 1);
        }
    }



    private void stopProduction() {
        timer.cancel();
        timer = new Timer(); // Réinitialiser le timer
        isCooking = false;
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
    public Recette getRecetteEnCours() {
        return recetteEnCours;
    }
    public String getNomRecetteEnCours() {
        try{
            return getRecetteEnCours().getNom();
        }catch(Exception e){
            return "";
        }
    }

    @Override
    public Usine getClone(){
        return new Usine(getOption(), getPlayer());
    }

    
}
