import Modele.Game;
import Vue.Affichage;
public class Main {
    public static void main(String[] args) {
        Game game = new Game("nathan");
        Affichage affichage = new Affichage(game);
    }
}
