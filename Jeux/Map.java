import java.util.Random;
public class Map {
    private Cell[][] grid;
    private int width, height;

    public Map(int width, int height, Player p) {
        grid = new Cell[height][width];
        this.width = width;
        this.height = height; 
        generateMap(p);
    }

    private void generateMap(Player p) {
        
  
        grid[height - 1][width - (width - 2)] = new Cell(new Position( width - (width - 2), height - 1), p);



    }

    private void generateRessource(){

        Random rand = new Random();
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);
    }


    public Cell[][] getGrid(){return grid;}
}
