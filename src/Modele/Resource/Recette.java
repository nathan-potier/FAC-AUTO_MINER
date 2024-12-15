package Modele.Resource;

import java.util.ArrayList;
import java.util.List;

public class Recette {
    private String nom;
    private List<Resource> ingredients;
    private Resource produitFinal;

    public Recette(String n, List<Resource> ingr, Resource prodFinal) {
        nom = n;
        ingredients = ingr;
        produitFinal = prodFinal;
    }

    public String getNom() {
        return nom;
    }

    public List<Resource> getIngredients() {
        return ingredients;
    }

    public Resource getProduitFinal() {
        return produitFinal;
    }

    public static List<Recette> initialiserRecettes() {
        List<Recette> recettes = new ArrayList<>();

        // Recette pour une pioche (2 bois, 3 fer)
        List<Resource> ingredientsPickaxe = new ArrayList<>();
        ingredientsPickaxe.add(new Wood(2));
        ingredientsPickaxe.add(new Iron(3));
        recettes.add(new Recette("Pickaxe", ingredientsPickaxe, new Pickaxe(1)));

        // Recette pour un bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer = new ArrayList<>();
        ingredientsBlocFer.add(new Iron(9));
        recettes.add(new Recette("BlocDeFer", ingredientsBlocFer, new BlocDeFer(1)));

        // Recette pour une pioche (2 bois, 3 fer)
        List<Resource> ingredientsPickaxe1 = new ArrayList<>();
        ingredientsPickaxe1.add(new Wood(2));
        ingredientsPickaxe1.add(new Iron(3));
        recettes.add(new Recette("Pickaxe", ingredientsPickaxe1, new Pickaxe(1)));

        // Recette pour un bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer1 = new ArrayList<>();
        ingredientsBlocFer1.add(new Iron(9));
        recettes.add(new Recette("BlocDeFer", ingredientsBlocFer1, new BlocDeFer(1)));

        // Recette pour une pioche (2 bois, 3 fer)
        List<Resource> ingredientsPickaxe2 = new ArrayList<>();
        ingredientsPickaxe2.add(new Wood(2));
        ingredientsPickaxe2.add(new Iron(3));
        recettes.add(new Recette("Pickaxe", ingredientsPickaxe2, new Pickaxe(1)));

        // Recette pour un bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer2 = new ArrayList<>();
        ingredientsBlocFer2.add(new Iron(9));
        recettes.add(new Recette("BlocDeFer", ingredientsBlocFer2, new BlocDeFer(1)));

        // Recette pour une pioche (2 bois, 3 fer)
        List<Resource> ingredientsPickaxe3 = new ArrayList<>();
        ingredientsPickaxe3.add(new Wood(2));
        ingredientsPickaxe3.add(new Iron(3));
        recettes.add(new Recette("Pickaxe", ingredientsPickaxe3, new Pickaxe(1)));

        // Recette pour un bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer3 = new ArrayList<>();
        ingredientsBlocFer3.add(new Iron(9));
        recettes.add(new Recette("BlocDeFer", ingredientsBlocFer3, new BlocDeFer(1)));

        // Recette pour une pioche (2 bois, 3 fer)
        List<Resource> ingredientsPickaxe4 = new ArrayList<>();
        ingredientsPickaxe4.add(new Wood(2));
        ingredientsPickaxe4.add(new Iron(3));
        recettes.add(new Recette("Pickaxe", ingredientsPickaxe4, new Pickaxe(1)));

        // Recette pour un bloc de fer (9 fer)
        List<Resource> ingredientsBlocFer4 = new ArrayList<>();
        ingredientsBlocFer4.add(new Iron(9));
        recettes.add(new Recette("BlocDeFer", ingredientsBlocFer4, new BlocDeFer(1)));

        return recettes;
    }

}
