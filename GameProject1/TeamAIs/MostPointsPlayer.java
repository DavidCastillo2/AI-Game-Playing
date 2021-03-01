package TeamAIs;
import ProjectOneEngine.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MostPointsPlayer implements Player {

    public Move getMove(GameState state){

	boolean done = false;
	PlayerID cur_player = state.getCurPlayer();
	Move new_move = null;

	while ( ! done ){
		ArrayList<Integer> stones = new ArrayList<>();
	    for (int i=0; i<6; i++){
	    	int stone = state.getStones(cur_player, i);
			stones.add(stone);
		}

		int max = Collections.max(stones);
	    int bin = stones.indexOf(max);

	    new_move = new Move(bin, cur_player);
	    if (GameRules.makeMove(state, new_move) != null){
		done = true;
	    }
	}
	return new_move;
    }

    public String getPlayName(){
	return "Largest-picker Player";
    }
}
	
