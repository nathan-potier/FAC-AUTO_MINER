import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CartePanel extends JPanel {

    private Game game;

    public CartePanel(Game game) {
        this.game = game;
        this.setLayout(new GridLayout(game.getHeight(), game.getWidth()));
    }

    public void setGame(Game g){game = g;}

    public void afficherCarte() {
        this.removeAll(); // Supprimer les anciens éléments

        for (Cell[] row : game.getMap().getGrid()) {
            for (Cell cell : row) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellPanel.setPreferredSize(new Dimension(50, 50));

                if (cell != null && cell.getContent() != null) {
                    ImageIcon icon = getIconForType(cell.getContent().getClass().getSimpleName());
                    if (icon != null) {
                        JLabel contentLabel = new JLabel(icon);
                        contentLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); // Ajuste la taille
                        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        cellPanel.add(contentLabel);
                    }
                }

                this.add(cellPanel);
            }
        }
        this.revalidate();
        this.repaint();
    }
    public void actualiseAffichage(int temps) {
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherCarte();
                actualiseAffichage(10);

                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
        
    }

    // Méthode pour obtenir une icône pour un type donné
    private ImageIcon getIconForType(String type) {
        switch (type) {
            case "Player":
                return new ImageIcon("assets/player.png");
            case "Wood":
                return new ImageIcon("assets/Wood.png");
            case "Iron":
                return new ImageIcon("assets/Iron.png");
            default:
                return null;
        }
    }
}
