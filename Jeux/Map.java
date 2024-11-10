import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Map {
    private Cell[][] grid;
    private int width, height;
    private Spawn spawn;

    public Map(int width, int height, Player p, Spawn spawn) {
        grid = new Cell[height][width];
        this.width = width;
        this.height = height; 
        this.spawn = spawn;
        generateMap(p);
    }

    private void generateMap(Player p) {
        
  
        grid[height - 1][width - (width - 2)] = new Cell(new Position( width - (width - 2), height - 1), p);



    }

    

    public void attendreReplace(Position position, Cell cell, int temps) {
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("5 secondes écoulées");
                if(grid[position.getY()][position.getX()] != null){
                    attendreReplace(position, cell, 100) ;
                }else{
                    grid[position.getY()][position.getX()] = cell;
                }

                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
        
    }




    public void déplacer(Player p, Direction d){
        
        Position nouvelle = d.move(p.getPos());
        if (!(nouvelle.getX() >= 0 && nouvelle.getY() >= 0 && nouvelle.getX() < width && nouvelle.getY() < height )){
            return;
        }
        else if(grid[nouvelle.getY()][nouvelle.getX()] == null || grid[nouvelle.getY()][nouvelle.getX()].equals(grid[Spawn.getPos().getY()][Spawn.getPos().getX()])){
            grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
            grid[p.getPos().getY()][p.getPos().getX()] = null;
            p.setPos(nouvelle);
            //generateRessource();

        }else if (grid[nouvelle.getY()][nouvelle.getX()].getContent() instanceof Ressource){
            deplacerOnRessource(p, nouvelle);
            

        }
        
        verifSpawnAffichage();


    }

    public void deplacerOnRessource(Player p, Position nouvelle){
        p.collectResource((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent());

        Cell cell = grid[nouvelle.getY()][nouvelle.getX()];
        Position pos = new Position(nouvelle.getX(), nouvelle.getY());
        Boolean isTemporisée = ((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isTemporisée();
        Boolean isLaborieuse = ((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isLaborieuse();

        grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
        grid[p.getPos().getY()][p.getPos().getX()] = null;
        p.setPos(nouvelle);

        if(isTemporisée){
            attendreReplace(pos, cell, 5000);
            //System.out.println("Ressource réaparait");
        }
        
        else if (isLaborieuse){
            //mettre les déplacement en pause ICI ( j'y arrive pas )

            attendreReplace(pos, cell, 10);
        }
    }

    public void verifSpawnAffichage(){
        if(grid[Spawn.getPos().getY()][Spawn.getPos().getX()] == null){
            grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell(Spawn.getPos(), spawn);
        }
    }

    public void generateRessource(){

        Random rand = new Random();
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);

        Position posWood1 = new Position(2, 1);
        Wood wood = new Wood(1);
        grid[posWood1.getY()][posWood1.getX()] = new Cell(posWood1, wood);

        Position posIron1 = new Position(6, 3);
        Iron iron = new Iron(1);
        grid[posIron1.getY()][posIron1.getX()] = new Cell(posIron1, iron);


    }


    public Cell[][] getGrid(){return grid;}
}
