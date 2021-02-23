package OurCode;

import ProjectOneEngine.*;
import java.util.Random;


public class OurAI implements Player {

    public Move getMove(GameState state){

        Random rand = new Random();
        boolean done = false;
        PlayerID cur_player = state.getCurPlayer();
        Move rand_move = null;

        int bin1 = state.getStones(cur_player, 1);
        int no1 = state.getStones(cur_player, 1);
        System.out.println(bin1);
        System.out.println(no1);
        System.out.println("");

        while ( ! done ){
            int bin = rand.nextInt(6);
            rand_move = new Move(bin, cur_player);
            if (GameRules.makeMove(state, rand_move) != null){
                done = true;
            }
        }
        return rand_move;
    }

    public String getPlayName(){
        return "Completely Random Player";
    }
}

