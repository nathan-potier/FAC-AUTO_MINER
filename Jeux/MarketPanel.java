import java.awt.*;
import javax.swing.*;
import java.util.List;

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
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        this.setBounds(100, 50, 600, 400);
    
        // Affichage de l'argent du joueur avec espace supplémentaire en dessous
        JPanel moneyPanel = new JPanel(new BorderLayout());
        moneyPanel.setBackground(Color.DARK_GRAY);
        moneyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        moneyLabel = new JLabel("Argent: " + player.getMoney() + " coins");
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        moneyPanel.add(moneyLabel, BorderLayout.CENTER);
        this.add(moneyPanel, BorderLayout.NORTH);

    
        // Panneau central pour afficher les items disponibles et l'inventaire
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));  // 2 colonnes : Achats / Ventes
        centerPanel.setBackground(Color.DARK_GRAY);
    
        // Panneau pour les objets à acheter
        JPanel buyPanel = new JPanel();
        buyPanel.setLayout(new BoxLayout(buyPanel, BoxLayout.Y_AXIS));
        buyPanel.setBackground(Color.DARK_GRAY);
        buyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "À Acheter", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
    
        // Panneau pour les objets de l'inventaire
        JPanel sellPanel = new JPanel();
        sellPanel.setLayout(new BoxLayout(sellPanel, BoxLayout.Y_AXIS));
        sellPanel.setBackground(Color.DARK_GRAY);
        sellPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Inventaire", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        updateInventoryPanel(sellPanel);

        // Ajouter des objets au marché
        addItemToMarket(buyPanel, "assets/Wood.png", "Wood", new Wood(1), sellPanel);
        addItemToMarket(buyPanel, "assets/Iron.png", "Iron", new Iron(1), sellPanel);
    
        // Ajout des panneaux dans des JScrollPane
        JScrollPane buyScrollPane = new JScrollPane(buyPanel);
        buyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        buyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
        JScrollPane sellScrollPane = new JScrollPane(sellPanel);
        sellScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sellScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
        // Ajouter les JScrollPane au panneau central
        centerPanel.add(buyScrollPane);
        centerPanel.add(sellScrollPane);
    
        // Ajouter le panneau central à la fenêtre principale
        this.add(centerPanel, BorderLayout.CENTER);

        // Panneau pour le bouton "Fermer" avec marge
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Marge supérieure de 20 pixels

        // Bouton Fermer
        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            // Cache le MarketPanel au lieu de fermer toute la fenêtre
            this.setVisible(false);
        });

        // Ajouter le bouton au panneau
        closeButtonPanel.add(closeButton, BorderLayout.CENTER);

        // Ajouter le panneau contenant le bouton au bas du MarketPanel
        this.add(closeButtonPanel, BorderLayout.SOUTH);

    }

    private void addItemToMarket(JPanel panel, String imagePath, String name, Ressource resource, JPanel inventoryPanel) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        // Charger et redimensionner l'image
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
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
            updateInventoryPanel(inventoryPanel); // Utilise directement le panneau d'inventaire
        });
        itemPanel.add(buyButton);
    
        // Ajouter un peu d'espacement autour de l'item
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(itemPanel);
    }
    

    private void updateInventoryPanel(JPanel panel) {
        panel.removeAll();  // Supprime tous les composants actuels
        List<Ressource> inventory = player.getInv().getRessources();
    
        for (Ressource resource : inventory) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
            itemPanel.setBackground(Color.DARK_GRAY);
    
            // Image de la ressource
            ImageIcon scaledIcon = new ImageIcon(new ImageIcon("assets/" + resource.getName() + ".png")
                    .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(scaledIcon);
            itemPanel.add(imageLabel);
    
            // Nom et quantité
            JLabel nameLabel = new JLabel(resource.getName() + " x" + resource.getQuantity());
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            itemPanel.add(nameLabel);
    
            // Bouton pour vendre (prix)
            int sellPrice = resource.getPrix();
            JButton sellButton = new JButton(sellPrice + " coins");
            sellButton.addActionListener(e -> {
                if (resource.getQuantity() > 0) {
                    resource.removeQuantity(1);
                    player.setMoney(player.getMoney() + sellPrice);
                    updateMoneyLabel();
                    updateInventoryPanel(panel);
                }
            });
            itemPanel.add(sellButton);
    
            // Ajouter l'item avec une bordure pour espacement
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panel.add(itemPanel);
        }
        panel.revalidate();
        panel.repaint();
    }
    

    private void updateMoneyLabel() {
        moneyLabel.setText("Argent: " + player.getMoney() + " coins");
    }



}
