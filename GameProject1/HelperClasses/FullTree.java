package HelperClasses;

import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class FullTree {
    GameNode root;
    int maxDepth;

    public FullTree() {
    }


    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future
    */
    private static ArrayList<GameNode> generateStates(GameNode curNode){
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
        this.root = root;
        this.maxDepth = depth;
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

    public String lengthOfEachLevel(){
        StringBuilder retVal = new StringBuilder();
        ArrayList<GameNode> cur = new ArrayList<>();
        cur.add(this.root);

        //For each layer
        for (int i=0; i < this.maxDepth; i++){
            ArrayList<GameNode> next = new ArrayList<>();

            long count = 0;
            retVal.append("Layer: ").append(i).append("\t NumberOfNodes: ");

            // For each node in the current Layer
            for (GameNode child : cur) {

                // Get all children
                next.addAll(child.getChildren());
                count += next.size();
            }
            cur = next;
            retVal.append(count).append("\n");
        }
        return retVal.toString();
    }

    public String utilityOfEachNode(){
        StringBuilder retVal = new StringBuilder();
        ArrayList<GameNode> prev = new ArrayList<>();
        prev.add(this.root);

        //For each layer
        for (int i=0; i < this.maxDepth; i++){
            ArrayList<GameNode> nextPrev = new ArrayList<>();

            // For each node in previous layer
            for (GameNode cur: prev){

                // Get all children
                ArrayList<GameNode> children = cur.getChildren();
                for (GameNode c : children) {
                    retVal.append(" | ");
                    retVal.append(c.utility).append(" ");
                }
                retVal.append(" | ");
                nextPrev.addAll(children);
            }
            prev = nextPrev;
        }
        return retVal.toString();
    }

}


