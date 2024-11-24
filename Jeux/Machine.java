import java.io.Serializable;
import java.util.ArrayList;


public class Machine implements Serializable{
    private String type; // "usine" ou "récolteuse"
    private ArrayList<Ressource> stock;
    private int prix;

    public Machine(String type, int prix) {
        this.type = type;
        this.stock = new ArrayList<>();
        this.prix = prix;
    }

    public String getName() {
        return type;
    }
    public int getPrix(){return prix;}

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