import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarketPanel extends JPanel {

    public MarketPanel(Affichage affichage, Market market, Player player) {
        this.setLayout(new GridLayout(3, 1, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel("Bienvenue au Marché !");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JButton buyButton = new JButton("Acheter");
        JButton sellButton = new JButton("Vendre");

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(affichage, market.displayResourcesForSale(), "Ressources disponibles", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(affichage, "Quelles ressources voulez-vous vendre ?", "Vente de Ressources", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.add(titleLabel);
        this.add(buyButton);
        this.add(sellButton);
    }

    
}
