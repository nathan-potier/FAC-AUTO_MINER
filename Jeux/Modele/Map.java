package Modele;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


import javax.swing.Timer;



import Vue.VueGenerale;
import Modele.Machine.*;
import Modele.Resource.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Map {
    private Cell[][] grid;
    private int width, height;
    private Spawn spawn;
    private Market market;


    private Game game;


    private VueGenerale vue;
    

    public Map(int width, int height, Player p, Spawn spawn, Market m, Game game) {
        grid = new Cell[height][width];
        this.width = width;
        this.height = height; 
        this.spawn = spawn;
        this.market = m;
        this.game = game;
        
        

    }
    public Map(Game game, ArrayList<ObjectWithPosition> objList){
        this(game.getWidth(), game.getHeight(), game.getPlayer(), game.getSpawn(), game.getMarket(), game);
        System.out.println(game.getMarket().getPos());
        for(ObjectWithPosition obj : objList){
            grid[obj.getPosition().getY()][obj.getPosition().getX()] = new Cell(obj.getPosition(), obj);
        }
        generateMap(game.getPlayer());
    }

    public void generateMap(Player p) {

        grid[height - 1][width - (width - 2)] = new Cell(new Position( width - (width - 2), height - 1), p);
        grid[market.getPos().getY()][market.getPos().getX()] = new Cell(new Position(market.getPos().getX(), market.getPos().getY()), market);
    }

    public void setVue(VueGenerale v){vue = v;}
    

    public void attendreReplace(Position position, Cell cell, Player p, int temps, boolean isLaborieuse) {
        
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLaborieuse){
                    p.setEcoute(true);
                }

                grid[position.getY()][position.getX()] = cell;
                game.updateAffichage(true);


                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
        
    }

    public ArrayList<Cell> regarderAutour(Position pos) {

        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        ArrayList<Cell> resultats = new ArrayList<>();

        for (Direction direction : directions) {
            Position newPosition = direction.move(pos);
            // Vérification des limites du tableau
            if (posIsValid(newPosition) &&  grid[newPosition.getY()][newPosition.getX()] != null) {
                
                resultats.add(grid[newPosition.getY()][newPosition.getX()]);
            }else{
                resultats.add(null);
            }
        }
        return resultats;
    }
    
    public void ActionByClick(Player player, Position pos){
        ArrayList<Cell> res = regarderAutour(player.getPos());
        for (Cell c : res){
              if ( c != null && c.getPosition().getX() == pos.getX() && c.getPosition().getY() == pos.getY() ){
                if(isInstanceOf(Resource.class, c.getPosition())){
                    //player.collectResource((Ressource)c.getContent());
                    collectRessource(player, c.getPosition());
                }else if (isInstanceOf(Market.class, c.getPosition())){
                    deplacer(player, Direction.getDirection(player.getPos(), c.getPosition()));
                }
            }
        }
    }
    
    public boolean posIsValid(Position position){
        return (position.getX() >= 0 && position.getY() >= 0 && position.getX() < width && position.getY() < height );

    }
    public boolean isSpawn(Position pos){
        return grid[pos.getY()][pos.getX()].equals(grid[Spawn.getPos().getY()][Spawn.getPos().getX()]);
    }
    public boolean cellIsEmpty(Position pos){
        return grid[pos.getY()][pos.getX()] == null;
    }
    public boolean isInstanceOf(Class<?> type, Position pos){
        return type.isInstance(grid[pos.getY()][pos.getX()].getContent());
    }


    public boolean deplacer(Player p, Direction d){
        
        Position nouvelle = d.move(p.getPos());
        if (!posIsValid(nouvelle) || !p.isEcoute()){
            
            return false;
        }
        else if(cellIsEmpty(nouvelle) || isSpawn(nouvelle)){
            Position startPos = p.getPos();
            grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
            grid[p.getPos().getY()][p.getPos().getX()] = null;
            p.setPos(nouvelle);
            verifSpawnAffichage(); 
            game.notifyMove(p, startPos, p.getPos(), Direction.getDirection(startPos, p.getPos()).toString());
            game.updateAffichage(false);

 
            return true;

        }else if (isInstanceOf(Usine.class, nouvelle)) {

            //deplacerOnUsine(p, nouvelle);
            vue.transmettreMarket();
            return true;
        } else if (isInstanceOf(Market.class, nouvelle)) {

            GameStates.setMoveOn(false);
            vue.transmettreMarket();
            return true;
        }


        return false;


    }

    public void collectRessource(Player p, Position nouvelle){

        int temps = 5000;
        
        p.collectResource((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent());
        
        
        Cell cell = grid[nouvelle.getY()][nouvelle.getX()];
        Position pos = new Position(nouvelle.getX(), nouvelle.getY());

        

        Boolean isTemporisée = ((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isTemporisée();
        Boolean isLaborieuse = ((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isLaborieuse();



        grid[nouvelle.getY()][nouvelle.getX()] = new Cell(pos, new Attente(pos, (Resource)cell.getContent()));

        
        game.updateAffichage(true);
        System.out.println((Attente)grid[nouvelle.getY()][nouvelle.getX()].getContent());

  

        if(isTemporisée){
            attendreReplace(pos, cell, p, temps, false);
            
        }
        
        else if (isLaborieuse){
            
            p.setEcoute(false);
            attendreReplace(pos, cell, p, temps, isLaborieuse);
        }
    }


    public void deplacerOnUsine(Player p, Position nouvelle) {
        Usine usine = (Usine) grid[nouvelle.getY()][nouvelle.getX()].getContent();
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Vous êtes sur une usine. Voulez-vous Récolter (R) ou Produire (P) ou voir le Stock (S)? (E) pour sortir");
        String choix = scanner.nextLine().toUpperCase();
    
        if (choix.equals("R")) {
            usine.recuperer();
        } else if (choix.equals("P")) {
            System.out.println("Quelle recette souhaitez-vous produire (Pickaxe ou Bloc de Fer) ?");
            String recetteChoisie = scanner.nextLine();
            usine.produire(recetteChoisie);
        }else if (choix.equals("E")) {
            System.out.println("Vous avez quitté l'usine");
        }else if (choix.equals("S")) {
            usine.afficherStock();
            deplacerOnUsine(p, nouvelle);
        }else {
            System.out.println("Choix invalide.");
        }
    }


    

    public void verifSpawnAffichage(){
        if(grid[Spawn.getPos().getY()][Spawn.getPos().getX()] == null){
            grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell(Spawn.getPos(), spawn);
        }
    }

    public void generateRessource(Player p){

        Random rand = new Random();
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);

        Position posWood1 = new Position(2, 1);
        Wood wood = new Wood(1, posWood1);
        grid[posWood1.getY()][posWood1.getX()] = new Cell(posWood1, wood);

        Position posIron1 = new Position(6, 3);
        Iron iron = new Iron(1, posIron1);
        grid[posIron1.getY()][posIron1.getX()] = new Cell(posIron1, iron);


        Position posIron2 = new Position(1, 6);
        Iron iron2 = new Iron(1, posIron2);
        grid[posIron2.getY()][posIron2.getX()] = new Cell(posIron2, iron2);

        // Placer une usine à côté de la position de `Wood`
        Position posUsine = new Position(posWood1.getX() + 1, posWood1.getY()); // Position adjacente
        Usine usine = new Usine(p.getInv(), posUsine);
        grid[posUsine.getY()][posUsine.getX()] = new Cell(posUsine, usine);

        // Placer une récolteuse à côté de la position de `Iron`
        // Position posRecolteuse = new Position(posIron1.getX() + 1, posIron1.getY()); // Position adjacente
        // Recolteuse recolteuse = new Recolteuse(posRecolteuse, 10, p.getInv(), iron);  // 10 est la capacité du stock max
        // grid[posRecolteuse.getY()][posRecolteuse.getX()] = new Cell(posRecolteuse, recolteuse);

    }


    public Cell[][] getGrid(){return grid;}
    public void setGrid(Cell[][] ngrid){grid = ngrid;}

    public boolean deplacerVersPosition(Player p, Position cible) {
        if (!posIsValid(cible) || 
            (grid[cible.getY()][cible.getX()] != null && !(grid[cible.getY()][cible.getX()].getContent() instanceof Spawn))) {
            System.out.println("Position cible invalide ou occupée.");
            return false;
        }
    
        boolean[][] visited = new boolean[height][width];
        Position[][] prev = new Position[height][width];
        Queue<Position> queue = new LinkedList<>();
        queue.add(p.getPos());
        visited[p.getPos().getY()][p.getPos().getX()] = true;
    
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    
        // BFS pour trouver un chemin
        while (!queue.isEmpty()) {
            Position current = queue.poll();
    
            if (current.equals(cible)) {
                break;
            }
    
            for (Direction d : directions) {
                Position next = d.move(current);
                if (posIsValid(next) && !visited[next.getY()][next.getX()] &&
                    (grid[next.getY()][next.getX()] == null || grid[next.getY()][next.getX()].getContent() instanceof Spawn)) {
                    queue.add(next);
                    visited[next.getY()][next.getX()] = true;
                    prev[next.getY()][next.getX()] = current;
                }
            }
        }
    
        // Vérifier si un chemin a été trouvé
        if (prev[cible.getY()][cible.getX()] == null) {
            System.out.println("Pas de chemin disponible.");
            return false;
        }
    
        // Reconstruire le chemin sans inclure la position de départ
        LinkedList<Position> chemin = new LinkedList<>();
        for (Position at = cible; prev[at.getY()][at.getX()] != null; at = prev[at.getY()][at.getX()]) {
            chemin.addFirst(at);
        }
        
        // Créer un Timer pour déplacer le joueur progressivement
        int delay = 80; // Temps de pause entre les déplacements (en millisecondes)
        Timer timer = new Timer(delay, null);
        AtomicInteger index = new AtomicInteger(0); // Pour gérer la position actuelle dans le chemin

        // Action à exécuter à chaque tick du Timer
        timer.addActionListener(e -> {
            int currentIndex = index.getAndIncrement();
            if (currentIndex < chemin.size()) {
                Position nextPos = chemin.get(currentIndex);
                Direction direction = Direction.getDirection(p.getPos(), nextPos);

                if (direction == null) {
                    System.out.println("Direction invalide.");
                    timer.stop(); // Arrêter le timer si une direction invalide est trouvée
                    return;
                }

                // Appeler la fonction de déplacement existante
                if (!deplacer(p, direction)) {
                    System.out.println("Impossible de continuer à cause d'un obstacle.");
                    timer.stop(); // Arrêter le timer si un obstacle est rencontré
                    return;
                }



            } else {
                timer.stop();  // Arrêter le timer une fois que le joueur a atteint la cible
            }
        });

        timer.start();  // Démarrer le Timer pour exécuter les déplacements progressivement

        return true;
    }

    public ArrayList<ObjectWithPosition> getPositionObject() {
        ArrayList<ObjectWithPosition> res = new ArrayList<>();
        for (Cell[] row : grid) {  // Parcours des lignes de la grille
            for (Cell c2 : row) {   // Parcours des cellules dans chaque ligne
                if (c2 != null && c2.getContent() instanceof ObjectWithPosition) {
                    // Vérifier si le contenu de la cellule implémente ObjectWithPosition
                    ObjectWithPosition obj = (ObjectWithPosition) c2.getContent();
                    res.add(obj);
                }
            }
        }
        return res;  // Retourner la liste des objets ayant une position
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Parcours de toutes les cases de la carte
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = grid[y][x];
                if (cell != null && cell.getContent() != null) {
                    sb.append(cell.getContent().getClass().getSimpleName().charAt(0)); // Utilise la première lettre du nom de la classe de l'objet contenu
                } else {
                    sb.append("."); // Si la case est vide
                }
                sb.append(" "); // Séparation des cases
            }
            sb.append("\n"); // Nouvelle ligne à la fin de chaque ligne de la carte
        }
        
        return sb.toString();
    }

}
