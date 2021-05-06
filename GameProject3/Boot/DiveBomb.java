package Boot;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;

import java.util.ArrayList;

public class DiveBomb implements Player {
    ArrayList<DirType> moves = new ArrayList<>();
    int curMove = 0;
    String num;

    // Just a testing class that can simulate the crashing into stuff behaviors.
    public DiveBomb(int playerNum) {
        this.num = "Player " + String.valueOf(playerNum+1);
        headOn(playerNum);
        Player2Wins(playerNum);
        Player1Wins(playerNum);
        // if (playerNum == 0)
            PlayerInsideSelf(playerNum);
    }

    public void PlayerInsideSelf(int playerNum) {
        this.moves = new ArrayList<>();

        if (playerNum == 0) this.moves.add(DirType.South);
        else this.moves.add(DirType.North);
        if (playerNum == 0) this.moves.add(DirType.East);
        else this.moves.add(DirType.West);
        if (playerNum == 0) this.moves.add(DirType.West);
        else this.moves.add(DirType.East);
    }

    public void headOn(int playerNum) {
        this.moves = new ArrayList<>();
        for (int i=0; i < 7; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
        for (int i=0; i < 5; i++) {
            if (playerNum == 0) moves.add(DirType.East);
            if (playerNum == 1) moves.add(DirType.West);
        }
        for (int i=0; i < 5; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
    }

    public void Player2Wins(int playerNum) {
        this.moves = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
        int width = 0;
        if (playerNum == 0) width = 4;
        else width = 5;
        for (int i=0; i < width; i++) {
            if (playerNum == 0) moves.add(DirType.East);
            if (playerNum == 1) moves.add(DirType.West);
        }
        for (int i=0; i < 5; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
    }

    public void Player1Wins(int playerNum) {
        this.moves = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
        int width = 0;
        if (playerNum == 1) width = 4;
        else width = 5;
        for (int i=0; i < width; i++) {
            if (playerNum == 0) moves.add(DirType.East);
            if (playerNum == 1) moves.add(DirType.West);
        }
        for (int i=0; i < 5; i++) {
            if (playerNum == 0) moves.add(DirType.South);
            if (playerNum == 1) moves.add(DirType.North);
        }
    }

    @Override
    public void begin(GameState init_state, int play_num) {
    }

    @Override
    public DirType getMove(GameState state) {
        try {
            return this.moves.get(curMove++);
        } catch (Exception e) {
            return DirType.South;
        }
    }

    @Override
    public String getPlayName() {
        return this.num;
    }
}
