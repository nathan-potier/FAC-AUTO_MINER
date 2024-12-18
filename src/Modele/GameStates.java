package Modele;


import java.util.Stack;

public class GameStates {
    private static Stack<Boolean> moveOn = new Stack<>();
    
    static {
        moveOn.push(true);
        moveOn.push(false);
    }

    public static boolean isMoveOn() {return moveOn.peek();}
    public  static void setMoveOn(boolean m) {
        if (m){
            if(!moveOn.peek()){
                moveOn.pop(); 
            }

        }else{
            moveOn.push(m);
        }
    }

}
