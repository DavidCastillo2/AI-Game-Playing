package HelperClasses.Reinforcement;

import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;
import HelperClasses.Reinforcement.USuckUnless;

import java.util.List;

public class QTable extends ReinforcmentLearner {
    public float[][] table = new float[5][4];
    // TABLE VISUALIZATION
    //                UP | LEFT | DOWN | RIGHT
    //            |------|------|------|------
    // SELF-BODY  |      |      |      |
    // ENEMY-BODY |      |      |      |
    // ENEMY-HEAD |      |      |      |
    // FOOD       |      |      |      |
    // EMPTY      |      |      |      |
    public void update(float gamma, float alpha, int x, int y, GameState previous, DirType previousDir, GameState state, List<DirType> available, int play_num){
        int cellType = getCellType(previous.getPiece(x, y), play_num);
        float old = table[cellType][MoveDirectionToNumber(previousDir)];
        float reward = rewardCalculator(previous, previousDir, play_num);
        float newQ = old + alpha*(reward + gamma*getBestValue(x,y,getCellType(state.getPiece(x, y), play_num),available,play_num) - old);
        table[cellType][MoveDirectionToNumber(previousDir)] = newQ;
    }

    public void update(float gamma, float alpha, int x, int y, GameState previous, DirType previousDir, FakeGameState state, List<DirType> available, int play_num){
        int cellType = getCellType(previous.getPiece(x, y), play_num);
        float old = table[cellType][MoveDirectionToNumber(previousDir)];
        float reward = rewardCalculator(previous, previousDir, play_num);
        float newQ = old + alpha*(reward + gamma*getBestValue(x,y,getCellType(state.getPiece(x, y), play_num),available,play_num) - old);
        table[cellType][MoveDirectionToNumber(previousDir)] = newQ;
    }

    private float rewardCalculator(GameState state, DirType dir, int play_num){
        return USuckUnless.grade(state, play_num, dir);
    }

    public float getBestValue(int x, int y, int cellType, List<DirType> available, int play_num){
        //next step is to then get the values for all surrounding moves that are available
        float[] row = table[cellType];
        float[] temp = new float[available.size()];
        int counter = 0;
        for(DirType dir: available){
            int dir_int = MoveDirectionToNumber(dir);
            temp[counter] = row[dir_int];
            counter++;
        }
        //then return the DirType of the one with the highest value
        return temp[highestIndex(temp)];
    }

    public DirType getBest(int x, int y, GameState state, List<DirType> available, int play_num){
        //first step is to find what row of the table we are supposed to be
        int cellType = getCellType(state.getPiece(x, y), play_num);
        //next step is to then get the values for all surrounding moves that are available
        float[] row = table[cellType];
        float[] temp = new float[available.size()];
        int counter = 0;
        for(DirType dir: available){
            int dir_int = MoveDirectionToNumber(dir);
            temp[counter] = row[dir_int];
            counter++;
        }
        //then return the DirType of the one with the highest value
        int highest = highestIndex(temp);
        return available.get(highest);
    }

    private int highestIndex(float[] array){
        int largest = 0;
        for(int i = 1; i < array.length; i++){
            if(array[i] > array[largest]) largest = i;
        }
        return largest;
    }
}
