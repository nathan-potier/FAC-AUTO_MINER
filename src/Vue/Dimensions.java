package Vue;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Dimensions {

    // Valeur de base de CELL_SIZE, ajustable en fonction de la taille de l'écran
    public static int BASE_CELL_SIZE = 50;  // Valeur de base pour CELL_SIZE (ajustable)

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

    // Calcul des dimensions et position de l'usinePanel
    public static final int USINE_PANEL_X = (int) (screenWidth * 0.01);  // 1% de la largeur de l'écran
    public static final int USINE_PANEL_Y = (int) (screenHeight * 0.05);  // 5% de la hauteur de l'écran
    public static final int USINE_PANEL_WIDTH = 15 * CELL_SIZE;  // 15 cellules de large
    public static final int USINE_PANEL_HEIGHT = 10 * CELL_SIZE;  // 10 cellules de haut

    // Méthode pour appliquer ces valeurs à usinePanel
    public static void setUsinePanelBounds(JPanel usinePanel) {
        usinePanel.setBounds(USINE_PANEL_X, USINE_PANEL_Y, USINE_PANEL_WIDTH, USINE_PANEL_HEIGHT);
    }
}
