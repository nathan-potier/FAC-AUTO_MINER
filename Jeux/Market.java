import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Market implements Serializable {
    private List<Ressource> resourcesForSale;
    private List<Machine> machinesForSale;

    // Initialisation des ressources et des machines disponibles
    public Market() {
        resourcesForSale = new ArrayList<>();
        machinesForSale = new ArrayList<>();

        // Ajouter des ressources à la vente
        resourcesForSale.add(new Wood(50));  // Exemple de ressource
        resourcesForSale.add(new Iron(30));


        // Ajouter des machines à la vente
        machinesForSale.add(new Usine(null));

    }

    public String buyResource(Player player, Ressource resource) {
        for (Ressource r : resourcesForSale) {
            if (r.getName().equals(resource.getName()) && player.getMoney() >= r.getPrix()) {
                player.setMoney(player.getMoney() - r.getPrix());
                player.collectResource(r);
                return player.getPseudo() + " a acheté " + r.getName() + " pour " + r.getPrix() + " coins.";
                
            }
        }
        return "Ressource non disponible ou fonds insuffisants.";
    }

    // Méthode de vente de ressource
    public String sellResource(Player player, Ressource resource) {
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
        for (Machine m : machinesForSale) {
            if (m.getName().equals(machine.getName()) && player.getMoney() >= m.getPrix() && player.addMachine(m)) {
                
                player.setMoney(player.getMoney() - m.getPrix());
                
                return player.getPseudo() + " a acheté " + m.getName() + " pour " + m.getPrix() + " coins.";
            }
        }
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
        for (Ressource r : resourcesForSale) {
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
}
