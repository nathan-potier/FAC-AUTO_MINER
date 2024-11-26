import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


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

    // Vitesse du jeu (délai entre deux actions)
    private final int actionDelay = 50;  // Délai entre deux déplacements (en millisecondes)
    private long lastActionTime = 0;  // Dernière fois qu'une action a été effectuée
    
    

    

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

        inventairePanel.setVisible(false);
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        menuPanelV2.addMouseListener(this);
        this.addMouseListener(this);


        
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



    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime = System.currentTimeMillis();
        
        // Si l'action a eu lieu récemment (avant l'écoulement de 20ms), on ignore la nouvelle action
        if (currentTime - lastActionTime < actionDelay) {
            return;
        }

        lastActionTime = currentTime;  // Met à jour l'heure de la dernière action

        
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
            listener.onKeyPress(e.getKeyCode());
            
        } else {
            menuPanelV2.setVisible(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}



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