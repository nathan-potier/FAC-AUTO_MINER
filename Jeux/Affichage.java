import javax.swing.*;
import java.awt.event.KeyEvent;      // Import de KeyEvent
import java.awt.event.KeyListener;   // Import de KeyListener

public class Affichage extends JFrame implements VueGenerale, KeyListener  {
    private JButton continueButton;
    private JButton newGameButton;
    private JButton settingsButton;
    private JButton quitButton;
    

    public Affichage() {
        this.setTitle("Main Menu");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);    // Ajoute le KeyListener au JFrame
        this.setFocusable(true);      // Rend le frame focusable pour recevoir les événements clavier

        JPanel panel = new JPanel();
        continueButton = new JButton("Poursuivre la Partie");
        newGameButton = new JButton("Nouvelle Partie");
        settingsButton = new JButton("Réglages");
        quitButton = new JButton("Quitter");

        // Ajout des écouteurs d'événements pour les boutons
        continueButton.addActionListener(e -> transmettreAction("Poursuivre"));
        newGameButton.addActionListener(e -> transmettreAction("Nouvelle Partie"));
        settingsButton.addActionListener(e -> transmettreAction("Réglages"));
        quitButton.addActionListener(e -> transmettreAction("Quitter"));

        panel.add(continueButton);
        panel.add(newGameButton);
        panel.add(settingsButton);
        panel.add(quitButton);

        this.add(panel);
        this.setVisible(true);
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
       
        System.out.println("Action transmise : " + action);
       
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // Vérifie quelle touche a été pressée
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                System.out.println("Flèche Haut");
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Flèche Bas");
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("Flèche Gauche");
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Flèche Droite");
                break;
            case KeyEvent.VK_ESCAPE:
                System.out.println("Touche Échap pressée");
                // Action pour Échap, par exemple quitter le jeu
                System.exit(0); // Quitte l'application
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Tu peux ajouter des actions ici si nécessaire
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Pas utilisé pour les flèches ou Échap
    }
}
