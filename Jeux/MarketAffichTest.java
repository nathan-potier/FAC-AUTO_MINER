import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MarketAffichTest {

    public static void main(String[] args) {
        // Création des objets nécessaires
        Market market = new Market();  // Le marché avec des ressources et machines

        // Fenêtre principale
        JFrame testFrame = new JFrame("Test MarketPanel");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(1280, 720);
        testFrame.setLayout(new BorderLayout());

        // Panneau de marché (vide au départ)
        MarketPanel marketPanel = new MarketPanel(null, market);
        JLabel emptyLabel = new JLabel("Appuyez sur 'Entrée' pour afficher le marché", SwingConstants.CENTER);
        testFrame.add(emptyLabel, BorderLayout.CENTER);

        // Ajout du KeyListener pour détecter "Entrée"
        testFrame.addKeyListener(new KeyListener() {
            private boolean marketDisplayed = false; // Suivi de l'état d'affichage

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !marketDisplayed) {
                    // Remplacer le contenu de la fenêtre par le marché
                    testFrame.remove(emptyLabel); // Retire l'étiquette vide
                    testFrame.add(marketPanel, BorderLayout.CENTER); // Ajoute le panneau du marché
                    testFrame.revalidate(); // Actualise la mise en page
                    testFrame.repaint(); // Redessine la fenêtre
                    marketDisplayed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Rien à faire ici
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // Rien à faire ici
            }
        });

        // Rendre la fenêtre focusable pour capturer les entrées clavier
        testFrame.setFocusable(true);
        testFrame.requestFocusInWindow();

        // Affichage de la fenêtre
        testFrame.setVisible(true);
    }
}

