package Modele.Machine;
import java.io.Serializable;
import java.util.ArrayList;

import Modele.Inventory;
import Modele.Position;
import Modele.Resource.Resource;

public class Recolteuse extends Machine implements Serializable{
    private Position position; // Position actuelle de la récolteuse
    private Inventory inventaire;
    private int stockMax; // Capacité maximale du stock
    private Resource ressourceChoisie; // La ressource choisie pour la récolte

    public Recolteuse(Position position, int stockMax, Inventory inventaire, Resource ressourceChoisi) {
        super("recolteuse", 500, position);
        this.position = position;
        this.stockMax = stockMax;
        this.inventaire = inventaire;
        this.ressourceChoisie = ressourceChoisi;
    }

    // Méthode de récolte qui cherche les ressources adjacentes et les ajoute au stock
    public void recolterRessourcesAdjacentes(ArrayList<Resource> ressourcesAdjacentes) {
        if (getStock().size() >= stockMax) {
            System.out.println("Le stock de la récolteuse est plein.");
            return;
        }

        if (ressourceChoisie == null) {
            System.out.println("Aucune ressource choisie.");
            return;
        }

        // Vérifie si la ressource choisie est dans les ressources adjacentes
        for (Resource ressource : ressourcesAdjacentes) {
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


    public void setPosition(Position position) {
        this.position = position;
    }

    // Afficher le stock
    @Override
    public void afficherStock() {
        super.afficherStock();
    }

    public Resource getRessourceChoisie() {
        return ressourceChoisie;
    }

    public void setRessourceChoisie(Resource ressourceChoisie) {
        this.ressourceChoisie = ressourceChoisie;
    }
}
