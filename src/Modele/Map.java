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
    public Map(Game game, ArrayList<ObjectWithPosition> objList, ArrayList<Machine> machinesListe){
        this(game.getWidth(), game.getHeight(), game.getPlayer(), game.getSpawn(), game.getMarket(), game);

        for(ObjectWithPosition obj : objList){
            grid[obj.getPosition().getY()][obj.getPosition().getX()] = new Cell(obj.getPosition(), obj);
        }
        for(Machine m : machinesListe){
            grid[m.getPosition().getY()][m.getPosition().getX()] = new Cell(m.getPosition(), m);
        }
        
        generateMap(game.getPlayer());
        
        
    }

    public void generateMap(Player p) {

        grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell(new Position( Spawn.getPos().getX(), Spawn.getPos().getY()), p);
        if (market.getPos() == null){
            Position posMarket = genRandomPosForMarket();
            while(!isValidPosition(posMarket)){
                posMarket = genRandomPosForMarket();
            }
            market.setPos(posMarket);
            grid[posMarket.getY()][posMarket.getX()] = new Cell(posMarket, market);
        }else{
            grid[market.getPos().getY()][market.getPos().getX()] = new Cell(new Position(market.getPos().getX(), market.getPos().getY()), market);
        }

        
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
    
    public ArrayList<Position> posAutour4(Position pos) {
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        ArrayList<Position> positions = new ArrayList<>();

        for (Direction direction : directions) {
            Position newPosition = direction.move(pos);
            if (posIsValid(newPosition)) { // Vérification des limites du tableau
                positions.add(newPosition);
            }
        }
        return positions;
    }

    
    public ArrayList<Cell> regarderAutour(Position pos) {
        ArrayList<Cell> resultats = new ArrayList<>();
        ArrayList<Position> positionsAutour = posAutour4(pos);

        for (Position newPosition : positionsAutour) {
            // Ajouter la cellule si elle existe, sinon null
            if (grid[newPosition.getY()][newPosition.getX()] != null) {
                resultats.add(grid[newPosition.getY()][newPosition.getX()]);
            } else {
                resultats.add(null);
            }
        }

        return resultats;
    }

    
    public void ActionByClick(Player player, Position pos){
        ArrayList<Cell> res = regarderAutour(player.getPos());
        for (Cell c : res){
              if ( c != null && c.getPosition().getX() == pos.getX() && c.getPosition().getY() == pos.getY() ){
                if(isInstanceOf(Resource.class, c.getPosition())  ){
                    //player.collectResource((Ressource)c.getContent());
                    collectRessource(player, c.getPosition());
                }else if (isInstanceOf(Market.class, c.getPosition()) || isInstanceOf(Machine.class, c.getPosition())){
                    deplacer(player, Direction.getDirection(player.getPos(), c.getPosition()));
                }
            }
        }
    }
    
    public boolean posIsValid(Position position){
        return (position.getX() >= 0 && position.getY() >= 0 && position.getX() < width && position.getY() < height );

    }
    public boolean isSpawn(Position pos){
        return pos.getX() == Spawn.getPos().getX() && pos.getY() == Spawn.getPos().getY();
    }
    public boolean cellIsEmpty(Position pos){
        return grid[pos.getY()][pos.getX()] == null;
    }
    public boolean isInstanceOf(Class<?> type, Position pos){
        return type.isInstance(grid[pos.getY()][pos.getX()].getContent());
    }

    public void deplacerDun(Position nouvelle, Player p){
        Position startPos = p.getPos();
        grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
        grid[p.getPos().getY()][p.getPos().getX()] = null;
        p.setPos(nouvelle);
        verifSpawnAffichage(); 
        game.notifyMove(p, startPos, p.getPos(), Direction.getDirection(startPos, p.getPos()).toString());
        game.updateAffichage(false);
    }

    public boolean deplacer(Player p, Direction d){
        
        Position nouvelle = d.move(p.getPos());
        if (!posIsValid(nouvelle) || !p.isEcoute()){
            
            return false;
        }
        else if(cellIsEmpty(nouvelle) || isSpawn(nouvelle)){
            deplacerDun(nouvelle, p);
 
            return true;

        }else if (isInstanceOf(Resource.class, nouvelle)) {
            collectRessource(p, nouvelle);
           return true;
        }else if (isInstanceOf(Usine.class, nouvelle)) {

            //deplacerOnUsine(p, nouvelle);
            vue.transmettreUsine((Usine)grid[nouvelle.getY()][nouvelle.getX()].getContent());
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
        
        if ( !(p.collectResource((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent())) ){
            return;
        }
        
        
        Cell cell = grid[nouvelle.getY()][nouvelle.getX()];
        Position pos = new Position(nouvelle.getX(), nouvelle.getY());

        

        Boolean isTemporisee = ((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isTemporisee();
        Boolean isLaborieuse = ((Resource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isLaborieuse();



        grid[nouvelle.getY()][nouvelle.getX()] = new Cell(pos, new Attente(pos, (Resource)cell.getContent()));

        
        game.updateAffichage(true);
        System.out.println((Attente)grid[nouvelle.getY()][nouvelle.getX()].getContent());

  

        if(isTemporisee){
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
    //temporaire
    private void genNObjects(int n, String type) {
        for (int i = 0; i < n; i++) {
            // Générer une position valide
            Position pos = genRandomPosValid();
    
            // Vérifie si une position valide est trouvée
            if (pos == null) {
                System.out.println("Impossible de placer plus d'objets, espace insuffisant.");
                break;
            }
    
            // Crée l'objet en fonction du type spécifié
            Object obj = null;
            switch (type.toLowerCase()) {
                case "wood":
                    obj = new Wood(1, pos);
                    break;
                case "iron":
                    obj = new Iron(1, pos);
                    break;
                // Ajouter d'autres types d'objets ici si nécessaire
                default:
                    System.out.println("Type inconnu : " + type);
                    continue;
            }
    
            // Place l'objet dans la grille
            grid[pos.getY()][pos.getX()] = new Cell(pos, obj);
        }
    }
    


    public void generateRessource(Player p){
        // Placer une usine à côté de la position de `Wood`
        Position posUsine = new Position(3, 1); // Position adjacente
        Usine usine = new Usine(new Inventory(), posUsine, "XL", new Inventory(), game.getPlayer());
        grid[posUsine.getY()][posUsine.getX()] = new Cell(posUsine, usine);

        genNObjects(50, "Wood");
        genNObjects(3, "Iron");

        // Placer une récolteuse à côté de la position de `Iron`
        // Position posRecolteuse = new Position(posIron1.getX() + 1, posIron1.getY()); // Position adjacente
        // Recolteuse recolteuse = new Recolteuse(posRecolteuse, 10, p.getInv(), iron);  // 10 est la capacité du stock max
        // grid[posRecolteuse.getY()][posRecolteuse.getX()] = new Cell(posRecolteuse, recolteuse);

    }

    private Position genRandomPos(){
        Random rand = new Random();
        int aleatoireX = rand.nextInt(width);
        int aleatoireY = rand.nextInt(height);

        Position pos= new Position(aleatoireX, aleatoireY);
        int limiteExecution = 50;
        int execution = 0;
        while (isSpawn(pos) || grid[pos.getY()][pos.getX()] != null) {
            if (limiteExecution <= execution){return null;}
            pos = genRandomPos();
            execution++;
        }
        return pos;
    }

    private Position genRandomPosForMarket(){
        Random rand = new Random();
        int aleatoireX = (rand.nextInt(2) == 0) ? 0 : width - 1;
        int aleatoireY = rand.nextInt(height);

        Position pos= new Position(aleatoireX, aleatoireY);
        int limiteExecution = 50;
        int execution = 0;
        while (isSpawn(pos) || grid[pos.getY()][pos.getX()] != null) {
            if (limiteExecution <= execution){return null;}
            pos = genRandomPos();
            execution++;
        }
        return pos;
    }

    /**
     * Cette méthode retourne toutes les positions valide autour d'une position donnée.
     *
     * @param pos La position de départ à partir de laquelle les positions autours seront calculées.
     * @return Une liste de toutes les positions valides autour de la position donnée.
     */
    public ArrayList<Position> posAutour8(Position pos){
        ArrayList<Position> posAutour = posAutour4(pos);
        // Calcul des positions diagonales
        Position[] diagonales = {
            new Position(pos.getX() - 1, pos.getY() - 1), // Haut gauche
            new Position(pos.getX() + 1, pos.getY() - 1), // Haut droit
            new Position(pos.getX() - 1, pos.getY() + 1), // Bas gauche
            new Position(pos.getX() + 1, pos.getY() + 1)  // Bas droit
        };

        // Vérification si les positions diagonales sont valides
        for (Position p : diagonales) {
            if (posIsValid(p)) {
                posAutour.add(p);
            }
        }
        return posAutour;
    }

    private boolean isValidPosition(Position pos) {
        ArrayList<Position> posAutour = posAutour8(pos);
        grid[pos.getY()][pos.getX()] = new Cell(pos, new Attente(pos, null));
        for (Position neighbor : posAutour) {
            System.out.println(neighbor);
            boolean test = false;
    
            // Vérifie si la grille contient un élément à la position voisine.
            if (grid[neighbor.getY()][neighbor.getX()] != null) {
                test = true;
    
                // Vérifie les positions autour de cet élément.
                ArrayList<Position> posAutourElement = posAutour4(neighbor);
                for (Position neighborNeighbor : posAutourElement) {
                    if (trouverChemin(Spawn.getPos(), neighborNeighbor) != null) {
                        test = false;
                        break;
                    }
                }
            }
    
            // Vérifie les autres conditions pour refuser la position.
            if ((trouverChemin(Spawn.getPos(), neighbor) == null && grid[neighbor.getY()][neighbor.getX()] == null) || test) {
                grid[pos.getY()][pos.getX()] = null;
                return false;
            }
        }
        grid[pos.getY()][pos.getX()] = null;
        return true; // Si aucune condition n'échoue, la position est valide.
    }
    

    private Position genRandomPosValid() {
        Position pos = genRandomPos();
    
        System.out.println(pos);

        
    
        // Vérifie si la position est valide en utilisant la fonction de validation.
        if (!isValidPosition(pos)) {
            return genRandomPosValid(); // Rappel récursif pour trouver une position valide.
        }
    
        grid[pos.getY()][pos.getX()] = null; // Nettoie la grille.
        return pos; // Retourne la position valide.
    }
    

    


    public ArrayList<Position> trouverChemin(Position depart, Position cible) {
        if (!posIsValid(cible) || 
            (grid[cible.getY()][cible.getX()] != null && !(grid[cible.getY()][cible.getX()].getContent() instanceof Spawn))) {
            //System.out.println("Position cible invalide ou occupée.");
            return null;
        }
    
        boolean[][] visited = new boolean[height][width];
        Position[][] prev = new Position[height][width];
        Queue<Position> queue = new LinkedList<>();
        queue.add(depart);
        visited[depart.getY()][depart.getX()] = true;
    
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
            return null;
        }
    
        // Reconstruire le chemin sans inclure la position de départ
        ArrayList<Position> chemin = new ArrayList<>();
        for (Position at = cible; prev[at.getY()][at.getX()] != null; at = prev[at.getY()][at.getX()]) {
            chemin.add(0, at); // Ajouter au début pour maintenir l'ordre
        }
    
        return chemin;
    }
    
    public boolean deplacerVersPosition(Player p, Position cible) {
        ArrayList<Position> chemin = trouverChemin(p.getPos(), cible);
        if (chemin == null) {
            return false;
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



    public void placerUsine(Player player){
        Position posMachine = new Position(player.getPos().getX(), player.getPos().getY());
        
        if(!isValidPosition(posMachine)){
            return;
        }

        Cell nouvMachine = new Cell(posMachine ,player.getMachine());
        ArrayList<Position> posAutour = posAutour4(posMachine);
        for(Position pos : posAutour){
            
            if (trouverChemin(Spawn.getPos(), pos) != null){
                deplacerDun(pos, player);
                grid[posMachine.getY()][posMachine.getX()] = nouvMachine;
                player.removeMachine();
                game.updateAffichage(false);
                return;
            }
        }
        
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


    public ArrayList<Machine> getMachines(){
        ArrayList<Machine> res = new ArrayList<>();
        for (Cell[] row : grid) {  // Parcours des lignes de la grille
            for (Cell c2 : row) {   // Parcours des cellules dans chaque ligne
                if (c2 != null && c2.getContent() instanceof Machine) {

                    Machine obj = (Machine) c2.getContent();
                    res.add(obj);
                }
            }
        }
        return res;  // Retourner la liste des objets ayant une position
    }
    public Cell[][] getGrid(){return grid;}
    public void setGrid(Cell[][] ngrid){grid = ngrid;}

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
