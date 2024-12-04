package Modele.Machine;

import java.util.ArrayList;

import Modele.Resource.Resource;
import Modele.*;

public class Machine implements ObjectWithPosition{
    private String type; // "usine" ou "récolteuse"
    private ArrayList<Resource> stock;
    private int prix;
    private Position pos;
    private int tempFabriq;
    private int limiteStock;
    private boolean modifiable;
    private boolean fonctionneBien;

    public Machine(String type, int prix, Position pos, int tempFabriq, int limiteStock, boolean modifiable, boolean fonctionneBien) {
        this.type = type;
        this.stock = new ArrayList<>();
        this.prix = prix;
        this.pos = pos;
        this.tempFabriq = tempFabriq;
        this.limiteStock = limiteStock;
        this.modifiable = modifiable;
        this.fonctionneBien = fonctionneBien;
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
                found = ressource.addQuantity(ressourceProduite.getQuantity());
                break;
            }
        }
        if (!found && stock.size() < limiteStock) {
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