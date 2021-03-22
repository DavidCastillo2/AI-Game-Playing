package Algorithms;

import ProjectOneEngine.PlayerID;

public class KickMe extends AlphaBeta {
    public KickMe(PlayerID self) {
        super(self);
    }

    // This just inverts the utility function so we think bad moves are good moves
    public int utilityFormula(int pointsDiff, int playerCapturable, int enemyCapturable) {
        return playerCapturable - pointsDiff - enemyCapturable;
    }
}
