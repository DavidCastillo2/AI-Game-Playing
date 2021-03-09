package TeamAIs;

import MiniMax.Minimax;
import ProjectOneEngine.*;

public class MinMaxPlayer implements Player {
    private Minimax miniMax = null;

    public Move getMove(GameState state){
        System.out.println("");  // Just to help make it clear what move was made
        boolean done = false;
        Move move = null;

        if (this.miniMax == null)
            this.miniMax = new Minimax(state.getCurPlayer());

        while (!done) {
            move = this.miniMax.findMove(state, 8);
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
