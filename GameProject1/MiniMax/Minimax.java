package MiniMax;

import HelperClasses.FullTree;
import HelperClasses.GameNode;
import HelperClasses.TreePrinter;
import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Minimax {


    private final PlayerID self;
    private final PlayerID enemy;
    private final int MIN = Integer.MIN_VALUE;
    private final int MAX = Integer.MAX_VALUE;
    private PlayerID playerReturn;
    private int enemyP;
    private int usP;

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
        int r = maxMin(root, depth, true);
        System.out.println(this.playerReturn + ": " + r + " EnemyPoints: " + this.enemyP + " CurrPoints: " + this.usP + " this.self: " + this.self + " this.enemy: " + this.enemy);

        // System.out.println(TreePrinter.lengthOfEachLevel(root));
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
        int ourStones = 0;
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(curP, i);
            if (stones == 1 || stones == 2) inRowBad ++;
            ourStones += stones;
        }

        int enemyPoints = s.getHome(enemy);

        int inRowGood = 0;
        int enemyStones = 0;
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(enemy, i);
            if (stones == 1 || stones == 2) inRowBad ++;
            enemyStones += stones;
        }

        this.playerReturn = curP;
        this.enemyP = enemyPoints;
        this.usP = curPoints;

        int result = endGameCheck(s, curP, enemy);  // result is 0 if its not an endgame board

        int retVal = utilityFormula(curPoints - enemyPoints, inRowBad, inRowGood, ourStones, enemyStones);
        return retVal + result;
    }

    private static int utilityFormula(int pointsDiff, int inRowBad, int inRowGood, int ourStones, int enemyStones) {
        return pointsDiff*50 - inRowBad*2 + inRowGood + ourStones - enemyStones;
    }

    private static int endGameCheck(GameState s, PlayerID us, PlayerID enemy) {

        int sumUs = 0;
        ArrayList<Integer> enemyStones = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(us, i);
            enemyStones.add(stones);
            sumUs += stones;
        }

        int sumThem = 0;
        ArrayList<Integer> usStones = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            int stones = s.getStones(enemy, i);
            usStones.add(stones);
            sumThem += stones;
        }

        // We have no stones, See if the enemy forfeits their stones
        int stonesWon = sumThem;
        if (s.getHome(us) + sumThem >= s.getHome(enemy)) stonesWon = Integer.MAX_VALUE-100;  // We win the game or Tie
        if (sumUs == 0) {
            for (int i=0; i < 6; i++) {
                int stones = enemyStones.get(i);
                if (stones >= 6 - i) {
                    // They're able to put some stones into another bin and not end the game
                    stonesWon = 0;
                    break;
                }
            }
        }

        // They have no stones, See if WE forfeits our stones
        int stonesLost = sumUs;
        if (s.getHome(enemy) + sumUs >= s.getHome(us)) stonesLost = Integer.MAX_VALUE-100;  // Enemy win the game or Tie
        if (sumUs == 0) {
            for (int i = 0; i < 6; i++) {
                int stones = usStones.get(i);
                if (stones >= 6 - i) {
                    // We're able to put some stones into another bin and not end the game
                    stonesLost = 0;
                    break;
                }
            }
        }
        // Only return a value if its an endgame
        if ((stonesLost != Integer.MAX_VALUE-100) || (stonesWon != Integer.MAX_VALUE-100)) {
            return stonesWon - stonesLost;
        }
        return 0;
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
