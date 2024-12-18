package Vue;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class DimensionsEtPositions {

    // Valeur de base de CELL_SIZE, que tu peux ajuster facilement
    private static final int BASE_CELL_SIZE = 50;  // Valeur de base pour CELL_SIZE (ajustable)

    // Récupérer la taille de l'écran
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Calcul de CELL_SIZE en fonction de la taille de l'écran
    // Nous allons multiplier BASE_CELL_SIZE par un facteur d'adaptation basé sur la largeur de l'écran
    private static final double scaleFactor = screenSize.width / 1920.0;  // 1920 est la résolution cible
    public static final int CELL_SIZE = (int) (BASE_CELL_SIZE * scaleFactor);

    // Dimensions principales
    public static final int WINDOW_WIDTH = 19 * CELL_SIZE;
    public static final int WINDOW_HEIGHT = 13 * CELL_SIZE;

    // Dimensions des panneaux
    public static final Dimension PANEL_DIMENSION = new Dimension(140, WINDOW_HEIGHT); // Panneau de boutons
    public static final Dimension USINE_PANEL_DIMENSION = new Dimension(775, 550); // Panneau Usine
    public static final Dimension RECOLTEUSE_PANEL_DIMENSION = new Dimension(775, 550); // Panneau Récolteuse

    // Positions des panneaux
    public static final Point USINE_PANEL_POSITION = new Point(10, 50); // Position de Usine Panel
    public static final Point RECOLTEUSE_PANEL_POSITION = new Point(10, 50); // Position de Recolteuse Panel

    // Dimensions des boutons
    public static final Dimension BUTTON_DIMENSION = new Dimension(120, 40); // Taille des boutons

}
