package Modele.Resource;

import Modele.*;

public abstract class Resource implements  ObjectWithPosition{
    private String name;
    private int quantity;
    private int prix;
    private boolean laborieuse = false;
    private boolean temporisée = false;
    private Position pos;
    private int limiteStock = 20;
    

    public Resource(String name, int quantity, Boolean laborieuse, int prix) {
        this.name = name;
        this.quantity = quantity;
        this.laborieuse = laborieuse;
        this.temporisée = !laborieuse;
        this.prix = prix;
    }
    public Resource(String name, int quantity, Boolean laborieuse, int prix, Position pos) {
        this(name, quantity, laborieuse, prix);
        this.pos = pos;

    }

    public Resource(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }
    public Position getPosition(){return pos;}
    public int getQuantity() {
        return quantity;
    }

    public boolean addQuantity(int amount) {
        if(quantity + amount < limiteStock){
            quantity += amount;
            return true;
        }
        return false;

    }

    public void removeQuantity(int amount) {
        quantity = Math.max(0, quantity - amount);
    }

    public boolean isLaborieuse() {
        return laborieuse;
    }

    public boolean isTemporisée() {
        return temporisée;
    }
    public int getPrix(){return prix;}


    public abstract Resource clone();
}
