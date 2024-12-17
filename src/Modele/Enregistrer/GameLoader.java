package Modele.Enregistrer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

import Modele.*;
import Modele.Machine.*;
import Modele.Resource.*;

public class GameLoader {
    private static Player player = null;

    private static ArrayList<Savable<?>> objectOnMap = new ArrayList<>();
    private static Market market = null;


    public static Game loadGame(String fileName, Game game) {
        fileName = "Save/" + fileName + ".txt";

        Carte map = null;


        // Variables temporaires
        String pseudo = "";
        int money = 0;
        Position playerPosition = null;

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
                    resources.add((Resource)createEntity(resourceName, resourceQuantity, null));
                }
            }
            player = new Player(pseudo, money, playerPosition, new Inventory(resources));
            
        }
        
        String machineSection = readSection("Machines du joueur:", fileName).split("\n")[0].trim();
        Machine machineJoueur = null;
        if (isMachine(machineSection)){
            machineJoueur = lireMachine(machineSection);
        }




        
        // Lecture des objets sur la carte
        String objectsSection = readSection("Objets sur la carte:", fileName);
        if (!objectsSection.isEmpty()) {
            for (String objectLine : objectsSection.split("\n")) {
                try{
                    
                    
                    if (isMachine(objectLine)) {
                        
                       
                        Machine machine = lireMachine(objectLine );
                        objectOnMap.add(machine); // Ajout à la liste des machines sur la carte
    
                     
                    }else if(isMarket(objectLine)){
                        market = lireMarket(objectLine);
                        
                    }else{
                        lireResource(objectLine);
                        
                        
                    }
                }catch (Exception e) {
                    System.err.println("Erreur lors de la lecture de : " + objectLine);
                }

            }
        }





        // Initialiser le jeu
        if (machineJoueur != null){
            player.addMachine(machineJoueur);
        }

        game = new Game(market, game.getSpawn(), player);
        

        map = new Carte(game, objectOnMap);
        

        for (Savable<?> m : objectOnMap){
            if (m instanceof Machine){
                ((Machine)m).setMap(map);
            }

        }

        game.setMap(map);

        return game;

    }

    private static boolean isMachine(String objectLine){
        return objectLine.contains("option->");
    }
    private static boolean isMarket(String objectLine){
        return objectLine.contains("Market");
    }

    private static Machine lireMachine(String objectLine ){

        // Extraction du type de machine (e.g., "Usine")
        String machineType = objectLine.split("option->")[0].trim();

        // Extraction des données après "option->"
        String machineData = objectLine.split("option->")[1].trim();

        // Extraction des parties option, position, stock, inventaire et recette
        String[] machineParts = machineData.split("a la position");
        String option = machineParts[0].trim();

        String[] positionStockInventoryRecipe = machineParts[1].split("Stock->");

        // Position
        String[] positionParts = positionStockInventoryRecipe[0].trim().split(",");
        int x = Integer.parseInt(positionParts[0].trim());
        int y = Integer.parseInt(positionParts[1].trim());
        Position position = new Position(x, y);

        // Stock
        ArrayList<Resource> stock = new ArrayList<>();
        ArrayList<Resource> inventory = new ArrayList<>();
        String recipes = null;

        //lecture du stock
        if (positionStockInventoryRecipe.length > 1) {
            String[] stockAndInventoryRecipe = positionStockInventoryRecipe[1].split("Inventaire->");

            if (stockAndInventoryRecipe.length > 0) {
                    
                String stockData = stockAndInventoryRecipe[0].replace("{", "").replace("}", "").trim();
                    
                if (!stockData.isEmpty()) {
                   
                    String[] stockItems = stockData.split(",");
                    for (String item : stockItems) {
                        
                        String[] parts = item.replace("(", "").replace(")", "").split(";");
                        String resourceName = parts[0].trim();
                        int quantity = Integer.parseInt(parts[1].trim());
                        Resource resource = (Resource) createEntity(resourceName, quantity, null);
                        stock.add(resource);
                    }
                }
            }

            //lecture de l'inventaire
            if (stockAndInventoryRecipe.length > 1) {
                
                String[] inventoryAndRecipe = stockAndInventoryRecipe[1].split("Recette->");
                String inventoryData = inventoryAndRecipe[0].replace("{", "").replace("}", "").trim();

                if (!inventoryData.isEmpty()) {
                    String[] inventoryItems = inventoryData.split(",");
                    for (String item : inventoryItems) {
                        
                        String[] parts = item.replace("(", "").replace(")", "").split(";");
                        String resourceName = parts[0].trim();
                        int quantity = Integer.parseInt(parts[1].trim());
                        Resource resource = (Resource) createEntity(resourceName, quantity, null);
                        
                        inventory.add(resource);
                    }
                }
                
                //lecture de la recette
                if (inventoryAndRecipe.length > 1) {
                    String recipeData = inventoryAndRecipe[1].trim();
                    
                    recipes = recipeData;
                }
            }
        }
        // Création de l'objet machine avec stock, inventaire et recettes
        Machine machine = (Machine) createEntity(
                machineType, 
                new Inventory(inventory), // args[0] -> Inventory (inventory)
                position,        // args[1] -> Position
                option,          // args[2] -> String (option)
                new Inventory(stock),    // args[3] -> Inventory (stock)
                player,     // args[4] -> Player
                recipes          // args[5] -> String (recette, nullable)
        );

        

        return machine;
    }
    
    private static void lireResource(String objectLine){
        String[] objectData = objectLine.split(" a la position ");
        if (objectData.length == 2) {
            String objectName = objectData[0].trim();
            String[] positionData = objectData[1].split(",");
            Position objectPosition = new Position(
                Integer.parseInt(positionData[0].trim()),
                Integer.parseInt(positionData[1].trim())
            );
            Savable<?> object = (Savable<?>) createEntity(
                objectName,     // args[0] -> String (nom de l'objet)
                1,
                objectPosition  // args[1] -> Position (position de l'objet)
            );
            if (object != null) {
                objectOnMap.add(object);
            }
        }
    }

    private static Market lireMarket(String objectLine){
        Position marketPosition = null;
        // Extraction de la position du marché
        try{


        objectLine = objectLine.split("Market ")[1].trim();

        String[] positionData = objectLine.substring(1, objectLine.indexOf(")")).split(";");
        int x = Integer.parseInt(positionData[0].trim());
        int y = Integer.parseInt(positionData[1].trim());
        marketPosition = new Position(x, y);
        }catch(StringIndexOutOfBoundsException | NumberFormatException e){
            e.printStackTrace();
            System.err.println("Erreur lors de la lecture de la position du marché : " + objectLine);
        }
        return new Market(marketPosition);
    }
    private static final Map<String, Function<Object[], Object>> ENTITY_CREATORS = Map.of(
        // Ressources
        "Wood", args -> new Wood((int) args[0], (Position) args[1]),
        "Iron", args -> new Iron((int) args[0], (Position) args[1]),
        "Pickaxe", args -> new Pickaxe((int) args[0], (Position) args[1]),
        "BlocDeFer", args -> new BlocDeFer((int) args[0], (Position) args[1]),

        // Machines
        "Usine", args -> {
            Usine usine = new Usine((Inventory) args[0], (Position) args[1], (String) args[2], (Inventory) args[3], (Player) args[4]);
            if (args[5] != null) {
                usine.setRecette((String) args[5]);
            }
            return usine;
        },
        "Recolteuse", args -> {
            Recolteuse recolteuse = new Recolteuse((Position) args[1], (String) args[2], (Inventory) args[3], (Player) args[4]);
            if (args[5] != null) {
                recolteuse.setTypeRessource((String) args[5]);
            }
            return recolteuse;
        },

        // Objets spécifiques
        "Attente", args -> new Attente((Position) args[1], null)
    );

    public static Object createEntity(String name, Object... args) {
        Function<Object[], Object> creator = ENTITY_CREATORS.get(name);
        return creator != null ? creator.apply(args) : null;
    }


    public static String readSection(String startKeyword, String fileName)  {
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
