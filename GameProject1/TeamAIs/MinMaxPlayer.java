package TeamAIs;

import MiniMax.Minimax;
import ProjectOneEngine.*;

public class MinMaxPlayer implements Player {

    public Move getMove(GameState state){

        boolean done = false;
        Move move = null;

        Minimax Minimax = new Minimax(state.getCurPlayer());
        while (!done){
            move = Minimax.findMove(state, 9);
            if (GameRules.makeMove(state, move) != null){
                done = true;
            }
        }
        // System.out.println("");  // Just to help make it clear what move was made
        return move;
    }

    public String getPlayName(){
        return "MinMax Player";
    }
}
