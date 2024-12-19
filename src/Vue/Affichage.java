package Vue;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Modele.Game;
import Modele.GameStates;
import Modele.Position;
import Modele.Controle;
import Modele.Enregistrer.*;
import Modele.Machine.Recolteuse;
import Modele.Machine.Usine;

public class Affichage extends JFrame implements VueGenerale, KeyListener, MouseListener, SetMenuListener, UpdateInterface{
    private Game game;
    private String pseudo;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private MarketPanel marketPanel;
    private JPanel gamePanel;
    private JLayeredPane layeredPane;
    private MenuPanel menuPanel;
    private SauvegardePanel sauvegardePanel;
    private SettingsPanel settingsPanel;
    private UsinePanel usinePanel;
    private RecolteusePanel recolteusePanel;
    private JPanel boutonPanel;
    private JPanel button2Panel = new JPanel();

    private int height;
    private int width;

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
        this.pseudo = Game.getPlayer().getPseudo();
        // Récupérer la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Définir la taille de la fenêtre
        width = Dimensions.WINDOW_WIDTH;
        height = Dimensions.WINDOW_HEIGHT;

        // Ajuster la taille de la fenêtre pour qu'elle ne dépasse pas l'écran
        if (width > screenWidth) {
            width = screenWidth - 50;  // Ajouter une marge de sécurité
        }
        if (height > screenHeight) {
            height = screenHeight - 50;  // Ajouter une marge de sécurité
        }

