package HelperClasses;

import ProjectOneEngine.GameRules;
import ProjectOneEngine.GameState;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class FullTree {
    GameNode root = null;
    GameState curState;
    ArrayList<GameNode> lastRow;
    ArrayList<GameNode> enemyMoveRow;

    public FullTree() { }

    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future
    */
    private ArrayList<GameNode> generateStates(GameNode curNode) {
        PlayerID cur_player = curNode.getGameState().getCurPlayer();
        for (int i = 0; i<6; i++){
            if (curNode.getGameState().getStones(cur_player, i) > 0){
                GameState childState = GameRules.makeMove(curNode.getGameState(), i);
                GameNode childNode = new GameNode(childState, curNode, i);
                curNode.addChild(childNode);
                curNode.setGeneration(i);
            }
        }
        return curNode.getChildren();
    }

    /*
    Takes in a current GameState, returns a list of possible GameState's
    one-turn in the future.

    Adds a generation to the node when added!
    */
    private ArrayList<GameNode> generateStatesWithName(GameNode curNode, int generation) {
        PlayerID cur_player = curNode.getGameState().getCurPlayer();
        for (int i = 0; i<6; i++){
            if (curNode.getGameState().getStones(cur_player, i) > 0){
                GameState childState = GameRules.makeMove(curNode.getGameState(), i);
                GameNode childNode = new GameNode(childState, curNode, i);
                curNode.addChild(childNode);
                curNode.setGeneration(generation);
            }
        }
        return curNode.getChildren();
    }

    /*
    Builds a tree, where each Node contains a GameState, parent, and children.
     */
    public GameNode buildTree(GameState curState, int depth) {

        // Check to see if we already have a tree, append to it if we do.
        if (this.root != null) {
            this.root = this.reformatTree(curState, depth);
            this.curState = curState;
            return this.root;
        }

        // Create out root
        GameNode root = new GameNode(curState, null, -1);
        this.root = root;
        this.curState = curState;
        ArrayList<GameNode> cur  = new ArrayList<>();
        ArrayList<GameNode> next = new ArrayList<>();
        cur.add(this.root);

        // For first 2 layers, isolating the Enemy move
        for (int i=0; i < 2; i++) {
            next = new ArrayList<>();

            // For each node in current layer
            for (GameNode child: cur) {
                next.addAll(generateStates(child)); // Get all children
            }

            // Store enemy Move Row
            if (i == 1) this.enemyMoveRow = next;
            cur = next;
        }

        // All layers beyond 2
        for (int i=2; i < depth; i++) {
            next = new ArrayList<>();

            // For each node in current layer
            for (GameNode child: cur) {

                // Get all children
                next.addAll(generateStatesWithName(child, child.generation));
            }
            cur = next;
        }

        this.lastRow = next;
        return root;
    }

    // Only called when a tree already exists and is handed a new GameState
    private GameNode reformatTree(GameState curState, int depth) {

        // Find the gameState that is our new root
        GameNode oldRoot = this.root;
        int generation = -1;
        for (GameNode child : this.enemyMoveRow) {
            if (child.compareTo(curState)) {
                this.root = child;
                this.root.parent = null;
                this.root.connectingMove = -1;
                generation = child.generation;
                System.out.println("GENERATION FOUND!");
                break;
            }
        }
        // If we couldn't find the gameState inside our tree, go hog wild
        if (this.root == oldRoot) {
            System.out.println("FUCKIGN PANIC\n\n\n");
        }

        // Iterate through our last row, and generate our new states for 2 Levels
        ArrayList<GameNode> newChildren = new ArrayList<>();
        for (int i=0; i < 2; i++){
            newChildren = new ArrayList<>();
            for (GameNode c : this.lastRow) {
                // If it is part of the possible gameStates we can see in the future, generateStates
                if (c.generation == generation) {
                    newChildren.addAll(this.generateStates(c));
                }
            }
        }
        this.lastRow = newChildren;

        // Find the New EnemyMoveRow
        ArrayList<GameNode> cur  = new ArrayList<>();
        ArrayList<GameNode> next = new ArrayList<>();
        cur.add(this.root);

        // For first 2 layers, isolating the Enemy move
        for (int i=0; i < 2; i++) {
            next = new ArrayList<>();

            // For each node in current layer
            for (GameNode child: cur) {
                if (child.generation == generation)
                    next.addAll(child.getChildren()); // Get all children
            }
            cur = next;
        }
        // Store EnemyMoveRow
        this.enemyMoveRow = next;
        return this.root;
    }
}























