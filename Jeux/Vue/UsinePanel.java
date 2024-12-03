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
    private JButton takeButton; // Déclarer un seul bouton pour "Prendre"

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

        // Panneau pour la partie droite avec deux sections : "Choisir une recette" et "Stock de l'usine"
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.DARK_GRAY);

        // Section "Choisir une recette"
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
        produceButton.setBackground(Color.GRAY);  // Bouton gris
        produceButton.setForeground(Color.WHITE); // Texte en blanc
        produceButton.addActionListener(e -> {
            String selectedRecette = (String) recetteComboBox.getSelectedItem();
            usine.produire(selectedRecette);
            stockPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alignement à gauche pour tous les composants
            updateInventairePanel(inventairePanel);  // Met à jour l'inventaire du joueur après la production
            updateStockPanel();
            stockPanel.add(Box.createVerticalGlue()); // Ajouter un espacement flexible avant le bouton pour le pousser en bas
            stockPanel.add(takeButton);  // Ajouter directement le bouton à la fin

        });
        recetteSelectionPanel.add(produceButton);

        // Ajouter la section "Choisir une recette" à la partie droite
        rightPanel.add(recetteSelectionPanel);

        // Section "Stock de l'usine"
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Stock de l'usine", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        // Ajout d'une bordure intérieure pour éviter que le bouton touche les bords
        stockPanel.setBorder(BorderFactory.createCompoundBorder(
            stockPanel.getBorder(), // Bordure existante
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Bordure intérieure de 10 pixels de chaque côté
        ));

        updateStockPanel();

        // Bouton "Prendre" en bas de stockPanel
        takeButton = new JButton("Prendre");
        takeButton.setBackground(Color.GRAY);  // Bouton gris
        takeButton.setForeground(Color.WHITE); // Texte en blanc
        takeButton.setFocusPainted(false); // Désactiver le focus visuel
        takeButton.addActionListener(e -> {
            usine.recuperer();
            updateStockPanel();
            updateInventairePanel(inventairePanel);
            stockPanel.add(Box.createVerticalGlue()); // Ajouter un espacement flexible avant le bouton pour le pousser en bas
            stockPanel.add(takeButton);  // Ajouter directement le bouton à la fin
        });

        // Ajouter directement le bouton au stockPanel sans centrer
        stockPanel.add(Box.createVerticalGlue()); // Ajouter un espacement flexible avant le bouton pour le pousser en bas
        stockPanel.add(takeButton);  // Ajouter directement le bouton à la fin

        // Scroll pane pour le stock de l'usine
        JScrollPane stockScrollPane = new JScrollPane(stockPanel);
        stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scrollbar uniquement si nécessaire
        stockScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder());

        rightPanel.add(stockScrollPane);



        this.add(rightPanel, BorderLayout.CENTER);

        // Panneau pour l'inventaire du joueur à gauche
        inventairePanel = new JPanel();
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Inventaire", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
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
        closeButton.setBackground(Color.GRAY); // Bouton gris
        closeButton.setForeground(Color.WHITE); // Texte en blanc
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
        for (Ressource r : usine.getStock()) {
            if (r.getQuantity() > 0) {
                hasStock = true;
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                itemPanel.setBackground(Color.DARK_GRAY);
        
                // Ajouter une icône et le texte de la ressource
                ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel itemLabel = new JLabel(icon);
                itemLabel.setText(" " + r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.WHITE);
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
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        }
        stockPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alignement à gauche pour tous les composants
        stockPanel.revalidate();
        stockPanel.repaint();
    }
    
    

    private void updateInventairePanel(JPanel panel) {
        panel.removeAll();
        for (Ressource r : player.getInv().getRessources()) {
            if (r.getQuantity() > 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS)); // Utiliser BoxLayout en horizontal pour mieux contrôler l'espacement
                itemPanel.setBackground(Color.DARK_GRAY);
    
                // Ajouter l'image de la ressource
                ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                JLabel itemLabel = new JLabel(icon);
                itemLabel.setText(r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.WHITE);  // Texte en blanc
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
}
