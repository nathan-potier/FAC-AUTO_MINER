import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartePanel extends JPanel implements GameListener {
    private final int CELL_SIZE = 40; // Taille d'une cellule
    private Map<Object, Point> animatedPositions = new HashMap<>();



    public CartePanel(Game game) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(game.getWidth() * CELL_SIZE, game.getHeight() * CELL_SIZE));
        game.addGameListener(this); // S'abonner au modèle
        this.setDoubleBuffered(true);


        

    }
    @Override
    public void updateObjects(ArrayList<Object> obj, ArrayList<Position> pos) {
        animatedPositions.clear(); // On efface les objets précédents
        for(int i = 0; i < obj.size(); i++){
            animatedPositions.put(obj.get(i), new Point(pos.get(i).getX() * CELL_SIZE, pos.get(i).getY() * CELL_SIZE));
        }
        //repaint(); // Redessine le panneau
    }
    
    

    @Override
    public void onMove(Object content, Position startPos, Position targetPos) {
        // Si une animation est déjà en cours, on ne commence pas une nouvelle animation

        animateObjectMovement(content, startPos, targetPos, 100); // Animation sur 1000ms
    
    }

    

    /**
     * Gère l'animation d'un objet à partir d'une position de départ vers une position cible.
     */
    public void animateObjectMovement(Object object, Position startPos, Position targetPos, int duration) {
        Point start = new Point(startPos.getX() * CELL_SIZE, startPos.getY() * CELL_SIZE);
        Point end = new Point(targetPos.getX() * CELL_SIZE, targetPos.getY() * CELL_SIZE);
    
        Timer timer = new Timer(5, null); // Intervalle réduit pour une fluidité accrue
        long startTime = System.currentTimeMillis();
    
        timer.addActionListener(e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, (double) elapsedTime / duration);
    
            // Interpolation douce (ease-in-out)
            double smoothProgress = progress * progress * (3 - 2 * progress);
    
            int newX = (int) (start.x + smoothProgress * (end.x - start.x));
            int newY = (int) (start.y + smoothProgress * (end.y - start.y));
    
            animatedPositions.put(object, new Point(newX, newY));
            this.repaint(); // Réactualiser l'affichage

            if (progress >= 1.0) {
                timer.stop();
                animatedPositions.put(object, end); // Position finale
                this.repaint(); // Dernière actualisation
            }
        });
    
        timer.start();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dessiner tous les objets (y compris le joueur)
        for (Map.Entry<Object, Point> entry : animatedPositions.entrySet()) {
            Object content = entry.getKey();
            Point position = entry.getValue();
            ImageIcon icon = getIconForType(content.getClass().getSimpleName());
        
            if (icon != null) {
                g.drawImage(icon.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
    

    /**
     * Obtient l'icône correspondant au type d'objet.
     */
    private ImageIcon getIconForType(String type) {
        String path = "assets/" + type + ".png";
        
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            
            return null;
        }
        return icon;
    }
    
}
