package Vue;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import Modele.Game;
import Modele.GameStates;
import Modele.Player;
import Modele.Position;
import Modele.Controle;
import Modele.Enregistrer.*;
import Modele.Machine.Usine;

public class Affichage extends JFrame implements VueGenerale, KeyListener, MouseListener, SettingsListener, SauvegardeListener{
    private Game game;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private MarketPanel marketPanel; // Panel supplémentaire
    private JPanel mainPanel;
    private JLayeredPane layeredPane; // Pour superposer les panneaux
    private MenuPanel menuPanel;
    private SauvegardePanel sauvegardePanel;
    private SettingsPanel settingsPanel;
    private UsinePanel usinePanel;

    private final int height;
    private final int width;

    private boolean inventaireAfficher = false;
    private boolean menuBoutonClique = true;
    private boolean isNewGame;
    private String nameGameLoad;

    private Controle listener;

    // Vitesse du jeu (délai entre deux actions)
    private final int actionDelay = 50;  // Délai entre deux déplacements (en millisecondes)
    private long lastActionTime = 0;  // Dernière fois qu'une action a été effectuée

    /**
     * permet de savoir si la game à été lancer ou non ( utile pour le retour au menu pour 
     * savoir si il est bon de construire la game ou juste d'afficher la game)
    */
    private boolean isRunning = false;

    

