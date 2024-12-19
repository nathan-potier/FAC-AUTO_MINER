package Modele;
import Vue.UpdateInterface;
import Modele.Machine.*;
import Modele.Resource.*;

public class Player {
    private Inventory inventory;
    private Position position; // Position du joueur sur la carte
    private String pseudo;
    private int money;
    private UpdateInterface marketInterface;
    private UpdateInterface affichage;

    public Player(String pseudo, Position position){
        this.pseudo = pseudo;
        inventory = new Inventory();
        this.position = position;
        money = 0;
    }

    public Player(String pseudo, int money, Position playerPosition, Inventory inventory) {
        this.pseudo = pseudo;
        this.money = money;
        this.position = playerPosition;
        this.inventory = inventory;

    }
    
    public boolean collectResource(Resource resource) {
        if(inventory.addResource(resource)){
            if(marketInterface != null){
                marketInterface.notificationUpdate();
            }

            return true;
            
        }
        return false;
    }

    public boolean collectQuantityResource(Resource resource) {
        if(inventory.addQuantityResource(resource)){
            if(marketInterface != null){
                marketInterface.notificationUpdate();
            }
            return true;

        }
        return false;
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
        marketInterface.notificationUpdate();
    }

    public void deplacerDun(Position nouvelle, Cell[][] grid, Game game){
        Position startPos = position;
        grid[nouvelle.getY()][nouvelle.getX()] = new Cell<>(nouvelle, this);
        grid[position.getY()][position.getX()] = null;
        this.position = nouvelle;
        game.notifyMove(this, startPos, position, Direction.getDirection(startPos, position).toString());
        game.updateAffichage(false);
    }

    public Position getPos(){return position;}
    public Inventory getInv() {return inventory;}
    public String getPseudo(){return pseudo;}
    public void setMoney(int m){money = m;}
    public int getMoney(){return money;}
    public Machine getMachine(){return inventory.getMachine();}

    public void setMarketInterface(UpdateInterface marketInterface) {this.marketInterface = marketInterface;}
    public void setAffichageInterface(UpdateInterface affichage) {this.affichage = affichage;}
    public void setPos(Position p){this.position = p;}
}