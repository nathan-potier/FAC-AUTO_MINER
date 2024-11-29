package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestBouton {
    public static void main(String[] args) {
        // Créer une fenêtre (JFrame)
        JFrame frame = new JFrame("Test Boutons Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null); // Positionnement libre des composants

        // Créer un bouton 1 avec une action spécifique
        Bouton2 bouton1 = new Bouton2("../assets/Default.png", 50, 50, 150, 100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bouton 1 cliqué : Action spécifique 1");
            }
        });

        // Créer un bouton 2 avec une autre action spécifique
        Bouton2 bouton2 = new Bouton2("assets/Wood.png", 200, 50, 100, 100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bouton 2 cliqué : Action spécifique 2");
            }
        });

        // Créer un bouton 3 avec une autre action spécifique
        Bouton2 bouton3 = new Bouton2("../assets/Market.png", 350, 50, 100, 100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bouton 3 cliqué : Action spécifique 3");
            }
        });

        // Ajouter les boutons à la fenêtre
        frame.add(bouton1);
        frame.add(bouton2);
        frame.add(bouton3);

        // Afficher la fenêtre
        frame.setVisible(true);
    }
}
