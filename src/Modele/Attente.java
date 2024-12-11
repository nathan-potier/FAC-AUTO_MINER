package Modele;

import Modele.Resource.*;

public class Attente implements ObjectWithPosition {
    public Position pos;
    public Resource r;
    public Attente(Position pos, Resource r){this.pos = pos; this.r = r;}
    @Override
    public String getName() {
        return r.getName();
    }
    @Override
    public Position getPosition() {
        return pos;
    }



}