package Vue;
import Modele.Machine.*;
public interface VueGenerale {
    void transmettreMarket();
    void transmettreUsine(Usine m);
    void transmettreRecolteuse(Recolteuse r);
}