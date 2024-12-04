package Modele.Machine;

import java.util.ArrayList;

import Modele.Resource.Resource;
import Modele.*;

public class Machine {
    private String type; // "usine" ou "récolteuse"
    private ArrayList<Resource> stock = new ArrayList<>();
    private int prix;
    private Position pos;
    private int tempFabriq;
    private int limiteStock;
    private boolean modifiable;
    private boolean fonctionneBien;
    private String option;

    public Machine(String type, Position pos, String option, ArrayList<Resource> stock) {
        if (stock != null){
            this.stock = stock;
        }
        this.type = type;

        this.prix = getPrix(option);
        this.pos = pos;
        this.tempFabriq = getTempFabriq(option);
        this.limiteStock = getLimiteStock(type);
        this.modifiable = isModifiable(type);
        this.fonctionneBien = fonctionneBien(type);
        this.option = option;
        System.out.println(option);
    }
    private static int getPrix(String type) {
        return switch (type) {
            case "XL", "Rapide" -> 150;
            case "Fragile" -> 50;
            default -> 100; // Polyvalente
            
        };
    }

    private static int getTempFabriq(String type) {
        return switch (type) {
            case "Rapide" -> 2;
            default -> 10;
        };
    }

    private static int getLimiteStock(String type) {
        return switch (type) {
            case "XL" -> 10;
            default -> 5;
        };
    }

    private static boolean isModifiable(String type) {
        return false; // Toutes les usines sont non modifiables dans cet exemple
    }

    private static boolean fonctionneBien(String type) {
        return !type.equals("Fragile");
    }

    public String getName() {
        return type;
    }
    public int getPrix(){return prix;}
    public Position getPosition(){return pos;}
    public String getOption(){return option;}
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

    public String getStockSTR(){
        String res = "";
        for (Resource r : stock){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;


    }
    @Override
    public String toString(){
        return getName() + " " + getOption();
    }
    
}