package Modele;

import Modele.Resource.*;

public class Attente implements Savable {
    public Position pos;
    public Resource r;
    public Attente(Position pos, Resource r){this.pos = pos; this.r = r;}

    @Override
    public String saveAsString() {
        return r.getName() + " a la position " + pos.getX()+","+pos.getY();
    }

    @Override
    public void addToGrid(Cell[][] grid) {

        grid[pos.getY()][pos.getX()] = new Cell<>(pos, this);

    }

}