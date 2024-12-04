package Modele.Enregistrer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Modele.*;
import Modele.Machine.*;
import Modele.Resource.*;

public class GameSaver {

    public static boolean saveGame(String fileName, Player player, Market market, ArrayList<ObjectWithPosition> objectsOnMap, ArrayList<Machine> machines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Sauvegarder les informations du joueur
            writer.write("Joueur: " + player.getPseudo());
            writer.newLine();
            writer.write("Position: " + Spawn.getPos().getX() + "," + Spawn.getPos().getY());
            writer.newLine();
            writer.write("Argent: " + player.getMoney());
            writer.newLine();
            
            // Sauvegarder les ressources du joueur
            writer.write("Ressources du joueur:");
            writer.newLine();
            for (Resource r : player.getInv().getResources()) {
                writer.write(r.getName() + ":" + r.getQuantity());
                writer.newLine();
            }

            // Sauvegarder les machines du joueur avec leur position
            writer.write("Machines du joueur:");
            writer.newLine();
            Machine machine = player.getInv().getMachine();
            if (machine != null){
                writer.write(machine.getName() + " à la position " + machine.getPosition().getX() + "," + machine.getPosition().getY());
                writer.newLine();
            }

            // Sauvegarder les ressources disponibles dans le marché avec leur position
            writer.write("Marché position:");
            writer.newLine();
            writer.write("("+market.getPos().getX() + ";"+ market.getPos().getY() +")");
            writer.newLine();
            writer.write("Marché - Ressources à vendre:");
            writer.newLine();


            for (Resource r : market.getResourcesForSale()) {
                writer.write(r.getName() + ":" + r.getQuantity() + " pour " + r.getPrix() + " coins");
                writer.newLine();
            }

            // Sauvegarder les machines disponibles dans le marché avec leur position
            writer.write("Marché - Machines à vendre:");
            writer.newLine();
            for (Machine m : market.getMachinesForSale()) {
                writer.write(m.getName() + " option-> " + m.getOption() );
                writer.newLine();
            }

            // Sauvegarder la position de tous les objets sur la carte
            writer.write("Objets sur la carte:");
            writer.newLine();
            for (ObjectWithPosition obj : objectsOnMap) {

                writer.write(obj.getName() + " à la position " + obj.getPosition().getX() + "," + obj.getPosition().getY());
                writer.newLine();
            }

            writer.write("Machines sur la carte:");
            writer.newLine();
            for (Machine m : machines){
                writer.write(m.getName() + " option-> "+ m.getOption() + " à la position " + m.getPosition().getX() + "," + m.getPosition().getY() + " inventaire-> {" + m.getStockSTR() +"}");
                writer.newLine();
            }

            System.out.println("Partie sauvegardée dans " + fileName);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
