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
import Modele.Machine.Machine;
import Modele.Resource.*;


public class GameSaver {
    private static final String SAVE_DIRECTORY = "Save";
    private static final String SAVE_LIST_FILE = SAVE_DIRECTORY + File.separator + "nom_save.txt";

    public static boolean saveGame(String fileName, Player player, Market market, ArrayList<Savable> savables) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_DIRECTORY + "/" + fileName + ".txt"))) {
            // Sauvegarder les informations du joueur
            writer.write("Joueur: " + player.getPseudo());
            writer.newLine();
            writer.write("Position: " + Spawn.getPos().getX() + "," + Spawn.getPos().getY());
            writer.newLine();
            writer.write("Argent: " + player.getMoney());
            writer.newLine();

            // Sauvegarder les ressources dynamiquement
            writer.write("Ressources du joueur:");
            writer.newLine();
            for (Resource r : player.getInv().getResources()) {
                writer.write(r.getName() + ":" + r.getQuantity());
                writer.newLine();
            }

            // Sauvegarder les machines du joueur avec leur position
            writer.write("Machines du joueur:");
            writer.newLine();
            Savable machine = player.getInv().getMachine();
            if (machine != null){
                writer.write(machine.saveAsString());
                writer.newLine();
            }

            writer.write("Objets sur la carte:");
            writer.newLine();

            // Sauvegarder les savables (machines, objets, etc.) dynamiquement
            for (Savable savable : savables) {
                writer.write(savable.saveAsString());
                writer.newLine();
            }

            System.out.println("Partie sauvegardee dans " + fileName);
            updateSaveList(fileName);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateUniqueSaveName(String baseName) {
        List<String> existingSaves = readExistingSaves();
        int maxNumber = 0;

        for (String save : existingSaves) {
            if (save.startsWith(baseName)) {
                try {
                    String numberPart = save.substring(baseName.length() + 1);
                    int number = Integer.parseInt(numberPart);
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    System.err.println("Erreur lors de l'extraction du numero de sauvegarde : " + save);
                }
            }
        }

        return baseName + "_" + (maxNumber + 1);
    }

    private static List<String> readExistingSaves() {
        List<String> saves = new ArrayList<>();
        try {
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
            File saveListFile = new File(SAVE_LIST_FILE);
            if (!saveListFile.exists()) {
                saveListFile.getParentFile().mkdirs();
                saveListFile.createNewFile();
            }

            List<String> existingSaves = Files.readAllLines(Paths.get(SAVE_LIST_FILE));

            if (!existingSaves.contains(fileName)) {
                try (FileWriter saveListWriter = new FileWriter(SAVE_LIST_FILE, true)) {
                    saveListWriter.write(fileName + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la mise a jour du fichier des noms de sauvegarde : " + SAVE_LIST_FILE);
            e.printStackTrace();
        }
    }
}

