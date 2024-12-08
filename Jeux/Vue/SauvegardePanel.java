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

    private SauvegardeListener sauvegardeListener;  // Référence au listener

    public SauvegardePanel() {
        // Configuration du JPanel principal
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel title = new JLabel("SAUVEGARDE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Conteneur des sauvegardes
        JPanel sauvegardesPanel = chargerSauvegardes();
        JScrollPane scrollPane = new JScrollPane(sauvegardesPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inférieur pour le bouton Retour
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Bouton Retour
        JButton backButton = new JButton("Retour");
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sauvegardeListener != null) {
                    sauvegardeListener.onRetourMenu();  // Notifie le retour au menu
                }
            }
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Met à jour les sauvegardes affichées en rechargeant le fichier de sauvegardes.
     */
    public void mettreAJourSauvegardes() {
        // Retirer l'ancien panel contenant les sauvegardes
        removeAll();

        // Ajouter à nouveau les composants (titre, sauvegardes, bouton retour)
        JLabel title = new JLabel("SAUVEGARDE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel sauvegardesPanel = chargerSauvegardes();
        JScrollPane scrollPane = new JScrollPane(sauvegardesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton backButton = new JButton("Retour");
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sauvegardeListener != null) {
                    sauvegardeListener.onRetourMenu(); // Notifie le retour au menu
                }
            }
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Rafraîchir l'interface graphique
        revalidate();
        repaint();
    }



    /**
     * Charge toutes les sauvegardes depuis le fichier et retourne un panel contenant les sauvegardes.
     */
    private JPanel chargerSauvegardes() {
        JPanel sauvegardesPanel = new JPanel();
        sauvegardesPanel.setLayout(new BoxLayout(sauvegardesPanel, BoxLayout.Y_AXIS));
        sauvegardesPanel.setBackground(Color.DARK_GRAY);

        File sauvegardesFile = new File("Save/nom_save.txt");
        if (sauvegardesFile.exists()) {
            try {
                List<String> sauvegardes = Files.readAllLines(Paths.get(sauvegardesFile.getPath()));
                if (sauvegardes.isEmpty()) {
                    System.out.println("Le fichier de sauvegarde est vide.");
                }

                for (String sauvegarde : sauvegardes) {
                    JPanel sauvegardePanel = new JPanel();
                    sauvegardePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
                    sauvegardePanel.setBackground(Color.DARK_GRAY);

                    JLabel sauvegardeLabel = new JLabel(sauvegarde);
                    sauvegardeLabel.setForeground(Color.WHITE);
                    sauvegardeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

                    JButton jouerButton = new JButton("Jouer");
                    jouerButton.setFocusPainted(false);
                    jouerButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            sauvegardeListener.startLoadGame(sauvegarde);
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
            JLabel noSavesLabel = new JLabel("Aucune sauvegarde trouvée.");
            noSavesLabel.setForeground(Color.WHITE);
            noSavesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            sauvegardesPanel.add(noSavesLabel);
        }

        return sauvegardesPanel;
    }

    // Setter pour le listener
    public void setSauvegardeListener(SauvegardeListener listener) {
        this.sauvegardeListener = listener;
    }
}
