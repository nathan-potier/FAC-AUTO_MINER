package Vue;

import javax.swing.*;

import Modele.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    private boolean isFullScreen;

    private SetMenuListener listener;

    // Variable temporaire pour les changements en attente
    private boolean tempFullScreen = false;

    public SettingsPanel(Affichage frame) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SETTINGS", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.add(Box.createVerticalStrut(400));

        JPanel fullScreenOption = new JPanel();
        fullScreenOption.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        fullScreenOption.setBackground(Color.DARK_GRAY);

        JLabel fullScreenLabel = new JLabel("Full Screen: ");
        fullScreenLabel.setForeground(Color.WHITE);
        fullScreenLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton fullScreenButton = new JButton(isFullScreen ? "ON" : "OFF");
        fullScreenButton.setFocusPainted(false);
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

        topPanel.add(fullScreenOption);

        add(topPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton saveButton = new JButton("Save Settings");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {     
                isFullScreen = tempFullScreen;

                Game game = frame.getGame();
                game.setFullScreen(isFullScreen);
        
                frame.close();
                if (isFullScreen) {
                    Dimensions.BASE_CELL_SIZE = 80; // Mode plein écran
                } else {
                    Dimensions.BASE_CELL_SIZE = 50; // Mode fenêtré
                }
    
                Affichage affichage = new Affichage(game);
                affichage.setVisible(true);
            }
        });
        bottomPanel.add(saveButton);

        JButton quitButton = new JButton("Back");
        quitButton.setFocusPainted(false);
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
