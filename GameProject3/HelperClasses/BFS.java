package HelperClasses;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BFS {

    public static List<Tile> search(int x, int y, int target_x, int target_y, GameBoard gb) {
        ArrayList<Tile> visited = new ArrayList<>();  // Could do a HashTable to make it faster but eh its smol map
        LinkedList<Tile> queue = new LinkedList<>(gb.nearByTiles(x, y));
        ArrayList<Tile> retVal = new ArrayList<>();

        System.out.print("Queue Starting Size: ");
        System.out.println(queue.size());

        while(queue.size() != 0) {
            // peep our queue
            Tile t = queue.poll();
            retVal.add(t);

            // We found it
            if (t.x == target_x && t.y == target_y) return retVal;

            // Add unseen children to the queue
            for (Tile next: gb.nearByTiles(t.x, t.y)) {
                if (!visited.contains(next) && (next.isEmpty())) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        return null; // Could not be found
    }

    public static List<Tile> search(Tile start, Tile goal, GameBoard gb) {
        return search(start.x, start.y, goal.x, goal.y, gb);
    }
}

// Just for printing reasons maybe?
class TileNode extends Tile {
    boolean isPath = false;

    public TileNode(int X, int Y, int usNum) {
        super(X, Y, usNum);
    }

    public TileNode(Tile t) {
        super(t.x, t.y, t.us_num);
    }
}

