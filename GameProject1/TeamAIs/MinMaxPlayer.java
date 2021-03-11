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
            move = Minimax.findMove(state, 9);
            if (GameRules.makeMove(state, move) != null){
                done = true;
            }
        }
        System.out.print("Enemy: ");
        System.out.println(state.getHome(state.getCurPlayer()));
        System.out.print("Player: ");
        System.out.print(state.getHome(PlayerID.TOP));
        System.out.print("\tDifference: ");
        System.out.println(state.getHome(state.getCurPlayer()) - state.getHome(PlayerID.TOP));
        return move;
    }

    public String getPlayName(){
        return "MinMax Player";
    }
}
