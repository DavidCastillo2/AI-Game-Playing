package HelperClasses;

import ProjectOneEngine.GameState;
import ProjectOneEngine.PlayerID;

import java.util.ArrayList;

public class GameNode {
    GameState state;
    GameNode parent;
    ArrayList<GameNode> children;
    int utility;
    // int represents the move that connects parent to this node
    int connectingMove;
    public int generation = -1;
    GameNode favoriteChild;

    public GameNode(GameState state, GameNode parent, int connectingMove){
        this.state = state;
        this.parent = parent;
        this.connectingMove = connectingMove;
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

    public void setGeneration(int gen) {
        this.generation = gen;
    }

    public boolean compareTo(GameState s) {
        // Identifying us and enemy
        PlayerID us = this.state.getCurPlayer();
        PlayerID enemy;
        if (us == PlayerID.TOP) {
            enemy = PlayerID.BOT;
        } else {
            enemy = PlayerID.TOP;
        }

        // Checking our stones VS stones in s's stones
        for (int i=0; i < 6; i++){
            int bin = this.state.getStones(us, i);
            if (bin != s.getStones(us, i)) return false;
        }

        // Checking enemy stones VS stones in s's stones
        for (int i=0; i < 6; i++) {
            int bin = this.state.getStones(enemy, i);
            if (bin != s.getStones(enemy, i)) return false;
        }

        // Check that our points VS their points match
        return this.state.getHome(us) == s.getHome(us);
    }


}
