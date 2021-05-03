package Boot;

import HelperClasses.*;
import ProjectThreeEngine.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

// TODO snake runs into self?
//  It does this because there are not 100% safe movements to make except for one that points back into it's own body

//  This could be solved by choosing a Target only if when that target it chosen, it has spaces near it that are OPEN
//  OR have a path to another point that is greater than the enemy snake's length


public class BrainlessSnake implements Player {
    int us_num;
    int enemy_num;
    GameBoard gb;
    Dijkstra dk = new Dijkstra();
    int us_head_x;     // Currently Hardcoded For Initial Value
    int us_head_y;     // Currently Hardcoded For Initial Value
    int enemy_head_x;  // Currently Hardcoded For Initial Value
    int enemy_head_y;  // Currently Hardcoded For Initial Value
    boolean print = false;

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

    // Marks the tiles the enemy snake can reach as filled unless our head is inside one of those tiles
    public void markDangerTiles() {
        List<Tile> dangerTiles = this.gb.nearByTiles(this.enemy_head_x, this.enemy_head_y);
        if (dangerTiles.contains(this.gb.get(this.us_head_x, this.us_head_y)))
            return;  // Don't force fill if our head is there
        for (Tile t : dangerTiles)
            t.setForceFilled(true);
    }

    // This snake just moves up.
    @Override
    public DirType getMove(GameState state) {
        if (print) System.out.println();
        this.gb.update(state);  // Must be done every turn
        this.findEnemySnake();  // Update Enemy Snake's head Location
        this.markDangerTiles(); // Mark tiles the Enemy Snake can reach and filled
        if (print) System.out.println("DIVEBOMB ENTER -> Us_X: " + this.us_head_x + ", Us_Y: " + this.us_head_y + "  -> " + this.us_num);
        Tile target = this.diveBomb();
        if (print) System.out.print("DIVEBOMB EXIT -> Target_X: " + target.getX() + " Target_Y: " + target.getY());
        DirType retVal = getDir(target);
        if (print) System.out.println(" Dir: " + retVal);

        if (print) this.gb.printBoard();
        return retVal;
    }

    // Using a tile it chooses the Dir to reach that tile, updates this.Head vars
    public DirType getDir(Tile target) {
        // If the target is the same as our current location, that means no targets were reachable
        //      We get all open tiles next to our head
        if (target.getX() == this.us_head_x && target.getY() == this.us_head_y) {
            if (print) System.out.println(" PANIC!!");
            List<Tile> options = this.gb.panicOpenTiles(target);

            // There are no open tiles next to our head
            if (options.size() == 0) {  // Accept Death
                if (print) System.out.println(" WE DIE!");
                return DirType.South;

            // There are open tiles, so we find the one that results in the longest path
            } else {
                int longestPath = -1;
                Tile newTarget = target;

                // For every tile next to our head, find the longest path to a tile near us.
                // TODO maybe find a better target to aim for during this panic state?
                for (Tile t : options) {
                    this.dk.setPanicMode();
                    LinkedList<Tile> path = this.dk.path(target, t, gb);
                    this.dk.undoPanicMode();

                    if (path == null) continue;
                    int pathLength = path.size();
                    if (this.print) System.out.println(pathLength);
                    if (pathLength > longestPath) {
                        newTarget = t;
                        longestPath = pathLength;
                    }
                }
                if (this.print) System.out.println();
                target = newTarget;
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
        int foodDistance = 3;  // We look at a distance 3 for food.

        // If fearDistance gets this big, then its just GG i guess
        while (targets.size() == 0 && fearDistance < 15) {
            // Get the tiles around the Enemy's head
            targets = this.gb.nearByTiles(this.enemy_head_x, this.enemy_head_y, fearDistance);
            fearDistance += 1;
        }

        // Get the food tiles near us
        for (int i=0; i < foodDistance; i++) {
            List<Tile> nearBy = this.gb.nearByTiles(this.us_head_x, this.us_head_y, i);
            for (Tile t : nearBy) {
                if (t.isFood() && !targets.contains(t)) targets.add(t);
            }
        }
        return targets;
    }

    // Find the enemy snake head
    public void findEnemySnake() {
        // Cheap fix to GameState spawning a food on a person's HeadPiece
        if (this.gb.heads.size() == 1) {
            if (print) System.out.println(" ONLY 1 HEAD FOUND IN GAME BOARD!");
            HeadPiece h1 = this.gb.heads.get(0);
            if ((h1.getX() == this.us_head_x) && (h1.getY() == this.us_head_y)) {
                // fucking panic i guess
                if (print) System.out.println(" CANNOT FIND ENEMY HEAD?????");
                this.enemy_head_x = 0;
                this.enemy_head_y = 0;
            } else {
                // Correctly found enemy head
                this.enemy_head_x = h1.getX();
                this.enemy_head_y = h1.getY();
            }
            return;
        }

        // Find the enemy snake head and update member variables
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
