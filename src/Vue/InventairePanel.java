package Vue;
import javax.swing.*;
import javax.swing.border.LineBorder;



import java.awt.*;

import Modele.Player;
import Modele.Machine.Machine;

public class InventairePanel extends JPanel {

    private Player player;

    public InventairePanel(Player player) {
        this.player = player;
        this.setLayout(new GridLayout(2, 5, 5, 5)); // Grille 9x3 avec marges
        this.setBorder(new LineBorder(Color.DARK_GRAY, 20));  // Bordure noire de 20 pixels
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 170));
        this.setBackground(Color.DARK_GRAY);
    }

    public void setPlayer(Player p){player = p;}

    public void afficherInventaire() {
        this.removeAll(); // Nettoie les anciens slots
    
        // Affichage des ressources
        String[] resInvName = player.getInv().getResInvName();
        int[] resInvQuant = player.getInv().getResInvQuant();
    
        // Affichage des ressources dans l'inventaire
        for (int i = 0; i < resInvName.length; i++) {
            JPanel slot = new JPanel();
            slot.setLayout(new GridBagLayout()); // Utiliser GridBagLayout pour un contrôle précis du placement
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            slot.setBackground(Color.DARK_GRAY);
    
            // Définir des contraintes pour centrer les composants
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.CENTER; // Centrer les composants dans la cellule
    
            // Icône de la ressource
            JLabel iconLabel = new JLabel();
            ImageIcon icon = new ImageIcon("assets/" + resInvName[i] + ".png");
            iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    
            // Quantité de la ressource
            JLabel quantityLabel = new JLabel(String.valueOf(resInvQuant[i]));
            quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
            quantityLabel.setForeground(Color.WHITE);
    
            // Nom de la ressource
            JLabel nameLabel = new JLabel(resInvName[i]);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            nameLabel.setForeground(Color.WHITE);
    
            // Ajouter l'icône à la grille
            gbc.gridx = 0;
            gbc.gridy = 0;
            slot.add(iconLabel, gbc);
    
            // Ajouter le nom de la ressource sous l'icône
            gbc.gridy = 1;
            slot.add(nameLabel, gbc);
    
            // Ajouter la quantité sous le nom de la ressource
            gbc.gridy = 2;
            slot.add(quantityLabel, gbc);
    
            this.add(slot);
        }
    
        // Affichage de la machine si elle existe
        Machine machine = player.getInv().getMachine();
        if (machine != null) {
            JPanel machineSlot = new JPanel();
            machineSlot.setLayout(new GridBagLayout()); // Utiliser GridBagLayout pour la machine
            machineSlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            machineSlot.setBackground(Color.DARK_GRAY);
    
            // Définir des contraintes pour centrer les composants
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.CENTER; // Centrer les composants dans la cellule
    
            // Icône de la machine
            JLabel machineIconLabel = new JLabel();
            String machineImagePath = "assets/" + machine.getName().toLowerCase() + ".png"; // Nom de l'image en fonction du type de machine
            ImageIcon machineIcon = new ImageIcon(machineImagePath);
            machineIconLabel.setIcon(new ImageIcon(machineIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    
            // Nom de la machine
            JLabel machineNameLabel = new JLabel(machine.getName() + " (" + machine.getOption() + ")");
            machineNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            machineNameLabel.setForeground(Color.WHITE);
    
            // Ajouter l'icône de la machine à la grille
            gbc.gridx = 0;
            gbc.gridy = 0;
            machineSlot.add(machineIconLabel, gbc);
    
            // Ajouter le nom de la machine sous l'icône
            gbc.gridy = 1;
            machineSlot.add(machineNameLabel, gbc);
    
            this.add(machineSlot);
        }
    
        // Ajouter des slots vides si nécessaire
        int totalSlots = 10;
        for (int i = resInvName.length + (machine != null ? 1 : 0); i < totalSlots; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            emptySlot.setBackground(Color.DARK_GRAY);
            this.add(emptySlot);
        }
    
        this.revalidate();
        this.repaint();
    }
    


}

