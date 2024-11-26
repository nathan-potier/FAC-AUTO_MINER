import javax.swing.*;

public class MarketAffichTest {

    public static void main(String[] args) {
        // Crée la fenêtre principale (JFrame)
        JFrame frame = new JFrame("Test Market Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        // Instancie le Market et le Player
        Market market = new Market();
        Position position = new Position(0, 0);
        Player player = new Player("Joueur1", position);  // Le joueur a 100 coins

        // Instancie le MarketPanel avec le market et le player
        MarketPanel marketPanel = new MarketPanel(null, market, player);

        // Ajoute le panneau à la fenêtre
        frame.add(marketPanel);

        // Affiche la fenêtre
        frame.setVisible(true);
    }
}
