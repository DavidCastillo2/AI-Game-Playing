package HelperClasses;

import java.util.*;
import java.util.function.IntUnaryOperator;

public class Dijkstra {
    GameBoard gb;
    Graph graph;
    Tile start;
    public boolean ignoreForceFilled = false;


    public Dijkstra() {}

    public LinkedList<Tile> path(Tile start, Tile target, GameBoard gb) {
        if (this.gb != gb || this.start != start) {
            this.gb = gb;
            this.graph = new Graph(this.gb);
            this.start = start;
            this.dkTime();


            return this.graph.pathToTile(target);
        }
        return this.graph.pathToTile(target);
    }

    private void dkTime() {
        Node source = new Node(this.start);
        source.setDistance(0);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> not_visited = new ArrayList<>();

        not_visited.add(source);
        while (not_visited.size() != 0) {
            Node current = not_visited.remove(0);
            List<Node> children = this.graph.nearByTiles(current);
            if (this.ignoreForceFilled) children = this.graph.panicOpenTiles(current);
            int distanceToChild = current.distance + 1;

            for (Node n : children) {
                if (!visited.contains(n)) {
                    if (distanceToChild < n.getDistance()) {
                        n.setDistance(distanceToChild);
                        n.setPrev(current);
                        if (!not_visited.contains(n)) not_visited.add(n);
                    }
                }
            }

            visited.add(current);
        }
    }

    public void setPanicMode() {
        this.gb = null;  // To make us re-do Dijkstra
        this.ignoreForceFilled = true;
    }

    public void undoPanicMode() {
        this.gb = null;
        this.ignoreForceFilled = false;
    }

    private class Node {
        Tile tile;
        Integer distance = Integer.MAX_VALUE;
        Node lastNode = null;


        public Node(Tile t) {
            this.tile = t;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer d) {
            this.distance = d;
        }

        public void setPrev(Node n) { this.lastNode = n; }

        public Node getPrev() { return this.lastNode; }

        @Override
        public String toString() {
            return "Node{" +
                    "X=" + tile.getX() +
                    "  Y=" + tile.getY() +
                    '}';
        }
    }

    private class Graph {
        List<List<Node>> tiles = new ArrayList<>();
        GameBoard gb;

        public Graph(GameBoard gb) {
            this.gb = gb;
            fillGraph(gb);
        }

        public List<Node> panicOpenTiles(Node n) {
            List<Tile> tiles = this.gb.panicOpenTiles(n.tile);
            ArrayList<Node> retVal = new ArrayList<>();

            for (Tile t : tiles) {
                retVal.add(this.get(t.getX(), t.getY()));
            }
            return retVal;
        }

        public LinkedList<Tile> pathToTile(Tile goal) {
            LinkedList<Tile> retVal = new LinkedList<>();
            retVal.addFirst(goal);
            Node prev = this.get(goal.getX(), goal.getY()).getPrev();
            while (prev != null) {
                retVal.addFirst(prev.tile);
                prev = this.get(prev.tile.getX(), prev.tile.getY()).getPrev();
            }

            // This means we cannot reach the desired target
            if (retVal.size() == 1) {
                return null;
            }
            return retVal;
        }

        public void fillGraph(GameBoard gb) {
            for (int x=0; x < gb.max_X; x++) {
                ArrayList<Node> row = new ArrayList<>();
                for (int y = 0; y < gb.max_Y; y++) {
                    row.add(new Node(gb.get(x, y)));
                }
                this.tiles.add(row);
            }
        }

        public Node get(int x, int y) {
            return this.tiles.get(x).get(y);
        }

        public List<Node> nearByTiles(Node n) {
            List<Tile> tiles = this.gb.nearByTiles(n.tile);
            ArrayList<Node> retVal = new ArrayList<>();
            for (Tile t : tiles) {
                retVal.add(this.get(t.getX(), t.getY()));
            }

            return retVal;
        }
    }
}
