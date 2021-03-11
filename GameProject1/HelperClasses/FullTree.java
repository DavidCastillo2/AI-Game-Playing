package HelperClasses;

import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class FullTree {
    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future
    */
    private static ArrayList<GameNode> generateStates(GameNode curNode){
        PlayerID cur_player = curNode.getGameState().getCurPlayer();
        for (int i = 0; i<6; i++){
            if (curNode.getGameState().getStones(cur_player, i) > 0){
                GameState childState = GameRules.makeMove(curNode.getGameState(), i);
                new GameNode(childState, curNode, i);
            }
        }
        return curNode.getChildren();
    }

    /*
    Builds a tree, where each Node contains a GameState, parent, and children.
     */
    public static GameNode buildTree(GameState curState, int depth){
        GameNode root = new GameNode(curState, null, -1);
        ArrayList<GameNode> cur = new ArrayList<>();
        cur.add(root);

        //For each layer
        for (int i=0; i < depth; i++){
            ArrayList<GameNode> next = new ArrayList<>();

            // For each node in previous layer
            for (GameNode child: cur){

                // Get all children
                next.addAll(generateStates(child));
            }
            cur = next;
        }
        return root;
    }


}


