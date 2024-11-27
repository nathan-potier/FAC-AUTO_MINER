import java.util.ArrayList;

public interface GameListener {
    void onMove(Object content, Position start, Position target, String dir);
    void updateObjects(ArrayList<Object> obj, ArrayList<Position> pos);
}
