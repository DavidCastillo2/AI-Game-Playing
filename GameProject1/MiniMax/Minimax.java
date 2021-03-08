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
    private PlayerID playerReturn;
    private int enemyP;
    private int usP;
    private FullTree tree = null;

    public Minimax(PlayerID self) {
        this.self = self;
        if (self == PlayerID.TOP) {
            this.enemy = PlayerID.BOT;
        } else {
            this.enemy = PlayerID.TOP;
        }
    }

    /*
    Call Function to perform minimax on the tree, returning the move we should make
     */
    public Move findMove(GameState curState, int depth){
        // Creating Tree
        if (this.tree == null) {
            this.tree = new FullTree();
        } else {
            this.tree.update(curState);
        }

        GameNode root = this.tree.buildTree(curState, depth);

        // Min Max the tree
        int r = maxMin(root, depth, true);
        System.out.println(this.playerReturn + ": " + r + " EnemyPoints: " + this.enemyP + " CurrPoints: " + this.usP + " this.self: " + this.self + " this.enemy: " + this.enemy);

        // Return best move
        GameNode bestMove = root.getFavoriteChild();
        return new Move(bestMove.getConnectingMove(), root.getGameState().getCurPlayer());
    }

    /*
    Calculates the utility of a gameState, taking into account who the current player is.
     */
    public int findUtility(GameNode node, boolean maxitPlayer) {
        // Get Data to feed into utilityFormula()
        GameState s = node.getGameState();
        PlayerID  curP = s.getCurPlayer();
        PlayerID  enemy;

        if (maxitPlayer) enemy = this.enemy;
        else             enemy = this.self;

        int curPoints = s.getHome(curP);
        int inRowBad = 0;
        int sum = 0;
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(curP, i);
            if (stones == 1 || stones == 2) inRowBad ++;
            sum += stones;
        }

        int enemyPoints = s.getHome(enemy);

        int inRowGood = 0;
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(enemy, i);
            if (stones == 1 || stones == 2) inRowBad ++;
        }

        this.playerReturn = curP;
        this.enemyP = enemyPoints;
        this.usP = curPoints;

        // System.out.println(maxitPlayer + "\tCur: " + curP + "\tCurP: " + curPoints + "\tEnemy: " + enemy + "\tEpoints: " + enemyPoints);
        return utilityFormula(curPoints - enemyPoints, inRowBad, inRowGood);
    }

    private static int utilityFormula(int pointsDiff, int inRowBad, int inRowGood) {
        return pointsDiff - inRowBad*2 + inRowGood;
    }


    /**
     Recursive method - maxitPlayer TRUE means we want to maximize their points, and if false we wanna minimize it.
     Functions a lot like Depth First Search.
     */
    private int maxMin(GameNode currNode, int d, boolean maxitPlayer) {
        // We reach an end of a branch or reached max depth, calculate Utility
        if (d == 0 || currNode.getChildren().isEmpty()) {
            int utility = findUtility(currNode, maxitPlayer);
            currNode.setUtility(utility);
            return utility;
        }

        // Our player = Max it
        int value, prev;
        if (maxitPlayer) {
            value = this.MIN;
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
            value = this.MAX;
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
