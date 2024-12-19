package Modele;


public class Spawn {
    private static Position pos;
    

    public Spawn(Position p){
        pos = p;

    }

    public static Position getPos(){return pos;}

    public static boolean isSpawn(Position pos){
        return pos.getX() == Spawn.getPos().getX() && pos.getY() == Spawn.getPos().getY();
    }

    
}
