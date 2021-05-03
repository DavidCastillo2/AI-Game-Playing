package Boot;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;

import java.util.ArrayList;
import java.util.List;

public class DontTreadOnMe implements Player {
    ArrayList<DirType> dirs = new ArrayList<>();
    int curMove = 0;

    public DontTreadOnMe(int startX, int startY, int enemyX, int enemyY) {  }

    @Override
    public void begin(GameState init_state, int play_num) {
        this.dirs.add(DirType.North);
        this.dirs.add(DirType.West);
        this.dirs.add(DirType.South);
        this.dirs.add(DirType.East);
    }

    @Override
    public DirType getMove(GameState state) {
        if (curMove + 1 == 5) curMove = 0;
        return dirs.get(curMove++);
    }

    @Override
    public String getPlayName() {
        return "Dont Tread On Me";
    }
}
