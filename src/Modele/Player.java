package Modele;
import Vue.UpdateInterface;
import Modele.Machine.*;
import Modele.Resource.*;

public class Player {
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;
    private boolean ecoute = true;
    private int money;
    private UpdateInterface marketInterface;
    private UpdateInterface affichage;
    public void setEcoute(boolean ecoute) {
        this.ecoute = ecoute;
    }
    public boolean isEcoute(){return this.ecoute;} 

    public void move(Direction direction) {
        // Code pour déplacer le joueur sur la carte
    }
    
    public boolean collectResource(Resource resource) {
        if(inventory.addResource(resource)){
            try{
                marketInterface.notificationUpdate();
            }catch(Exception e){
                
            }
            return true;
            
        }
        
        return false;
    }
    public boolean collectQuantityResource(Resource resource, int quantity) {
        if(inventory.addQuantityResource(resource, quantity)){
            try{
                marketInterface.notificationUpdate();
            }catch(Exception e){
                
            }
            return true;
            
        }
        
        return false;
    }
    public void setMarketInterface(UpdateInterface marketInterface) {
        this.marketInterface = marketInterface;
    }
    public void setAffichageInterface(UpdateInterface affichage) {
        this.affichage = affichage;
    }
    public boolean addMachine(Machine machine) {
        
        boolean b =  inventory.addMachine(machine.getClone());
        
        if (affichage != null && marketInterface != null){
            marketInterface.notificationUpdate();
            affichage.notificationUpdate();
        }
        
        return b;
    }

    public boolean hasMachine(Machine m){
        return inventory.hasMachine(m);
    }
    public void removeMachine(){
        marketInterface.notificationUpdate();
        inventory.removeMachine();
    }


    public Player(String pseudo, Position position){
        this.pseudo = pseudo;
        inventory = new Inventory();
        this.position = position;
        money = 150;
    }

    public Player(String pseudo, int money, Position playerPosition, Inventory inventory) {
        this.pseudo = pseudo;
        this.money = money;
        this.position = playerPosition;
        this.inventory = inventory;

    }
    public boolean hasResource(Resource ressource){
        for (Resource r : inventory.getResources()){
            if (r.getName().equals(ressource.getName())){
                return true;
            }
        }
        return false;
    }

    public void removeResource(Resource r){

        inventory.removeResource(r, 1);
        System.out.println("test");
        marketInterface.notificationUpdate();
    }

    public void setPos(Position p){
        this.position = p;
    }
    public Position getPos(){return position;}

    public Inventory getInv() {return inventory;}
    public String getPseudo(){return pseudo;}
    public void setMoney(int m){money = m;}
    public int getMoney(){return money;}
    public Machine getMachine(){return inventory.getMachine();}
    
    
}