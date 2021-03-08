package HelperClasses;

import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class FullTree {
    GameNode root;
    ArrayList<GameNode> lastRow;
    ArrayList<GameNode> nextMoveRow;

    public FullTree() { }

    /*
    Takes in the current GameState, and uses that to append to the current tree and reset the Root,
        Thus increasing efficiency.
    So you first make a FullTree object, then when the gameState changes you just call this before exploring
        the tree.
     */
    public void update(GameState s) {

    }

    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future
    */
    private ArrayList<GameNode> generateStates(GameNode curNode){
        PlayerID cur_player = curNode.getGameState().getCurPlayer();
        for (int i = 0; i<6; i++){
            if (curNode.getGameState().getStones(cur_player, i) > 0){
                GameState childState = GameRules.makeMove(curNode.getGameState(), i);
                GameNode childNode = new GameNode(childState, curNode, i);
                curNode.addChild(childNode);
            }
        }
        return curNode.getChildren();
    }

    /*
    Builds a tree, where each Node contains a GameState, parent, and children.
     */
    public GameNode buildTree(GameState curState, int depth){
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


