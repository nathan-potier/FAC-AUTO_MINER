package Vue;

import Modele.Machine.Recolteuse;
import Modele.GameStates;
import Modele.Player;
import Modele.Resource.Resource;
import javax.swing.*;
import java.awt.*;

public class RecolteusePanel extends JPanel implements UpdateInterface {

    private Player player;
    private Recolteuse recolteuse;
    private JPanel inventairePanel;  // Panneau pour le stock de la récolteuse
    private JPanel parametresPanel;  // Panneau pour paramétrer la récolteuse
    private JComboBox<String> ressourceComboBox;  // Menu de sélection de ressource
    private JButton startCollectingButton;  // Bouton pour commencer la récolte
    private JLabel nomTypeRecolteuse;
    private JButton closeButton;

    public RecolteusePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        this.setBounds(50, 50, 700, 500);

        initializeTitrePanel();
        initializePanels();
        initializeCloseButton();
        addParametresPanel();
    }

    private void initializeTitrePanel() {
        JPanel titre = new JPanel(new BorderLayout());
        titre.setBackground(Color.DARK_GRAY);
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        nomTypeRecolteuse = new JLabel("");
        nomTypeRecolteuse.setForeground(Color.white);
        nomTypeRecolteuse.setFont(new Font("Arial", Font.BOLD, 16));
        nomTypeRecolteuse.setHorizontalAlignment(SwingConstants.CENTER);
        titre.add(nomTypeRecolteuse, BorderLayout.CENTER);
        this.add(titre, BorderLayout.NORTH);
    }

    private void initializePanels() {
        // Panneau de gauche (Inventaire de la récolteuse)
        inventairePanel = new JPanel();
        inventairePanel.setLayout(new BoxLayout(inventairePanel, BoxLayout.Y_AXIS));
        inventairePanel.setBackground(Color.DARK_GRAY);
        inventairePanel.setPreferredSize(new Dimension(300, 400));

        // Panneau de droite (Paramétrage de la récolteuse)
        parametresPanel = new JPanel();
        parametresPanel.setLayout(new BoxLayout(parametresPanel, BoxLayout.Y_AXIS));
        parametresPanel.setBackground(Color.DARK_GRAY);
        parametresPanel.setPreferredSize(new Dimension(400, 400));

        // Ajouter les deux panneaux dans un layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inventairePanel, parametresPanel);
        splitPane.setDividerSize(10);
        splitPane.setResizeWeight(0.4); // 40% pour l'inventaire, 60% pour les paramètres
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void addInventairePanel() {
        if (recolteuse == null || recolteuse.getStock() == null) {
            return; // Si la récolteuse ou son stock est null, ne rien ajouter
        }

        JPanel inventairePanelInner = new JPanel();
        inventairePanelInner.setLayout(new BoxLayout(inventairePanelInner, BoxLayout.Y_AXIS));
        inventairePanelInner.setBackground(Color.DARK_GRAY);
        inventairePanelInner.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)),
            "Inventaire de la récolteuse", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));

        for (Resource r : recolteuse.getStock().getResources()) {
            if (r.getQuantity() > 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setBackground(Color.DARK_GRAY);

                JLabel itemLabel = new JLabel(r.getName() + " x" + r.getQuantity());
                itemLabel.setForeground(Color.white);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                itemPanel.add(itemLabel);

                JButton collectButton = new JButton("Récupérer");
                collectButton.setBackground(new Color(221, 242, 172));
                collectButton.setForeground(Color.BLACK);
                collectButton.setFont(new Font("Arial", Font.BOLD, 14));
                collectButton.addActionListener(e -> {
                    recolteuse.stopCollecting();
                    updateInventairePanel();
                });
                itemPanel.add(collectButton);

                inventairePanelInner.add(itemPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(inventairePanelInner);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inventairePanel.add(scrollPane);
    }

    private void addParametresPanel() {

        JPanel parametresPanelInner = new JPanel();
        parametresPanelInner.setLayout(new BoxLayout(parametresPanelInner, BoxLayout.Y_AXIS));
        parametresPanelInner.setBackground(Color.DARK_GRAY);
        parametresPanelInner.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(221, 242, 172)),
            "Paramétrer la récolteuse", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));

        // Initialiser la JComboBox vide avant de la remplir
        ressourceComboBox = new JComboBox<>();
        ressourceComboBox.setBackground(Color.LIGHT_GRAY);
        ressourceComboBox.setForeground(Color.BLACK);
        ressourceComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        parametresPanelInner.add(ressourceComboBox);

        startCollectingButton = new JButton("Commencer la récolte");
        startCollectingButton.setBackground(new Color(221, 242, 172));
        startCollectingButton.setForeground(Color.BLACK);
        startCollectingButton.setFont(new Font("Arial", Font.BOLD, 14));
        startCollectingButton.addActionListener(e -> {
            String selectedRessource = (String) ressourceComboBox.getSelectedItem();
            if (recolteuse != null) {
                recolteuse.setTypeRessource(selectedRessource);
                recolteuse.produire(); // Démarre la récolte
            }
        });
        parametresPanelInner.add(startCollectingButton);

    }

    private void initializeCloseButton() {
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.setBackground(Color.DARK_GRAY);

        closeButton = new JButton("Fermer");
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
    }

    public void setRecolteuse(Recolteuse recolteuse) {
        this.recolteuse = recolteuse;
        nomTypeRecolteuse.setText("Récolteuse : " + (recolteuse != null ? recolteuse.getOption() : ""));

        // Si recolteuse est non null, ajouter les ressources disponibles dans le menu
        if (recolteuse != null) {
            ressourceComboBox.removeAllItems(); // S'assure que la JComboBox est vide
            ressourceComboBox.addItem(recolteuse.getTypeRessource());
        }

        updateInventairePanel();
    }

    private void updateInventairePanel() {
        inventairePanel.removeAll();

        addInventairePanel(); // Rajoute l'inventaire de la récolteuse

        inventairePanel.revalidate();
        inventairePanel.repaint();
    }

    @Override
    public void notificationUpdate() {
        updateInventairePanel();
    }
}
