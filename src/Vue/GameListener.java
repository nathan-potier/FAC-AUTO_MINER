package Vue;
import java.util.ArrayList;
import Modele.Position;

public interface GameListener {
    void onMove(Object content, Position start, Position target, String dir);
    void updateObjects(ArrayList<Object> obj, ArrayList<Position> pos);
    void attenteAnimation(Position position, int duration);
}
