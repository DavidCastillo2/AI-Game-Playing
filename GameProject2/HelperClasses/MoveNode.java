package HelperClasses;

import ProjectTwoEngine.GameState;
import ProjectTwoEngine.Move;

import java.util.ArrayList;


public class MoveNode {
    GameState state;
    MoveNode parent;
    ArrayList<MoveNode> children;
    int utility;
    // int represents the move that connects parent to this MoveMoveNode
    Move connectingMove;
    MoveNode favoriteChild;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    public MoveNode(GameState state, MoveNode parent, Move connectingMove){
        this.state = state;
        this.parent = parent;
        this.connectingMove = connectingMove;
        this.children = new ArrayList<>();
        if (parent != null) parent.addChild(this);

    }

    public void addChild(MoveNode child){
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

    public ArrayList<MoveNode> getChildren(){
        return this.children;
    }

    public void setFavoriteChild(MoveNode child){
        this.favoriteChild = child;
    }

    public MoveNode getFavoriteChild(){
        return this.favoriteChild;
    }

    public Move getConnectingMove(){
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
