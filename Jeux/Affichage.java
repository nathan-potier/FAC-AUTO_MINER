import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Affichage extends JFrame implements VueGenerale, KeyListener {

    private Game game;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private JPanel mainPanel;
    private MenuPanel menuPanel;

    private boolean inventaireAfficher = false;
    private Controle listener;

    public Affichage(Game game) {
        this.game = game;

        this.setTitle("Jeu de Gestion de Ressources");
        this.setSize(game.getWidth() * 50, game.getHeight() * 50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        cartePanel = new CartePanel(game);
        inventairePanel = new InventairePanel(game);
        menuPanel = new MenuPanel(e -> transmettreAction(((JButton) e.getSource()).getText()));

        mainPanel.add(menuPanel, BorderLayout.NORTH);
        mainPanel.add(cartePanel, BorderLayout.CENTER);
        mainPanel.add(inventairePanel, BorderLayout.EAST);

        inventairePanel.setVisible(false);
        this.add(mainPanel);
        this.setVisible(true);
    }

    public void afficherCarte() {
        cartePanel.afficherCarte();
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
            menuPanel.setVisible(true);
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
                menuPanel.setVisible(false);
                inventaireAfficher = false;
                game.start();
                cartePanel.actualiseAffichage(100);
                setEcoute(game);
                afficherCarte();
                break;
            case "Charger une Partie":
                Game nouvellePartie = Game.chargerCarte("Sauvegarde_1");
                
                if (nouvellePartie.getMap() != null) {
                    game = nouvellePartie;
                    cartePanel.setGame(nouvellePartie);
                    inventairePanel.setGame(nouvellePartie);
                    cartePanel.actualiseAffichage(100);
                    setEcoute(game);
                    afficherCarte();
                    
                }
                break;
            default:
                break;
        }
    }
}
