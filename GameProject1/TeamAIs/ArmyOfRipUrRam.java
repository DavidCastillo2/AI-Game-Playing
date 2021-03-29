package TeamAIs;

import Algorithms.*;
import ProjectOneEngine.*;


public class ArmyOfRipUrRam implements Player {
    Algorithm chosenAlg = null;
    private String algType;

    public ArmyOfRipUrRam(String algType) {
        setAlgType(algType);
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }

    public Move getMove(GameState state) {
        System.out.println("");  // Just to help make it clear what move was made
        boolean done = false;
        Move move = null;

        this.chosenAlg = algFactory(this.algType, state.getCurPlayer());

        while (!done) {
            move = this.chosenAlg.findMove(state, this.chosenAlg.depth);
            if (GameRules.makeMove(state, move) != null){
                done = true;
            }
        }
        nicePrint(state);
        return move;
    }

    public Algorithm algFactory(String typeAlg, PlayerID curPlayer) {
        if (this.chosenAlg == null) {
            return switch (typeAlg.toLowerCase()) {
                case "alphabeta" -> new AlphaBeta(curPlayer);
                case "minimax" -> new MiniMax(curPlayer);
                case "mostpoints" -> new MostPoints(curPlayer);
                case "greedyalphabeta" -> new GreedyAlphaBeta(curPlayer);
                case "kickme" -> new KickMe(curPlayer);
                default -> null;
            };
        }
        return this.chosenAlg;
    }

    public String getPlayName(){
        return this.algType;
    }

    public void nicePrint(GameState state) {
        System.out.print("Player: ");
        System.out.println(state.getHome(state.getCurPlayer()));
        System.out.print("Enemy:  ");
        System.out.print(state.getHome(PlayerID.TOP));
        System.out.print("\t\t\tDifference: ");
        System.out.print(state.getHome(state.getCurPlayer()) - state.getHome(PlayerID.TOP));
        System.out.print("\t\tDepth: ");
        System.out.println(this.chosenAlg.depth);
    }

}
