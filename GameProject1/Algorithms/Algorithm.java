package Algorithms;

import HelperClasses.GameNode;
import ProjectOneEngine.GameState;
import ProjectOneEngine.Move;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public abstract class Algorithm {
    PlayerID self;
    PlayerID enemy;
    private final int MIN = Integer.MIN_VALUE;
    private final int MAX = Integer.MAX_VALUE;
    public int depth = -1;

    public Move findMove(GameState curState, int depth) { return null; }

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
        int retVal = utilityFormula(curPoints - enemyPoints, playerCapturable, enemyCapturable);
        return result + retVal;
    }

    public int utilityFormula(int pointsDiff, int playerCapturable, int enemyCapturable) {
        return pointsDiff*3 - playerCapturable/2 + enemyCapturable/2;
    }

    private int endGameCheck(GameState s, PlayerID player, PlayerID enemy) {
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

    private int stoneForfeit(int opponentSum, ArrayList<Integer> stones){
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

    public void setDepth(int d) {
        this.depth = d;
    }

    public String getPlayName(){
        return "Blank Algorithm";
    }
}
