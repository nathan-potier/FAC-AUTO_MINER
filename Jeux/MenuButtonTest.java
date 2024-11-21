import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButtonTest extends JPanel {

    private MenuButton playButton;

    public MenuButtonTest() {
        // Initialiser le bouton au centre de la fenêtre
        playButton = new MenuButton(400, 300);

        // Ajouter un gestionnaire de souris pour interagir avec le bouton
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (playButton.getBounds().contains(e.getPoint())) {
                    System.out.println("Bouton cliqué !");
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                playButton.setMouseOver(playButton.getBounds().contains(e.getPoint()));
                repaint(); // Repeindre pour refléter le changement d'état
            }
        });

        // Configuration de la fenêtre (facultatif pour un rendu propre)
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner le bouton
        playButton.draw(g);
    }

    public static void main(String[] args) {
        // Créer une fenêtre JFrame
        JFrame frame = new JFrame("Test MenuButton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ajouter l'instance de MenuButtonTest au JFrame
        MenuButtonTest panel = new MenuButtonTest();
        frame.add(panel);

        // Ajuster la taille et rendre visible
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}