package Algorithms;

import ProjectOneEngine.Move;


import HelperClasses.FullTree;
import HelperClasses.GameNode;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class MiniMax extends Algorithm {
    private final int maxDepth = 9;

    public MiniMax(PlayerID self) {
        this.self = self;
        this.setDepth(this.maxDepth);
        if (self == PlayerID.TOP) {
            this.enemy = PlayerID.BOT;
        } else {
            this.enemy = PlayerID.TOP;
        }
    }

    public Move findMove(GameState curState, int depth){
        GameNode root = FullTree.buildTree(curState, depth);
        maxMin(root, depth, true);

        GameNode bestMove = root.getFavoriteChild();
        return new Move(bestMove.getConnectingMove(), root.getGameState().getCurPlayer());
    }

    private int maxMin(GameNode currNode, int d, boolean maxitPlayer) {
        // We reach an end of a branch or reached max depth, calculate Utility
        if (d == 0 || currNode.getChildren().isEmpty()) {
            int utility = findUtility(currNode);
            currNode.setUtility(utility);
            return utility;
        }

        // Our player = Max it
        int value, prev;
        if (maxitPlayer) {
            value = Integer.MIN_VALUE;
            prev  = value;
            for (GameNode child : currNode.getChildren()) {
                value = Math.max(value, maxMin(child, d - 1, false));
                if (value > prev) {
                    prev = value;
                    currNode.setFavoriteChild(child);
                }
            }
            return value;

            // Enemy player = Min it
        } else {
            value = Integer.MAX_VALUE;
            prev  = value;
            for (GameNode child : currNode.getChildren()) {
                value = Math.min(value, maxMin(child, d - 1, true));
                if (value < prev) {
                    prev = value;
                    currNode.setFavoriteChild(child);
                }
            }
            return value;
        }
    }
}
