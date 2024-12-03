package Vue;

import java.awt.*;
import javax.swing.*;
import Modele.Player;
import Modele.Machine.Usine;
import Modele.Ressource.*;
import Modele.Inventory;

public class UsinePanel extends JPanel {

    private Usine usine;
    private Player player;
    private JLabel moneyLabel;
    private JPanel recettePanel;
    private JPanel inventairePanel;
    private JPanel stockPanel;
    private JComboBox<String> recetteComboBox;

    public UsinePanel(Usine usine, Player player) {
        this.usine = usine;
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

        // Panneau pour les recettes
        JPanel recetteSelectionPanel = new JPanel();
        recetteSelectionPanel.setLayout(new FlowLayout());
        recetteSelectionPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Choisir une recette", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        // Combobox pour sélectionner la recette
        recetteComboBox = new JComboBox<>(new String[] {"Pickaxe", "Bloc de Fer"});
        recetteComboBox.setBackground(Color.LIGHT_GRAY);
        recetteComboBox.setForeground(Color.BLACK);
        recetteComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recetteSelectionPanel.add(recetteComboBox);

        // Bouton pour produire la ressource
        JButton produceButton = new JButton("Produire");
        produceButton.setBackground(Color.GREEN);
        produceButton.setForeground(Color.BLACK);
        produceButton.addActionListener(e -> {
            String selectedRecette = (String) recetteComboBox.getSelectedItem();
            usine.produire(selectedRecette);
            updateInventairePanel(inventairePanel);  // Met à jour l'inventaire du joueur après la production
        });
        recetteSelectionPanel.add(produceButton);

        this.add(recetteSelectionPanel, BorderLayout.CENTER);

        // Panneau pour l'inventaire de l'usine (ressources produites)
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Stock de l'usine", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        updateStockPanel();

        JScrollPane stockScrollPane = new JScrollPane(stockPanel);
        stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        stockScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(stockScrollPane, BorderLayout.EAST);

        // Panneau pour l'inventaire du joueur
        inventairePanel = new JPanel();
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Inventaire du joueur", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        updateInventairePanel(inventairePanel);

        JScrollPane inventaireScrollPane = new JScrollPane(inventairePanel);
        inventaireScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inventaireScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inventaireScrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(inventaireScrollPane, BorderLayout.WEST);

        // Panneau pour le bouton "Fermer"
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Marge supérieure de 20 pixels

        // Bouton Fermer
        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            this.setVisible(false); // Fermer l'interface de l'usine
        });

        // Ajouter le bouton au panneau
        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    private void updateStockPanel() {
        stockPanel.removeAll();
        for (Ressource r : usine.getStock()) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new FlowLayout());
            itemPanel.setBackground(Color.DARK_GRAY);

            JLabel itemLabel = new JLabel(r.getName() + " x" + r.getQuantity());
            itemLabel.setForeground(Color.WHITE);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemPanel.add(itemLabel);

            stockPanel.add(itemPanel);
        }
        stockPanel.revalidate();
        stockPanel.repaint();
    }

    private void updateInventairePanel(JPanel panel) {
        panel.removeAll();
        for (Ressource r : player.getInv().getRessources()) {
            if (r.getQuantity() > 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout());
                itemPanel.setBackground(Color.DARK_GRAY);

                JLabel itemLabel = new JLabel(r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.WHITE);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                itemPanel.add(itemLabel);

                JButton takeButton = new JButton("Prendre");
                takeButton.setBackground(Color.BLUE);
                takeButton.setForeground(Color.WHITE);
                takeButton.addActionListener(e -> {
                    usine.recuperer();
                    updateStockPanel();
                    updateInventairePanel(inventairePanel);
                });
                itemPanel.add(takeButton);

                panel.add(itemPanel);
            }
        }
        panel.revalidate();
        panel.repaint();
    }
}
