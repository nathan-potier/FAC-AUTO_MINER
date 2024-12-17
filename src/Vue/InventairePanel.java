package Vue;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import Modele.Player;
import Modele.Machine.Machine;
import Modele.Resource.Recette;
import Modele.Resource.Resource;
import java.util.List;

public class InventairePanel extends JPanel implements UpdateInterface{

    private Player player;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel borderPanel;
    private List<Recette> recettes = Recette.initialiserRecettes();
    private boolean isProducing = false; // Indique si une production est en cours


    public InventairePanel(Player player) {
        this.player = player;
    
        // Définir la mise en page principale
        this.setLayout(new BorderLayout());
        this.setBorder(new LineBorder(Color.DARK_GRAY, 20));
        this.setBackground(Color.DARK_GRAY);
    
        // Panneau gauche (colonnes)
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 2, 5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10));
        leftPanel.setBackground(Color.DARK_GRAY);
    
        // Panneau droit avec texte "Recette"
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // Organiser les recettes en colonne
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
    
        // Envelopper le panneau des recettes dans un JScrollPane
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Pour éviter le défilement horizontal
        rightScrollPane.setBorder(null); // Pour éviter le bord autour du JScrollPane
    
        // Panneau de bordure de 150px à droite
        borderPanel = new JPanel();
        borderPanel.setPreferredSize(new Dimension(150, 0));
        borderPanel.setBackground(Color.DARK_GRAY);
    
        // Utiliser JSplitPane pour diviser l'espace
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(leftPanel), rightScrollPane);
        splitPane.setDividerLocation(375);
        splitPane.setDividerSize(0); 
        splitPane.setBorder(null);
    
        this.add(splitPane, BorderLayout.CENTER);
        this.add(borderPanel, BorderLayout.EAST);
    
        afficherRecettes(recettes);
    }
    

    public void setPlayer(Player p) {
        player = p;
    }

    public void afficherInventaire() {
        leftPanel.removeAll(); // Nettoie les anciens slots dans le panneau gauche

        // Affichage des ressources
        String[] resInvName = player.getInv().getResInvName();
        int[] resInvQuant = player.getInv().getResInvQuant();

        // Ajouter les ressources à gauche
        for (int i = 0; i < resInvName.length; i++) {
            JPanel slot = creerSlot(resInvName[i], resInvQuant[i]);
            leftPanel.add(slot);
        }

        // Affichage de la machine si elle existe
        Machine machine = player.getInv().getMachine();
        if (machine != null) {
            JPanel machineSlot = creerSlot(machine.getName() + " (" + machine.getOption() + ")", 1); // Ex. Nom de la machine
            leftPanel.add(machineSlot);
        }

        // Ajouter des slots vides si nécessaire
        int totalSlots = 10;
        for (int i = resInvName.length + (machine != null ? 1 : 0); i < totalSlots; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            emptySlot.setBackground(Color.DARK_GRAY);
            leftPanel.add(emptySlot);
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Méthode utilitaire pour créer un slot
    private JPanel creerSlot(String name, int quantity) {
        JPanel slot = new JPanel();
        slot.setLayout(new GridBagLayout());
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        slot.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon("assets/" + name + ".png");
        iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

        JLabel quantityLabel = new JLabel(String.valueOf(quantity));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quantityLabel.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nameLabel.setForeground(Color.WHITE);

        gbc.gridy = 0;
        slot.add(iconLabel, gbc);
        gbc.gridy = 1;
        slot.add(nameLabel, gbc);
        gbc.gridy = 2;
        slot.add(quantityLabel, gbc);

        return slot;
    }

    public void afficherRecettes(List<Recette> recettes) {
        rightPanel.removeAll(); // Nettoie les anciennes recettes
    
        // Ajouter chaque recette dans le panneau de droite
        for (Recette recette : recettes) {
            JPanel recettePanel = new JPanel();
            recettePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            recettePanel.setBackground(Color.DARK_GRAY);
    
            // Ajouter les ingrédients de la recette avec les images
            JPanel ingredientsPanel = new JPanel();
            ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
            ingredientsPanel.setBackground(Color.DARK_GRAY);
    
            for (Resource ingredient : recette.getIngredients()) {
                addIngredientToPanel(ingredientsPanel, ingredient.getClass().getSimpleName(), ingredient.getQuantity());
            }
    
            recettePanel.add(ingredientsPanel);
    
            // Ajouter la flèche après les ingrédients
            JLabel arrowLabel = new JLabel(new ImageIcon(new ImageIcon("assets/ArrowRight.png").getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH)));
            recettePanel.add(arrowLabel);
    
            // Ajouter l'ingrédient final sans la quantité
            Resource finalIngredient = recette.getProduitFinal();
            if (finalIngredient != null) {
                JPanel finalIngredientPanel = new JPanel();
                finalIngredientPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                finalIngredientPanel.setBackground(Color.DARK_GRAY);
    
                addIngredientToPanel(finalIngredientPanel, finalIngredient.getClass().getSimpleName(), -1); // Passer -1 pour ne pas afficher la quantité
                recettePanel.add(finalIngredientPanel);
            }
    
            // Ajouter le bouton de production pour chaque recette
            JButton produceButton = new JButton("Produire");
            produceButton.setBackground(new Color(221, 242, 172));
            produceButton.setForeground(Color.BLACK);
            produceButton.setFocusPainted(false);
    
            // Ajouter un ActionListener au bouton
            produceButton.addActionListener(e -> {
                if (!isProducing) {
                    if (recette.isTempo()) {
                        isProducing = true;
                        setAllProduceButtonsVisible(false); // Rendre tous les boutons invisibles
                    }
    
                    player.getInv().produire(recette);
                }
            });
    
            recettePanel.add(produceButton);
            rightPanel.add(recettePanel);
    
            // Ajouter une barre horizontale pour séparer les recettes
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Color.LIGHT_GRAY); // Couleur de la ligne
            separator.setPreferredSize(new Dimension(rightPanel.getWidth(), 1)); // Ajuster la taille
            rightPanel.add(separator);
        }
    
        // Mettre à jour la vue des recettes
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    
    
    private void addIngredientToPanel(JPanel ingredientsPanel, String resourceName, int quantity) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBackground(Color.DARK_GRAY);
    
        // Chargement de l'image de l'ingrédient
        ImageIcon icon = new ImageIcon(new ImageIcon("assets/" + resourceName + ".png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel ingredientLabel = new JLabel(icon);
        if(quantity == -1){
            ingredientLabel.setText(resourceName);
        }else{
            ingredientLabel.setText(resourceName + " x" + quantity);
        }
        ingredientLabel.setForeground(Color.white);
        ingredientLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemPanel.add(ingredientLabel);
    
        ingredientsPanel.add(itemPanel);
    }

    private void setAllProduceButtonsVisible(boolean visible) {
        for (Component comp : rightPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel recettePanel = (JPanel) comp;
                for (Component innerComp : recettePanel.getComponents()) {
                    if (innerComp instanceof JButton) {
                        innerComp.setEnabled(visible);
                    }
                }
            }
        }
    }


    @Override
    public void notificationUpdate() {
        setAllProduceButtonsVisible(true);
        isProducing = false;
        afficherInventaire();
    }

    
}
