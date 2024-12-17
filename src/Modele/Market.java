package Modele;
import java.util.ArrayList;
import java.util.List;

import Modele.Machine.*;
import Modele.Resource.Armoire;
import Modele.Resource.Chaise;
import Modele.Resource.Clou;
import Modele.Resource.Iron;
import Modele.Resource.Marteau;
import Modele.Resource.Pierre;
import Modele.Resource.Planche;
import Modele.Resource.Resource;
import Modele.Resource.Table;
import Modele.Resource.Wood;

public class Market implements Savable{
    private List<Resource> resourcesForSale;
    private List<Machine> machinesForSale;
    private Position pos;



    // Initialisation des ressources et des machines disponibles
    public Market(Position pos) {

        resourcesForSale = new ArrayList<>();
        machinesForSale = new ArrayList<>();

        // Ajouter des ressources à la vente
        resourcesForSale.add(new Wood(1));   // Bois
        resourcesForSale.add(new Iron(1));   // Fer
        resourcesForSale.add(new Clou(1));   // Clou
        resourcesForSale.add(new Planche(1)); // Planche
        resourcesForSale.add(new Pierre(1)); // Pierre
        resourcesForSale.add(new Marteau(1)); // Marteau
        resourcesForSale.add(new Chaise(1)); // Chaise
        resourcesForSale.add(new Table(1));  // Table
        resourcesForSale.add(new Armoire(1)); // Armoire

    


        // Ajouter des machines à la vente
        machinesForSale.add(new Usine("XL") );
        machinesForSale.add(new Usine("Rapide") );
        machinesForSale.add(new Usine("Polyvalente") );
        machinesForSale.add(new Usine("Fragile") );
        // Ajouter des machines à la vente
        machinesForSale.add(new Recolteuse("XL") );
        machinesForSale.add(new Recolteuse("Rapide") );
        machinesForSale.add(new Recolteuse("Polyvalente") );
        machinesForSale.add(new Recolteuse("Fragile") );

        this.pos = pos;

    }
 

    public List<Resource> getResourcesForSale(){
        return resourcesForSale;
    }
    public List<Machine> getMachinesForSale(){return machinesForSale;}

    public String buyResource(Player player, Resource resource) {
        int prix = resource.getQuantity() * resource.getPrix();
        if(player.getMoney() >= prix){
            if (player.collectResource(resource)){
                player.setMoney(player.getMoney() - prix);
                return player.getPseudo() + " a acheté " + resource.getName() + " pour " + prix + " coins.";
            }
            
            
            
        }
        return "Ressource non disponible ou fonds insuffisants.";
    }

    // Méthode de vente de ressource
    public String sellResource(Player player, Resource resource) {
        if (player.hasResource(resource)) {
            player.setMoney(player.getMoney() + resource.getPrix() / 2);  // Vendre à moitié prix
            player.removeResource(resource);
            return player.getPseudo() + " a vendu " + resource.getName() + " pour " + resource.getPrix() / 2 + " coins.";
        } else {
            return "Le joueur ne possède pas cette ressource.";
        }
    }

    // Méthode d'achat de machine
    public String buyMachine(Player player, Machine machine) {

        if ( player.getMoney() >= machine.getPrix() && player.addMachine(machine) ) {
            
            player.setMoney(player.getMoney() - machine.getPrix());
            
            return player.getPseudo() + " a acheté " + machine.getName() + " pour " + machine.getPrix() + " coins.";
        }
        System.out.println(player.getMoney() >= machine.getPrix());
    
        return "Machine non disponible ou fonds insuffisants.";
    }

    // Méthode de vente de machine
    public String sellMachine(Player player, Machine machine) {
        if (player.hasMachine(machine)) {
            player.setMoney(player.getMoney() + machine.getPrix() / 2);  // Vendre à moitié prix
            player.removeMachine();
            return player.getPseudo() + " a vendu " + machine.getName() + " pour " + machine.getPrix() / 2 + " coins.";
        } else {
            return "Le joueur ne possède pas cette machine.";
        }
    }

    // Méthode pour afficher les ressources disponibles à l'achat
    public String displayResourcesForSale() {
        StringBuilder display = new StringBuilder("Ressources disponibles à l'achat :\n");
        for (Resource r : resourcesForSale) {
            display.append(r.getName()).append(" - ").append(r.getPrix()).append(" coins\n");
        }
        return display.toString();
    }

    // Méthode pour afficher les machines disponibles à l'achat
    public String displayMachinesForSale() {
        StringBuilder display = new StringBuilder("Machines disponibles à l'achat :\n");
        for (Machine m : machinesForSale) {
            display.append(m.getName()).append(" - ").append(m.getPrix()).append(" coins\n");
        }
        return display.toString();
    }

    public Position getPos(){
        return pos;
    }
    public void setPos(Position pos){
        this.pos = pos;
    }
    @Override
    public String saveAsString() {
        return "Market " + "(" + pos.getX() + ";" + pos.getY() + ")";
    }
    @Override
    public void addToGrid(Cell[][] grid) {

        grid[pos.getY()][pos.getX()] = new Cell<>(pos, this);

    }

}
