package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton continueButton;
    private JButton newGameButton;
    private JButton settingsButton;
    private JButton quitButton;

    public MenuPanel(ActionListener listener) {
        this.setLayout(new FlowLayout());

        // Création des boutons
        continueButton = createStyledButton("Charger une Partie");
        newGameButton = createStyledButton("Nouvelle Partie");
        settingsButton = createStyledButton("Réglages");
        quitButton = createStyledButton("Sauvegarder et Quitter");

        // Ajout des écouteurs d'événements
        continueButton.addActionListener(listener);
        newGameButton.addActionListener(listener);
        settingsButton.addActionListener(listener);
        quitButton.addActionListener(listener);

        // Ajout des boutons au panneau
        this.add(continueButton);
        this.add(newGameButton);
        this.add(settingsButton);
        this.add(quitButton);
    }

    // Méthode utilitaire pour créer un bouton stylisé
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        return button;
    }


    
}