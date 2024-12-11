package Vue;
import javax.swing.*;
import javax.swing.border.LineBorder;



import java.awt.*;

import Modele.Player;

public class InventairePanel extends JPanel {

    private Player player;

    public InventairePanel(Player player) {
        this.player = player;
        this.setLayout(new GridLayout(3, 9, 5, 5)); // Grille 9x3 avec marges
        this.setBorder(new LineBorder(Color.DARK_GRAY, 20));  // Bordure noire de 20 pixels
        this.setBackground(Color.DARK_GRAY); // Arrière-plan gris foncé pour l'ensemble du panneau
    }
    public void setPlayer(Player p){player = p;}

    public void afficherInventaire() {
        this.removeAll(); // Nettoie les anciens slots

        String[] resInvName = player.getInv().getResInvName();
        int[] resInvQuant = player.getInv().getResInvQuant();

        for (int i = 0; i < resInvName.length; i++) {
            JPanel slot = new JPanel(new BorderLayout());
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Bordure gris clair autour de chaque slot
            slot.setBackground(Color.DARK_GRAY); // Assurez-vous que le fond de chaque slot est aussi gris foncé

            // Icône
            JLabel iconLabel = new JLabel();
            ImageIcon icon = new ImageIcon("assets/" + resInvName[i] + ".png");
            iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Quantité
            JLabel quantityLabel = new JLabel(String.valueOf(resInvQuant[i]));
            quantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            quantityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
            quantityLabel.setForeground(Color.WHITE); // Texte en blanc pour mieux contraster

            // Ajout des composants à chaque slot
            slot.setLayout(new OverlayLayout(slot));
            slot.add(quantityLabel);
            slot.add(iconLabel);

            this.add(slot);
        }

        // Ajouter des slots vides si nécessaire
        int totalSlots = 27; // 9x3 slots
        for (int i = resInvName.length; i < totalSlots; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            emptySlot.setBackground(Color.DARK_GRAY); // Assurez-vous que les slots vides sont également gris foncé
            this.add(emptySlot);
        }

        this.revalidate();
        this.repaint();
    }
}

