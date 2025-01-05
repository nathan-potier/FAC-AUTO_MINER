package Modele;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.Timer;

import Vue.VueGenerale;
import Modele.Machine.*;
import Modele.Resource.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.concurrent.atomic.AtomicInteger;

public class Carte {
    private Cell<?>[][] grid;
    private int width, height;
    private Spawn spawn;
    private Market market;
    private ArrayList<Timer> timers;
    private int temps = 5000;

    private Game game;

    private VueGenerale vue;

    public Carte(int width, int height, Player p, Spawn spawn, Market m, Game game) {
        grid = new Cell[height][width];
        timers = new ArrayList<>();
        this.width = width;
        this.height = height; 
        this.spawn = spawn;
        this.market = m;
        this.game = game;
    }

    public Carte(Game game, ArrayList<Savable<?>> objList){
        this(Game.getWidth(), Game.getHeight(), Game.getPlayer(), game.getSpawn(), game.getMarket(), game);
        
        for (Savable<?> obj : objList) {
            obj.addToGrid(grid); // Chaque objet sait comment s'ajouter à la grille
        }
        generateMap(Game.getPlayer());
    }

    public void generateMap(Player p) {
        grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell<>(new Position( Spawn.getPos().getX(), Spawn.getPos().getY()), p);
        if (market.getPos() == null){
            Position posMarket = Position.genRandomPosForMarket(this);
            while(!posMarket.isValidPosition(this)){
                posMarket = Position.genRandomPosForMarket(this);
            }
            market.setPos(posMarket);
            grid[posMarket.getY()][posMarket.getX()] = new Cell<>(posMarket, market);
        }else{
            grid[market.getPos().getY()][market.getPos().getX()] = new Cell<>(new Position(market.getPos().getX(), market.getPos().getY()), market);
        }  
    }

    public void attendreReplace(Position position, Cell<?> cell, Player p, int temps, boolean isLaborieuse) {
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLaborieuse && p != null) {
                    GameStates.setMoveOn(true);
                }

                grid[position.getY()][position.getX()] = cell;
                game.updateAffichage(true);

