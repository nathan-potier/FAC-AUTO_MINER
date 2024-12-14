package Vue;
import Modele.Machine.*;
public interface VueGenerale {
    void transmettreAction(String action);
    void transmettreMarket();
    void transmettreUsine(Usine m);
    void transmettreRecolteuse(Recolteuse r);
}
