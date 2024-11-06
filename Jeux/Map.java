public class Map {
    private Cell[][] grid;

    public Map(int width, int height) {
        grid = new Cell[width][height];
        generateMap();
    }

    private void generateMap() {
        // Remplir la carte avec des ressources et autres objets
    }
    public Cell[][] getGrid(){return grid;}
}
