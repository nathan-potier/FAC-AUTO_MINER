import javax.swing.*;

public class MainMenu extends JFrame implements VueGenerale {
    private JButton continueButton;
    private JButton newGameButton;
    private JButton settingsButton;
    private JButton quitButton;

    public MainMenu() {
        this.setTitle("Main Menu");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
}
