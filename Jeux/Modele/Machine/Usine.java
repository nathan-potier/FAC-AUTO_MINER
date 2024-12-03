package Modele.Machine;
import java.util.ArrayList;
import java.util.List;

import Modele.Inventory;
import Modele.Resource.BlocDeFerTempo;

import Modele.Resource.Pickaxe;
import Modele.Resource.*;
import Modele.*;




public class Usine extends Machine implements  ObjectWithPosition {
    private Inventory inventaire;
    private String[] recettes;
    private List<List<Resource>> ingredients;

    public Usine(Inventory inventaire, Position position) {
        super("Usine", 100, position);
        this.inventaire = inventaire;
        initialiserRecettes();
    }

    private void initialiserRecettes() {
        recettes = new String[] { "Pickaxe", "Bloc de Fer" };

        ingredients = new ArrayList<>();

        // Ingrédients pour la Pickaxe (2 bois et 3 fer)
        List<Resource> ingredientsPickaxe = new ArrayList<>();
        ingredientsPickaxe.add(new Wood(2));
        ingredientsPickaxe.add(new Iron(3));
        ingredients.add(ingredientsPickaxe);

        // Ingrédients pour le bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer = new ArrayList<>();
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
        List<Resource> ingredientsRecette = ingredients.get(index);
    
        // Vérifier si l'inventaire contient suffisamment de chaque ingrédient
        for (Resource ingredient : ingredientsRecette) {
            int quantityInInventory = 0;
            // Calculer la quantité totale de l'ingrédient dans l'inventaire
            for (Resource ressource : inventaire.getResources()) {
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
        for (Resource ingredient : ingredientsRecette) {
            inventaire.removeResource(ingredient, ingredient.getQuantity());
        }
    
        // Créer la ressource produite et l'ajouter au stock de l'usine
        Resource ressourceProduite;
        if ("Pickaxe".equalsIgnoreCase(nomRecette)) {
            ressourceProduite = new Pickaxe(1);
        } else if ("Bloc de Fer".equalsIgnoreCase(nomRecette)) {
            ressourceProduite = new BlocDeFerTempo(1);
        } else {
            System.out.println("Erreur lors de la production.");
            return;
        }
    
        ajouterRessource(ressourceProduite);
    }
    
    

    public void recuperer() {
        if(getStock().isEmpty()){
            System.out.println("Il n'y a rien à récolter dans cette usine");
        }else{
            while (!getStock().isEmpty()) {
                Resource ressource = getStock().remove(0); // Supprime la ressource du stock de l'usine
                inventaire.addResource(ressource); // Ajoute la ressource à l'inventaire
            }
        }
    }
}
