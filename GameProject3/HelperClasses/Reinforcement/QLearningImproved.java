package HelperClasses.Reinforcement;

import HelperClasses.GameBoard;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.HeadPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QLearningImproved extends ReinforcmentLearner {
    final static float epsilon = (float)0.10;
    final static float gamma = (float)0.50;
    final static float alpha = (float)0.50;
    static QLearningImproved self1 = new QLearningImproved(0);
    static QLearningImproved self2 = new QLearningImproved(1);
    static QTable table = new QTable();

    GameState previous;
    int play_num;

    private QLearningImproved(int play_num){
        super();
        this.play_num = play_num;
    }

    public void initialize(GameState init, int play_num, int anti_play_num){
        this.previous = init;
    }

    public DirType getMove(GameState state){
        List<DirType> available = availableMoves(state);
        float random = (float) Math.random();
        DirType temp;
        HeadPiece head = GameBoard.getMyHead(state, this.play_num);
        int x = head.getX(), y = head.getY();
        if(state != this.previous) table.update(gamma, alpha, x, y, this.previous, GameBoard.getMyHead(this.previous, this.play_num).getDir(),
                state, available, this.play_num);
        if(random<epsilon){
            int index = (int)Math.floor(Math.random()*(available.size()-1));
            temp = available.get(index);
        } else {
            temp = table.getBest(x, y, this.previous, available, this.play_num);
        }
        return temp;
    }

    private List<DirType> availableMoves(GameState state){
        List<DirType> temp = new ArrayList<>(Arrays.asList(DirType.North,DirType.South,DirType.East,DirType.West));
        GameBoard tempBoard = new GameBoard(state, this.play_num, this.play_num==0?1:0);
        HeadPiece head = GameBoard.getMyHead(state, this.play_num);
        int x = head.getX(), y = head.getY();
        if(x == 0) temp.remove(DirType.West);
        if(x == 15) temp.remove(DirType.East);
        if(y == 0) temp.remove(DirType.South);
        if(y == 15) temp.remove(DirType.North);
        return temp;
    }

    private List<DirType> availableMoves(FakeGameState state){
        List<DirType> temp = new ArrayList<>(Arrays.asList(DirType.North,DirType.South,DirType.East,DirType.West));
        HeadPiece head = state.getHead(this.play_num);
        int x = head.getX(), y = head.getY();
        if(x == 0) temp.remove(DirType.West);
        if(x == 15) temp.remove(DirType.East);
        if(y == 0) temp.remove(DirType.South);
        if(y == 15) temp.remove(DirType.North);
        return temp;
    }

    public void update(FakeGameState gameState){
        HeadPiece head1 = gameState.head1;
        HeadPiece head2 = gameState.head2;
        table.update(gamma, alpha, head1.getX(), head1.getY(), previous, head1.getDir(), gameState, availableMoves(gameState), 0);
        table.update(gamma, alpha, head2.getX(), head2.getY(), previous, head2.getDir(), gameState, availableMoves(gameState), 1);
    }

    public static QLearningImproved getSelf(int play_num){
        if(play_num == 0) return self1;
        return self2;
    }

    public static QLearningImproved resetSelf(int play_num){
        if(play_num == 0){
            self1 = new QLearningImproved(0);
            return self1;
        }
        else{
            self2 = new QLearningImproved(1);
            return self2;
        }
    }
}
