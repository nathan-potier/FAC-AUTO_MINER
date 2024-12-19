package Modele;

import java.util.ArrayList;
import java.util.Random;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;

    }
    public Position(){}

    /**
     * Cette méthode retourne toutes les positions valide autour d'une position donnée.
     *
     * @param pos La position de départ à partir de laquelle les positions autours seront calculées.
     * @return Une liste de toutes les positions valides autour de la position donnée.
     */
    public ArrayList<Position> posAutour8(){
        ArrayList<Position> posAutour = posAutour4();
        // Calcul des positions diagonales
        Position[] diagonales = {
            new Position(this.x - 1, this.y - 1), // Haut gauche
            new Position(this.x + 1, this.y - 1), // Haut droit
            new Position(this.x - 1, this.y + 1), // Bas gauche
            new Position(this.x + 1, this.y + 1)  // Bas droit
        };

        // Vérification si les positions diagonales sont valides
        for (Position p : diagonales) {
            if (p.posIsInMap()) {
                posAutour.add(p);
            }
        }
        return posAutour;
    }

    public ArrayList<Position> posAutour4() {
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        ArrayList<Position> positions = new ArrayList<>();

        for (Direction direction : directions) {
            Position newPosition = direction.move(this);
            if (newPosition.posIsInMap()) { // Vérification des limites du tableau
                positions.add(newPosition);
            }
        }
        return positions;
    }

    public static Position genRandomPos(Carte map, boolean forMarket) {
        Random rand = new Random();
        int aleatoireX, aleatoireY;
    
        // Determine the X and Y coordinates based on the market flag
        if (forMarket) {
            aleatoireX = (rand.nextInt(2) == 0) ? 0 : Game.getWidth() - 1;
            aleatoireY = rand.nextInt(Game.getHeight());
        } else {
            aleatoireX = rand.nextInt(Game.getWidth());
            aleatoireY = rand.nextInt(Game.getHeight());
        }
    
        Position pos = new Position(aleatoireX, aleatoireY);
        int limiteExecution = 50;
        int execution = 0;
    
        // Loop to find a valid position
        while (Spawn.isSpawn(pos) || map.getElementAt(pos) != null) {
            if (limiteExecution <= execution) {
                return null;
            }
            // Recursively generate a new position
            pos = genRandomPos(map, forMarket);
            execution++;
        }
    
        return pos;
    }
    
    public static Position genRandomPosForMarket(Carte map) {
        return genRandomPos(map, true);
    }
    
    public static Position genRandomPosValid(Carte map) {
        Position pos = genRandomPos(map, false);
    
        // Vérifie si la position est valide en utilisant la fonction de validation.
        if (!pos.isValidPosition(map)) {
            return genRandomPosValid(map); // Rappel récursif pour trouver une position valide.
        }
        return pos; // Retourne la position valide.
    }    
    
    public boolean isValidPosition(Carte map) {
        ArrayList<Position> posAutour = this.posAutour8();
        map.setElementAt(this , new Cell<>(this, new Attente(this, null)));
        for (Position neighbor : posAutour) {
            
            boolean test = false;
    
            // Vérifie si la grille contient un élément à la position voisine.
            if (map.getElementAt(neighbor) != null) {
                test = true;
    
                // Vérifie les positions autour de cet élément.
                ArrayList<Position> posAutourElement = neighbor.posAutour4();
                for (Position neighborNeighbor : posAutourElement) {
                    if (map.trouverChemin(Spawn.getPos(), neighborNeighbor) != null) {
                        test = false;
                        break;
                    }
                }
            }
    
            // Vérifie les autres conditions pour refuser la position.
            if ((map.trouverChemin(Spawn.getPos(), neighbor) == null && map.getElementAt(neighbor) == null) || test) {
                map.setElementAt(this, null);
                return false;
            }
        }
        map.setElementAt(this, null);
        return true; // Si aucune condition n'échoue, la position est valide.
    }

    public boolean posIsInMap(){
        return (this.x >= 0 && this.y >= 0 && this.x < Game.getWidth() && this.y < Game.getHeight() );

    }

    public int getX() {return x;}
    public int getY() {return y;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
