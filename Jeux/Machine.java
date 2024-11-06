import java.util.ArrayList;


public class Machine {
    private String type; // "usine" ou "r"colteuse"
    private ArrayList<Ressource> stock; 

    public Machine(String type) {
        this.type = type;
        this.stock = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public ArrayList<Ressource> getStock() {
        return stock;
    }

    public void ajouterRessource(Ressource ressource) {
        stock.add(ressource);
    }

    public void retirerRessource(Ressource ressource) {
        stock.remove(ressource);
    }

    // Méthode de production spécifique aux usines
    public void produire() {
        if ("usine".equals(type)) {
            // Logique de production en fonction des recettes et des ressources disponibles
        }
    }

    // Méthode de récolte spécifique aux récolteuses
    public void recolter(Ressource ressource) {
        if ("recolteuse".equals(type)) {
            // Logique de récolte pour ajouter la ressource au stock
            ajouterRessource(ressource);
        }
    }
}