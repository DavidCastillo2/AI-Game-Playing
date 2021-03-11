package TeamAIs;

import MiniMax.Minimax;
import ProjectOneEngine.*;

public class MinMaxPlayer implements Player {

    public Move getMove(GameState state){
        System.out.println("");  // Just to help make it clear what move was made

        boolean done = false;
        Move move = null;

        Minimax Minimax = new Minimax(state.getCurPlayer());
        while (!done) {
            move = Minimax.findMove(state, 8);
            if (GameRules.makeMove(state, move) != null){
                done = true;
            }
        }
        return move;
    }

    public String getPlayName(){
        return "MinMax Player";
    }
}
