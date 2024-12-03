package Modele;

public class Attente implements ObjectWithPosition {
    public Position pos;
    public Attente(Position pos){this.pos = pos;}
    @Override
    public String getName() {
        return "Attente";
    }
    @Override
    public Position getPosition() {
        return pos;
    }
}