import java.io.Serializable;
import java.util.List;

public class Market implements Serializable{
    private List<Ressource> resourcesForSale;
    private List<Machine> machinesForSale;

    public void buyResource(Player player, Ressource resource) {
        // Code pour acheter une ressource
    }

    public void sellResource(Player player, Ressource resource) {
        // Code pour vendre une ressource
    }
}