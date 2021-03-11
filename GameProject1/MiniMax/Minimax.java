package MiniMax;

import HelperClasses.FullTree;
import HelperClasses.GameNode;
import HelperClasses.TreePrinter;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;


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
        return new Move(bestMove.getConnectingMove(), curState.getCurPlayer());
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

        int playerCapturable = 0;
        int enemyCapturable = 0;
        int selfTotal = 0;
        int enemyTotal = 0;

        for (int i=0; i < 6; i++) {
            int playerStones = s.getStones(this.self, i);
            if (playerStones == 1 || playerStones == 2) playerCapturable ++;
            selfTotal +=playerStones;

            int enemyStones = s.getStones(this.enemy, i);
            if (enemyStones == 1 || enemyStones == 2) enemyCapturable++;
            enemyTotal += enemyStones;
        }
        int result = endGameCheck(s, this.self, this.enemy);
        int retval = utilityFormula(curPoints - enemyPoints, playerCapturable, enemyCapturable, selfTotal, enemyTotal);
        return result + retval;
    }

    private static int utilityFormula(int pointsDiff, int playerCapturable, int enemyCapturable, int ourStones, int enemyStones) {
        return pointsDiff*2 - playerCapturable + enemyCapturable;
    }

    private static int endGameCheck(GameState s, PlayerID player, PlayerID enemy) {
        int playerSum = 0;
        int enemySum = 0;

        ArrayList<Integer> enemyStones = new ArrayList<>();
        ArrayList<Integer> playerStones = new ArrayList<>();

        for (int i=0; i < 6; i++) {
            int playerBin = s.getStones(player, i);
            playerStones.add(playerBin);
            playerSum += playerBin;

            int enemyBin = s.getStones(enemy, i);
            enemyStones.add(enemyBin);
            enemySum += enemyBin;
        }

        int stonesWon = stoneForfeit(playerSum, enemyStones);
        int stonesLost = stoneForfeit(enemySum, playerStones);

        //if we win stones, check if this wins us the game. If so, do it.
        if (stonesWon != 0 && (stonesWon+s.getHome(player)> s.getHome(enemy))){
            return Integer.MAX_VALUE - 100;
        //if we lose stones...
        } else if (stonesLost != 0){
            //if we have more stones still, do it
            if (stonesLost + s.getHome(enemy) < s.getHome(player)){
                return Integer.MAX_VALUE - 100;
            } else {
                return Integer.MIN_VALUE + 100;

            }
        }
        return 0;
    }

    private static int stoneForfeit(int opponentSum, ArrayList<Integer> stones){
        if (opponentSum == 0){
            for (int i=0; i < 6; i++){
                int stone = stones.get(i);
                if (stone >= 6 - i){
                    return 0;
                }
            }
        }
        return opponentSum;
    }

    /**
     Recursive method - maxitPlayer TRUE means we want to maximize their points, and if false we wanna minimize it.
     Functions a lot like Depth First Search.
     */
    public int maxMin(GameNode cur, int d, boolean maximize) {
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
                child.setBeta(cur.getBeta());
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
                child.setAlpha(cur.getAlpha());
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
