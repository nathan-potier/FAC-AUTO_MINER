package Vue;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import Modele.Ressource.Wood;
import Modele.Market;
import Modele.Position;
import Modele.Player;




public class MarketAffichTest {

    @SuppressWarnings("removal")
    public static void main(String[] args) {
        // Crée la fenêtre principale (JFrame)
        JFrame frame = new JFrame("Test Market Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Fenêtre plus grande que le MarketPanel
        frame.setLayout(null);    // Utilisation du layout null pour positionnement absolu

        // Crée un JLayeredPane pour gérer la superposition des composants
        JLayeredPane layeredPane = new JLayeredPane();
        frame.setContentPane(layeredPane);  // Remplace le content pane par le JLayeredPane

        // Instancie le Market et le Player
        Market market = new Market(new Position(0, 0));
        Position position = new Position(0, 0);
        Player player = new Player("Joueur1", position);  // Le joueur a 100 coins
        player.collectResource(new Wood(3)); 

        // Instancie un panneau d'accueil (pour l'affichage initial)
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBounds(0, 0, 800, 600);  // Prendre toute la taille de la fenêtre
        welcomePanel.setBackground(Color.BLACK);
        welcomePanel.add(new JLabel("Bienvenue ! Appuyez sur Entrée pour ouvrir le Marché"));

        // Instancie le MarketPanel avec le market et le player
        MarketPanel marketPanel = new MarketPanel(market, player);
        marketPanel.setVisible(false);  // Ne pas afficher le MarketPanel initialement

        // Ajoute les panneaux au JLayeredPane
        layeredPane.add(welcomePanel, new Integer(1));  // Panneau d'accueil au fond
        layeredPane.add(marketPanel, new Integer(2));   // MarketPanel par-dessus le welcomePanel

        // Ajouter un KeyListener pour détecter la touche "Entrée"
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Lorsque "Entrée" est pressée, afficher le MarketPanel
                    marketPanel.setVisible(true);  // Afficher le MarketPanel
                }
            }
        });

        // Nécessaire pour que la fenêtre puisse capter les événements clavier
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        // Affiche la fenêtre
        frame.setVisible(true);
    }
}
