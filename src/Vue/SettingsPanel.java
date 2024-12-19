package Vue;

import javax.swing.*;

import Modele.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    private boolean isFullScreen;
    private int controlMode = 0; // 0 = Arrows, 1 = Mouse, 2 = Les deux

    private SetMenuListener listener;  // Référence au listener pour notifier l'action

    // Variables temporaires pour les changements en attente
    private boolean tempFullScreen = false;
    private int tempControlMode = 0;

    public SettingsPanel(Affichage frame) {
        // Configuration du JPanel principal
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        // Ajouter une bordure pour créer de l'espace autour des éléments
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre en haut
        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Panel pour les options (Full Screen et Control)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(Color.DARK_GRAY);

        // Espacement entre le titre et les boutons
        topPanel.add(Box.createVerticalStrut(400));

        // Option Full Screen avec BoxLayout vertical
        JPanel fullScreenOption = new JPanel();
        fullScreenOption.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        fullScreenOption.setBackground(Color.DARK_GRAY);

        JLabel fullScreenLabel = new JLabel("Full Screen: ");
        fullScreenLabel.setForeground(Color.WHITE);
        fullScreenLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton fullScreenButton = new JButton("OFF");
        fullScreenButton.setFocusPainted(false); // Désactiver le focus visuel
        fullScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempFullScreen = !tempFullScreen;
                fullScreenButton.setText(tempFullScreen ? "ON" : "OFF");
            }
        });

        fullScreenOption.add(fullScreenLabel);
        fullScreenOption.add(Box.createVerticalStrut(10));
        fullScreenOption.add(fullScreenButton);

        // Option Control avec BoxLayout vertical
        JPanel controlOption = new JPanel();
        controlOption.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlOption.setBackground(Color.DARK_GRAY);

        JLabel controlLabel = new JLabel("Control: ");
        controlLabel.setForeground(Color.WHITE);
        controlLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton controlButton = new JButton("Arrows");
        controlButton.setFocusPainted(false); // Désactiver le focus visuel
        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempControlMode = (tempControlMode + 1) % 3; // Passer à la prochaine option
                switch (tempControlMode) {
                    case 0:
                        controlButton.setText("Arrows");
                        break;
                    case 1:
                        controlButton.setText("Mouse");
                        break;
                    case 2:
                        controlButton.setText("Arrows and Mouse");
                        break;
                }
            }
        });

        controlOption.add(controlLabel);
        controlOption.add(Box.createVerticalStrut(10));
        controlOption.add(controlButton);

        // Ajouter les options dans le topPanel
        topPanel.add(fullScreenOption);
        topPanel.add(controlOption);

        // Ajouter le panneau topPanel dans le centre
        add(topPanel, BorderLayout.CENTER);

        // Panneau pour les boutons en bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);

        // Ajouter un espace sous les boutons
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Message de confirmation
        JLabel confirmationLabel = new JLabel("");
        confirmationLabel.setForeground(Color.GREEN);
        confirmationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmationLabel.setVisible(false); // Masqué par défaut
        bottomPanel.add(confirmationLabel);

        // Bouton Save
        JButton saveButton = new JButton("Save Settings");
        saveButton.setFocusPainted(false); // Désactiver le focus visuel
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appliquer les modifications
                isFullScreen = tempFullScreen;
                controlMode = tempControlMode;
                Game game = frame.getGame();
                frame.close();
                // Mettre à jour BASE_CELL_SIZE en fonction du mode plein écran
                if (isFullScreen) {
                    Dimensions.BASE_CELL_SIZE = 80; // Mode plein écran
                } else {
                    Dimensions.BASE_CELL_SIZE = 50; // Mode fenêtre
                    
                }

                Affichage affichage = new Affichage(game);
        
                // Afficher le message de confirmation
                confirmationLabel.setText("Preferences sauvegardées");
                confirmationLabel.setVisible(true);

                // Supprimer le message après un court délai
                Timer timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirmationLabel.setVisible(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        

        bottomPanel.add(saveButton);

        // Bouton Quitter
        JButton quitButton = new JButton("Back");
        quitButton.setFocusPainted(false); // Désactiver le focus visuel
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onRetourMenu();
            }
        });

        bottomPanel.add(quitButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setMenuListener(SetMenuListener listener) {
        this.listener = listener;
    }

    

}
