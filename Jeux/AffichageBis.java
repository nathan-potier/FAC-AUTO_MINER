import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AffichageBis extends JFrame implements VueGenerale, KeyListener {
    private Game game;
    private CartePanel cartePanel;
    private InventairePanel inventairePanel;
    private JPanel mainPanel;
    private MenuPanelBis menuPanel;

    private boolean inventaireAfficher = false;
    private Controle listener;

    public AffichageBis(Game game) {
        this.game = game;

        this.setTitle("Jeu de Gestion de Ressources");
        this.setSize(game.getWidth() * 50, game.getHeight() * 50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        // cartePanel = new CartePanel(game);
        // inventairePanel = new InventairePanel(game);
        menuPanel = new MenuPanelBis(mainPanel, e -> transmettreAction(((JButton) e.getSource()).getText()));

        //  mainPanel.add(menuPanel, BorderLayout.NORTH);

        // mainPanel.add(cartePanel, BorderLayout.CENTER);
        // mainPanel.add(inventairePanel, BorderLayout.EAST);

        //inventairePanel.setVisible(false);
        this.add(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    @Override
    public void afficherMenu() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherMenu'");
    }

    @Override
    public void afficherEtatJeu(String etat) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherEtatJeu'");
    }

    @Override
    public void transmettreAction(String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transmettreAction'");
    }
}
