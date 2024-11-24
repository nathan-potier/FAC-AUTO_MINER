import javax.swing.*;
import java.awt.*;

public class InventairePanel extends JPanel {

    private Game game;

    public InventairePanel(Game game) {
        this.game = game;
        this.setLayout(new GridLayout(3, 9, 5, 5)); // Grille 9x3 avec marges
    }
    public void setGame(Game g){game = g;}

    public void afficherInventaire() {
        this.removeAll(); // Nettoie les anciens slots

        String[] resInvName = game.getPlayer().getInv().getResInvName();
        int[] resInvQuant = game.getPlayer().getInv().getResInvQuant();

        for (int i = 0; i < resInvName.length; i++) {
            JPanel slot = new JPanel(new BorderLayout());
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            JLabel iconLabel = new JLabel();
            ImageIcon icon = new ImageIcon("assets/" + resInvName[i] + ".png");
            iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel quantityLabel = new JLabel(String.valueOf(resInvQuant[i]));
            quantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            quantityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));

            slot.setLayout(new OverlayLayout(slot));
            slot.add(quantityLabel);
            slot.add(iconLabel);

            this.add(slot);
        }

        int totalSlots = 27; // 9x3 slots
        for (int i = resInvName.length; i < totalSlots; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            this.add(emptySlot);
        }

        this.revalidate();
        this.repaint();
    }
}
