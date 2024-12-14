package Vue;

import Modele.Cell;

public class AffichageTemporaire {

    
    public static void affiche(Cell<?> [][] map){
        System.out.println();
        for(Cell<?> [] y : map){
            System.out.println("-----------------");
            System.out.print("|");
            for (Cell<?> x : y){
                if(x != null){
                    System.out.print(x.getContent().getClass().getName().charAt(0) + "|" );
                }else{
                    System.out.print( " |");
                }
                
            }
            System.out.println();
            
        }
        System.out.println("-----------------");
    }

}
