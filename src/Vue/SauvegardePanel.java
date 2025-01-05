package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SauvegardePanel extends JPanel {

    private SetMenuListener menuListener;

    public SauvegardePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("GAME SAVES", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel sauvegardesPanel = chargerSauvegardes();
        JScrollPane scrollPane = new JScrollPane(sauvegardesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuListener != null) {
                    menuListener.onRetourMenu();
                }
            }
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void mettreAJourSauvegardes() {
        removeAll();

        JLabel title = new JLabel("GAME SAVES", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel sauvegardesPanel = chargerSauvegardes();
        JScrollPane scrollPane = new JScrollPane(sauvegardesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuListener != null) {
                    menuListener.onRetourMenu();
                }
            }
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel chargerSauvegardes() {
        JPanel sauvegardesPanel = new JPanel();
        sauvegardesPanel.setLayout(new BoxLayout(sauvegardesPanel, BoxLayout.Y_AXIS));
        sauvegardesPanel.setBackground(Color.DARK_GRAY);

        File sauvegardesFile = new File("Save/nom_save.txt");
        if (sauvegardesFile.exists()) {
            try {
                List<String> sauvegardes = Files.readAllLines(Paths.get(sauvegardesFile.getPath()));

                for (String sauvegarde : sauvegardes) {
                    JPanel sauvegardePanel = new JPanel();
                    sauvegardePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
                    sauvegardePanel.setBackground(Color.DARK_GRAY);

                    JLabel sauvegardeLabel = new JLabel(sauvegarde);
                    sauvegardeLabel.setForeground(Color.WHITE);
                    sauvegardeLabel.setFont(new Font("Arial", Font.PLAIN, 25));

                    JButton jouerButton = new JButton("Play");
                    jouerButton.setFocusPainted(false);
                    jouerButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            menuListener.startLoadGame(sauvegarde);
                        }
                    });

                    sauvegardePanel.add(sauvegardeLabel);
                    sauvegardePanel.add(jouerButton);
                    sauvegardesPanel.add(sauvegardePanel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JLabel noSavesLabel = new JLabel("No Game Saves found");
            noSavesLabel.setForeground(Color.WHITE);
            noSavesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            sauvegardesPanel.add(noSavesLabel);
        }

        return sauvegardesPanel;
    }

    public void setMenuListener(SetMenuListener listener) {
        this.menuListener = listener;
    }
}
