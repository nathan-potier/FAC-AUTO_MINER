import java.awt.*;
import javax.swing.*;

public class NestedPanelsExample {
    public static void main(String[] args) {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Exemple de JPanel imbriqués");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Créer le panneau principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // Utiliser null layout pour positionner précisément les composants
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // Créer un sous-panneau
        JPanel nestedPanel = new JPanel();
        nestedPanel.setBounds(100, 50, 200, 100); // Positionner et définir une taille pour le sous-panneau
        nestedPanel.setBackground(Color.BLUE);

        // Ajouter le sous-panneau au panneau principal
        mainPanel.add(nestedPanel);

        // Ajouter le panneau principal à la fenêtre
        frame.add(mainPanel);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }
}
