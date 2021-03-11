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
    private int maxMin(GameNode cur, int d, boolean maximize) {
        // We reach an end of a branch or reached max depth, calculate Utility
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
                int childUtility = maxMin(child, d - 1, false);
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
                int childUtility = maxMin(child, d - 1, true);
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
}
