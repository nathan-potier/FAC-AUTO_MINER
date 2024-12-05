package Vue;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MenuPanel extends JPanel {

    private MenuButton playButton;
    private MenuButton newGame;
    private MenuButton settings;
    private MenuButton exit;

    private ImageIcon logoIcon;  // ImageIcon pour le logo

    public MenuPanel(int xpos, int ypos, int width, int height) {
        logoIcon = new ImageIcon(getClass().getResource("/assets/Logo.png"));
        
        playButton = new MenuButton(180, 110, xpos - 115, ypos + 10, "../assets/PlayDefault.png", "../assets/PlayHover.png");
        newGame = new MenuButton(180, 110, xpos + 100, ypos + 10, "../assets/NewGameDefault.png", "../assets/NewGameHover.png");
        settings = new MenuButton(180, 110, xpos - 115, ypos + 120, "../assets/SettingDefault.png", "../assets/SettingHover.png");
        exit = new MenuButton(180, 110, xpos + 100, ypos + 120, "../assets/QuitDefault.png", "../assets/QuitHover.png");

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.DARK_GRAY);

        // Ajouter un listener pour la souris
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

        if (logoIcon != null) {
            Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(logoImage);

            int logoWidth = scaledLogoIcon.getIconWidth();
            int x = (getWidth() - logoWidth) / 2;
            int y = 50;
            scaledLogoIcon.paintIcon(this, g, x, y);
        }

        playButton.draw(g);
        newGame.draw(g);
        settings.draw(g);
        exit.draw(g);
    }

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
