package Modele;
import java.io.Serializable;

public class Spawn implements Serializable{
    private static Position pos;
    

    public Spawn(Position p){
        pos = p;

    }

    public static Position getPos(){return pos;}




    
}
