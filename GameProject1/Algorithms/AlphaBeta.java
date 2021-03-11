package Algorithms;

import HelperClasses.FullTree;
import HelperClasses.GameNode;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;


public class AlphaBeta extends Algorithm {
    private final int maxDepth = 12;

    private final PlayerID self;
    private final PlayerID enemy;
    private final int MIN = Integer.MIN_VALUE;
    private final int MAX = Integer.MAX_VALUE;


    public AlphaBeta(PlayerID self) {
        this.self = self;
        this.setDepth(this.maxDepth);
        if (self == PlayerID.TOP) {
            this.enemy = PlayerID.BOT;
        } else {
            this.enemy = PlayerID.TOP;
        }
    }

    public Move findMove(GameState curState, int depth){
        GameNode root = new GameNode(curState, null, -1);
        alphaBeta(root, depth, true);

        // System.out.println(TreePrinter.lengthOfEachLevel(root));
        GameNode bestMove = root.getFavoriteChild();
        return new Move(bestMove.getConnectingMove(), root.getGameState().getCurPlayer());
    }

    /**
     Recursive method - maxitPlayer TRUE means we want to maximize their points, and if false we wanna minimize it.
     Functions a lot like Depth First Search.
     */
    public int alphaBeta(GameNode cur, int d, boolean maximize) {
        // We reach an end of a branch or reached max depth, calculate Utility
        FullTree.generateStates(cur);
        if (d == 0 || cur.getChildren().isEmpty()) {
            int utility = findUtility(cur);
            cur.setUtility(utility);
            return utility;
        }
        int value;
        // When curPlayer = self, maximize utility
        if (maximize) {
            //current utility
            value = this.MIN;
            for (GameNode child : cur.getChildren()) {
                child.setAlpha(cur.getAlpha());
                child.setBeta(cur.getBeta());
                int childUtility = alphaBeta(child, d - 1, false);
                if (childUtility > value) {
                    value = childUtility;
                    cur.setFavoriteChild(child);
                }
                if (childUtility >= cur.getBeta()) return value;
                if (childUtility > cur.getAlpha()) cur.setAlpha(childUtility);
            }
        }
        // When curPlayer = enemy, minimize utility
        else {
            value = this.MAX;
            for (GameNode child : cur.getChildren()) {
                child.setBeta(cur.getBeta());
                child.setAlpha(cur.getAlpha());
                int childUtility = alphaBeta(child, d - 1, true);
                if (childUtility < value) {
                    value = childUtility;
                    cur.setFavoriteChild(child);
                }
                if (childUtility <= cur.getAlpha()) return value;
                if (childUtility < cur.getBeta()) cur.setBeta(childUtility);
            }
        }
        return value;
    }

    public String getPlayName(){
        return "AlphaBeta";
    }
}
