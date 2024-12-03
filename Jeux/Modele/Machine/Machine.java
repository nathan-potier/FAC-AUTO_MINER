package Modele.Machine;
import java.io.Serializable;
import java.util.ArrayList;

import Modele.Resource.Resource;
import Modele.*;

public class Machine implements Serializable, ObjectWithPosition{
    private String type; // "usine" ou "récolteuse"
    private ArrayList<Resource> stock;
    private int prix;
    private Position pos;

    public Machine(String type, int prix, Position pos) {
        this.type = type;
        this.stock = new ArrayList<>();
        this.prix = prix;
        this.pos = pos;
    }

    public String getName() {
        return type;
    }
    public int getPrix(){return prix;}
    public Position getPosition(){return pos;}

    public ArrayList<Resource> getStock() {
        return stock;
    }


    public void ajouterRessource(Resource ressourceProduite) {
        boolean found = false;
        for (Resource ressource : getStock()) {
            if (ressource.getName().equals(ressourceProduite.getName())) {
                ressource.addQuantity(ressourceProduite.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            getStock().add(ressourceProduite);
        }

    }
    

    public void retirerRessource(Resource ressource) {
        stock.remove(ressource);
    }

    public void produire() {}

    // Méthode de récolte spécifique aux récolteuses
    public void recolter(Resource ressource) {
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
            for (Resource ressource : stock) {
                System.out.println(ressource.getName() + ": " + ressource.getQuantity());
            }
        }
    }
    
}