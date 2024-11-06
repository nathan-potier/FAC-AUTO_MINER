import java.util.Random;
public class Map {
    private Cell[][] grid;

    public Map(int width, int height) {
        grid = new Cell[width][height];
        generateMap(width, height);
    }

    private void generateMap(int width, int height) {
        Random rand = new Random();
        Cell start = new Cell(new Position( width - (width - 2), height - 1), null);
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);


    }


    public Cell[][] getGrid(){return grid;}
}
