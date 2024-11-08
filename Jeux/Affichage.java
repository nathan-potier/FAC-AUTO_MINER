import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;      // Import de KeyEvent
import java.awt.event.KeyListener;   // Import de KeyListener



public class Affichage extends JFrame implements VueGenerale, KeyListener  {
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
    
    private KeyPressListener listener;

    public Affichage(Game g) {

        game = g;
        
      
        

       
        
        this.setTitle("Main Menu");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        mainPanel = new JPanel(new BorderLayout());
        menuPanel = new JPanel();
        continueButton = new JButton("Poursuivre la Partie");
        newGameButton = new JButton("Nouvelle Partie");
        settingsButton = new JButton("Réglages");
        quitButton = new JButton("Quitter");
        
        continueButton.setFocusable(false);
        newGameButton.setFocusable(false);
        settingsButton.setFocusable(false);
        quitButton.setFocusable(false);

        // Ajout des écouteurs d'événements pour les boutons
        continueButton.addActionListener(e -> transmettreAction("Poursuivre"));
        newGameButton.addActionListener(e -> transmettreAction("Nouvelle Partie"));
        settingsButton.addActionListener(e -> transmettreAction("Réglages"));
        quitButton.addActionListener(e -> transmettreAction("Quitter"));

        menuPanel.add(continueButton);
        menuPanel.add(newGameButton);
        menuPanel.add(settingsButton);
        menuPanel.add(quitButton);

        mainPanel.add(menuPanel, BorderLayout.NORTH);

        // Création des panneaux pour le plateau de jeu et l'inventaire
        gamePanel = new JPanel(new GridLayout(game.getHeight(), game.getWidth()));
        inventoryPanel = new JPanel(new GridLayout(3, 3));  
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        centerPanel.add(gamePanel); // Ajoute gamePanel au centre
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Ajoute le panel centré dans le mainPanel
        mainPanel.add(inventoryPanel, BorderLayout.EAST);

        this.add(mainPanel);
        this.setVisible(true);
    }


   // Méthode pour afficher la carte
   public void afficherCarte() {
    
    gamePanel.removeAll();  // Supprime les anciens éléments
        for (Cell[] row : game.getMap().getGrid()) {
            for (Cell cell : row) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                if (cell != null && cell.getContent() != null) {
                    
                    JLabel contentLabel = new JLabel(cell.getContent().getClass().getSimpleName().substring(0, 1));
                    cellPanel.add(contentLabel);
                }
                
                gamePanel.add(cellPanel);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    // Méthode pour afficher l'inventaire
    public void afficherInventaire() {
        inventoryPanel.removeAll();  // Supprime les anciens éléments de l'inventaire
        
        String [] resInvName = game.getPlayer().getInv().getResInvName();
        int [] resInvQuant = game.getPlayer().getInv().getResInvQuant();

        for (int i = 0; i < resInvName.length ; i ++) {  
            JLabel itemLabel = new JLabel(resInvName[i] +" " + resInvQuant[i]);
            inventoryPanel.add(itemLabel);
        }
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }


    public void setEcoute(KeyPressListener listener){
        this.listener = listener;
        this.addKeyListener(this);    // Ajoute le KeyListener au JFrame
        this.setFocusable(true);      // Rend le frame focusable pour recevoir les événements clavier
    

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
                menuPanel.remove(newGameButton);  // Retirer le bouton
                menuPanel.revalidate();        // Revalider le layout du panel
                menuPanel.repaint();
                game.start();
                setEcoute(game);
                afficherCarte();
            

            default:
                break;
        }
        System.out.println("Action transmise : " + action);

        
        
       
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(inventaireAfficher){
                // Masque l'inventaire et réaffiche la grille
                inventoryPanel.setVisible(false);
                gamePanel.setVisible(true);
                afficherCarte();
                inventaireAfficher = false;
            }else{
                // Masque la grille et réaffiche l'inventaire
                inventaireAfficher = true;
                gamePanel.setVisible(false);
                inventoryPanel.setVisible(true);
                afficherInventaire();  
            }
            
        }
        // Notifie la classe principale de la touche pressée
        if (listener != null) {
            listener.onKeyPress(e.getKeyCode());

        }

        afficherCarte();
        
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
