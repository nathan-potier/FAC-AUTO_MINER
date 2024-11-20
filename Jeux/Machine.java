import java.util.ArrayList;


public class Machine {
    private String type; // "usine" ou "récolteuse"
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

    public void produire() {}

    // Méthode de récolte spécifique aux récolteuses
    public void recolter(Ressource ressource) {
        if ("recolteuse".equals(type)) {
            // Logique de récolte pour ajouter la ressource au stock
            ajouterRessource(ressource);
        }
    }

    public void afficherStock() {
        if (stock.isEmpty()) {
            System.out.println("Le stock est vide.");
        } else {
            System.out.println("Contenu du stock :");
            for (Ressource ressource : stock) {
                System.out.println(ressource.getName() + ": " + ressource.getQuantity());
            }
        }
    }
    
}