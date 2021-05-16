package HelperClasses.Reinforcement;

import HelperClasses.Dijkstra;
import HelperClasses.GameBoard;
import HelperClasses.Tile;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.HeadPiece;

import java.util.LinkedList;
import java.util.List;

// TODO: It appears that Dijskstras is angry that we're targeting a head and that is unreachable 100% of the time.

public class USuckUnless {
    // Grade: Current Game State GIVEN a Specific move?
    public static float grade(GameState state, int player_num, DirType directionChosen) {
        int enemy_num = -1;
        if (player_num == 0) {
            enemy_num = 1;
        } else {
            enemy_num = 0;
        }
        GameBoard gb = new GameBoard(state, player_num, enemy_num);
        gb.update(state);

        return finalCalc(gb, state, player_num, enemy_num);
    }

    public static float finalCalc(GameBoard gb, GameState state, int pNum, int eNum) {
        int distToEnemyHead       = distToEnemyHead(gb, state, pNum, eNum);   // 0 - 40ish who cares
        float nearEnemyBody       = nearEnemyBody(gb, state, pNum, eNum);     // 0 - 22 OR MinFloat for DEATH
        float lengthVsEnemyLength = lengthOfEnemyBody(gb, state, pNum, eNum); // 0 - infinity
        float nearFood            = nearFood(gb, state, pNum, eNum);          // 0 - 22
        float p1, p2, p3, p4;


        if (distToEnemyHead == 0) p1 = 1;
        else p1 = (40.0f / distToEnemyHead) / 40;

        if (nearEnemyBody == Float.MIN_VALUE) p2 = -100;
        else p2 = (22    / nearEnemyBody)   / 22;

        if (lengthVsEnemyLength > 2) p3 = 0.0f;
        else p3 = lengthVsEnemyLength / 2;

        p4 = nearFood / 22;

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4*1000);


        return p1 + p2 + p3 + p4*1000;
    }

    // Our head is close to Enemy head value
    public static int distToEnemyHead(GameBoard gb, GameState state, int pNum, int eNum) {
        HeadPiece us = gb.getMyHead(state, pNum);
        HeadPiece enemy = gb.getMyHead(state, eNum);
        Dijkstra dk = new Dijkstra();
        Tile start = gb.get(us.getX(), us.getY());
        Tile end   = gb.get(enemy.getX(), enemy.getY());
        LinkedList<Tile> path = dk.path(start, end, gb);

        if (path == null) return 0;
        return path.size();
    }

    // Our head near enemy body value
    // This also is a death calc function
    public static float nearEnemyBody(GameBoard gb, GameState state, int pNum, int eNum) {
        HeadPiece us = gb.getMyHead(state, pNum);
        HeadPiece enemy = gb.getMyHead(state, eNum);

        // Set the enemy's head location as empty manually so we can path directly to it
        Dijkstra dk = new Dijkstra();
        gb.get(enemy.getX(), enemy.getY()).reset();
        Tile start = gb.get(us.getX(), us.getY());

        int totalDist = 0;
        int reachabel = 0;
        for (Tile t : gb.getEnemySnake()) {
            LinkedList<Tile> path = dk.path(start, t, gb);
            if (path != null) {
                totalDist += path.size();
                reachabel++;
            }
        }
        if (reachabel == 0) {
            HeadPiece enemyHead = gb.getMyHead(state, eNum);
            List<Tile> distToEnemyHead = dk.path(start, gb.get(enemy.getX(), enemyHead.getY()), gb);
            if (distToEnemyHead == null) return Float.MIN_VALUE;  // This means death
            return distToEnemyHead.size() * 1.0f;
        }

        return (totalDist * 1.0f) / reachabel;
    }

    // Get size of Us compared to Enemy Length
    public static float lengthOfEnemyBody(GameBoard gb, GameState state, int pNum, int eNum) {
        int us_body_length = gb.getUsSnake().size();
        int enemy_body_length = gb.getEnemySnake().size();
        return ( (us_body_length*1.0f) / enemy_body_length);
    }

    // Get Average of nearby food -> bigger is better
    public static float nearFood(GameBoard gb, GameState state, int pNum, int eNum) {
        List<Tile> foods = gb.getFoods();
        HeadPiece us = gb.getMyHead(state, pNum);
        Dijkstra dk = new Dijkstra();
        Tile start = gb.get(us.getX(), us.getY());

        int distance = 0;
        int reachableFoods = 0;
        for (Tile t : foods) {
            List<Tile> path = dk.path(start, t, gb);
            if (path != null) {
                reachableFoods += 1;
                distance += path.size();
            }
        }
        if (reachableFoods == 0) return 0;
        return ( reachableFoods / (distance*1.0f));
    }

}
