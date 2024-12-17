package Modele.Machine;

import Modele.Resource.Resource;
import Vue.UpdateInterface;

import Modele.*;

public abstract class Machine implements Savable{
    private String type; // "usine" ou "récolteuse"
    private Inventory stockProd = new Inventory(); // stock de ce qui est recolté ou produit
    private Player player;
    private int prix;
    private Position pos;
    private int tempFabriq;
    private int limiteStock;
    private boolean modifiable;
    private boolean fonctionneBien;
    private String option;
    private UpdateInterface update;

    public Machine(String type, Position pos, String option, Inventory stockProd, Player player) {
        if (stockProd != null){
            this.stockProd = stockProd;
        }
        this.type = type;
        this.player = player;
        this.prix = getPrix(option);
        this.pos = pos;
        this.tempFabriq = getTempFabriq(option);
        this.limiteStock = getLimiteStock(option);
        this.modifiable = isModifiable(option);
        this.fonctionneBien = fonctionneBien(option);
        this.option = option;
        
    }
    public static int getPrix(String type) {
        return switch (type) {
            case "XL", "Rapide" -> 150;
            case "Fragile" -> 50;
            default -> 100; // Polyvalente
            
        };
    }

    public static int getTempFabriq(String type) {
        return switch (type) {
            case "Rapide" -> 2;
            default -> 5;
        };
    }

    public static int getLimiteStock(String type) {
        return switch (type) {
            case "XL" -> 30;
            default -> 20;
        };
    }

    public static boolean isModifiable(String type) {
        return switch (type) {
            case "Polyvalente" -> true;
            default -> false;
        };
    }

    public static boolean fonctionneBien(String type) {
        return !type.equals("Fragile");
    }

    public String getName() {
        return type;
    }
    public int getPrix(){return prix;}
    public Position getPosition(){return pos;}
    public String getOption(){return option;}
    public Inventory getStock() {return stockProd;}
    public boolean getIsModifiable(){return modifiable;}
    public int getTempFabriq(){return tempFabriq;}
    public boolean getFonctionneBien(){return fonctionneBien;}

    public void setInterfaceUpdate(UpdateInterface update){this.update = update;}
    public UpdateInterface getInterfaceUpdate(){return update;}

    public Player getPlayer(){return player;}

    public void ajouterAustock(Resource ressourceProduite) {
        for (Resource resource : stockProd.getResources()){
            resource.setLimiteStock(limiteStock); 
        }
        stockProd.addResource(ressourceProduite);
        update.notificationUpdate();
    }
    

    public void retirerRessource(Resource ressource) {
        stockProd.removeResource(ressource, 1);
        update.notificationUpdate();
    }

    public void produire() {}



    public String getStockSTR(){
        String res = "";
        for (Resource r : stockProd.getResources()){
            res += "(" + r.getName()+";"+ r.getQuantity()+ "),";
        }
        return res;
    }

    public abstract String getResourcesSTR();
    public abstract String getNomRecetteEnCours();
    public abstract Machine getClone();
    public abstract void setMap(Carte map);
    public void setPos(Position pos){this.pos = pos;}
    @Override
    public String toString(){
        return getName() + " " + getOption() + " " + getPosition();
    }

    @Override
    public String saveAsString() {
        return type + " option-> " + option + " a la position " + pos.getX() + "," + pos.getY() +
                " Stock-> {" + getStockSTR() + "} Inventaire-> {" + getResourcesSTR() + "} Recette->" + getNomRecetteEnCours();
    }
    @Override
    public void addToGrid(Cell[][] grid) {

        grid[pos.getY()][pos.getX()] = new Cell<>(pos, this);

    }
    
}