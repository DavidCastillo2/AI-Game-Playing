package Algorithms;

import ProjectOneEngine.PlayerID;

// This uses the AlphaBeta algorithm but its utility is just GIVE ME MOAR POINTS
public class GreedyAlphaBeta extends AlphaBeta {

    public GreedyAlphaBeta(PlayerID self) {
        super(self);
    }

    public int utilityFormula(int pointsDiff, int playerCapturable, int enemyCapturable) {
        return pointsDiff;
    }
}
