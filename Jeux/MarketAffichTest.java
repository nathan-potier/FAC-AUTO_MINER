import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MarketAffichTest {

    public static void main(String[] args) {
        // Crée la fenêtre principale (JFrame)
        JFrame frame = new JFrame("Test Market Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Fenêtre plus grande que le MarketPanel

        // Instancie le Market et le Player
        Market market = new Market();
        Position position = new Position(0, 0);
        Player player = new Player("Joueur1", position);  // Le joueur a 100 coins

        // Instancie un panneau d'accueil (pour l'affichage initial)
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(new JLabel("Bienvenue ! Appuyez sur Entrée pour ouvrir le Marché"));

        // Ajoute le panneau d'accueil à la fenêtre
        frame.add(welcomePanel);
        frame.setVisible(true);

        // Ajouter un KeyListener pour détecter la touche "Entrée"
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Lorsque "Entrée" est pressée, remplacer le panneau d'accueil par le MarketPanel
                    frame.remove(welcomePanel);  // Retirer le panneau d'accueil

                    // Instancie le MarketPanel avec le market et le player
                    MarketPanel marketPanel = new MarketPanel(market, player);

                    // Ajoute le MarketPanel à la fenêtre
                    frame.add(marketPanel);
                    frame.revalidate();  // Revalidé le layout pour appliquer le changement
                    frame.repaint();     // Repeint la fenêtre

                    // Si besoin, vous pouvez désactiver les autres événements clavier après l'entrée
                    frame.removeKeyListener(this);
                }
            }
        });

        // Nécessaire pour que la fenêtre puisse capter les événements clavier
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
}
