package Boot;

import HelperClasses.*;
import ProjectThreeEngine.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class BrainlessSnake implements Player {
    int us_num;
    int enemy_num;
    GameBoard gb;
    Dijkstra dk = new Dijkstra();
    int us_head_x;     // Currently Hardcoded For Initial Value
    int us_head_y;     // Currently Hardcoded For Initial Value
    int enemy_head_x;  // Currently Hardcoded For Initial Value
    int enemy_head_y;  // Currently Hardcoded For Initial Value

    public BrainlessSnake(int usX, int usY, int badX, int badY) {
        // Sad hard coded section
        us_head_x = usX;
        us_head_y = usY;
        enemy_head_x = badX;
        enemy_head_y = badY;
    }

    @Override
    public void begin(GameState init_state, int play_num) {
        // System.out.println(play_num);
        us_num = play_num;
        if (this.us_num == 0) {
            this.enemy_num = 1;
        } else {
            this.enemy_num = 0;
        }
        this.gb = new GameBoard(init_state, this.us_num, this.enemy_num);  // update out GameBoard
    }

    // Marks the tiles the enemy snake can reach as filled.
    public void markDangerTiles() {
        List<Tile> dangerTiles = this.gb.nearByTiles(this.enemy_head_x, this.enemy_head_y);
        for (Tile t : dangerTiles) {
            t.setForceFilled(true);
        }
    }

    // This snake just moves up.
    @Override
    public DirType getMove(GameState state) {
        //System.out.println();
        this.gb.update(state);  // Must be done every turn
        this.findEnemySnake();  // Update Enemy Snake's head Location
        this.markDangerTiles(); // Mark tiles the Enemy Snake can reach and filled
        // System.out.println(this.us_head_x + ", " + this.us_head_y + "  - " + this.us_num);

        Tile target = this.diveBomb();
        // System.out.print("Target_X: " + target.getX() + " Target_Y: " + target.getY());
        DirType retVal = getDir(target);
        // System.out.println(" Dir: " + retVal);

        // this.gb.printBoard();
        return retVal;
    }

    // Using a tile it chooses the Dir to reach that tile, updates this.Head vars
    public DirType getDir(Tile target) {
        if (target.getX() == this.us_head_x && target.getY() == this.us_head_y) {
            List<Tile> options = this.gb.panicOpenTiles(target);

            if (options.size() == 0) {  // Accept Death
                return DirType.South;
            } else {
                target = options.get(0);
            }
        }

        int dist_x = this.us_head_x - target.getX();
        int dist_y = this.us_head_y - target.getY();
        assert(dist_x != 0 && dist_y != 0);

        if (abs(dist_x) > abs(dist_y)) {
            if (dist_x > 0) {
                this.us_head_x -= 1;
                return DirType.West;
            } else {
                this.us_head_x += 1;
                return DirType.East;
            }
        } else if(dist_y > 0) {
            this.us_head_y -= 1;
            return DirType.North;
        } else {
            this.us_head_y += 1;
            return DirType.South;
        }

    }

    // The goal of this is to cut off our opponent and make them run into us and that's it no food
    // Returns Our Snake Head's Current Location When No Targets Are Reachable.
    // TODO The MarkDangerTiles makes it possible to make the AI think we cannot go anywhere. FIX?
    public Tile diveBomb() {
        List<Tile> targets = this.findTargets();
        Tile us_tile = this.gb.get(this.us_head_x, this.us_head_y);
        LinkedList<Tile> shortestPath = null;

        // Chose the shortest path
        //System.out.println("HeadX: " + this.us_head_x + " HeadY: " + this.us_head_y);
        //System.out.println("EnemyX: " + this.enemy_head_x + " EnemyY: " + this.enemy_head_y);
        // System.out.println("Targets - " + targets);
        for (Tile target : targets) {
            LinkedList<Tile> path = this.dk.path(us_tile, target, this.gb);

            if (path != null) {  // means we couldn't reach the desired target
                // System.out.println("Path - " + path);
                if (shortestPath == null) {
                    shortestPath = path;
                } else if (path.size() < shortestPath.size()) {
                    shortestPath = path;
                }
            }
        }
        // System.out.println("Shortest Path - " + shortestPath);
        if (shortestPath == null) return us_tile;
        return shortestPath.get(1);
    }

    // Find targets 2 spots away from the enemy snake's head
    public List<Tile> findTargets() {
        List<Tile> targets = new ArrayList<>();  // our return value
        int fearDistance = 2;  // Ideally we're 2 spots away from their head, increases when we can't do that.

        // If fearDistance gets this big, then its just GG i guess
        while (targets.size() == 0 && fearDistance < 15) {
            targets = this.gb.nearByTiles(this.enemy_head_x, this.enemy_head_y, fearDistance);
            fearDistance += 1;

        }
        return targets;
    }

    // Find the enemy snake head
    public void findEnemySnake() {
        HeadPiece h1 = this.gb.heads.get(0);
        HeadPiece h2 = this.gb.heads.get(1);
        HeadPiece enemyHead;
        if ((h1.getX() == this.us_head_x) && (h1.getY() == this.us_head_y)) {
            enemyHead = h2;
        } else {
            enemyHead = h1;
        }
        this.enemy_head_x = enemyHead.getX();
        this.enemy_head_y = enemyHead.getY();
    }


    @Override
    public String getPlayName() {
        if (this.us_head_x == 11)
        return """
                ∵*.•´¸.•*´✶´♡
                ° ☆ °˛*˛☆_Π______*˚☆*
                ˚ ˛★˛•˚*/______/ ~⧹。˚˚
                ˚ ˛•˛•˚ ｜ 田田 ｜門｜ ˚*
                \uD83C\uDF37╬╬\uD83C\uDF37╬╬\uD83C\uDF37╬╬\uD83C\uDF37╬╬\uD83C\uDF37
                
                
                """;
        return "Ima house";
    }
}
