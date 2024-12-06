package Modele.Machine;

import java.util.ArrayList;

import Modele.Resource.Resource;
import Modele.*;

public abstract class Machine {
    private String type; // "usine" ou "récolteuse"
    private Inventory stockProd = new Inventory(); //stock de ce qui est recolté ou produit
    private int prix;
    private Position pos;
    private int tempFabriq;
    private int limiteStock;
    private boolean modifiable;
    private boolean fonctionneBien;
    private String option;

    public Machine(String type, Position pos, String option, Inventory stockProd) {
        if (stockProd != null){
            this.stockProd = stockProd;
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
    public Inventory getStock() {return stockProd;}


    public void ajouterRessource(Resource ressourceProduite) {
        stockProd.addResource(ressourceProduite);

    }
    

    public void retirerRessource(Resource ressource) {
        stockProd.removeResource(ressource, 1);;
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
        System.out.println(stockProd);
    }

    public String getStockSTR(){
        String res = "";
        for (Resource r : stockProd.getResources()){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;
    }

    public abstract String getResourcesSTR();

    @Override
    public String toString(){
        return getName() + " " + getOption();
    }
    
}