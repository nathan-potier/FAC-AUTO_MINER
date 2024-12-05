package Vue;

import java.awt.*;
import javax.swing.*;
import Modele.Player;
import Modele.Machine.Usine;
import Modele.Resource.*;

public class UsinePanel extends JPanel {

    private Usine usine;
    private Player player;
    private JLabel moneyLabel;
    private JPanel recettePanel;
    private JPanel inventairePanel;
    private JPanel stockPanel;
    private JComboBox<String> recetteComboBox;
    private JButton takeButton;
    private JPanel ingredientsPanel;
    private boolean isProducing = false;  // Indicateur pour vérifier si la production a commencé
    private Timer productionTimer;  // Timer pour la mise à jour périodique


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
        moneyLabel.setForeground(Color.white);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        moneyPanel.add(moneyLabel, BorderLayout.CENTER);
        this.add(moneyPanel, BorderLayout.NORTH);

        // Panneau pour la partie droite avec deux sections : "Choisir une recette" et "Stock de l'usine"
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.DARK_GRAY);

        // Section "Choisir une recette"
        JPanel recetteSelectionPanel = new JPanel();
        recetteSelectionPanel.setLayout(new FlowLayout());
        recetteSelectionPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Choisir une recette", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));

        // Combobox pour sélectionner la recette
        recetteComboBox = new JComboBox<>(new String[] {"Pickaxe", "Bloc de Fer"});
        recetteComboBox.setBackground(Color.LIGHT_GRAY);
        recetteComboBox.setForeground(Color.BLACK);
        recetteComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recetteComboBox.addActionListener(e -> updateIngredientsPanel());
        recetteSelectionPanel.add(recetteComboBox);

        
        ImageIcon arrowIcon = new ImageIcon(new ImageIcon("assets/ArrowRight.png").getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        JLabel arrowLabel = new JLabel(arrowIcon);
        recetteSelectionPanel.add(arrowLabel);

        // Panneau pour les ingrédients nécessaires
        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        ingredientsPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.add(ingredientsPanel);

        updateIngredientsPanel();

        JButton produceButton = new JButton("Produire");
        produceButton.setBackground(new Color(221, 242, 172));
        produceButton.setFocusPainted(false); // Désactiver le focus visuel
        produceButton.setForeground(Color.BLACK); // Texte en blanc
        produceButton.addActionListener(e -> {
            // Désactiver le bouton pendant que le délai s'écoule
            produceButton.setEnabled(false);

            // Démarrer un Timer qui attend 5 secondes avant de commencer la production
            Timer startProductionTimer = new Timer(5000, event -> {
                String selectedRecette = (String) recetteComboBox.getSelectedItem();
                usine.produire(selectedRecette);  // Démarre la production une fois les 5 secondes écoulées
                updateStockPanel();  // Mettre à jour le stock immédiatement après la production de la première ressource
                updateInventairePanel(inventairePanel);
            
                // Après le début de la production, commencer à mettre à jour périodiquement le stock
                if (!isProducing) {
                    isProducing = true;
            
                    // Démarrer un timer pour appeler updateStockPanel() et updateInventairePanel() toutes les 5 secondes
                    productionTimer = new Timer(5000, eventUpdate -> {
                        updateStockPanel();           // Mise à jour du stock
                        updateInventairePanel(inventairePanel);  // Mise à jour de l'inventaire
                    });
                    productionTimer.start();  // Démarre la mise à jour périodique des deux panels
                }
            });
            

            // Démarrer le timer
            startProductionTimer.setRepeats(false);  // Ne pas répéter après 5 secondes
            startProductionTimer.start();
        });
        recetteSelectionPanel.add(produceButton);


        // Ajouter la section "Choisir une recette" à la partie droite
        rightPanel.add(recetteSelectionPanel);

        // Section "Stock de l'usine"
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Stock de l'usine", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));

        // Ajout d'une bordure intérieure pour éviter que le bouton touche les bords
        stockPanel.setBorder(BorderFactory.createCompoundBorder(
            stockPanel.getBorder(), // Bordure existante
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Bordure intérieure de 10 pixels de chaque côté
        ));

        updateStockPanel();

        // Crée un panneau séparé pour le bouton
        JPanel takeButtonPanel = new JPanel();
        takeButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        takeButtonPanel.setBackground(Color.DARK_GRAY);

        // Assurez-vous de n'ajouter le bouton qu'une seule fois
        takeButton = new JButton("Prendre");
        takeButton.setBackground(new Color(221, 242, 172));
        takeButton.setForeground(Color.BLACK); // Texte en blanc
        takeButton.setFocusPainted(false); // Désactiver le focus visuel
        takeButton.addActionListener(e -> {
            usine.recuperer();
            updateStockPanel();
            updateInventairePanel(inventairePanel);
        });

        // Ajoute le bouton au panneau séparé
        takeButtonPanel.add(takeButton);

        // Ajoute le panneau du bouton sous le stock
        stockPanel.add(Box.createHorizontalGlue()); // Ajoute de l'espacement flexible
        stockPanel.add(takeButtonPanel);  // Ajoute le bouton au panel sans le supprimer

        // Ajoute un JScrollPane pour le stock avec le bouton
        JScrollPane stockScrollPane = new JScrollPane(stockPanel);
        stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stockScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(stockScrollPane);



        this.add(rightPanel, BorderLayout.CENTER);

        // Panneau pour l'inventaire du joueur à gauche
        inventairePanel = new JPanel();
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 242, 172)), "Inventaire", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white));
        updateInventairePanel(inventairePanel);

        JScrollPane inventaireScrollPane = new JScrollPane(inventairePanel);
        inventaireScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scrollbar uniquement si nécessaire
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
        closeButton.setBackground(new Color(221, 242, 172));
        closeButton.setForeground(Color.BLACK); // Texte en blanc
        closeButton.addActionListener(e -> {
            this.setVisible(false); // Fermer l'interface de l'usine
        });

        // Ajouter le bouton au panneau
        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    private void updateStockPanel() {
        stockPanel.removeAll();
    
        boolean hasStock = false; // Vérifie si l'usine a des ressources en stock
        for (Resource r : usine.getStock()) {
            if (r.getQuantity() > 0) {
                hasStock = true;
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                itemPanel.setBackground(Color.DARK_GRAY);
    
                // Ajouter une icône et le texte de la ressource
                ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel itemLabel = new JLabel(icon);
                itemLabel.setText(" " + r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.white);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                itemPanel.add(itemLabel);
    
                // Ajouter un espacement autour du panneau d'élément
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                stockPanel.add(itemPanel);
            }
        }
    
        if (!hasStock) {
            // Affiche un message si le stock est vide
            JLabel emptyLabel = new JLabel("Le stock est vide.");
            emptyLabel.setForeground(Color.white);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        }
    
        stockPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alignement à gauche pour tous les composants
        stockPanel.revalidate();
        stockPanel.repaint();
    
        // Ajouter le bouton "Prendre" après la mise à jour du stock
        if (takeButton != null && !isTakeButtonAdded()) {
            JPanel takeButtonPanel = new JPanel();
            takeButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            takeButtonPanel.setBackground(Color.DARK_GRAY);
    
            takeButtonPanel.add(takeButton);
            stockPanel.add(takeButtonPanel);  // Ajouter le bouton au panel sans le supprimer à chaque mise à jour
            stockPanel.revalidate();
            stockPanel.repaint();
        }
    }
    
    // Vérifie si le bouton "Prendre" a déjà été ajouté
    private boolean isTakeButtonAdded() {
        for (Component comp : stockPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JButton && subComp == takeButton) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    

    private void updateInventairePanel(JPanel panel) {
        panel.removeAll();
        for (Resource r : player.getInv().getResources()) {
            if (r.getQuantity() > 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS)); // Utiliser BoxLayout en horizontal pour mieux contrôler l'espacement
                itemPanel.setBackground(Color.DARK_GRAY);
    
                // Ajouter l'image de la ressource
                ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel itemLabel = new JLabel(icon);
                itemLabel.setText(r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.white);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                itemPanel.add(itemLabel);
    
                // Ajouter un petit espacement vertical entre chaque élément
                panel.add(itemPanel);
                panel.add(Box.createVerticalStrut(20)); // Réduit l'espacement vertical entre les éléments
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private void updateIngredientsPanel() {
        ingredientsPanel.removeAll();
    
        String selectedRecette = (String) recetteComboBox.getSelectedItem();
    
        // Définir les ingrédients nécessaires pour chaque recette
        if ("Pickaxe".equals(selectedRecette)) {
            addIngredientToPanel("Wood", 2);
            addIngredientToPanel("Iron", 3);
        } else if ("Bloc de Fer".equals(selectedRecette)) {
            addIngredientToPanel("Iron", 9);
        }
    
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void addIngredientToPanel(String resourceName, int quantity) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        // Ajouter l'image de l'ingrédient
        ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + resourceName + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel iconLabel = new JLabel(icon);
    
        // Ajouter le texte avec le nom et la quantité
        JLabel textLabel = new JLabel(resourceName + " x" + quantity);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    
        itemPanel.add(iconLabel);
        itemPanel.add(textLabel);
    
        ingredientsPanel.add(itemPanel);
    }

    public void stopProducing() {
        if (productionTimer != null) {
            productionTimer.stop();
            isProducing = false;
        }
    }
    
}
