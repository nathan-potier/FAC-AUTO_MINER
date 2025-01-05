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
        ingredientsChaise.add(new Plank(4)); // 4 planches
        ingredientsChaise.add(new Nail(4));    // 4 clous
        ingredientsChaise.add(new Hammer(1)); // 1 marteau
        recettes.add(new Recette("Chair", ingredientsChaise, new Chair(1)));
    
        // Recette pour une table (6 planches, 4 clous, 1 marteau)
        List<Resource> ingredientsTable = new ArrayList<>();
        ingredientsTable.add(new Plank(6));  // 6 planches
        ingredientsTable.add(new Nail(4));     // 4 clous
        ingredientsTable.add(new Hammer(1));  // 1 marteau
        recettes.add(new Recette("Table", ingredientsTable, new Table(1)));
    
        // Recette pour un marteau (2 bois, 2 fer, 2 Pierre)
        List<Resource> ingredientsMarteau = new ArrayList<>();
        ingredientsMarteau.add(new Wood(2));   // 2 bois
        ingredientsMarteau.add(new Iron(2));   // 2 fer
        ingredientsMarteau.add(new Rock(2));   // 2 Pierre
        recettes.add(new Recette("Hammer", ingredientsMarteau, new Hammer(1) ));
    
        // Recette pour une scie (2 bois, 1 fer)
        List<Resource> ingredientsScie = new ArrayList<>();
        ingredientsScie.add(new Wood(2));      // 2 bois
        ingredientsScie.add(new Iron(1));      // 1 fer
        ingredientsScie.add(new Rock(3));   // 2 Pierre
        recettes.add(new Recette("Saw", ingredientsScie, new Saw(1)));
    
        // Recette pour une planche (1 bois)
        List<Resource> ingredientsPlanche = new ArrayList<>();
        ingredientsPlanche.add(new Wood(1));  // 1 bois
        recettes.add(new Recette("Plank", ingredientsPlanche, new Plank(1)));
    
        // Recette pour des clous (1 fer => 4 clous)
        List<Resource> ingredientsClous = new ArrayList<>();
        ingredientsClous.add(new Iron(1));    // 1 fer (produit 4 clous)
        recettes.add(new Recette("Nail", ingredientsClous, new Nail(4)));
    
        // Recette pour une armoire (8 planches, 6 clous, 1 scie, 1 marteau)
        List<Resource> ingredientsArmoire = new ArrayList<>();
        ingredientsArmoire.add(new Plank(8)); // 8 planches
        ingredientsArmoire.add(new Nail(6));    // 6 clous
        ingredientsArmoire.add(new Saw(1));    // 1 scie
        ingredientsArmoire.add(new Hammer(1)); // 1 marteau
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
