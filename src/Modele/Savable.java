package Modele;

// Interface pour rendre les objets sauvegardables dynamiquement
public interface Savable <T> {
    String saveAsString();
    void addToGrid(Cell<?>[][] grid);
}
