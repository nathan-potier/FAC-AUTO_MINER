import javax.swing.*;
import java.awt.*;

public class MarketAffichTest {

    public static void main(String[] args) {
        // Création des objets nécessaires
        Player player = new Player("Jonas");  // Un joueur avec 100 coins
        Market market = new Market();  // Le marché avec des ressources et machines
        JFrame testFrame = new JFrame("Test MarketPanel");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(400, 300);
        testFrame.setLayout(new BorderLayout());

        // Création et ajout du panneau de marché
        MarketPanel marketPanel = new MarketPanel(null, market, player);
        testFrame.add(marketPanel, BorderLayout.CENTER);

        // Affichage de la fenêtre
        testFrame.setVisible(true);
    }
}
