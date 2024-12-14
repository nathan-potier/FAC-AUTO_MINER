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
        
        playButton = new MenuButton(180, 110, xpos - 115, ypos + 10, "/assets/PlayDefault.png", "/assets/PlayHover.png");
        newGame = new MenuButton(180, 110, xpos + 100, ypos + 10, "/assets/NewGameDefault.png", "/assets/NewGameHover.png");
        settings = new MenuButton(180, 110, xpos - 115, ypos + 120, "/assets/SettingDefault.png", "/assets/SettingHover.png");
        exit = new MenuButton(180, 110, xpos + 100, ypos + 120, "/assets/QuitDefault.png", "/assets/QuitHover.png");

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
        System.out.println("Exception in thread java.lang.NullPointerException: Attempt to invoke method void Vue.InventairePanel.afficherInventaire() on a null object reference at Vue.Affichage.afficherInventaire(Affichage.java:XX) at Vue.Affichage.keyPressed(Affichage.java:YY) at java.base/java.awt.event.KeyEvent.dispatch(KeyEvent.java:315) at java.base/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:777) at java.base/java.awt.EventQueue$4.run(EventQueue.java:728) at java.base/java.awt.EventQueue$4.run(EventQueue.java:722) at java.base/java.security.AccessController.doPrivileged(AccessController.java:399) at java.base/java.security.AccessController.doPrivileged(AccessController.java:331) at java.base/java.awt.EventQueue$5.run(EventQueue.java:752) at java.base/java.awt.EventQueue$5.run(EventQueue.java:750) at java.base/java.awt.EventQueue$3.run(EventQueue.java:726) at java.base/java.awt.EventQueue$3.run(EventQueue.java:724) at java.base/java.awt.EventQueue.dispatchEvent(EventQueue.java:743) at java.base/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203) at java.base/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:128) at java.base/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:117) at java.base/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:113) at java.base/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:105) at java.base/java.awt.EventDispatchThread.run(EventDispatchThread.java:91)");
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
