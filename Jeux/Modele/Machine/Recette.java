package Modele.Machine;

import java.util.ArrayList;
import java.util.List;
import Modele.Resource.*;

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
        recettes.add(new Recette("Bloc de Fer", ingredientsBlocFer, new BlocDeFer(1)));

        return recettes;
    }

}
