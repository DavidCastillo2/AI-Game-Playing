package Boot;

import HelperClasses.GameBoard;
import ProjectThreeEngine.*;

import java.util.List;

public class BrainlessSnake implements Player {
    int us_num;
    int enemy_num;
    Snake us;
    Snake enemy;
    List<FoodPiece> foods;
    GameBoard gb;

    @Override
    public void begin(GameState init_state, int play_num) {
        us_num = play_num;
        if (this.us_num == 0) {
            this.enemy_num = 1;
        } else {
            this.enemy_num = 0;
        }
        this.gb = new GameBoard(init_state, this.us_num, this.enemy_num);


    }

    // This snake just moves up.
    @Override
    public DirType getMove(GameState state) {
        this.gb.update(state);  // Must be done every turn
        this.gb.printBoard();
        keepAlive(state);

        return DirType.North;
    }

    public DirType keepAlive(GameState state) {
        return null;
    }


    @Override
    public String getPlayName() {
        return "Brainless";
    }
}
