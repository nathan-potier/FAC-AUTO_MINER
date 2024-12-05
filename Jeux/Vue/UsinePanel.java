package Vue;

import java.awt.*;
import javax.swing.*;
import Modele.Player;
import Modele.Machine.Recette;
import Modele.Machine.Usine;
import Modele.Resource.*;
import java.util.List;

public class UsinePanel extends JPanel {

    private Player player;
    private Usine usine;
    private JLabel moneyLabel;
    private JPanel inventairePanel;
    private JPanel stockPanel;
    private JComboBox<String> recetteComboBox;
    private JButton takeButton;
    private JPanel ingredientsPanel;
    private boolean isProducing = false;
    private Timer productionTimer;

    public UsinePanel() {
        // Configuration de base du panel
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        this.setBounds(100, 50, 600, 400);

        // Affichage de l'argent du joueur
        JPanel moneyPanel = new JPanel(new BorderLayout());
        moneyPanel.setBackground(Color.DARK_GRAY);
        moneyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        moneyLabel = new JLabel("Argent: 0 coins");
        moneyLabel.setForeground(Color.white);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        moneyPanel.add(moneyLabel, BorderLayout.CENTER);
        this.add(moneyPanel, BorderLayout.NORTH);

        // Section gauche pour l'inventaire
        inventairePanel = new JPanel();
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)),
            "Inventaire", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
        updateInventairePanel();

        JScrollPane inventaireScrollPane = new JScrollPane(inventairePanel);
        inventaireScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inventaireScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inventaireScrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(inventaireScrollPane, BorderLayout.WEST);

        // Section centrale pour la recette et le stock
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBackground(Color.DARK_GRAY);

        // Section "Choisir une recette"
        JPanel recetteSelectionPanel = new JPanel(new FlowLayout());
        recetteSelectionPanel.setPreferredSize(new Dimension(200, 50));
        recetteSelectionPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)),
            "Choisir une recette", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));

        recetteComboBox = new JComboBox<>();
        recetteComboBox.setBackground(Color.LIGHT_GRAY);
        recetteComboBox.setForeground(Color.BLACK);
        recetteComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recetteComboBox.addActionListener(e -> updateIngredientsPanel());
        recetteSelectionPanel.add(recetteComboBox);

        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        ingredientsPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.add(ingredientsPanel);

        JButton produceButton = new JButton("Produire");
        produceButton.setBackground(new Color(221, 242, 172));
        produceButton.setFocusPainted(false);
        produceButton.setForeground(Color.BLACK);
        produceButton.addActionListener(e -> startProduction(produceButton));
        recetteSelectionPanel.add(produceButton);

        middlePanel.add(recetteSelectionPanel);

        // Section "Stock de l'usine"
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)),
            "Stock de Production", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));

        JScrollPane stockScrollPane = new JScrollPane(stockPanel);
        stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stockScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder());
        middlePanel.add(stockScrollPane);

        this.add(middlePanel, BorderLayout.CENTER);

        // Bouton "Fermer"
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(221, 242, 172));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> this.setVisible(false));

        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    public void setPlayer(Player player) {
        this.player = player;
        moneyLabel.setText("Argent: " + player.getMoney() + " coins");
        updateInventairePanel();
    }

    public void setUsine(Usine usine) {
        this.usine = usine;

        // Mettre à jour les recettes disponibles
        recetteComboBox.removeAllItems();
        for (String recette : usine.getTabRecettes()) {
            recetteComboBox.addItem(recette);
        }

        updateStockPanel();
        updateIngredientsPanel();
    }

    private void startProduction(JButton produceButton) {
        produceButton.setEnabled(false);

        Timer startProductionTimer = new Timer(5000, e -> {
            if (usine != null) {
                String selectedRecette = (String) recetteComboBox.getSelectedItem();
                usine.produire(selectedRecette);
                updateStockPanel();
                updateInventairePanel();
            }
            produceButton.setEnabled(true);
        });

        startProductionTimer.setRepeats(false);
        startProductionTimer.start();
    }

    private void updateStockPanel() {
        stockPanel.removeAll();

        if (usine == null || usine.getStock().isEmpty()) {
            JLabel emptyLabel = new JLabel("Le stock est vide.");
            emptyLabel.setForeground(Color.white);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        } else {
            for (Resource r : usine.getStock()) {
                if (r.getQuantity() > 0) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    itemPanel.setBackground(Color.DARK_GRAY);

                    JLabel itemLabel = new JLabel(r.getName() + " x" + r.getQuantity());
                    itemLabel.setForeground(Color.white);
                    itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    itemPanel.add(itemLabel);

                    stockPanel.add(itemPanel);
                }
            }
        }

        stockPanel.revalidate();
        stockPanel.repaint();
    }

    private void updateInventairePanel() {
        inventairePanel.removeAll();
        if (player != null) {
            for (Resource r : player.getInv().getResources()) {
                if (r.getQuantity() > 0) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    itemPanel.setBackground(Color.DARK_GRAY);

                    JLabel itemLabel = new JLabel(r.getName() + " x" + r.getQuantity());
                    itemLabel.setForeground(Color.white);
                    itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    itemPanel.add(itemLabel);

                    inventairePanel.add(itemPanel);
                }
            }
        }

        inventairePanel.revalidate();
        inventairePanel.repaint();
    }

    private void updateIngredientsPanel() {
        ingredientsPanel.removeAll();

        if (usine != null) {
            String selectedRecette = (String) recetteComboBox.getSelectedItem();
            List<Recette> recettes = Recette.initialiserRecettes();

            for (Recette recette : recettes) {
                if (recette.getNom().equals(selectedRecette)) {
                    for (Resource ingredient : recette.getIngredients()) {
                        addIngredientToPanel(ingredient.getName(), ingredient.getQuantity());
                    }
                }
            }
        }

        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void addIngredientToPanel(String resourceName, int quantity) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.DARK_GRAY);

        JLabel ingredientLabel = new JLabel(resourceName + " x" + quantity);
        ingredientLabel.setForeground(Color.white);
        ingredientLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemPanel.add(ingredientLabel);

        ingredientsPanel.add(itemPanel);
    }
}
