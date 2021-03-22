package HelperClasses;


import ProjectTwoEngine.GameRules;
import ProjectTwoEngine.GameState;
import ProjectTwoEngine.Move;

import java.util.ArrayList;
import java.util.List;


public class MasterTree {


    public static ArrayList<MoveNode> generateStates(MoveNode curMoveNode) {
        List<Move> possibleMoves = GameRules.getLegalMoves(curMoveNode.getGameState());

        for (Move m : possibleMoves) {
            GameState childState = GameRules.makeMove(curMoveNode.getGameState(), m);  // This is where we die
            new MoveNode(childState, curMoveNode, m);
        }

        return curMoveNode.getChildren();
    }


    public static MoveNode generateTree(GameState state, int depth) {
        MoveNode root = new MoveNode(state, null, null);
        ArrayList<MoveNode> cur = new ArrayList<>();
        cur.add(root);

        //For each layer
        for (int i = 0; i < depth; i++) {
            ArrayList<MoveNode> next = new ArrayList<>();

            // For each MoveNode in previous layer
            for (MoveNode child : cur) {
                next.addAll(generateStates(child));  // Get all children
            }
            cur = next;
        }
        return root;
    }
}






