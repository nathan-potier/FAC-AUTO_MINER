import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButtonTest extends JPanel {

    private MenuButton playButton;

    public MenuButtonTest() {
        // Initialiser le bouton au centre de la fenêtre
        playButton = new MenuButton(400, 300);


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

    // Ajoutez une méthode pour vérifier si un point se trouve sur le bouton
    public boolean isMouseOver(Point point) {
        return playButton.getBounds().contains(point);
    }

}