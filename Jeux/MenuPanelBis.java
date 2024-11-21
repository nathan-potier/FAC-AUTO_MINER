import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanelBis {

    private Bouton newGameButton;
    private JPanel customPanel; // Le JPanel qui va contenir et dessiner le bouton

    public MenuPanelBis(JPanel mainPanel, ActionListener listener) {
        // Créer un bouton avec les coordonnées et l'image spécifiée
        newGameButton = new Bouton(50, 50,160,85, "assets/play_button.png");

        // Créer un JPanel personnalisé pour dessiner le bouton
        customPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessiner le bouton sur le panneau
                newGameButton.afficher(g);
            }
        };

        // Définir la taille du customPanel en fonction des dimensions du bouton
        customPanel.setBounds(0, 0, (int) newGameButton.getWidth(), (int) newGameButton.getHeight());
        customPanel.setOpaque(false); // Rendre le fond transparent

        // Ajouter le customPanel au mainPanel
        mainPanel.setLayout(null); // Utiliser un layout null pour positionner les composants librement
        mainPanel.add(customPanel);
    }
}
