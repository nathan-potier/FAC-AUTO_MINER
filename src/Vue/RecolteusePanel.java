package Vue;

import javax.swing.*;
import java.awt.*;

import Modele.GameStates;
import Modele.Machine.Recolteuse;
import Modele.Resource.*;

public class RecolteusePanel extends JPanel implements UpdateInterface {

    private Recolteuse recolteuse;
    private JPanel stockPanel;
    private JComboBox<String> ressourceComboBox;
    private JButton startButton;
    private JButton stopButton;
    private JLabel statusLabel;
    private JLabel nomTypeRecolteuse;
    private String typeResource;
    private JButton takeButton;

    public RecolteusePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        this.setBounds(50, 50, 700, 500);

        initializeTitrePanel();
        initializeMiddlePanel();
        initializeCloseButton();

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

    private void initializeMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setPreferredSize(new Dimension(100, 500));
        middlePanel.setBackground(Color.DARK_GRAY);

        // Retirer le inventairePanel et ne garder que le middlePanel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, middlePanel, null);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.2);
        this.add(splitPane, BorderLayout.CENTER);

        // Ajouter les panels sélection de ressource et stock
        addRessourceSelectionPanel(middlePanel);
        addStockPanel(middlePanel);
    }

    private void addRessourceSelectionPanel(JPanel parent) {
        JPanel ressourceSelectionPanel = new JPanel(new FlowLayout());
        ressourceSelectionPanel.setBackground(Color.DARK_GRAY);
        ressourceSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(221, 242, 172)),
                "Choisir une ressource", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
        ressourceSelectionPanel.setMinimumSize(new Dimension(300, 100));

        ressourceComboBox = new JComboBox<>();
        ressourceComboBox.setBackground(Color.LIGHT_GRAY);
        ressourceComboBox.setForeground(Color.BLACK);
        ressourceComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        ressourceSelectionPanel.add(ressourceComboBox);

        startButton = new JButton("Commencer la récolte");
        startButton.setBackground(new Color(221, 242, 172));
        startButton.setForeground(Color.BLACK);
        startButton.addActionListener(e -> {
            typeResource = (String) ressourceComboBox.getSelectedItem();
            startHarvesting();

        });
        ressourceSelectionPanel.add(startButton);

        stopButton = new JButton("Arrêter la récolte");
        stopButton.setBackground(new Color(221, 242, 172));
        stopButton.setForeground(Color.BLACK);
        stopButton.addActionListener(e -> stopHarvesting());
        ressourceSelectionPanel.add(stopButton);

        statusLabel = new JLabel("Status: Inactif");
        statusLabel.setForeground(Color.white);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ressourceSelectionPanel.add(statusLabel);

        parent.add(ressourceSelectionPanel);
    }

    private void addStockPanel(JPanel parent) {
        stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBackground(Color.DARK_GRAY);
        stockPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(221, 242, 172)),
                "Stock de Récolte", 0, 0, new Font("Arial", Font.BOLD, 14), Color.white
        ));
        stockPanel.setPreferredSize(new Dimension(300, 150));

        JScrollPane scrollPane = new JScrollPane(stockPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        parent.add(scrollPane, BorderLayout.CENTER);
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



    public void setRecolteuse(Recolteuse recolteuse) {
        this.recolteuse = recolteuse;
        if (recolteuse.getOption().equals("Polyvalente") || recolteuse.getNomRecetteEnCours() != null){
            ressourceComboBox.setEnabled(true);
        }
        nomTypeRecolteuse.setText("Recolteuse : " + recolteuse.getOption());
        recolteuse.setInterfaceUpdate(this);
        ressourceComboBox.removeAllItems();
        for (Resource ressource : recolteuse.getRessourceCollectable()) {
            ressourceComboBox.addItem(ressource.getName());
        }
        updateStockPanel();
        
    }

    private void startHarvesting() {
        if (recolteuse != null) {
   
            recolteuse.setTypeRessource(typeResource);
            recolteuse.startCollecting();
            statusLabel.setText("Status: Collecte en cours");
            if (!recolteuse.getOption().equals("Polyvalente")){
                ressourceComboBox.setEnabled(false);
            }else{
                ressourceComboBox.setEnabled(true);
            }
            
        }
    }

    private void stopHarvesting() {
        if (recolteuse != null) {
            recolteuse.stopWorking();
            statusLabel.setText("Status: Inactif");
        }
    }

    private void updateStockPanel() {
        stockPanel.removeAll();

        if (recolteuse == null || recolteuse.getStock().getResources().isEmpty()) {
            JLabel emptyLabel = new JLabel("Le stock est vide.");
            emptyLabel.setForeground(Color.white);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            stockPanel.add(emptyLabel);
        } else {
            for (Resource r : recolteuse.getStock().getResources()) {
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

            JPanel takeButtonPanel = new JPanel();
            takeButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            takeButtonPanel.setBackground(Color.DARK_GRAY);

            takeButton = new JButton("Prendre");
            takeButton.setBackground(new Color(221, 242, 172));
            takeButton.setForeground(Color.BLACK);
            takeButton.setFocusPainted(false);
            takeButton.addActionListener(e -> {
                
                recolteuse.recuperer();
                updateStockPanel();
                });

            takeButtonPanel.add(takeButton);

            stockPanel.add(Box.createHorizontalGlue());
            stockPanel.add(takeButtonPanel);
        }

        stockPanel.revalidate();
        stockPanel.repaint();
    }

    @Override
    public void notificationUpdate() {
        updateStockPanel();
    }
}
