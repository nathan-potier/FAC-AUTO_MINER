package Vue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import Modele.Player;
import Modele.Position;

public class CartePanel extends JPanel implements GameListener {
    private final int CELL_SIZE = 40; // Taille d'une cellule
    private final Map<Object, Point> animatedPositions = new ConcurrentHashMap<>();
    private final Map<Position, Timer> activeTimers = new HashMap<>();
    private final List<Runnable> animationQueue = new ArrayList<>();

    private boolean animated = false;
    private int duration = 100; // Durée d'animation
    private int totalDuration = 100; // Durée totale initiale
    private Timer currentTimer = null;
    private String playerDirection = "right"; // Direction par défaut

    public CartePanel(int width, int height) {
        this.setPreferredSize(new Dimension(width * CELL_SIZE, height * CELL_SIZE));
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    @Override
    public void updateObjects(ArrayList<Object> objects, ArrayList<Position> positions) {
        animatedPositions.clear();
        System.out.println(currentTimer==null);
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

    /**
     * Anime le mouvement d'un objet d'une position de départ à une position cible.
     */
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
        String image2Path = "Attente2";

        attenteAnimation(position, duration,  image1Path,  image2Path);
    }

    public void attenteAnimation(Position position, int duration, String image1Path, String image2Path) {

        if (activeTimers.containsKey(position)) return;
    
        Point point = positionToPoint(position);
        ImageIcon image1 = getIconForType(image1Path);
        ImageIcon image2 = getIconForType(image2Path);
    
        Timer timer = createAlternatingImageTimer(point, image1, image2, duration, () -> {
            activeTimers.remove(position);
            repaint();
        });
    
        activeTimers.put(position, timer);
        timer.start();
    }

    /**
     * Crée un Timer pour alterner entre deux images sur une position spécifique.
     */
    private Timer createAlternatingImageTimer(Point position, ImageIcon image1, ImageIcon image2, int duration, Runnable onComplete) {
        Timer timer = new Timer(1000, null); // Changement toutes les secondes
        long startTime = System.currentTimeMillis();
        boolean[] toggle = {true}; // État pour alterner entre les deux images

        timer.addActionListener(e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Alterner l'image
            toggle[0] = !toggle[0];
            Graphics g = getGraphics();
            if (toggle[0]) {
                g.drawImage(image1.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
            } else {
                g.drawImage(image2.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
            }

            // Arrêter l'animation après la durée spécifiée
            if (elapsedTime >= duration) {
                timer.stop();
                onComplete.run();
            }
        });

        return timer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner les cases animées
        //activeTimers.keySet().forEach(pos -> drawHighlight(g, positionToPoint(pos)));

        // Dessiner tous les objets
        animatedPositions.forEach((object, position) -> drawObject(g, object, position));
    }

    /**
     * Méthodes d'outils
     */
    private Point positionToPoint(Position pos) {
        return new Point(pos.getX() * CELL_SIZE, pos.getY() * CELL_SIZE);
    }

    private void drawObject(Graphics g, Object content, Point position) {
        ImageIcon icon = content instanceof Player ?
                getIconForDirection() : getIconForType(content.getClass().getSimpleName());
        if (icon != null) {
            g.drawImage(icon.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
        }
    }

    private void drawHighlight(Graphics g, Point position) {
        g.setColor(Color.RED);
        g.fillRect(position.x, position.y, CELL_SIZE, CELL_SIZE);
    }

    /**
     * Crée un Timer pour des animations de type "clignotement".
     */
    private Timer createBlinkingTimer(Point position, int duration, Runnable onComplete) {
        Timer timer = new Timer(200, null);
        long startTime = System.currentTimeMillis();

        timer.addActionListener(e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if ((elapsedTime / 200) % 2 == 0) {
                drawHighlight(getGraphics(), position);
            } else {
                repaint();
            }
            if (elapsedTime >= duration) {
                timer.stop();
                onComplete.run();
            }
        });

        return timer;
    }

    /**
     * Crée un Timer pour des animations de type "mouvement fluide".
     */
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

    /**
     * Gestion des icônes
     */
    private ImageIcon getIconForDirection() {
        return getIconForType("Player_" + playerDirection);
    }

    private ImageIcon getIconForType(String type) {
        String relativePath = "assets/" + type + ".png";
        File file = new File(relativePath);
        if (!file.exists()) {
            System.err.println("Fichier non trouvé : " + file.getAbsolutePath());
            return new ImageIcon("assets/error.png");
        }
        return new ImageIcon(file.getAbsolutePath());
    }



}
