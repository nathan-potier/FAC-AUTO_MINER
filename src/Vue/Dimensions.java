package Vue;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

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

        // Taille cible de 1920*1080
        double scaleFactorWidth = screenWidth / 1920.0;
        double scaleFactorHeight = screenHeight / 1080.0;

        double scaleFactor = Math.min(scaleFactorWidth, scaleFactorHeight);

        // Calculer la taille des cellules en fonction du facteur d'échelle
        int cellSize = (int) (BASE_CELL_SIZE * scaleFactor);

        if (cellSize < 40) {
            cellSize = 40;
        } else if (cellSize > 100) {
            cellSize = 100;
        }
        return cellSize;
    }

 




    // Calcul des dimensions et position
    public static final int PANEL_X = (int) (screenWidth * 0.01);
    public static final int PANEL_Y = (int) (screenHeight * 0.05);
    public static final int PANEL_WIDTH = 15 * calculateCellSize();
    public static final int PANEL_HEIGHT = 10 * calculateCellSize();

    // Méthode pour appliquer ces valeurs aux Panels
    public static void setPanelBounds(JPanel Panel) {
        Panel.setBounds(PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT);
    }
}
