package MiniMax;

import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;

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
    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future
     */
    public static ArrayList<GameNode> generateStates(GameNode curNode){
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
    //TODO: Test tree is built properly
    public static GameNode buildTree(GameState curState, int depth){
        GameNode root = new GameNode(curState, null, -1);
        ArrayList<GameNode> prev = new ArrayList<>();
        prev.add(root);
        //For each layer
        for (int i=0; i < depth; i++){
            ArrayList<GameNode> nextPrev = new ArrayList<>();
            // For each node in previous layer
            for (GameNode cur: prev){
                // Get all children
                nextPrev.addAll(generateStates(cur));
            }
            prev = nextPrev;
        }
        return root;
    }

    /**
    Call Function to perform minimax on the tree, returning the move we should make
     */
    public Move findMove(GameState curState, int depth){
        GameNode root = buildTree(curState, depth);
        int r = maxMin(root, depth, true);
        System.out.println(this.playerReturn + ": " + r + " EnemyPoints: " + this.enemyP + " CurrPoints: " + this.usP + " this.self: " + this.self + " this.enemy: " + this.enemy);
        // root.printUtilities();
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
        return pointsDiff - inRowBad*0 + inRowGood*0;
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
