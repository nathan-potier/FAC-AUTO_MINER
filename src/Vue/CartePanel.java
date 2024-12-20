package Vue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import Modele.Attente;
import Modele.Player;
import Modele.Position;

public class CartePanel extends JPanel implements GameListener {
    private final int CELL_SIZE = Dimensions.calculateCellSize(); // Taille d'une cellule
    private final Map<Object, Point> animatedPositions = new ConcurrentHashMap<>();
    private final List<Runnable> animationQueue = new ArrayList<>();

    private boolean animated = false;
    private int duration = 100; // Durée d'animation
    private int totalDuration = 100; // Durée totale initiale
    private Timer currentTimer = null;
    private String playerDirection = "right"; // Direction par défaut

    public CartePanel(int width, int height) {
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    public void updateObjects(ArrayList<Object> objects, ArrayList<Position> positions) {
        animatedPositions.clear();
        for (int i = 0; i < objects.size(); i++) {
            animatedPositions.put(objects.get(i), new Point(positions.get(i).getX() * CELL_SIZE, positions.get(i).getY() * CELL_SIZE));
        }
        if (currentTimer == null) {
            repaint();
        }
    }

    @Override
    public void onMove(Object content, Position startPos, Position targetPos, String direction) {
        playerDirection = direction;
        // Si une animation est déjà en cours, stoppez et ajustez la durée
        if (animated) {
            stopCurrentAnimation();
            duration = totalDuration / 2; // Accélération si nécessaire
        }
        // Ajouter l'animation dans la file d'attente
        animationQueue.add(() -> animateObjectMovement(content, startPos, targetPos, duration));
        if (!animated) {
            processNextAnimation();
        }
    }

    private void stopCurrentAnimation() {
        if (currentTimer != null) {
            currentTimer.stop();
            currentTimer = null;
        }
        animationQueue.clear();
        animated = false;
    }

    private void processNextAnimation() {
        if (!animationQueue.isEmpty()) {
            animated = true;
            Runnable nextAnimation = animationQueue.remove(0);
            nextAnimation.run();
        } else {
            animated = false;
            duration = totalDuration;
        }

    }

    //Anime le mouvement d'un objet d'une position de départ à une position cible
    public void animateObjectMovement(Object object, Position startPos, Position targetPos, int duration) {
        Point start = positionToPoint(startPos);
        Point end = positionToPoint(targetPos);

        currentTimer = createSmoothAnimationTimer(start, end, duration, point -> {
            animatedPositions.put(object, point);
            repaint();
        }, () -> {
            animatedPositions.put(object, end);
            repaint();
            currentTimer = null; // Réinitialiser le timer à null après la fin
            processNextAnimation();
        });
        currentTimer.start();
    }

    @Override
    public void attenteAnimation(Position position, int duration){
        String image1Path = "Attente";
        attenteAnimation(position, duration,  image1Path);
    }

    public void attenteAnimation(Position position, int duration, String image1Path) {
        Point point = positionToPoint(position);
        // Ajouter l'icône animée à la liste des images à dessiner
        animatedPositions.put(new Attente(position, null), point);
        repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner tous les objets animés
        animatedPositions.forEach((object, position) -> {

            if (object instanceof Attente) {
                drawObject(g, object, position, "gif");
            } else {
                drawObject(g, object, position, "png");
            }
        });
    }

    private Point positionToPoint(Position pos) {
        return new Point(pos.getX() * CELL_SIZE, pos.getY() * CELL_SIZE);
    }

    private void drawObject(Graphics g, Object content, Point position, String format) {
        ImageIcon icon = content instanceof Player ?
                getIconForDirection() : getIconForType(content.getClass().getSimpleName(), format);
        if (icon != null) {
            g.drawImage(icon.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
        }
    }




    
    //Crée un Timer pour des animations fluides
    private Timer createSmoothAnimationTimer(Point start, Point end, int duration, Consumer<Point> onUpdate, Runnable onComplete) {
        Timer timer = new Timer(5, null);
        long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, (double) elapsedTime / duration);
            double smoothProgress = progress * progress * (3 - 2 * progress);

            Point interpolated = new Point(
                    (int) (start.x + smoothProgress * (end.x - start.x)),
                    (int) (start.y + smoothProgress * (end.y - start.y))
            );

            onUpdate.accept(interpolated);

            if (progress >= 1.0) {
                timer.stop();
                onComplete.run();
            }
        });

        return timer;
    }


    //Gestion des icônes
    private ImageIcon getIconForDirection() {
        return getIconForType("Player_" + playerDirection.toLowerCase(), "png");
    }

    private ImageIcon getIconForType(String type, String format) {
        String relativePath = "assets/" + type + "." + format;
        File file = new File(relativePath);
        if (!file.exists()) {
            return new ImageIcon("assets/error.png");
        }
        return new ImageIcon(file.getAbsolutePath());
    }



}
