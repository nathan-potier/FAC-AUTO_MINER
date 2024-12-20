package Vue;

import java.awt.*;
import javax.swing.*;
import Modele.GameStates;
import Modele.Player;
import Modele.Machine.Usine;
import Modele.Resource.*;
import java.util.List;

public class UsinePanel extends JPanel implements UpdateInterface {

    private Player player;
    private Usine usine;
    private JPanel inventairePanel;
    private JPanel stockPanel;
    private JPanel ressourcePanel;
    private JComboBox<String> recetteComboBox;
    private JPanel ingredientsPanel;
    private JLabel nomTypeUsine;
    private JButton produceButton;
    
    public UsinePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));

        initializeTitrePanel();
        initializeMiddlePanel();
        initializeInventairePanels();
        initializeCloseButton();
    }

    private void initializeTitrePanel() {
        JPanel titre = new JPanel(new BorderLayout());
        titre.setBackground(Color.DARK_GRAY);
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        nomTypeUsine = new JLabel("");
        nomTypeUsine.setForeground(Color.white);
        nomTypeUsine.setFont(new Font("Arial", Font.BOLD, 16));
        nomTypeUsine.setHorizontalAlignment(SwingConstants.CENTER);
        titre.add(nomTypeUsine, BorderLayout.CENTER);
        this.add(titre, BorderLayout.NORTH);
    }

    private void initializeMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setPreferredSize(new Dimension(100, 500));
        middlePanel.setMaximumSize(new Dimension(100, 500));
        middlePanel.setMinimumSize(new Dimension(100, 500));
        middlePanel.setBackground(Color.DARK_GRAY);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ressourcePanel, middlePanel);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.2);
        this.add(splitPane, BorderLayout.CENTER);

        addRecetteSelectionPanel(middlePanel);
        addStockPanel(middlePanel);
    }

    private void addRecetteSelectionPanel(JPanel parent) {
        JPanel recetteSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Change to LEFT flow for more flexibility
        recetteSelectionPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)), 
            "Choisir une recette", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
        recetteSelectionPanel.setMinimumSize(new Dimension(300, 100));
        
        // Create a JScrollPane with both horizontal and vertical scrollbars as needed
        JScrollPane scrollPane = new JScrollPane(recetteSelectionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(Color.DARK_GRAY);  // Fond sombre du viewport
        
        recetteComboBox = new JComboBox<>();
        recetteComboBox.setBackground(Color.LIGHT_GRAY);
        recetteComboBox.setForeground(Color.BLACK);
        recetteComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recetteComboBox.addActionListener(e -> updateIngredientsPanel());
        recetteSelectionPanel.add(recetteComboBox);
        
        JLabel arrowLabel = new JLabel(new ImageIcon(new ImageIcon("assets/ArrowRight.png").getImage().getScaledInstance(10, 30, Image.SCALE_SMOOTH)));
        arrowLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        recetteSelectionPanel.add(arrowLabel);
        
        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        ingredientsPanel.setBackground(Color.DARK_GRAY);
        recetteSelectionPanel.add(ingredientsPanel);
        
        produceButton = new JButton("Produire");
        produceButton.setBackground(new Color(221, 242, 172));
        produceButton.setFocusPainted(false);
        produceButton.setForeground(Color.BLACK);
        produceButton.addActionListener(e -> {
            String selectedRecette = (String) recetteComboBox.getSelectedItem();
            if (usine != null){
                usine.setRecette(selectedRecette);
            }
            if (usine != null && usine.getRecetteEnCours() != null && !usine.getIsModifiable())  {
                produceButton.setEnabled(false);
                recetteComboBox.setEnabled(false);
            }
        });
        recetteSelectionPanel.add(produceButton);
        
        parent.add(scrollPane); // Add the scrollPane to the parent panel
    }
    
    

    private void addStockPanel(JPanel parent) {
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.X_AXIS)); // Disposition horizontale
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)), 
            "Stock de Production", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
    
        // Suppression des dimensions fixes pour que le panneau s'adapte dynamiquement
        stockPanel.setPreferredSize(new Dimension(500, 150)); // Taille préférée, mais pas fixe
        stockPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Largeur infinie, hauteur fixe
    
        // Ajout d'un JScrollPane pour gérer le défilement horizontal et vertical
        JScrollPane scrollPane = new JScrollPane(stockPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Ajout du défilement horizontal
    
        parent.add(scrollPane, BorderLayout.CENTER);
    }
    
    
    private void initializeInventairePanels() {
        JPanel inventaire = createInventairePanel("Inventaire");
        JPanel Ressources = createInventairePanel("Ressources");

        JSplitPane splitInventairePanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inventaire, Ressources);
        splitInventairePanels.setDividerSize(0);
        this.add(splitInventairePanels, BorderLayout.WEST);
    }

    private JPanel createInventairePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)), 
            title, 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
        panel.setPreferredSize(new Dimension(175, 500));
        panel.setMaximumSize(new Dimension(175, 500));
        panel.setMinimumSize(new Dimension(175, 500));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        if (title.contains("Inventaire")) {
            inventairePanel = contentPanel;
        }
        if(title.contains("Ressources")){
            ressourcePanel = contentPanel;
        }

        return panel;
    }

    private void initializeCloseButton() {
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);

        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(221, 242, 172));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            this.setVisible(false);
            GameStates.setMoveOn(true);
        });

        closeButtonPanel.add(closeButton, BorderLayout.CENTER);
        this.add(closeButtonPanel, BorderLayout.SOUTH);
    }


    public void setPlayer(Player player) {
        this.player = player;
        updateInventairePanel();
        updateRessourcePanel();
    }

    public void setUsine(Usine usine) {
        this.usine = usine;
        usine.setInterfaceUpdate(this);
        nomTypeUsine.setText("Usine : " + usine.getOption());
        recetteComboBox.removeAllItems();
        for (String recette : usine.getTabRecettes()) {
            recetteComboBox.addItem(recette);
        }

        if (usine != null && usine.getRecetteEnCours() == null || usine.getIsModifiable() ) {
            if (usine.getRecetteEnCours() != null){
                recetteComboBox.setSelectedItem(usine.getNomRecetteEnCours());
            }
            produceButton.setEnabled(true);
            recetteComboBox.setEnabled(true);
        }else{
            recetteComboBox.setSelectedItem(usine.getNomRecetteEnCours());
            produceButton.setEnabled(false);
            recetteComboBox.setEnabled(false);
        }
        
        updateStockPanel();
        updateIngredientsPanel();
    }

    private void updateAll(){
        
        updateStockPanel();
        updateIngredientsPanel();
        updateInventairePanel();
        updateRessourcePanel();
    }

    private void updateStockPanel() {
        stockPanel.removeAll();
    
        if (usine == null || usine.getStock().getResources().isEmpty()) {
            JLabel emptyLabel = new JLabel("Le stock est vide.");
            emptyLabel.setForeground(Color.white);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        } else {
            // Panneau pour les ressources
            JPanel resourcesPanel = new JPanel();
            resourcesPanel.setLayout(new GridLayout(0, 3)); // 0 rows means it will adjust based on content
            resourcesPanel.setBackground(Color.DARK_GRAY);
    
            for (Resource r : usine.getStock().getResources()) {
                if (r.getQuantity() > 0) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    itemPanel.setBackground(Color.DARK_GRAY);
    
                    ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + r.getName() + ".png")
                            .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    JLabel itemLabel = new JLabel(icon);
                    itemLabel.setText(" " + r.getName() + " x" + r.getQuantity());
                    itemLabel.setForeground(Color.white);
                    itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    itemPanel.add(itemLabel);
    
                    resourcesPanel.add(itemPanel);
                }
            }
    
            // Panneau pour le bouton "Prendre"
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.DARK_GRAY);
    
            JButton takeResourceButton = new JButton("Recuperer");
            takeResourceButton.setBackground(new Color(221, 242, 172));
            takeResourceButton.setForeground(Color.BLACK);
            takeResourceButton.setFocusPainted(false);
            takeResourceButton.setPreferredSize(new Dimension(100, 30)); // Taille réduite du bouton
            takeResourceButton.addActionListener(e -> {
                usine.recuperer();
                updateAll();
            });
    
            buttonPanel.add(takeResourceButton);
    
            // Ajout des deux panneaux dans stockPanel
            stockPanel.setLayout(new BorderLayout());
            stockPanel.add(resourcesPanel, BorderLayout.CENTER);
            stockPanel.add(buttonPanel, BorderLayout.SOUTH);
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
                    
                    
                    if (usine.ajouterRessource(r)){
                        player.removeResource(r);
                    }
                    updateAll();});
                buttonPanel.add(ajouter);

                inventairePanel.add(buttonPanel);
            }
        }

        inventairePanel.revalidate();
        inventairePanel.repaint();
    }


    private void updateRessourcePanel() {
        ressourcePanel.removeAll();
        for (Resource r : usine.getStockResources().getResources()) {
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
    
                ressourcePanel.add(itemPanel);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(Color.DARK_GRAY);
    
                JButton enlever = new JButton("Enlever");
                enlever.setFont(new Font("Arial", Font.BOLD, 14));
                enlever.setBackground(new Color(221, 242, 172));
                enlever.setFocusable(false);
                enlever.setForeground(Color.BLACK);
                enlever.addActionListener(e -> {
                    usine.retirerRessource(r);
                    updateAll();
                });
    
                buttonPanel.add(enlever);
                ressourcePanel.add(buttonPanel);
            }
        }
    
        ressourcePanel.revalidate();
        ressourcePanel.repaint();
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

        if(!usine.getFonctionneBien() && !usine.isCooking()){
            produceButton.setEnabled(true);
        }

        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void addIngredientToPanel(String resourceName, int quantity) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.DARK_GRAY);
        int width;
        int height;
        if(Dimensions.BASE_CELL_SIZE == 50){
            width = 30;
            height = 30;
        }else{
            width = 50;
            height = 50;
        }
        ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + resourceName + ".png").getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    
        JLabel ingredientLabel = new JLabel(icon);
        ingredientLabel.setText(resourceName + " x" + quantity);
        ingredientLabel.setForeground(Color.white);
        ingredientLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        itemPanel.add(ingredientLabel);
    
        ingredientsPanel.add(itemPanel);
    }
    

    @Override
    public void notificationUpdate() {
        updateAll();
    }
}
