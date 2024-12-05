import java.util.ArrayList;

public class Recolteuse extends Machine {
    private Position position; // Position actuelle de la récolteuse
    private Inventory inventaire;
    private int stockMax; // Capacité maximale du stock
    private Ressource ressourceChoisie; // La ressource choisie pour la récolte

    public Recolteuse(Position position, int stockMax, Inventory inventaire, Ressource ressourceChoisi) {
        super("recolteuse");
        this.position = position;
        this.stockMax = stockMax;
        this.inventaire = inventaire;
        this.ressourceChoisie = ressourceChoisi;
    }

    // Méthode de récolte qui cherche les ressources adjacentes et les ajoute au stock
    public void recolterRessourcesAdjacentes(ArrayList<Ressource> ressourcesAdjacentes) {
        if (getStock().size() >= stockMax) {
            System.out.println("Le stock de la récolteuse est plein.");
            return;
        }

        if (ressourceChoisie == null) {
            System.out.println("Aucune ressource choisie.");
            return;
        }

        // Vérifie si la ressource choisie est dans les ressources adjacentes
        for (Ressource ressource : ressourcesAdjacentes) {
            if (ressourceChoisie.equals(ressource)) {
                if (getStock().size() < stockMax) {
                    recolter(ressource);
                    System.out.println("Ressource récoltée : " + ressource.getName());
                    return; // Une fois la ressource récoltée, on arrête
                }
            }
        }

        System.out.println("La ressource choisie n'est pas présente parmi les ressources adjacentes.");
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Afficher le stock
    @Override
    public void afficherStock() {
        super.afficherStock();
    }

    public Ressource getRessourceChoisie() {
        return ressourceChoisie;
    }

    public void setRessourceChoisie(Ressource ressourceChoisie) {
        this.ressourceChoisie = ressourceChoisie;
    }
}
