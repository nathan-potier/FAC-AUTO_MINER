package Modele.Enregistrer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Modele.*;
import Modele.Machine.*;
import Modele.Resource.*;

public class GameLoader {

    public static Game loadGame(String fileName, Game game) {


        String line;
        Player player = null;
        Market market = null;
        Map map = null;
        ArrayList<ObjectWithPosition> objectsOnMap = new ArrayList<>(); // Utilisation d'une ArrayList pour stocker les objets

        // Variables temporaires
        String pseudo = "";
        int money = 0;
        Position playerPosition = null;
        Position marketPosition = null; // Position du marché

        ArrayList<Resource> marketResources = new ArrayList<>();
        ArrayList<Machine> marketMachines = new ArrayList<>();

        // Lecture du joueur
        String playerSection = readSection( "Joueur:", fileName);
        if (!playerSection.isEmpty()) {

            String[] lines = playerSection.split("\n");
            for (String playerLine : lines) {
                if (playerLine.startsWith("Joueur:")) {
                    pseudo = playerLine.split(":")[1].trim();
                } else if (playerLine.startsWith("Position:")) {
                    String[] coords = playerLine.split(":")[1].trim().split(",");
                    playerPosition = new Position(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));
                } else if (playerLine.startsWith("Argent:")) {
                    money = Integer.parseInt(playerLine.split(":")[1].trim());
                }
            }
        }

        // Lecture des ressources du joueur
        String resourcesSection = readSection("Ressources du joueur:", fileName);

        if (!resourcesSection.isEmpty()) {
            ArrayList<Resource> resources = new ArrayList<>();

            for (String resourceLine : resourcesSection.split("\n")) {
                if (resourceLine.equals("Machines du joueur:")){
                    break;
                }
                String[] resourceData = resourceLine.split(":");
                if (resourceData.length == 2) {
                    
                    String resourceName = resourceData[0].trim();
                    int resourceQuantity = Integer.parseInt(resourceData[1].trim());
                    resources.add(createResource(resourceName, resourceQuantity));
                }
            }
            player = new Player(pseudo, money, playerPosition, new Inventory(resources));

        }

        // Lecture du marché
        String marketSection = readSection("Marché position:", fileName);

        if (!marketSection.isEmpty()) { 
            String[] marketLines = marketSection.split("\n");


            for (String marketLine : marketLines) {

                // Extraction de la position du marché
                if (marketLine.trim().startsWith("(")) {

                    try {
                        String[] positionData = marketLine.substring(1, marketLine.indexOf(")")).split(";");
                        int x = Integer.parseInt(positionData[0].trim());
                        int y = Integer.parseInt(positionData[1].trim());
                        marketPosition = new Position(x, y);
                        
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la lecture de la position du marché : " + marketLine);
                    }
                } else if (marketLine.startsWith("Marché - Ressources à vendre:")) {
                    try{
  
                        String resourcesData = readSection("Marché - Ressources à vendre:",fileName);
                        for (String resourceLine : resourcesData.split("\n")) {
                            if (resourceLine.startsWith("Marché - Machines à vendre:")){
                                break;
                            }
                            String[] resourceData = resourceLine.split(":");
                            String resourceName = resourceData[0].trim();

                            int quantity = Integer.parseInt(resourceData[1].split(" pour ")[0].trim());
                            marketResources.add(createResource(resourceName, quantity));
                        }
                    }catch(Exception e){
                        System.err.println("Erreur lors de la lecture des Ressources à vendre du marché : " + marketLine);
                    }   
                } else if (marketLine.startsWith("Marché - Machines à vendre:")) {
                    try {
                        String machinesData = readSection("Marché - Machines à vendre:", fileName);
                        for (String machineLine : machinesData.split("\n")) {
                            String machineName = machineLine.split(" pour ")[0].trim();
                            marketMachines.add(createMachine(machineName));
                        }
                    }catch(Exception e){
                        System.err.println("Erreur lors de la lecture des Machines à vendre du marché : " + marketLine);
                    }
                }
            }

            market = new Market(marketPosition, marketResources, marketMachines);

        }

        // Lecture des objets sur la carte
        String objectsSection = readSection("Objets sur la carte:", fileName);
        if (!objectsSection.isEmpty()) {
            for (String objectLine : objectsSection.split("\n")) {
                String[] objectData = objectLine.split(" à la position ");
                if (objectData.length == 2) {
                    String objectName = objectData[0].trim();
                    String[] positionData = objectData[1].split(",");
                    Position objectPosition = new Position(
                        Integer.parseInt(positionData[0].trim()),
                        Integer.parseInt(positionData[1].trim())
                    );
                    ObjectWithPosition object = createObject(objectName, objectPosition);
                    if (object != null) {
                        objectsOnMap.add(object);
                    }
                }
            }
        }

        // Initialiser le jeu
        game = new Game(market, game.getSpawn(), player);
        map = new Map(game, objectsOnMap);
        System.out.println(map);
        game.setMap(map);

        return game;

    }
    

    // Méthode pour créer une ressource en fonction du nom
    private static Resource createResource(String name, int quantity) {
        switch (name) {
            case "Wood":
                return new Wood(quantity);  // Utilisation de la sous-classe existante
            case "Iron":
                return new Iron(quantity);  // Utilisation de la sous-classe existante
            default:
                return null;
        }
    }

    private static Machine createMachine(String name) {
        switch (name) {
            case "Usine":
                return new Usine(null, null);  // Utilisation de la sous-classe existante
            default:
                return null;
        }
    }

    private static ObjectWithPosition createObject(String name, Position pos) {
        switch (name) {
            case "Wood":
                return new Wood(1, pos);  // Utilisation de la sous-classe existante
            case "Iron":
                return new Iron(1, pos);  // Utilisation de la sous-classe existante
            case "Usine":
                return new Usine(null, pos);  // Utilisation de la sous-classe existante
            default:
                return null;
        }
    }

    private static String readSection(String startKeyword, String fileName)  {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sectionContent = new StringBuilder();
            String line;
        
            // Chercher le début de la section
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(startKeyword)) {
                    // Ajoute les lignes suivantes tant qu'elles font partie de la section
                    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                        sectionContent.append(line).append("\n");
                    }
                    break; // On sort une fois la section terminée
                }
            }
        
            return sectionContent.toString().trim();

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
