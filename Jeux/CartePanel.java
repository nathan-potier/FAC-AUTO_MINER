import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartePanel extends JPanel implements GameListener {
    private final int CELL_SIZE = 40; // Taille d'une cellule
    private final Map<Object, Point> animatedPositions = new HashMap<>();
    private final ArrayList<Runnable> animationQueue = new ArrayList<>();
    private boolean animated = false;

    public CartePanel(Game game) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(game.getWidth() * CELL_SIZE, game.getHeight() * CELL_SIZE));
        game.addGameListener(this); // S'abonner au modèle
        this.setDoubleBuffered(true);
    }

    @Override
    public void updateObjects(ArrayList<Object> obj, ArrayList<Position> pos) {
        animatedPositions.clear(); // On efface les objets précédents
        for (int i = 0; i < obj.size(); i++) {
            animatedPositions.put(obj.get(i), new Point(pos.get(i).getX() * CELL_SIZE, pos.get(i).getY() * CELL_SIZE));
        }
    }

    @Override
    public void onMove(Object content, Position startPos, Position targetPos) {
        // Ajouter une nouvelle animation à la file
        animationQueue.add(() -> animateObjectMovement(content, startPos, targetPos, 150));
        
        // Si aucune animation n'est en cours, démarrer le traitement
        if (!animated) {
            processNextAnimation();
        }
    }

    private void processNextAnimation() {
        if (!animationQueue.isEmpty()) {
            animated = true; // Une animation est en cours
            Runnable nextAnimation = animationQueue.remove(0);
            nextAnimation.run(); // Exécuter l'animation suivante
        } else {
            animated = false; // Plus d'animations en attente
        }
    }

    public void animateObjectMovement(Object object, Position startPos, Position targetPos, int duration) {
        // Calcul des positions de départ et d'arrivée
        Point start = animatedPositions.getOrDefault(
            object,
            new Point(startPos.getX() * CELL_SIZE, startPos.getY() * CELL_SIZE)
        );
        Point end = new Point(targetPos.getX() * CELL_SIZE, targetPos.getY() * CELL_SIZE);

        // Distances totales en pixels
        int totalDistanceX = end.x - start.x;
        int totalDistanceY = end.y - start.y;

        // Calcul du nombre total de pixels à parcourir (diagonale max pour ne pas sauter de pixels)
        int totalPixels = Math.max(Math.abs(totalDistanceX), Math.abs(totalDistanceY));

        // Intervalle de temps réduit pour une animation fluide

        int totalSteps = totalPixels; // 1 pixel par étape
        int stepDuration = Math.max(1, duration / totalSteps); // Durée totale divisée par les étapes

        Timer timer = new Timer(stepDuration, null);
        int[] currentPixel = {0}; // Pixel actuel parcouru

        timer.addActionListener(e -> {
            MouseInfo.getPointerInfo ();
            if (currentPixel[0] >= totalSteps) {
                // Arrêt de l'animation une fois tous les pixels parcourus
                timer.stop();
                animatedPositions.put(object, end); // Position finale
                this.repaint();
                processNextAnimation(); // Passer à l'animation suivante
            } else {
                // Calcul de la progression pour le pixel actuel
                double progress = (double) currentPixel[0] / totalPixels;

                // Interpolation linéaire
                int newX = start.x + (int) (progress * totalDistanceX);
                int newY = start.y + (int) (progress * totalDistanceY);

                animatedPositions.put(object, new Point(newX, newY));
                this.repaint();
                currentPixel[0]++;
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
