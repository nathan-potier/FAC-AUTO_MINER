import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarketPanel extends JPanel {

    private Market market;
    private Player player;  // Assurez-vous que le joueur est passé au panel
    private JLabel moneyLabel; // Label pour afficher l'argent du joueur
    private int woodQuantity = 0;  // Quantité initiale de bois (modifiable)
    private int ironQuantity = 0;  // Quantité initiale de fer (modifiable)

    // Panneau d'achat, qui sera affiché quand on clique sur "Acheter"
    private JPanel buyPanel;

    public MarketPanel(Affichage affichage, Market market, Player player) {
        this.setLayout(null);
        this.setBackground(Color.GRAY);
        this.market = market;
        this.player = player;

        // Affichage de l'argent du joueur en haut à gauche
        moneyLabel = new JLabel("Argent: " + player.getMoney() + " coins");
        moneyLabel.setBounds(10, 10, 200, 30);
        moneyLabel.setForeground(Color.WHITE);
        this.add(moneyLabel);

        // Créer le bouton "Acheter" à gauche
        Bouton2 buyButton = new Bouton2("assets/Acheter.png", 50, 200, 150, 100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBuyOptions();  // Afficher les options d'achat
            }
        });

        // Créer le bouton "Vendre" à droite
        Bouton2 sellButton = new Bouton2("assets/Vendre.png", 450, 200, 150, 100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Vendre des ressources (logique non implémentée pour l'instant)
                JOptionPane.showMessageDialog(MarketPanel.this, "Fonction de vente non implémentée.");
            }
        });

        // Ajouter les boutons à la fenêtre (panel)
        this.add(buyButton);
        this.add(sellButton);

        // Initialiser le panneau d'achat, mais ne pas l'afficher encore
        buyPanel = new JPanel();
        buyPanel.setBounds(50, 350, 550, 250);  // Positionner le panneau
        buyPanel.setBackground(Color.gray);
        buyPanel.setLayout(new GridBagLayout());  // Utiliser GridBagLayout pour plus de contrôle

        // Créer un objet GridBagConstraints pour la gestion de l'alignement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Ajouter des espacements entre les composants
        gbc.gridx = 0; // Positionnement horizontal des composants
        gbc.gridy = 0; // Positionnement vertical des composants

        // Bois
        buyPanel.add(new JLabel("Bois:"), gbc);

        // Bouton - Bois
        JLabel woodQuantityLabel = new JLabel(String.valueOf(woodQuantity));

        gbc.gridx = 1;
        JButton woodMinusButton = new JButton("-");
        woodMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (woodQuantity > 0) {
                    woodQuantity--;
                    woodQuantityLabel.setText(String.valueOf(woodQuantity));  // Mise à jour de la quantité
                }
            }
        });
        buyPanel.add(woodMinusButton, gbc);

        // Label de la quantité de bois
        gbc.gridx = 2;
        buyPanel.add(woodQuantityLabel, gbc);

        // Bouton + Bois
        gbc.gridx = 3;
        JButton woodPlusButton = new JButton("+");
        woodPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                woodQuantity++;
                woodQuantityLabel.setText(String.valueOf(woodQuantity));  // Mise à jour de la quantité
            }
        });
        buyPanel.add(woodPlusButton, gbc);

        // Bouton acheter Bois
        gbc.gridx = 4;
        JButton confirmWoodButton = new JButton("Acheter Bois");
        confirmWoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ressource wood = new Wood(woodQuantity);  // Créer la ressource bois avec la quantité choisie
                String message = market.buyResourceBis(player, wood);
                JOptionPane.showMessageDialog(MarketPanel.this, message);
                updateMoneyLabel();  // Mettre à jour l'affichage de l'argent
            }
        });
        buyPanel.add(confirmWoodButton, gbc);

        // Fer
        gbc.gridx = 0;
        gbc.gridy = 1;  // Passer à la ligne suivante
        buyPanel.add(new JLabel("Fer:"), gbc);

        // Bouton - Fer
        JLabel ironQuantityLabel = new JLabel(String.valueOf(ironQuantity));
        gbc.gridx = 1;
        JButton ironMinusButton = new JButton("-");
        ironMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ironQuantity > 0) {
                    ironQuantity--;
                    ironQuantityLabel.setText(String.valueOf(ironQuantity));  // Mise à jour de la quantité
                }
            }
        });
        buyPanel.add(ironMinusButton, gbc);

        // Label de la quantité de fer
        gbc.gridx = 2;
        buyPanel.add(ironQuantityLabel, gbc);

        // Bouton + Fer
        gbc.gridx = 3;
        JButton ironPlusButton = new JButton("+");
        ironPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ironQuantity++;
                ironQuantityLabel.setText(String.valueOf(ironQuantity));  // Mise à jour de la quantité
            }
        });
        buyPanel.add(ironPlusButton, gbc);

        // Bouton acheter Fer
        gbc.gridx = 4;
        JButton confirmIronButton = new JButton("Acheter Fer");
        confirmIronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ressource iron = new Iron(ironQuantity);  // Créer la ressource fer avec la quantité choisie
                String message = market.buyResource(player, iron);
                JOptionPane.showMessageDialog(MarketPanel.this, message);
                updateMoneyLabel();  // Mettre à jour l'affichage de l'argent
            }
        });
        buyPanel.add(confirmIronButton, gbc);

        this.add(buyPanel);  // Ajouter le panneau d'achat au panel principal
        buyPanel.setVisible(false);  // Masquer le panneau d'achat au départ
    }

    // Cette méthode est appelée quand on appuie sur "Acheter"
    private void showBuyOptions() {
        // Rendre visible le panneau d'achat
        buyPanel.setVisible(true);
    }

    private void updateMoneyLabel() {
        moneyLabel.setText("Argent: " + player.getMoney() + " coins");
    }
}
