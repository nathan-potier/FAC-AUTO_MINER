package Modele;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;



import Vue.VueGenerale;
import Modele.Machine.*;
import Modele.Ressource.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Scanner;

public class Map implements Serializable{
    private Cell[][] grid;
    private int width, height;
    private Spawn spawn;
    private Market market;


    private Game game;


    private VueGenerale vue;
    

    public Map(int width, int height, Player p, Spawn spawn, Market m, Game game) {
        grid = new Cell[height][width];
        this.width = width;
        this.height = height; 
        this.spawn = spawn;
        this.market = m;
        this.game = game;
        generateMap(p);
        

    }

    private void generateMap(Player p) {

        grid[height - 1][width - (width - 2)] = new Cell(new Position( width - (width - 2), height - 1), p);
        grid[market.getPos().getY()][market.getPos().getX()] = new Cell(new Position(market.getPos().getX(), market.getPos().getY()), market);
    }

    public void setVue(VueGenerale v){vue = v;}
    

    public void attendreReplace(Position position, Cell cell, Player p, int temps) {
        
        Timer timer = new Timer(temps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                p.setEcoute(true);
                grid[position.getY()][position.getX()] = cell;
                game.updateAffichage(true);


                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // S'assure que le timer ne se répète pas
        timer.start();
        
    }

    public ArrayList<Cell> regarderAutour(Position pos) {

        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        ArrayList<Cell> resultats = new ArrayList<>();

        for (Direction direction : directions) {
            Position newPosition = direction.move(pos);
            // Vérification des limites du tableau
            if ( posIsValid(newPosition) &&  grid[newPosition.getY()][newPosition.getX()] != null) {
                
                resultats.add(grid[newPosition.getY()][newPosition.getX()]);
            }else{
                resultats.add(null);
            }
        }
        return resultats;
    }
    
    public void ActionByClick(Player player, Position pos){
        ArrayList<Cell> res = regarderAutour(player.getPos());
        for (Cell c : res){
              if ( c != null && c.getPosition().getX() == pos.getX() && c.getPosition().getY() == pos.getY() ){
                if(c.isRessource()){
                    //player.collectResource((Ressource)c.getContent());
                    collectRessource(player, c.getPosition());
                }
            }
        }
    }
    
    public boolean posIsValid(Position position){
        return (position.getX() >= 0 && position.getY() >= 0 && position.getX() < width && position.getY() < height );

    }

    public boolean déplacer(Player p, Direction d){
        
        Position nouvelle = d.move(p.getPos());
        if (!posIsValid(nouvelle) || !p.isEcoute()){
            
            return false;
        }
        else if(grid[nouvelle.getY()][nouvelle.getX()] == null || grid[nouvelle.getY()][nouvelle.getX()].equals(grid[Spawn.getPos().getY()][Spawn.getPos().getX()])){
            grid[nouvelle.getY()][nouvelle.getX()] = new Cell(nouvelle, p);
            grid[p.getPos().getY()][p.getPos().getX()] = null;
            p.setPos(nouvelle);
            verifSpawnAffichage(); 
            return true;

        }else if (grid[nouvelle.getY()][nouvelle.getX()].getContent() instanceof Usine) {

            //deplacerOnUsine(p, nouvelle);
            vue.transmettreMarket();
            return true;
        } else if (grid[nouvelle.getY()][nouvelle.getX()].getContent() instanceof Market) {

            GameStates.setMoveOn(false);
            vue.transmettreMarket();
            return true;
        }


        return false;


    }

    public void collectRessource(Player p, Position nouvelle){

        int temps = 5000;
        
        p.collectResource((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent());
        
        
        Cell cell = grid[nouvelle.getY()][nouvelle.getX()];
        Position pos = new Position(nouvelle.getX(), nouvelle.getY());

        

        Boolean isTemporisée = ((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isTemporisée();
        Boolean isLaborieuse = ((Ressource)grid[nouvelle.getY()][nouvelle.getX()].getContent()).isLaborieuse();



        grid[nouvelle.getY()][nouvelle.getX()] = new Cell(pos, new Attente(pos));

        game.CallAnnimationRespawn(pos, temps);
        game.updateAffichage(true);
  

        if(isTemporisée){
            attendreReplace(pos, cell, p, temps);
            
        }
        
        else if (isLaborieuse){
            
            p.setEcoute(false);
            attendreReplace(pos, cell, p, temps);
        }
    }


    public void deplacerOnUsine(Player p, Position nouvelle) {
        Usine usine = (Usine) grid[nouvelle.getY()][nouvelle.getX()].getContent();
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Vous êtes sur une usine. Voulez-vous Récolter (R) ou Produire (P) ou voir le Stock (S)? (E) pour sortir");
        String choix = scanner.nextLine().toUpperCase();
    
        if (choix.equals("R")) {
            usine.recuperer();
        } else if (choix.equals("P")) {
            System.out.println("Quelle recette souhaitez-vous produire (Pickaxe ou Bloc de Fer) ?");
            String recetteChoisie = scanner.nextLine();
            usine.produire(recetteChoisie);
        }else if (choix.equals("E")) {
            System.out.println("Vous avez quitté l'usine");
        }else if (choix.equals("S")) {
            usine.afficherStock();
            deplacerOnUsine(p, nouvelle);
        }else {
            System.out.println("Choix invalide.");
        }
    }


    

    public void verifSpawnAffichage(){
        if(grid[Spawn.getPos().getY()][Spawn.getPos().getX()] == null){
            grid[Spawn.getPos().getY()][Spawn.getPos().getX()] = new Cell(Spawn.getPos(), spawn);
        }
    }

    public void generateRessource(Player p){

        Random rand = new Random();
        int aleatoireIntLargeur = rand.nextInt(width);
        int aleatoireIntHauteur = rand.nextInt(height);

        Position posWood1 = new Position(2, 1);
        Wood wood = new Wood(1);
        grid[posWood1.getY()][posWood1.getX()] = new Cell(posWood1, wood);

        Position posIron1 = new Position(6, 3);
        Iron iron = new Iron(1);
        grid[posIron1.getY()][posIron1.getX()] = new Cell(posIron1, iron);


        Position posIron2 = new Position(1, 6);
        Iron iron2 = new Iron(1);
        grid[posIron2.getY()][posIron2.getX()] = new Cell(posIron2, iron2);

        // Placer une usine à côté de la position de `Wood`
        Position posUsine = new Position(posWood1.getX() + 1, posWood1.getY()); // Position adjacente
        Usine usine = new Usine(p.getInv());
        grid[posUsine.getY()][posUsine.getX()] = new Cell(posUsine, usine);

        // Placer une récolteuse à côté de la position de `Iron`
        // Position posRecolteuse = new Position(posIron1.getX() + 1, posIron1.getY()); // Position adjacente
        // Recolteuse recolteuse = new Recolteuse(posRecolteuse, 10, p.getInv(), iron);  // 10 est la capacité du stock max
        // grid[posRecolteuse.getY()][posRecolteuse.getX()] = new Cell(posRecolteuse, recolteuse);

    }


    public Cell[][] getGrid(){return grid;}
    public void setGrid(Cell[][] ngrid){grid = ngrid;}
}
