package Vue;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MenuButtonPanel extends JPanel {

    private MenuButton playButton;
    private MenuButton newGame;
    private MenuButton settings;
    private MenuButton exit;

    public MenuButtonPanel(int xpos, int ypos) {
        // Initialiser le bouton au centre de la fenêtre
        playButton = new MenuButton(170, 95, xpos - 115, ypos, "../assets/PlayDefault.png", "../assets/PlayHover.png");
        newGame = new MenuButton(180, 110, xpos + 100, ypos + 2, "../assets/NewGameDefault.png", "../assets/NewGameHover.png");
        settings = new MenuButton(180, 110, xpos - 113, ypos + 110, "../assets/SettingDefault.png", "../assets/SettingHover.png");
        exit = new MenuButton(180, 110, xpos + 100, ypos + 110, "../assets/SettingDefault.png", "../assets/SettingHover.png");


        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.DARK_GRAY);
         
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                playButton.setMouseOver(playButton.getBounds().contains(e.getPoint()));
                newGame.setMouseOver(newGame.getBounds().contains(e.getPoint()));
                settings.setMouseOver(settings.getBounds().contains(e.getPoint()));
                exit.setMouseOver(exit.getBounds().contains(e.getPoint()));
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner le bouton
        playButton.draw(g);
        newGame.draw(g);
        settings.draw(g);
        exit.draw(g);
    }

    // Ajoutez une méthode pour vérifier si un point se trouve sur le bouton
    public boolean isMouseOverPlay(Point point) {
        return playButton.getBounds().contains(point);
    }

    public boolean isMouseOverNewGame(Point point) {
        return newGame.getBounds().contains(point);
    }

    public boolean isMouseOverSettings(Point point) {
        return settings.getBounds().contains(point);
    }

    public boolean isMouseOverExit(Point point) {
        return exit.getBounds().contains(point);
    }

}