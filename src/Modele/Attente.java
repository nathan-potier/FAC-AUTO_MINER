package Modele;

import Modele.Resource.*;

public class Attente implements Savable {
    private Position pos;
    private Resource r;
    public Attente(Position pos, Resource r){this.pos = pos; this.r = r;}

    public Resource getResource(){return r;}

    @Override
    public String saveAsString() {
        return r.getName() + " a la position " + pos.getX()+","+pos.getY();
    }

    @Override
    public void addToGrid(Cell[][] grid) {
        grid[pos.getY()][pos.getX()] = new Cell<>(pos, this);
    }

}