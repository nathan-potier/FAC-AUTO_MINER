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

    public void déplacer(Player p, Direction d){

        Position nouvelle = d.move(p.getPos());
        if (!(nouvelle.getX() >= 0 && nouvelle.getY() >= 0 && nouvelle.getX() < width && nouvelle.getY() < height )){

        }
        else if(grid[nouvelle.getY()][nouvelle.getX()] == null){
            grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
            grid[p.getPos().getY()][p.getPos().getX()] = null;
            p.setPos(nouvelle);

        }


    }

    public void verifSpawnAffichage(){
        
    }

    private void generateRessource(){

        Random rand = new Random();
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);
    }


    public Cell[][] getGrid(){return grid;}
}
