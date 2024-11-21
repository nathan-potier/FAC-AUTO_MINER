import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartePanel extends JPanel {

    private Game game;

    public CartePanel(Game game) {
        this.game = game;
        this.setLayout(new GridLayout(game.getHeight(), game.getWidth()));
        // Ajouter une bordure autour du panneau contenant la carte
        this.setBorder(new LineBorder(Color.DARK_GRAY, 20));  // Bordure noire de 20 pixels
    }

    public void afficherCarte() {
        this.removeAll(); // Supprimer les anciens éléments
    
        for (Cell[] row : game.getMap().getGrid()) {
            for (Cell cell : row) {
                JPanel cellPanel = new JPanel();
                //cellPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Changer la couleur de la grille
                cellPanel.setPreferredSize(new Dimension(50, 50)); // Taille fixe pour chaque cellule
                cellPanel.setBackground(Color.decode("#cc964e"));
                cellPanel.setLayout(new BorderLayout()); // Utiliser BorderLayout pour remplir l'espace disponible
    
                if (cell != null && cell.getContent() != null) {
                    // Si la case contient du contenu, afficher l'image correspondante
                    ImageIcon icon = getIconForType(cell.getContent().getClass().getSimpleName());
                    if (icon != null) {
                        JLabel contentLabel = new JLabel();
                        contentLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        contentLabel.setVerticalAlignment(SwingConstants.CENTER);
                        // Ajouter l'image de la cellule
                        cellPanel.add(contentLabel, BorderLayout.CENTER);
                    }
                } else {
                    // Si la case est vide, afficher l'image de Terre
                    ImageIcon terraIcon = new ImageIcon("assets/Terre.png");
                    JLabel terraLabel = new JLabel();
                    terraLabel.setIcon(new ImageIcon(terraIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    terraLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    terraLabel.setVerticalAlignment(SwingConstants.CENTER);
                    // Ajouter l'image de la Terre dans le panneau de la cellule
                    cellPanel.add(terraLabel, BorderLayout.CENTER);
                }
    
                this.add(cellPanel);
            }
        }
        this.revalidate();
        this.repaint();
    }
    

    public void actualiseAffichage(int temps) {
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherCarte();
                actualiseAffichage(10);

                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
    }

    // Méthode pour obtenir une icône pour un type donné
    private ImageIcon getIconForType(String type) {
        switch (type) {
            case "Player":
                return new ImageIcon("assets/player.png");
            case "Wood":
                return new ImageIcon("assets/Wood.png");
            case "Iron":
                return new ImageIcon("assets/Iron.png");
            default:
                return null;
        }
    }
}
