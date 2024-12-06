package Vue;

import java.awt.*;
import javax.swing.*;

import Modele.GameStates;
import Modele.Player;
import Modele.Machine.Recette;
import Modele.Machine.Usine;
import Modele.Resource.*;
import java.util.List;

public class UsinePanel extends JPanel {

    private Player player;
    private Usine usine;
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
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));

        JPanel RessourcePanel = new JPanel();
        RessourcePanel.setLayout(new BoxLayout(RessourcePanel, BoxLayout.Y_AXIS));
        RessourcePanel.setBackground(Color.DARK_GRAY);
        RessourcePanel.setPreferredSize(new Dimension(200, getHeight()));
        RessourcePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Ressources", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));
        
        // Panneau central (composé de "Choisir une recette" et "Stock de l'usine")
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBackground(Color.DARK_GRAY);

        // Diviser la partie centrale en deux : "Test" à gauche avec une taille fixe, le reste à droite
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, RessourcePanel, middlePanel);
        splitPane.setDividerSize(0);  // Masquer le séparateur pour ne pas permettre de redimensionner
        this.add(splitPane, BorderLayout.CENTER);

        // Section "Choisir une recette"
        JPanel recetteSelectionPanel = new JPanel(new FlowLayout());
        recetteSelectionPanel.setPreferredSize(new Dimension(200, 50));
        recetteSelectionPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Choisir une recette", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));

        recetteComboBox = new JComboBox<>();
        recetteComboBox.setBackground(Color.LIGHT_GRAY);
        recetteComboBox.setForeground(Color.BLACK);
        recetteComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recetteComboBox.addActionListener(e -> updateIngredientsPanel());
        recetteSelectionPanel.add(recetteComboBox);

        ImageIcon arrowIcon = new ImageIcon(new ImageIcon("assets/ArrowRight.png").getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        JLabel arrowLabel = new JLabel(arrowIcon);
        arrowLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        recetteSelectionPanel.add(arrowLabel);

        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        ingredientsPanel.setBackground(Color.DARK_GRAY);
        ingredientsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
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
        stockPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Stock de Production", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));

        stockPanel.setBorder(BorderFactory.createCompoundBorder(
            stockPanel.getBorder(), // Bordure existante
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel takeButtonPanel = new JPanel();
        takeButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        takeButtonPanel.setBackground(Color.DARK_GRAY);

        takeButton = new JButton("Prendre");
        takeButton.setBackground(new Color(221, 242, 172));
        takeButton.setForeground(Color.BLACK);
        takeButton.setFocusPainted(false);
        takeButton.addActionListener(e -> {
            usine.recuperer();
            // IL FAUT UPDATE TOUT LES STOCKS
        });

        takeButtonPanel.add(takeButton);

        stockPanel.add(Box.createHorizontalGlue());
        stockPanel.add(takeButtonPanel);

        JScrollPane stockScrollPane = new JScrollPane(stockPanel);
        stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stockScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder());
        middlePanel.add(stockScrollPane);

        // Panneau pour l'inventaire du joueur à gauche
        inventairePanel = new JPanel();
        inventairePanel.setPreferredSize(new Dimension(200, getHeight()));
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Inventaire", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));

        JScrollPane inventaireScrollPane = new JScrollPane(inventairePanel);
        inventaireScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inventaireScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inventaireScrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(inventaireScrollPane, BorderLayout.WEST);

        // Bouton "Fermer"
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(221, 242, 172));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> {
                this.setVisible(false);
                GameStates.setMoveOn(true);});

        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    public void setPlayer(Player player) {
        this.player = player;
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

                updateAll();
            }
            produceButton.setEnabled(true);
        });

        startProductionTimer.setRepeats(false);
        startProductionTimer.start();
    }
    private void updateAll(){
        
        updateStockPanel();
        updateIngredientsPanel();
        updateInventairePanel();
    }

    private void updateStockPanel() {
        stockPanel.removeAll();

        if (usine == null || usine.getStock().getResources().isEmpty()) {
            JLabel emptyLabel = new JLabel("Le stock est vide.");
            emptyLabel.setForeground(Color.white);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        } else {
            for (Resource r : usine.getStock().getResources()) {
                if (r.getQuantity() > 0) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    itemPanel.setBackground(Color.DARK_GRAY);

                    ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                    JLabel itemLabel = new JLabel(icon);
                    itemLabel.setText(" " + r.getName() + " x" + r.getQuantity());
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
        for (Resource r : player.getInv().getResources()) {
            if (r.getQuantity() > 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setBackground(Color.DARK_GRAY);

                ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel itemLabel = new JLabel(icon);
                itemLabel.setText(r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.white);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                itemPanel.add(itemLabel);

                // Ajouter un petit espacement vertical entre chaque élément
                inventairePanel.add(itemPanel);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                // Créer un panneau intermédiaire pour centrer le bouton
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrage horizontal
                buttonPanel.setBackground(Color.DARK_GRAY);
                

                JButton ajouter = new JButton("Ajouter");
                ajouter.setFont(new Font("Arial", Font.BOLD, 14));
                ajouter.setBackground(new Color(221, 242, 172));
                ajouter.setFocusable(false);
                ajouter.setForeground(Color.BLACK);
                ajouter.addActionListener(e -> {
                    
                    player.removeResource(r);
                    usine.ajouterRessource(r);
                    updateAll();});
                buttonPanel.add(ajouter);

                inventairePanel.add(buttonPanel);
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

        ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + resourceName + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel ingredientLabel = new JLabel(icon );
        ingredientLabel.setText(resourceName + " x" + quantity);
        ingredientLabel.setForeground(Color.white);
        ingredientLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemPanel.add(ingredientLabel);

        ingredientsPanel.add(itemPanel);
    }
}
