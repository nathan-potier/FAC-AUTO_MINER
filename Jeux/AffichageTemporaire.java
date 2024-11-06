public class AffichageTemporaire {

    
    public static void affiche(Cell [][] map){
        for(Cell [] y : map){
            System.out.print("|");
            for (Cell x : y){
                if(x != null){
                    System.out.print(x.getContent().getClass() );
                }else{
                    System.out.print( " |");
                }
                
            }
            System.out.println();
            System.out.println("-------------------");
        }
    }

}
