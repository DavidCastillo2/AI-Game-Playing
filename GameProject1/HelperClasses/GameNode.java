package HelperClasses;

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
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    public GameNode(GameState state, GameNode parent, int connectingMove){
        this.state = state;
        this.parent = parent;
        this.connectingMove = connectingMove;
        this.children = new ArrayList<>();
        if (parent != null) parent.addChild(this);

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

    public int getAlpha(){
        return this.alpha;
    }

    public void setAlpha(int alpha){
        this.alpha = alpha;
    }

    public int getBeta(){
        return this.beta;
    }

    public void setBeta(int beta){
        this.beta = beta;
    }

}
