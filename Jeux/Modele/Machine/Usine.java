package Modele.Machine;
import java.util.ArrayList;
import java.util.List;

import Modele.Inventory;
import Modele.Ressource.BlocDeFerTempo;
import Modele.Ressource.Iron;
import Modele.Ressource.Pickaxe;
import Modele.Ressource.Ressource;
import Modele.Ressource.Wood;

import java.io.Serializable;


public class Usine extends Machine implements Serializable {
    private Inventory inventaire;
    private String[] recettes;
    private List<List<Ressource>> ingredients;

    public Usine(Inventory inventaire) {
        super("usine", 100);
        this.inventaire = inventaire;
        initialiserRecettes();
    }

    private void initialiserRecettes() {
        recettes = new String[] { "Pickaxe", "Bloc de Fer" };

        ingredients = new ArrayList<>();

        // Ingrédients pour la Pickaxe (2 bois et 3 fer)
        List<Ressource> ingredientsPickaxe = new ArrayList<>();
        ingredientsPickaxe.add(new Wood(2));
        ingredientsPickaxe.add(new Iron(3));
        ingredients.add(ingredientsPickaxe);

        // Ingrédients pour le bloc de fer (9 fer)
        List<Ressource> ingredientsBlocFer = new ArrayList<>();
        ingredientsBlocFer.add(new Iron(9));
        ingredients.add(ingredientsBlocFer);
    }

    public void produire(String nomRecette) {
        // Trouver l'index de la recette
        int index = -1;
        for (int i = 0; i < recettes.length; i++) {
            if (recettes[i].equalsIgnoreCase(nomRecette)) {
                index = i;
                break;
            }
        }
    
        if (index == -1) {
            System.out.println("Recette non trouvée.");
            return;
        }
    
        // Afficher les ingrédients nécessaires pour la recette choisie
        List<Ressource> ingredientsRecette = ingredients.get(index);
        System.out.println("Ingrédients nécessaires pour " + nomRecette + ":");
        for (Ressource ingredient : ingredientsRecette) {
            System.out.println(ingredient.getName() + " x" + ingredient.getQuantity());
        }
    
        // Vérifier si l'inventaire contient suffisamment de chaque ingrédient
        for (Ressource ingredient : ingredientsRecette) {
            int quantityInInventory = 0;
            // Calculer la quantité totale de l'ingrédient dans l'inventaire
            for (Ressource ressource : inventaire.getRessources()) {
                if (ressource.getName().equals(ingredient.getName())) {
                    quantityInInventory += ressource.getQuantity();
                }
            }
    
            // Si la quantité dans l'inventaire est insuffisante
            if (quantityInInventory < ingredient.getQuantity()) {
                System.out.println("Ingrédients insuffisants pour produire : " + nomRecette);
                System.out.println("Il manque " + (ingredient.getQuantity() - quantityInInventory) + " de " + ingredient.getName());
                return;
            }
        }
    
        // Retirer les ingrédients de l'inventaire
        for (Ressource ingredient : ingredientsRecette) {
            inventaire.removeResource(ingredient, ingredient.getQuantity());
        }
    
        // Créer la ressource produite et l'ajouter au stock de l'usine
        Ressource ressourceProduite;
        if ("Pickaxe".equalsIgnoreCase(nomRecette)) {
            ressourceProduite = new Pickaxe(1);
        } else if ("Bloc de Fer".equalsIgnoreCase(nomRecette)) {
            ressourceProduite = new BlocDeFerTempo(1);
        } else {
            System.out.println("Erreur lors de la production.");
            return;
        }
    
        ajouterRessource(ressourceProduite);
        System.out.println(nomRecette + " produite et ajoutée au stock de l'usine.");
    }
    
    

    public void recuperer() {
        if(getStock().isEmpty()){
            System.out.println("Il n'y a rien à récolter dans cette usine");
        }else{
            while (!getStock().isEmpty()) {
                Ressource ressource = getStock().remove(0); // Supprime la ressource du stock de l'usine
                inventaire.addResource(ressource); // Ajoute la ressource à l'inventaire
            }
            System.out.println("Stock de l'usine transféré à l'inventaire.");
        }
    }
}