    public Affichage(Game game) {
        this.game = game;
        width = (game.getWidth()+6)*50;
        height = (game.getHeight()+2)*50;

        this.setTitle("Jeu de Gestion de Ressources");
        setPreferredSize(new Dimension(width, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation des composants
        mainPanel = new JPanel(new BorderLayout());
        layeredPane = new JLayeredPane();
        settingsPanel = new SettingsPanel();
        sauvegardePanel = new SauvegardePanel();

        settingsPanel.setSettingsListener(this);
        sauvegardePanel.setSauvegardeListener(this);

        cartePanel = new CartePanel(game.getWidth(), game.getHeight());
        game.addGameListener(cartePanel);

        inventairePanel = new InventairePanel(null);

        marketPanel = new MarketPanel(game.getMarket(), game.getPlayer());
        usinePanel = new UsinePanel();
        usinePanel.setBounds(100, 100, 800, 600);

        menuPanel = new MenuPanel(width / 2, height / 2, width, height);

        // Configuration des panneaux dans le layeredPane
        cartePanel.setBounds(0, 0, width, height);
        inventairePanel.setBounds(0, 0, width, height);
        settingsPanel.setBounds(0, 0, width, height);
        sauvegardePanel.setBounds(0, 0, width, height);


        // Ajout des panneaux au layeredPane
        layeredPane.add(cartePanel, Integer.valueOf(1));
        layeredPane.add(inventairePanel, Integer.valueOf(3));
        layeredPane.add(marketPanel, Integer.valueOf(2));
        layeredPane.add(usinePanel, Integer.valueOf(6));
        layeredPane.add(settingsPanel, Integer.valueOf(4));
        layeredPane.add(sauvegardePanel, Integer.valueOf(5));

        // Masquer les panneaux sauf la carte par défaut
        inventairePanel.setVisible(false);
        usinePanel.setVisible(false);
        marketPanel.setVisible(false);
        settingsPanel.setVisible(false);
        sauvegardePanel.setVisible(false);

        // Ajout des composants au mainPanel
        mainPanel.add(menuPanel, BorderLayout.SOUTH);
        mainPanel.add(layeredPane, BorderLayout.CENTER);

        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Listener pour les interactions
        menuPanel.addMouseListener(this);
        this.addMouseListener(this);
        this.setLayout(null);

        
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
        System.out.println("test");
        long currentTime = System.currentTimeMillis();
        
        // Si l'action a eu lieu récemment (avant l'écoulement de 20ms), on ignore la nouvelle action
        if (currentTime - lastActionTime < actionDelay) {
            return;
        }

        lastActionTime = currentTime;  // Met à jour l'heure de la dernière action

        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (inventaireAfficher) {
                afficherCarte();
                GameStates.setMoveOn(true);
                inventaireAfficher = false;
            } else {
                GameStates.setMoveOn(false);
                afficherInventaire();
                inventaireAfficher = true;

            }

        }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE ){
            GameStates.setMoveOn(false);
            saveAndBackMenu();
            menuBoutonClique = true;
            menuPanel.setVisible(true);
        }
        else if (listener != null && GameStates.isMoveOn()) {
            listener.onKeyPress(e.getKeyCode());
        }
    }
    public void saveAndBackMenu(){
        System.out.println("save");
        //game.sauvegarderCarte(game.getPlayer().getPseudo());
        String nomSv = "";
        if(isNewGame){
            nomSv = GameSaver.generateUniqueSaveName("Monde");
        }else{
            nomSv = nameGameLoad;
        }
        GameSaver.saveGame(nomSv, game.getPlayer(), game.getMarket(), game.getPositionObject(), game.getMachines());


    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public void setEcoute(Controle listener) {

        this.listener = listener;
        this.addKeyListener(this);
        this.setFocusable(true);
        requestFocusInWindow();
    }



    @Override
    public void transmettreAction(String action) {
        switch (action) {
            case "Sauvegarder et Quitter":
                game.sauvegarderCarte("Sauvegarde_1");
                System.exit(0);
                break;
            case "Nouvelle Partie":
                isNewGame = true;
                nouvellePartie();
                break;
            default:
                break;
        }
    }

    private void nouvellePartie(){
        menuPanel.setVisible(false);
        inventaireAfficher = false;
        game.create(); // Initialise le jeu
        inventairePanel.setPlayer(game.getPlayer());
        setEcoute(game);
        cartePanel.repaint();
        game.initVue(this);
        GameStates.setMoveOn(true);
        isRunning = true;
        game.getPlayer().setMarketInterface(marketPanel);
        marketPanel.setPlayer(game.getPlayer());
        marketPanel.setMarket(game.getMarket());
    }

    private void chargerPartie(String nameGameLoad){
        isNewGame = false;
        this.nameGameLoad = nameGameLoad;
        game = GameLoader.loadGame(nameGameLoad, game);
                
        if (game.getMap() != null) {
            game.addGameListener(cartePanel);
            menuPanel.setVisible(false);
            inventaireAfficher = false;
            game.start(); // Initialise le jeu
            setEcoute(game);
            cartePanel.repaint();
            game.initVue(this);
            GameStates.setMoveOn(true);
            isRunning = true;
            inventairePanel.setPlayer(game.getPlayer());
            game.getPlayer().setMarketInterface(marketPanel);
            marketPanel.setPlayer(game.getPlayer());
            marketPanel.setMarket(game.getMarket());


            
        }
    }

    public Position getCaseFromClick(MouseEvent e) {
        Point location = SwingUtilities.convertPoint(this, e.getPoint(), cartePanel);

        // Obtenir la position du clic dans les coordonnées du JPanel
        int x = location.x;
        int y = location.y;

        // Convertir ces coordonnées en indices de cellule sur la carte
        int gridX = x   / cartePanel.getCellSize(); // Diviser par la taille d'une cellule pour obtenir l'indice de la cellule
        int gridY = y  / cartePanel.getCellSize();

        return new Position(gridX, gridY);
    }
    

    @Override
    public void mousePressed(MouseEvent e) {

        if (listener != null && !menuBoutonClique){
            listener.clickOnCase(getCaseFromClick(e));
        }


        // Vérifiez si le clic est sur le bouton du Menu
        if (menuPanel.isMouseOverPlay(e.getPoint()) && menuBoutonClique) {  //Panel Game Load

            sauvegardePanel.setVisible(true);
            menuPanel.setVisible(false);

            menuBoutonClique = false;

        }else if(menuPanel.isMouseOverNewGame(e.getPoint()) && menuBoutonClique){ //Nouvelle partie
            
            transmettreAction("Nouvelle Partie");
            
            menuBoutonClique = false;

        }else if(menuPanel.isMouseOverSettings(e.getPoint()) && menuBoutonClique){ //pannel Paramètre
            settingsPanel.setVisible(true);
            menuPanel.setVisible(false);
            menuBoutonClique = false;
        }else if(menuPanel.isMouseOverExit(e.getPoint()) && menuBoutonClique){
            System.exit(0);
        }


    }

    @Override
    public void transmettreMarket(){
        marketPanel.setVisible(true);
    }
    @Override
    public void transmettreUsine(Usine u) {
        usinePanel.setUsine(u);

        usinePanel.setPlayer(game.getPlayer());
        usinePanel.setVisible(true);
    }


    @Override
    public void onBackToMenu() {
        // Cache le panneau des paramètres et affiche le menu principal
        settingsPanel.setVisible(false);
        menuPanel.setVisible(true);
        menuBoutonClique = true;
    }
    @Override
    public void onRetourMenu() {
        sauvegardePanel.setVisible(false);
        menuPanel.setVisible(true);
        menuBoutonClique = true;
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

    @Override
    public void startLoadGame(String nameGameLoad) {
        sauvegardePanel.setVisible(false);
        menuBoutonClique = false;

        chargerPartie(nameGameLoad);
    }


}