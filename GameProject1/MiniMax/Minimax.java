package MiniMax;

import HelperClasses.FullTree;
import HelperClasses.GameNode;
import HelperClasses.TreePrinter;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;


public class Minimax {


    private final PlayerID self;
    private final PlayerID enemy;
    private final int MIN = Integer.MIN_VALUE;
    private final int MAX = Integer.MAX_VALUE;
    private int alpha;
    private int beta;

    public Minimax(PlayerID self) {
        this.self = self;
        if (self == PlayerID.TOP) {
            this.enemy = PlayerID.BOT;
        } else {
            this.enemy = PlayerID.TOP;
        }
    }

    /**
    Call Function to perform minimax on the tree, returning the move we should make
     */
    public Move findMove(GameState curState, int depth){
        GameNode root = FullTree.buildTree(curState, depth);
        maxMin(root, depth, true);
        GameNode bestMove = root.getFavoriteChild();
        return new Move(bestMove.getConnectingMove(), root.getGameState().getCurPlayer());
    }

    /**
     * Find's the utility of the provided node and returns it
     * @param node : GameNode to find the utility of
     * @return utility of provided GameNode
     */
    public int findUtility(GameNode node) {
        GameState s = node.getGameState();
        int curPoints = s.getHome(this.self);
        int enemyPoints = s.getHome(this.enemy);

        int inRowBad = 0;
        int inRowGood = 0;
        for (int i=0; i < 6; i++) {
            int playerStones = s.getStones(this.self, i);
            if (playerStones == 1 || playerStones == 2) inRowBad ++;

            int enemyStones = s.getStones(this.enemy, i);
            if (enemyStones == 1 || enemyStones == 2) inRowGood++;
        }
        return utilityFormula(curPoints - enemyPoints, inRowBad, inRowGood);
    }

    private static int utilityFormula(int pointsDiff, int inRowBad, int inRowGood) {
        return pointsDiff - inRowBad*2 + inRowGood*1;
    }

    /**
     Recursive method - maxitPlayer TRUE means we want to maximize their points, and if false we wanna minimize it.
     Functions a lot like Depth First Search.
     */
    private int maxMin(GameNode currNode, int d, boolean maximize) {
        // We reach an end of a branch or reached max depth, calculate Utility
        if (d == 0 || currNode.getChildren().isEmpty()) {
            int utility = findUtility(currNode);
            currNode.setUtility(utility);
            /**
             * This changes to return minMaxValues JavaBean
             */
            return utility;
        }

        int value, prev;
        // When curPlayer = self, maximize utility
        if (maximize) {
            value = this.MIN;
            prev  = value;
            for (GameNode child : currNode.getChildren()) {
                value = Math.max(value, maxMin(child, d - 1, false));
                if (value > prev) {
                    prev = value;
                    currNode.setFavoriteChild(child);
                }
            }
        }
        // When curPlayer = enemy, minimize utility
        else {
            value = this.MAX;
            prev  = value;
            for (GameNode child : currNode.getChildren()) {
                value = Math.min(value, maxMin(child, d - 1, true));
                if (value < prev) {
                    prev = value;
                    currNode.setFavoriteChild(child);
                }
            }
        }
        return value;
    }
}
