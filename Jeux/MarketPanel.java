import java.awt.*;
import javax.swing.*;

public class MarketPanel extends JPanel {

    private Market market;
    private Player player;  
    private JLabel moneyLabel;  

    public MarketPanel(Market market, Player player) {
        this.market = market;
        this.player = player;

        // Configuration de base du panel
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau principal contenant la liste et les éléments
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);

        // Affichage de l'argent du joueur
        moneyLabel = new JLabel("Argent: " + player.getMoney() + " coins");
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(moneyLabel);

        // Ajouter une ligne de séparation
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Liste des items disponibles
        // Ajouter les items au marché
        addItemToMarket(mainPanel, "assets/Wood.png", "Wood", new Wood(1));
        addItemToMarket(mainPanel, "assets/Iron.png", "Iron", new Iron(1));


        // Ajouter un bouton "Fermer" en bas
        JButton closeButton = new JButton("Fermer");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> this.setVisible(false));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Espacement
        mainPanel.add(closeButton);

        // Ajouter un scrollPane pour la liste
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void addItemToMarket(JPanel panel, String imagePath, String name, Ressource resource) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        // Charger et redimensionner l'image
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
    
        // Image de l'objet
        JLabel imageLabel = new JLabel(scaledIcon);
        itemPanel.add(imageLabel);
    
        // Nom de l'objet
        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        itemPanel.add(nameLabel);
    
        // Récupérer le prix de la ressource
        int price = resource.getPrix();
    
        // Bouton d'achat (prix)
        JButton buyButton = new JButton(price + " coins");
        buyButton.addActionListener(e -> {
            String message = market.buyResource(player, resource);
            JOptionPane.showMessageDialog(this, message);
            updateMoneyLabel();
        });
        itemPanel.add(buyButton);
    
        // Ajouter un peu d'espacement autour de l'item
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(itemPanel);
    }
    

    private void updateMoneyLabel() {
        moneyLabel.setText("Argent: " + player.getMoney() + " coins");
    }
}
