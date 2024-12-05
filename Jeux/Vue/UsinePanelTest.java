package Vue;

import Modele.Player;
import Modele.Position;
import Modele.Machine.Usine;
import Modele.Resource.Iron;
import Modele.Resource.Wood;

import javax.swing.*;

public class UsinePanelTest {
    public static void main(String[] args) {
        // Créer un inventaire et un joueur
        Player player = new Player("TestPlayer", new Position(0, 0));

        // Ajouter des ressources au joueur
        player.collectResource(new Wood(100));
        player.collectResource(new Iron(100));

    


        

        // Créer une usine

        Usine usine = new Usine(player.getInv(), null, "", null);


        // Créer une fenêtre JFrame
        JFrame frame = new JFrame("Test Usine Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Créer un UsinePanel et l'ajouter à la fenêtre
        UsinePanel usinePanel = new UsinePanel(usine, player);
        frame.add(usinePanel);  

        // Centrer la fenêtre sur l'écran
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
