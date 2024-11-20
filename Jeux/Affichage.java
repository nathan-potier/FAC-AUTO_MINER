import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Affichage extends JFrame implements VueGenerale, KeyListener {
    private JButton continueButton;
    private JButton newGameButton;
    private JButton settingsButton;
    private JButton quitButton;
    private Game game;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel inventoryPanel;
    private JPanel menuPanel;
    private boolean inventaireAfficher = false;
    private Controle listener;

    public Affichage(Game g) {
        game = g;

        // Dimensions de la fenêtre
        int sizeWidth = game.getWidth() * 50; // Ajuster selon la taille des cases
        int sizeHeight = game.getHeight() * 50;

        this.setTitle("Jeu de Gestion de Ressources");
        this.setSize(sizeWidth, sizeHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation des panels
        mainPanel = new JPanel(new BorderLayout());
        menuPanel = new JPanel();
        gamePanel = new JPanel(new GridLayout(game.getHeight(), game.getWidth()));
        inventoryPanel = new JPanel(new GridLayout(3, 3));

        // Initialisation des boutons
        continueButton = createStyledButton("Poursuivre la Partie");
        newGameButton = createStyledButton("Nouvelle Partie");
        settingsButton = createStyledButton("Réglages");
        quitButton = createStyledButton("Quitter");

        // Ajout des écouteurs d'événements pour les boutons
        continueButton.addActionListener(e -> transmettreAction("Poursuivre"));
        newGameButton.addActionListener(e -> transmettreAction("Nouvelle Partie"));
        settingsButton.addActionListener(e -> transmettreAction("Réglages"));
        quitButton.addActionListener(e -> transmettreAction("Quitter"));

        // Organisation du menu
        menuPanel.add(continueButton);
        menuPanel.add(newGameButton);
        menuPanel.add(settingsButton);
        menuPanel.add(quitButton);
        mainPanel.add(menuPanel, BorderLayout.NORTH);

        // Ajout du panneau de jeu et de l'inventaire
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(inventoryPanel, BorderLayout.EAST);

        this.add(mainPanel);
        this.setVisible(true);
    }

    // Création de boutons avec style personnalisé
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        return button;
    }

    // Afficher la carte avec des images
    public void afficherCarte() {
        gamePanel.removeAll(); // Supprimer les anciens éléments

        for (Cell[] row : game.getMap().getGrid()) {
            for (Cell cell : row) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellPanel.setPreferredSize(new Dimension(50, 50));

                if (cell != null && cell.getContent() != null) {
                    ImageIcon icon = getIconForType(cell.getContent().getClass().getSimpleName());
                    if (icon != null) {
                        JLabel contentLabel = new JLabel(icon);
                        contentLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); // Ajuste la taille
                        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        cellPanel.add(contentLabel);
                    }
                }

                gamePanel.add(cellPanel);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    public void actualiseAffichage(int temps) {
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherCarte();
                actualiseAffichage(10);

                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
        
    }

    // Afficher l'inventaire avec des images et du texte
    public void afficherInventaire() {
        inventoryPanel.removeAll(); // Nettoie les anciens slots
        inventoryPanel.setLayout(new GridLayout(3, 9, 5, 5)); // Grille 9x3 avec marges entre les slots

        String[] resInvName = game.getPlayer().getInv().getResInvName(); // Noms des objets
        int[] resInvQuant = game.getPlayer().getInv().getResInvQuant(); // Quantités des objets

        // Remplir la grille avec des slots
        for (int i = 0; i < resInvName.length; i++) {
            JPanel slot = new JPanel(new BorderLayout());
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Bordure grise pour chaque slot

            // Icône de l'objet
            JLabel iconLabel = new JLabel();
            ImageIcon icon = new ImageIcon("assets/" + resInvName[i] + ".png"); // Charge l'icône
            iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); // Ajuste la taille
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Quantité (superposé à l'icône)
            JLabel quantityLabel = new JLabel(String.valueOf(resInvQuant[i]));
            quantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            quantityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            quantityLabel.setForeground(Color.BLACK); // Texte blanc
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Superposition (icône + quantité)
            slot.setLayout(new OverlayLayout(slot));
            slot.add(quantityLabel);
            slot.add(iconLabel);

            inventoryPanel.add(slot); // Ajoute le slot à la grille
        }

        // Complète les cases vides avec des slots vides
        int totalSlots = 27; // 9x3 slots
        for (int i = resInvName.length; i < totalSlots; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            inventoryPanel.add(emptySlot);
        }

        // Met à jour l'affichage
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }


    // Obtenir une icône pour un type donné
    private ImageIcon getIconForType(String type) {
        switch (type) {
            case "Player":
                return new ImageIcon("assets/player.png");
            case "Wood":
                return new ImageIcon("assets/Wood.png");
            case "Iron":
                return new ImageIcon("assets/Iron.png");
            default:
                return null;
        }
    }

    public void setEcoute(Controle listener) {
        this.listener = listener;
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    @Override
    public void afficherMenu() {
        this.setVisible(true);
    }

    @Override
    public void afficherEtatJeu(String etat) {
        JOptionPane.showMessageDialog(this, etat);
    }

    @Override
    public void transmettreAction(String action) {
        switch (action) {
            case "Quitter":
                System.exit(0);
                break;
            case "Nouvelle Partie":
                menuPanel.setVisible(false);
                game.start();
                setEcoute(game);
                actualiseAffichage(100);
                afficherCarte();
                break;
            default:
                break;
        }
        System.out.println("Action transmise : " + action);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (inventaireAfficher) {
                inventoryPanel.setVisible(false);
                gamePanel.setVisible(true);
                afficherCarte();
                inventaireAfficher = false;
            } else {
                gamePanel.setVisible(false);
                inventoryPanel.setVisible(true);
                afficherInventaire();
                inventaireAfficher = true;
            }
        }

        if (listener != null) {
            listener.onKeyPress(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
