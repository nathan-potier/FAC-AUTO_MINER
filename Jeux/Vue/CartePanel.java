package Vue;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Modele.Position;

public class CartePanel extends JPanel implements GameListener {
    private final int CELL_SIZE = 40; // Taille d'une cellule
    private final Map<Object, Point> animatedPositions = new HashMap<>();
    private final ArrayList<Runnable> animationList = new ArrayList<>();
    private boolean animated = false;
    private int duration = 100;  // Durée de l'animation en ms
    private int totalDuration = 100;  // Durée initiale de l'animation
    private Timer currentTimer = null;

    private String parametreImg = "right";

    public CartePanel(int width, int height) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(width * CELL_SIZE, height * CELL_SIZE));
        
        this.setDoubleBuffered(true);
    }

    @Override
    public void updateObjects(ArrayList<Object> obj, ArrayList<Position> pos) {
        animatedPositions.clear(); // On efface les objets précédents
        for (int i = 0; i < obj.size(); i++) {
            animatedPositions.put(obj.get(i), new Point(pos.get(i).getX() * CELL_SIZE, pos.get(i).getY() * CELL_SIZE));
        }

        if(currentTimer == null){

            this.repaint();
        }


    }

    @Override
    public void onMove(Object content, Position startPos, Position targetPos, String dir) {
        // Si une animation est déjà en cours, on l'arrête
        parametreImg = dir;
        if (animated) {
            stopCurrentAnimation();
            duration = (int) (totalDuration / 2); // Par exemple, on double la vitesse
        }


        // Ajouter la nouvelle animation à la file
        animationList.add(() -> animateObjectMovement(content, startPos, targetPos, duration));

        // Démarrer ou redémarrer le traitement des animations
        if (!animated) {
            processNextAnimation();
        }
    }

    private void stopCurrentAnimation() {
        // Arrêter le timer actuel si une animation est en cours
        if (currentTimer != null) {
            currentTimer.stop();
            currentTimer = null;
        }

        // Réinitialiser l'état pour démarrer une nouvelle animation
        animationList.clear();
        animated = false; // L'animation en cours est arrêtée
    }

    private void processNextAnimation() {
        if (!animationList.isEmpty()) {
            animated = true; // Une animation est en cours
            Runnable nextAnimation = animationList.remove(0);
            nextAnimation.run(); // Exécuter l'animation suivante
        } else {
            animated = false; // Plus d'animations en attente
            duration = 100;
        }
    }

    /**
     * Gère l'animation d'un objet à partir d'une position de départ vers une position cible.
     */
    public void animateObjectMovement(Object object, Position startPos, Position targetPos, int duration) {
        Point start = new Point(startPos.getX() * CELL_SIZE, startPos.getY() * CELL_SIZE);
        Point end = new Point(targetPos.getX() * CELL_SIZE, targetPos.getY() * CELL_SIZE);

        // Timer pour l'animation
        currentTimer = new Timer(5, null); // Intervalle réduit pour une fluidité accrue
        long startTime = System.currentTimeMillis();

        currentTimer.addActionListener(e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            double progress = Math.min(1.0, (double) elapsedTime / duration);

            // Interpolation douce (ease-in-out)
            double smoothProgress = progress * progress * (3 - 2 * progress);

            int newX = (int) (start.x + smoothProgress * (end.x - start.x));
            int newY = (int) (start.y + smoothProgress * (end.y - start.y));

            animatedPositions.put(object, new Point(newX, newY));
            this.repaint(); // Réactualiser l'affichage

            if (progress >= 1.0) {
                currentTimer.stop();
                currentTimer = null; // Reset du timer après l'animation
                animatedPositions.put(object, end); // Position finale
                this.repaint(); // Dernière actualisation
                processNextAnimation(); // Passer à l'animation suivante
            }
        });

        currentTimer.start();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner tous les objets (y compris le joueur)
        for (Map.Entry<Object, Point> entry : animatedPositions.entrySet()) {
            Object content = entry.getKey();
            Point position = entry.getValue();
            ImageIcon icon ;
            if (parametreImg != null && content.getClass().getSimpleName().equals("Player") ){
                icon = getIconForDirection();
            }else{
                icon = getIconForType(content.getClass().getSimpleName());
                
            }
            

            if (icon != null) {
                g.drawImage(icon.getImage(), position.x, position.y, CELL_SIZE, CELL_SIZE, this);
            }
        }

    }

    
    /**
     * Obtient l'icône correspondant à la direction du joueur.
     */
    private ImageIcon getIconForDirection() {

        String relativePath = "assets/Player_" + parametreImg + ".png";
        File file = new File(relativePath); 
        // Vérifie si le fichier existe
        if (!file.exists()) {
            System.err.println("Fichier non trouvé : " + file.getAbsolutePath());
            return null;
        }
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());

        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            return null;
        }

        return icon;
    }
    /**
     * Obtient l'icône correspondant au type d'objet.
     */
    private ImageIcon getIconForType(String type) {
        String relativePath = "assets/" + type + ".png";
        File file = new File(relativePath); 
        
        // Vérifie si le fichier existe
        if (!file.exists()) {
            System.err.println("Fichier non trouvé : " + file.getAbsolutePath());
            return null;
        }
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE ) {
            return null;
        }

        return icon;
    }
}
