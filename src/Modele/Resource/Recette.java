package Modele.Resource;

import java.util.ArrayList;
import java.util.List;

import Modele.Inventory;


public class Recette {
    private String nom;
    private List<Resource> ingredients;
    private Resource produitFinal;
    private boolean isTempo; 

    public Recette(String n, List<Resource> ingr, Resource prodFinal) {
        nom = n;
        ingredients = ingr;
        produitFinal = prodFinal;
        isTempo = prodFinal.isLaborieuse();
    }

    public static List<Recette> initialiserRecettes() {
        List<Recette> recettes = new ArrayList<>();
    
        // Recette pour une chaise (4 planches, 4 clous, 1 marteau)
        List<Resource> ingredientsChaise = new ArrayList<>();
        ingredientsChaise.add(new Planche(4)); // 4 planches
        ingredientsChaise.add(new Clou(4));    // 4 clous
        ingredientsChaise.add(new Marteau(1)); // 1 marteau
        recettes.add(new Recette("Chaise", ingredientsChaise, new Chaise(1)));
    
        // Recette pour une table (6 planches, 4 clous, 1 marteau)
        List<Resource> ingredientsTable = new ArrayList<>();
        ingredientsTable.add(new Planche(6));  // 6 planches
        ingredientsTable.add(new Clou(4));     // 4 clous
        ingredientsTable.add(new Marteau(1));  // 1 marteau
        recettes.add(new Recette("Table", ingredientsTable, new Table(1)));
    
        // Recette pour un marteau (2 bois, 2 fer)
        List<Resource> ingredientsMarteau = new ArrayList<>();
        ingredientsMarteau.add(new Wood(2));   // 2 bois
        ingredientsMarteau.add(new Iron(2));   // 2 fer
        recettes.add(new Recette("Marteau", ingredientsMarteau, new Marteau(1) ));
    
        // Recette pour une scie (2 bois, 1 fer)
        List<Resource> ingredientsScie = new ArrayList<>();
        ingredientsScie.add(new Wood(2));      // 2 bois
        ingredientsScie.add(new Iron(1));      // 1 fer
        recettes.add(new Recette("Scie", ingredientsScie, new Scie(1)));
    
        // Recette pour une planche (1 bois)
        List<Resource> ingredientsPlanche = new ArrayList<>();
        ingredientsPlanche.add(new Wood(1));  // 1 bois
        recettes.add(new Recette("Planche", ingredientsPlanche, new Planche(1)));
    
        // Recette pour des clous (1 fer => 4 clous)
        List<Resource> ingredientsClous = new ArrayList<>();
        ingredientsClous.add(new Iron(1));    // 1 fer (produit 4 clous)
        recettes.add(new Recette("Clous", ingredientsClous, new Clou(4)));
    
        // Recette pour une armoire (8 planches, 6 clous, 1 scie, 1 marteau)
        List<Resource> ingredientsArmoire = new ArrayList<>();
        ingredientsArmoire.add(new Planche(8)); // 8 planches
        ingredientsArmoire.add(new Clou(6));    // 6 clous
        ingredientsArmoire.add(new Scie(1));    // 1 scie
        ingredientsArmoire.add(new Marteau(1)); // 1 marteau
        recettes.add(new Recette("Cupboard", ingredientsArmoire, new Cupboard(1)));
   
        return recettes;
    }

    public static boolean hasSufficientResourcesForRecipe(Inventory stockResources ,Recette recette) {
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

    public String getNom() {return nom;}
    public List<Resource> getIngredients() {return ingredients;}
    public Resource getProduitFinal() {return produitFinal;}
    public boolean isTempo() {return isTempo;}

}
