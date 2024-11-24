import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;



public class Affichage extends JFrame implements VueGenerale, KeyListener, MouseListener {

    private Game game;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    
    private MenuButtonTest menuPanelV2;

    private boolean inventaireAfficher = false;
    private boolean menuBoutonClique = true;

    private Controle listener;

    public Affichage(Game game) {
        this.game = game;

        this.setTitle("Jeu de Gestion de Ressources");
        this.setSize(game.getWidth() * 50, game.getHeight() * 50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        cartePanel = new CartePanel(game);
        inventairePanel = new InventairePanel(game);
        //menuPanel = new MenuPanel(e -> transmettreAction(((JButton) e.getSource()).getText()));
        menuPanelV2 = new MenuButtonTest();

        mainPanel.add(cartePanel, BorderLayout.CENTER);
        mainPanel.add(inventairePanel, BorderLayout.EAST);
        mainPanel.add(menuPanelV2, BorderLayout.SOUTH);

        inventairePanel.setVisible(false);
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Ajout du MouseListener
        menuPanelV2.addMouseListener(this);
        this.addMouseListener(this); // Pour détecter le clic sur toute la fenêtre
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
            case "Quitter":
                System.exit(0);
                break;
            case "Nouvelle Partie":
                menuPanelV2.setVisible(false);
                inventaireAfficher = false;
                game.start();
                cartePanel.actualiseAffichage(100);
                setEcoute(game);    
                afficherCarte();
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