                ((Timer) e.getSource()).stop();
            }
        });

        timer.setRepeats(false); // Assure que le timer ne se répète pas
        timers.add(timer); // Ajouter ce timer à la liste
        timer.start(); // Démarrer le timer
    }
    
    public ArrayList<Cell<?>> regarderAutour(Position pos) {
        ArrayList<Cell<?>> resultats = new ArrayList<>();
        ArrayList<Position> positionsAutour = pos.posAutour4();

        for (Position newPosition : positionsAutour) {
            // Ajouter la cellule si elle existe, sinon null
            if (getElementAt(newPosition) != null) {
                resultats.add(getElementAt(newPosition));
            } else {
                resultats.add(null);
            }
        }

        return resultats;
    }

    public void ActionByClick(Player player, Position pos){
        ArrayList<Cell<?>> res = regarderAutour(player.getPos());
        for (Cell<?> c : res){
              if ( c != null && c.getPosition().getX() == pos.getX() && c.getPosition().getY() == pos.getY() ){
                if(isInstanceOf(Resource.class, c.getPosition())  ){
                    collectRessource(player, c.getPosition());
                }else if (isInstanceOf(Market.class, c.getPosition()) || isInstanceOf(Machine.class, c.getPosition())){
                    deplacer(player, Direction.getDirection(player.getPos(), c.getPosition()));
                }
            }
        }
    } 

    public boolean deplacer(Player p, Direction d){
        Position nouvelle = d.move(p.getPos());
        if (!nouvelle.posIsInMap() || !GameStates.isMoveOn()){  
            return false;
        }
        else if(cellIsEmpty(nouvelle) || Spawn.isSpawn(nouvelle)){
            p.deplacerDun(nouvelle, grid, game);
            verifSpawnAffichage(); 
            return true;
        }    
        // Gestion des interactions spécifiques
        Object content = getElementAt(nouvelle).getContent();
        if (interagirAvecElement(content, nouvelle, p)) {
            return true;
        }
        return false;
    }

    private boolean interagirAvecElement(Object content, Position nouvelle, Player p) {
        if (content instanceof Resource) {
            collectRessource(p, nouvelle);
            return true;
        } else if (content instanceof Usine) {
            vue.transmettreUsine((Usine) content);
            return true;
        } else if (content instanceof Recolteuse) {
            vue.transmettreRecolteuse((Recolteuse) content);
            return true;
        } else if (content instanceof Market) {
            GameStates.setMoveOn(false);
            vue.transmettreMarket();
            return true;
        }
        return false;
    }
    

    public void collectRessource(Player p, Position nouvelle){
        if (p != null &&  !(p.collectResource((Resource)getElementAt(nouvelle).getContent())) ){
            return;
        }
        
        Cell<?> cell = getElementAt(nouvelle);
        Position pos = new Position(nouvelle.getX(), nouvelle.getY());

        Boolean isTemporisee = ((Resource)getElementAt(nouvelle).getContent()).isTemporisee();

        grid[nouvelle.getY()][nouvelle.getX()] = new Cell<>(pos, new Attente(pos, (Resource)cell.getContent()));

        
        game.updateAffichage(true);

        if(isTemporisee){
            attendreReplace(pos, cell, p, temps, false);
            
        }else{
            if (p != null){
                GameStates.setMoveOn(false);
            }
            attendreReplace(pos, cell, p, temps, true);
        }
    }

    public void verifSpawnAffichage(){
        if(getElementAt(Spawn.getPos()) == null){
            grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell<>(Spawn.getPos(), spawn);
            game.updateAffichage(false);
        }
    }

    public void genNObjects(int n, String type) {
        for (int i = 0; i < n; i++) {
            // Générer une position valide
            Position pos = Position.genRandomPosValid(this);
    
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
                case "pierre":
                    obj = new Rock(1, pos);
                    break;
                // Ajouter d'autres types d'objets ici si nécessaire
                default:
                    System.out.println("Type inconnu : " + type);
                    continue;
            }
    
            // Place l'objet dans la grille
            grid[pos.getY()][pos.getX()] = new Cell<>(pos, obj);
        }
    }

    public void generateRessource(Player p){
        genNObjects(10, "Wood");
        genNObjects(5, "Iron");
        genNObjects(5, "Pierre");
    }  
  
    public ArrayList<Position> trouverChemin(Position depart, Position cible) {
        if (!cible.posIsInMap() || 
            (getElementAt(cible) != null && !(getElementAt(cible).getContent() instanceof Spawn))) {
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
                if (next.posIsInMap() && !visited[next.getY()][next.getX()] &&
                    (getElementAt(next) == null || getElementAt(next).getContent() instanceof Spawn)) {
                    queue.add(next);
                    visited[next.getY()][next.getX()] = true;
                    prev[next.getY()][next.getX()] = current;
                }
            }
        }
    
        // Vérifier si un chemin a été trouvé
        if (prev[cible.getY()][cible.getX()] == null) {
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
                    timer.stop(); // Arrêter le timer si une direction invalide est trouvée
                    return;
                }
    
                // Appeler la fonction de déplacement existante
                if (!deplacer(p, direction)) {
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

    public boolean placerMachine(Player player){
        Position posMachine = new Position(player.getPos().getX(), player.getPos().getY());
        
        if(!posMachine.isValidPosition(this) || player.getMachine() == null || 
        posMachine.equals(Spawn.getPos())){
            return false;
        }


        ArrayList<Position> posAutour = posMachine.posAutour4();
        boolean placablerecolteuse = false;
        
        for (Position pos : posAutour){
            if (getElementAt(pos) != null && isInstanceOf(Resource.class, pos)){
                placablerecolteuse = true;
            }
        }

        if(!placablerecolteuse && player.getMachine() instanceof Recolteuse){return false;}

        
        for(Position pos : posAutour){
            
            if (trouverChemin(Spawn.getPos(), pos) != null){
                Cell<?> nouvMachine = new Cell<>(posMachine ,player.getMachine());
                player.deplacerDun(pos, grid, game);
                verifSpawnAffichage(); 
                grid[posMachine.getY()][posMachine.getX()] = nouvMachine;
                player.getMachine().setPos(posMachine);
                player.getMachine().setMap(this);

                player.removeMachine();
                game.updateAffichage(false);

                return true;
            }
        }

        return false;
        
    }

    public void stopUpdateTimer(){
        for (Timer t : timers) {
            if (t != null) {
                t.stop();
            }
        }
    }

    public ArrayList<Savable> getSavableObject() {
        ArrayList<Savable> res = new ArrayList<>();
        for (Cell<?>[] row : grid) {  // Parcours des lignes de la grille
            for (Cell<?> c2 : row) {   // Parcours des cellules dans chaque ligne
                if (c2 != null && c2.getContent() instanceof Savable) {
                    // Vérifier si le contenu de la cellule implémente ObjectWithPosition
                    Savable obj = (Savable) c2.getContent();
                    res.add(obj);
                }
            }
        }
        return res;  // Retourner la liste des objets ayant une position
    }

    public ArrayList<Machine> getMachines(){
        ArrayList<Machine> res = new ArrayList<>();
        for (Cell<?>[] row : grid) {  // Parcours des lignes de la grille
            for (Cell<?> c2 : row) {   // Parcours des cellules dans chaque ligne
                if (c2 != null && c2.getContent() instanceof Machine) {

                    Machine obj = (Machine) c2.getContent();
                    res.add(obj);
                }
            }
        }
        return res;  // Retourner la liste des objets ayant une position
    }

    public Cell<?>[][] getGrid(){return grid;}
    public Cell<?> getElementAt(Position pos){return grid[pos.getY()][pos.getX()];}
    public boolean cellIsEmpty(Position pos){return getElementAt(pos) == null;}
    
    public boolean isInstanceOf(Class<?> type, Position pos){return type.isInstance(grid[pos.getY()][pos.getX()].getContent());}
    
    public void setGrid(Cell<?>[][] ngrid){grid = ngrid;}
    public void setElementAt(Position pos, Cell<?> cell){grid[pos.getY()][pos.getX()] = cell;}
    public void setVue(VueGenerale v){vue = v;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Parcours de toutes les cases de la carte
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell<?> cell = grid[y][x];
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
