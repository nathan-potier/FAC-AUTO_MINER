package Modele.Enregistrer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Modele.*;
import Modele.Machine.*;
import Modele.Resource.*;

public class GameSaver {
    private static final String SAVE_DIRECTORY = "Save";
    private static final String SAVE_LIST_FILE = SAVE_DIRECTORY + File.separator + "nom_save.txt";

    public static boolean saveGame(String fileName, Player player, Market market, ArrayList<ObjectWithPosition> objectsOnMap, ArrayList<Machine> machines) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_DIRECTORY+"/"+fileName))) {
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
                writer.write(m.getName() + " option-> "+ m.getOption() + " à la position " + m.getPosition().getX() + "," + m.getPosition().getY() 
                + " Stock-> {" + m.getStockSTR() +"}" 
                + " Inventaire-> "+ m.getResourcesSTR());
                writer.newLine();
            }




            System.out.println("Partie sauvegardée dans " + fileName);
            updateSaveList(fileName);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String generateUniqueSaveName(String baseName) {
        // Lire le fichier contenant les noms de sauvegarde existants
        List<String> existingSaves = readExistingSaves();

        int maxNumber = 0;

        // Vérifier les noms existants et extraire les numéros
        for (String save : existingSaves) {
            if (save.startsWith(baseName)) {
                try {
                    // Extraire le numéro à la fin du nom (Monde_numéro)
                    String numberPart = save.substring(baseName.length()+1);
                    int number = Integer.parseInt(numberPart);
                      maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    System.err.println("Erreur lors de l'extraction du numéro de sauvegarde : " + save);
                }
            }
        }

        // Incrémenter le numéro pour obtenir un nouveau nom unique
        int newNumber = maxNumber + 1;
        return baseName + "_" + newNumber;
    }

    private static List<String> readExistingSaves() {
        List<String> saves = new ArrayList<>();
        try {
            // Lire toutes les lignes du fichier
            if (Files.exists(Paths.get(SAVE_LIST_FILE))) {
                saves = Files.readAllLines(Paths.get(SAVE_LIST_FILE));
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier des sauvegardes : " + SAVE_LIST_FILE);
            e.printStackTrace();
        }
        return saves;
    }
    private static void updateSaveList(String fileName) {
        try {
            // Vérifier si le fichier existe, sinon le créer
            File saveListFile = new File(SAVE_LIST_FILE);
            if (!saveListFile.exists()) {
                saveListFile.getParentFile().mkdirs(); // Créer le dossier si nécessaire
                saveListFile.createNewFile();
            }

            // Lire les noms existants
            List<String> existingSaves = Files.readAllLines(Paths.get(SAVE_LIST_FILE));
            
            // Vérifier si le nom est déjà présent
            if (!existingSaves.contains(fileName)) {
                // Ajouter le nom au fichier si non présent
                try (FileWriter saveListWriter = new FileWriter(SAVE_LIST_FILE, true)) { // Mode append
                    saveListWriter.write(fileName + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la mise à jour du fichier des noms de sauvegarde : " + SAVE_LIST_FILE);
            e.printStackTrace();
        }
    }
}
