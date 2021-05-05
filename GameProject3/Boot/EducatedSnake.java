package Boot;

import HelperClasses.GameBoard;
import HelperClasses.Reinforcement.QLearning;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Player;

public class EducatedSnake implements Player{
    QLearning brain;
    GameBoard board;
    int play_num;
    public static DirType[] previousDirection = new DirType[2];
    public static int[] previousCellType = new int[2];
    public static GameState[] previousGameState = new GameState[2];
    public static int[] previousX = new int[2];
    public static int[] previousY = new int[2];
    @Override
    public void begin(GameState init_state, int play_num){
        this.play_num = play_num;
        this.board = new GameBoard(init_state, play_num, (play_num==0?1:0));
        this.brain = new QLearning(init_state.getSnake(play_num), this.board, init_state, play_num);
    }

    @Override
    public DirType getMove(GameState state) {
        this.board.update(state);
        DirType move = this.brain.decideMove(state, this.board.getMyHead(state, this.play_num));
        previousDirection[this.play_num] = this.brain.previousDirection;
        previousCellType[this.play_num] = this.brain.previousCellType;
        previousGameState[this.play_num] = this.brain.previousGameState;
        previousX[this.play_num] = this.brain.previousX;
        previousY[this.play_num] = this.brain.previousY;
        return move;
    }

    @Override
    public String getPlayName() {
        return "I went to College!";
    }
}