        this.setTitle("AutoMiner");
        setPreferredSize(new Dimension(width, height));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                afficherBoiteDeDialogueFermeture();
            }
        });

        // Initialisation des composants
        gamePanel = new JPanel(new BorderLayout());
        layeredPane = new JLayeredPane();
        settingsPanel = new SettingsPanel();
        sauvegardePanel = new SauvegardePanel();
        boutonPanel = new JPanel();
        inventairePanel = new InventairePanel(null);
        marketPanel = new MarketPanel(game.getMarket(), Game.getPlayer());
        usinePanel = new UsinePanel();
        recolteusePanel = new RecolteusePanel();
        menuPanel = new MenuPanel(width / 2, height / 2, width, height);

        ImageIcon icon = new ImageIcon("assets/Logo.png");
        this.setIconImage(icon.getImage());

        settingsPanel.setMenuListener(this);
        sauvegardePanel.setMenuListener(this);

        cartePanel = new CartePanel(game.getWidth(), game.getHeight());
        this.game.addGameListener(cartePanel);
        cartePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        Dimensions.setMachinePanelBounds(usinePanel);
        Dimensions.setMachinePanelBounds(recolteusePanel);
        Dimensions.setMachinePanelBounds(marketPanel);



        // Configuration des panneaux dans le layeredPane
        cartePanel.setBounds(0, 0, width, height);
        inventairePanel.setBounds(0, 0, width, height);
        settingsPanel.setBounds(0, 0, width, height);
        sauvegardePanel.setBounds(0, 0, width, height);


        // Ajout des panneaux au layeredPane
        layeredPane.add(cartePanel, Integer.valueOf(1));
        layeredPane.add(boutonPanel, Integer.valueOf(2));
        layeredPane.add(inventairePanel, Integer.valueOf(10));
        layeredPane.add(marketPanel, Integer.valueOf(2));
        layeredPane.add(usinePanel, Integer.valueOf(6));
        layeredPane.add(recolteusePanel, Integer.valueOf(7));
        layeredPane.add(settingsPanel, Integer.valueOf(4));
        layeredPane.add(sauvegardePanel, Integer.valueOf(5));
        

        // Masquer les panneaux sauf la carte par défaut
        inventairePanel.setVisible(false);
        usinePanel.setVisible(false);
        recolteusePanel.setVisible(false);
        marketPanel.setVisible(false);
        settingsPanel.setVisible(false);
        sauvegardePanel.setVisible(false);
        boutonPanel.setVisible(false);

        initialiserBoutons();

        gamePanel.add(menuPanel, BorderLayout.SOUTH);
        gamePanel.add(layeredPane, BorderLayout.CENTER);
        gamePanel.add(boutonPanel, BorderLayout.EAST);

        this.add(gamePanel);
    
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Listener pour les interactions
        menuPanel.addMouseListener(this);
        this.addMouseListener(this);
        this.setLayout(null);
    }

    public void resetGame(){
        game.stopGame();
        this.game = new Game(pseudo);
        this.game.addGameListener(cartePanel);
    }

    public void afficherCarte() {
        cartePanel.repaint();
        cartePanel.setVisible(true);
        inventairePanel.setVisible(false);
        boutonPanel.setVisible(true);
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
        if (listener != null && GameStates.isMoveOn()) {
            listener.onKeyPress(e.getKeyCode());
        }
    }

    public void saveAndBackMenu(){
        GameStates.setMoveOn(false);
        isRunning = false;
        String nomSv = "";
        if(isNewGame){
            nomSv = GameSaver.generateUniqueSaveName("Monde");
        }else{
            nomSv = nameGameLoad;
        }
        GameSaver.saveGame(nomSv, Game.getPlayer(), game.getSavableObject());
        menuBoutonClique = true;
        closeAll();
        menuPanel.setVisible(true);
        sauvegardePanel.mettreAJourSauvegardes();
        resetGame();
    }

    public void setEcoute(Controle listener) {
        this.listener = listener;
        this.addKeyListener(this);
        this.setFocusable(true);
        requestFocusInWindow();
    }

    private void nouvellePartie(){
        game.create();
        lancerPartie();
    }

    private void chargerPartie(String nameGameLoad){
        isNewGame = false;
        this.nameGameLoad = nameGameLoad;
        game = GameLoader.loadGame(nameGameLoad, game);

        if (game.getMap() != null) {
            game.addGameListener(cartePanel);
            game.start();
            lancerPartie();
        }
    }

    private void lancerPartie(){
        menuPanel.setVisible(false);
        inventaireAfficher = false;
        setEcoute(game);
        cartePanel.repaint();
        game.initVue(this);
        GameStates.setMoveOn(true);
        isRunning = true;
        inventairePanel.setPlayer(Game.getPlayer());
        Game.getPlayer().setMarketInterface(marketPanel);
        Game.getPlayer().setAffichageInterface(this);
        Game.getPlayer().getInv().setUpdateCraft(inventairePanel);
        marketPanel.setPlayer(Game.getPlayer());
        marketPanel.setMarket(game.getMarket());
        boutonPanel.setVisible(true);
        settingsPanel.setVisible(false);
    }

    public Position getCaseFromClick(MouseEvent e) {
        Point location = SwingUtilities.convertPoint(this, e.getPoint(), cartePanel);

        // Obtenir la position du clic dans les coordonnées du JPanel
        int x = location.x;
        int y = location.y;

        // Convertir ces coordonnées en indices de cellule sur la carte
        int gridX = x   / Dimensions.CELL_SIZE; // Diviser par la taille d'une cellule pour obtenir l'indice de la cellule
        int gridY = y  / Dimensions.CELL_SIZE;

        return new Position(gridX, gridY);
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        if (listener != null && !menuBoutonClique){
            listener.clickOnCase(getCaseFromClick(e));
        }
        if (menuPanel.isMouseOverPlay(e.getPoint()) && menuBoutonClique) {  //Panel Game Load
            sauvegardePanel.setVisible(true);
            menuPanel.setVisible(false);
            boutonPanel.setVisible(false);
            menuBoutonClique = false;
        }else if(menuPanel.isMouseOverNewGame(e.getPoint()) && menuBoutonClique){ //Nouvelle partie
            isNewGame = true;
            nouvellePartie();
            menuBoutonClique = false;
        }else if(menuPanel.isMouseOverSettings(e.getPoint()) && menuBoutonClique){ //pannel Paramètre
            settingsPanel.setVisible(true);
            menuPanel.setVisible(false);
            boutonPanel.setVisible(false);
            menuBoutonClique = false;
        }else if(menuPanel.isMouseOverExit(e.getPoint()) && menuBoutonClique){
            System.exit(0);
        }
    }

    private void closeAll(){
        inventairePanel.setVisible(false);
        marketPanel.setVisible(false);
        usinePanel.setVisible(false);
        recolteusePanel.setVisible(false);
        settingsPanel.setVisible(false);
        cartePanel.setVisible(true);
        menuBoutonClique = true;
    }



    // Fonction pour initialiser le bouton "Inventaire"
    public JButton creerBoutonInventaire() {
        JButton inventaire = new JButton("Inventaire");
        inventaire.setFocusPainted(false);
        inventaire.setFont(new Font("Arial", Font.PLAIN, Math.max(14, width / 80)));
        inventaire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inventaireAfficher && !inventairePanel.isProducing()) {
                    afficherCarte();
                    GameStates.setMoveOn(true);
                    inventaireAfficher = false;
                } else {
                    GameStates.setMoveOn(false);
                    afficherInventaire();
                    inventaireAfficher = true;
                }
                requestFocusInWindow();
            }
        });
        return inventaire;
    }

    // Fonction pour initialiser le bouton "Placer l'usine"
    public JButton creerBoutonPlaceUsine() {
        JButton placeUsine = new JButton("Placer l'usine");
        placeUsine.setFocusPainted(false);
        placeUsine.setFont(new Font("Arial", Font.PLAIN, Math.max(14, width / 80)));
        placeUsine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onKeyPress(-1);
                if (Game.getPlayer().getInv().getMachine() == null) {
                    button2Panel.setVisible(false);
                } else {
                    button2Panel.setVisible(true);
                }
                requestFocusInWindow();
            }
        });
        return placeUsine;
    }

    // Fonction pour initialiser le bouton "Retour Menu"
    public JButton creerBoutonRetourMenu() {
        JButton retourMenuButton = new JButton("Retour Menu");
        retourMenuButton.setFocusPainted(false);
        retourMenuButton.setFont(new Font("Arial", Font.PLAIN, Math.max(14, width / 80)));
        retourMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAndBackMenu();
                requestFocusInWindow();
            }
        });
        return retourMenuButton;
    }

    // Fonction pour initialiser le panneau des boutons
    public void initialiserBoutons() {
        // Panel principal pour les boutons
        int boutonPanelWidth = (int)(0.15 * width); // 15% de la largeur de la fenêtre
        boutonPanel.setLayout(new BoxLayout(boutonPanel, BoxLayout.Y_AXIS));
        boutonPanel.setPreferredSize(new Dimension(boutonPanelWidth, height));
        boutonPanel.setBackground(Color.GRAY); // Couleur de fond gris
        boutonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Créer les boutons en appelant les fonctions spécifiques
        JButton inventaire = creerBoutonInventaire();
        JButton placeUsine = creerBoutonPlaceUsine();
        JButton retourMenuButton = creerBoutonRetourMenu();

        // Créer un panneau temporaire pour centrer les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));  // Centré avec un espacement vertical
        buttonPanel.setPreferredSize(new Dimension(boutonPanelWidth, 100));  // Ajuster la taille
        buttonPanel.setBackground(Color.GRAY); // Mettre aussi ce panneau en gris

        // Ajouter les boutons dans le panneau
        buttonPanel.add(inventaire);
        buttonPanel.add(placeUsine);
        buttonPanel.add(retourMenuButton);

        // Ajouter ce panneau au boutonPanel
        boutonPanel.add(buttonPanel);

        // Si pas de machine, masquer le bouton "Placer l'usine"
        if (Game.getPlayer().getInv().getMachine() == null) {
            placeUsine.setVisible(false);
        }
    }

    
    
    

    private void afficherBoiteDeDialogueFermeture() {
        String[] options = {"Sauvegarder et Quitter", "Quitter", "Annuler"};
        if(isRunning){
            int choix = JOptionPane.showOptionDialog(
                this,
                "Voulez-vous sauvegarder avant de quitter ?",
                "Confirmer la fermeture",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
            switch (choix) {
                case 0:
                    saveAndBackMenu();
                    System.exit(0);
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2: //annuler
                default:
                    break;
            }
        }else{
            System.exit(0);
        } 
    }

    @Override
    public void transmettreMarket(){
        marketPanel.notificationUpdate();
        marketPanel.setVisible(true);
        
    }
    @Override
    public void transmettreUsine(Usine u) {
        GameStates.setMoveOn(false);
        usinePanel.setUsine(u);
        usinePanel.setPlayer(Game.getPlayer());
        usinePanel.setVisible(true);
    }

    @Override
    public void transmettreRecolteuse(Recolteuse r) {
        GameStates.setMoveOn(false);
        recolteusePanel.setRecolteuse(r);
        recolteusePanel.setVisible(true);
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
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void startLoadGame(String nameGameLoad) {
        sauvegardePanel.setVisible(false);
        menuBoutonClique = false;
        chargerPartie(nameGameLoad);
    }

    @Override
    public void notificationUpdate() {
        button2Panel.setVisible(true);
    }
}