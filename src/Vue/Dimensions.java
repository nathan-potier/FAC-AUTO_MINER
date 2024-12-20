package Vue;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Dimensions {

    public static int BASE_CELL_SIZE = 50;

    // Récupérer la taille de l'écran
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int screenWidth = screenSize.width;
    public static int screenHeight = screenSize.height;

    // Calcul de CELL_SIZE en fonction de la taille de l'écran
    public static int calculateCellSize() {
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        double scaleFactorWidth = screenWidth / 1920.0;
        double scaleFactorHeight = screenHeight / 1080.0;
        double scaleFactor = Math.min(scaleFactorWidth, scaleFactorHeight);
        int cellSize = (int) (BASE_CELL_SIZE * scaleFactor);

        if (cellSize < 40) {
            cellSize = 40;
        } else if (cellSize > 100) {
            cellSize = 100;
        }
        return cellSize;
    }
}
