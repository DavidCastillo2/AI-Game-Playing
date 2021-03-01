package MiniMax;

import ProjectOneEngine.GameState;

import java.util.ArrayList;

public class GameNode {
    GameState state;
    GameNode parent;
    ArrayList<GameNode> children;
    int utility;
    // int represents the move that connects parent to this node
    int connectingMove;
    GameNode favoriteChild;

    public GameNode(GameState state, GameNode parent, int i){
        this.state = state;
        this.parent = parent;
        this.connectingMove = i;
        this.children = new ArrayList<>();

    }

    public void addChild(GameNode child){
        this.children.add(child);
    }

    public GameState getGameState(){
        return this.state;
    }

    public void setUtility(int utility){
        this.utility = utility;
    }

    public int getUtility(){
        return this.utility;
    }

    public ArrayList<GameNode> getChildren(){
        return this.children;
    }

    public void setFavoriteChild(GameNode child){
        this.favoriteChild = child;
    }

    public GameNode getFavoriteChild(){
        return this.favoriteChild;
    }

    public int getConnectingMove(){
        return this.connectingMove;
    }

    /**
     * This is just a testing method for printing and ensuring utilities are what I think they are
     */
    public void printUtilities(){
        System.out.println("\nResults of moving to " + connectingMove + ": " + this.utility + "\n");
        ArrayList<Integer> utilities = new ArrayList<>();
        for (GameNode child: this.children){
            utilities.add(child.getUtility());
        }
        System.out.println(utilities + "\n");
        for (GameNode child: this.children){
            child.printUtilities();
        }
    }

}
