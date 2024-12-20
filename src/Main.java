import javax.swing.SwingUtilities;

import Modele.Game;
import Vue.Affichage;
public class Main {
    public static void main(String[] args) {
        Game game = new Game("nathan");
        SwingUtilities.invokeLater(() -> {
            Affichage affichage = new Affichage(game);
            affichage.setVisible(true);
        });
    }
}
