import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Affichage extends JFrame implements VueGenerale, KeyListener, MouseListener {
    private Game game;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private MenuButtonTest menuPanelV2;
    private final int height;
    private final int width;

    private boolean inventaireAfficher = false;
    private boolean menuBoutonClique = true;

    private Controle listener;

    private Timer movementTimer; // Timer pour les mouvements continus
    private ArrayList<Integer> activeKeys = new ArrayList<>(); // Liste des touches actuellement pressées
    private long lastMovementTime = 0;
    private static final long MIN_MOVEMENT_DELAY = 100;  // 100 ms minimum entre les mouvements

    private MarketPanel marketPanel;
    

    

    public Affichage(Game game) {
        this.game = game;
        width = game.getWidth() * 60;
        height = game.getHeight() * 50;

        this.setTitle("Jeu de Gestion de Ressources");

        setPreferredSize(new Dimension(width, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        cartePanel = new CartePanel(game);
        inventairePanel = new InventairePanel(game);
        menuPanelV2 = new MenuButtonTest(width/2, height/2);

        mainPanel.add(menuPanelV2, BorderLayout.NORTH);
        mainPanel.add(cartePanel, BorderLayout.CENTER);
        mainPanel.add(inventairePanel, BorderLayout.EAST);
        mainPanel.add(menuPanelV2, BorderLayout.SOUTH);

        //marketPanel = new MarketPanel(game.getMarket(), game.getPlayer());
        //marketPanel.setVisible(false);  // Ne pas afficher le MarketPanel initialement


        
        inventairePanel.setVisible(false);
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        menuPanelV2.addMouseListener(this);
        this.addMouseListener(this);

        // Ajoute les panneaux au JLayeredPane
        //mainPanel.add(cartePanel, new Integer(1));  // Panneau d'accueil au fond
        //mainPanel.add(marketPanel, new Integer(2));   // MarketPanel par-dessus le welcomePanel

        movementTimer = new Timer(50, e -> {
            if (listener != null) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMovementTime >= MIN_MOVEMENT_DELAY) {
                    for (int key : activeKeys) {  // Traite les touches actives
                        listener.onKeyPress(key);
                    }
                    lastMovementTime = currentTime;  // Met à jour le temps du dernier mouvement
                }
            }
        });
        
    }

    public void afficherCarte() {
        cartePanel.repaint();
        cartePanel.setVisible(true);
        inventairePanel.setVisible(false);
    }

    public void afficherInventaire() {
        inventairePanel.afficherInventaire();
        inventairePanel.setVisible(true);
        cartePanel.setVisible(false);
    }


    public void keyPressed(int key) {


        if (!activeKeys.contains(key)) { // Ajoute la touche si elle n'est pas déjà dans la liste
            activeKeys.add(key);
        }

        if (!movementTimer.isRunning()) { // Démarre le Timer si ce n'est pas déjà fait
            movementTimer.start();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            //marketPanel.setVisible(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (inventaireAfficher) {
                afficherCarte();
                inventaireAfficher = false;
            } else {
                afficherInventaire();
                inventaireAfficher = true;
            }
        }
        if (listener != null && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
            keyPressed(e.getKeyCode());
        } else {
            menuPanelV2.setVisible(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        activeKeys.remove((Integer) key); // Supprime la touche de la liste

        if (activeKeys.isEmpty()) { // Stoppe le Timer si plus aucune touche n'est active
            movementTimer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void setEcoute(Controle listener) {
        this.listener = listener;
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    @Override
    public void afficherMenu() {
        menuPanel.setVisible(true);
    }

    @Override
    public void afficherEtatJeu(String etat) {
        JOptionPane.showMessageDialog(this, etat);
    }

    @Override
    public void transmettreAction(String action) {
        switch (action) {
            case "Sauvegarder et Quitter":
                game.sauvegarderCarte("Sauvegarde_1");
                System.exit(0);
                break;
            case "Nouvelle Partie":
                menuPanelV2.setVisible(false);
                inventaireAfficher = false;
                game.start(); // Initialise le jeu
                setEcoute(game);
                cartePanel.repaint();
                break;
            case "Charger une Partie":
                Game nouvellePartie = Game.chargerCarte("Sauvegarde_1");
                
                if (nouvellePartie.getMap() != null) {
                    game = nouvellePartie;
                    //cartePanel.setGame(nouvellePartie);
                    inventairePanel.setGame(nouvellePartie);
                    //cartePanel.actualiseAffichage(100);
                    setEcoute(game);
                    afficherCarte();
                    
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // Vérifiez si le clic est sur le bouton du MenuButtonTest
        if (menuPanelV2.isMouseOver(e.getPoint()) && menuBoutonClique) {
            transmettreAction("Nouvelle Partie");
            menuBoutonClique = false;
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
}