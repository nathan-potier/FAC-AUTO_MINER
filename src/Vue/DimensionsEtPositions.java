package Vue;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class DimensionsEtPositions {

    // Valeur de base de CELL_SIZE, ajustable en fonction de la taille de l'écran
    public static final int BASE_CELL_SIZE = 50;  // Valeur de base pour CELL_SIZE (ajustable)

    // Récupérer la taille de l'écran
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int screenWidth = screenSize.width;
    public static int screenHeight = screenSize.height;

    // Calcul de CELL_SIZE en fonction de la taille de l'écran
    private static int calculateCellSize() {
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calcul d'un facteur d'échelle basé sur la largeur de l'écran, par rapport à une largeur cible de 1920
        double scaleFactorWidth = screenWidth / 1920.0;
        double scaleFactorHeight = screenHeight / 1080.0;

        // On prend le minimum entre les deux facteurs d'échelle pour assurer que la fenêtre ne dépasse pas
        double scaleFactor = Math.min(scaleFactorWidth, scaleFactorHeight);

        // Calculer la taille des cellules en fonction du facteur d'échelle
        int cellSize = (int) (BASE_CELL_SIZE * scaleFactor);

        // Limiter la taille des cellules pour éviter qu'elles ne deviennent trop grandes ou trop petites
        if (cellSize < 40) {
            cellSize = 40;
        } else if (cellSize > 100) {
            cellSize = 100;
        }

        return cellSize;
    }

    // Appliquer le calcul dynamique de CELL_SIZE
    public static final int CELL_SIZE = calculateCellSize();

    // Dimensions principales
    public static final int WINDOW_WIDTH = 19 * CELL_SIZE;
    public static final int WINDOW_HEIGHT = 13 * CELL_SIZE;

    // Dimensions des panneaux
    public static final Dimension PANEL_DIMENSION = new Dimension(140, WINDOW_HEIGHT); // Panneau de boutons
    public static final Dimension USINE_PANEL_DIMENSION = new Dimension(775, 550); // Panneau Usine
    public static final Dimension RECOLTEUSE_PANEL_DIMENSION = new Dimension(775, 550); // Panneau Récolteuse

    // Positions des panneaux
    public static final Point USINE_PANEL_POSITION = new Point(10, 50); // Position du panneau Usine
    public static final Point RECOLTEUSE_PANEL_POSITION = new Point(10, 50); // Position du panneau Récolteuse

    // Dimensions des boutons
    public static final Dimension BUTTON_DIMENSION = new Dimension(120, 40); // Taille des boutons
}
