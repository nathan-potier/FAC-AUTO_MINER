package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    private boolean isFullScreen = false;
    private boolean useMouse = false;
    private int controlMode = 0; // 0 = Arrows, 1 = Mouse, 2 = Both

    public SettingsPanel() {
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
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Alignement centré avec un espacement entre les boutons
        topPanel.setBackground(Color.DARK_GRAY);

        // Espacement entre le titre et les boutons
        topPanel.add(Box.createVerticalStrut(400));  // Ajouter un espacement de 50 pixels

        // Option Full Screen avec BoxLayout vertical
        JPanel fullScreenOption = new JPanel();
        fullScreenOption.setLayout(new BoxLayout(fullScreenOption, BoxLayout.Y_AXIS));  // Alignement vertical
        fullScreenOption.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Alignement centré avec un espacement entre les boutons
        fullScreenOption.setBackground(Color.DARK_GRAY);

        JLabel fullScreenLabel = new JLabel("Full Screen: ");
        fullScreenLabel.setForeground(Color.WHITE);
        fullScreenLabel.setFont(new Font("Arial", Font.PLAIN, 18));  // Augmenter la taille du texte
        JButton fullScreenButton = new JButton("OFF");
        fullScreenButton.setFocusPainted(false); // Désactiver le focus visuel
        fullScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isFullScreen = !isFullScreen;
                fullScreenButton.setText(isFullScreen ? "ON" : "OFF");
            }
        });

        fullScreenOption.add(fullScreenLabel);
        fullScreenOption.add(Box.createVerticalStrut(10));  // Ajouter un espacement supplémentaire entre le texte et le bouton
        fullScreenOption.add(fullScreenButton);

        // Option Control avec BoxLayout vertical
        JPanel controlOption = new JPanel();
        controlOption.setLayout(new BoxLayout(controlOption, BoxLayout.Y_AXIS));  // Alignement vertical
        controlOption.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Alignement centré avec un espacement entre les boutons
        controlOption.setBackground(Color.DARK_GRAY);

        JLabel controlLabel = new JLabel("Control: ");
        controlLabel.setForeground(Color.WHITE);
        controlLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Augmenter la taille du texte
        JButton controlButton = new JButton("Arrows");
        controlButton.setFocusPainted(false); // Désactiver le focus visuel
        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlMode = (controlMode + 1) % 3; // Passer à la prochaine option
                switch (controlMode) {
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
        controlOption.add(Box.createVerticalStrut(10));  // Ajouter un espacement supplémentaire entre le texte et le bouton
        controlOption.add(controlButton);

        // Ajouter les options dans le topPanel
        topPanel.add(fullScreenOption);
        topPanel.add(controlOption);

        // Ajouter le panneau topPanel dans le centre
        add(topPanel, BorderLayout.CENTER);

        // Panneau pour le bouton Save Settings en bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.DARK_GRAY);
        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SettingsPanel.this,
                    "Settings saved:\n" +
                    "Full Screen: " + (isFullScreen ? "ON" : "OFF") + "\n" +
                    "Control Mode: " + (useMouse ? "Mouse" : "Arrows"),
                    "Settings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Test du JPanel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new SettingsPanel());
        frame.setVisible(true);
    }
}
