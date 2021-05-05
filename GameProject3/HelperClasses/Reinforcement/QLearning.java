package HelperClasses.Reinforcement;

import HelperClasses.GameBoard;
import ProjectThreeEngine.*;
import com.sun.javafx.scene.DirtyBits;
import com.sun.security.jgss.GSSUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class QLearning extends ReinforcmentLearner {
    QTable qTable;
    public DirType previousDirection;
    public int previousCellType;
    public GameState previousGameState;
    public int previousX;
    public int previousY;
    int play_num;
    private static class QTable{
        static float learningValue = (float) 0.1;
        static float discountRate = (float) 0.9;
        static float[][] table = new float[4][5];
        // TABLE VISUALIZATION
        //                UP | LEFT | DOWN | RIGHT
        //            |------|------|------|------
        // SELF-BODY  |      |      |      |
        // ENEMY-BODY |      |      |      |
        // ENEMY-HEAD |      |      |      |
        // FOOD       |      |      |      |
        // EMPTY      |      |      |      |
        public QTable(){}

        public void update(DirType direction, int StartCell, GameState state, int x, int y, int player_num){
            //update the table based on some value
            //use basic version of points assigned to each square but also try out
            //Jaden's hueristic to see if that works as well
            float startValue = table[MoveDirectionToNumber(direction)-1][StartCell];
            GamePiece cell = state.getPiece(x, y);
            int cellType = getCellType(cell, player_num)-1;
            //System.out.println("CellType: "+cellType+" Reward: "+rewardForCell(cellType));
            float newValue = rewardForCell(cellType) + (discountRate*maxOfSurounding(x, y, cellType, direction)) - startValue;
            newValue = startValue + (learningValue*newValue);
            //System.out.println("Direction: "+direction+" X: "+x+" Y: "+y+" newValue: "+newValue);
            table[StartCell][MoveDirectionToNumber(direction)] = newValue;

            //maybe change learning value and discount rate
        }

        float rewardForCell(int cellType){
            if(cellType == 0) return 0;
            if(cellType == 1) return -10;
            if(cellType == 2) return 0;
            if(cellType == 3) return 5;
            return 0;
        }

        static float maxOfSurounding(int x, int y, int cellType, DirType previousDirection){
            int EastCover = 0, WestCover = 0, SouthCover = 0, NorthCover = 0;
            if((x+1) > 15 || previousDirection==DirType.East){
                //East direction is cut out
                EastCover = -30;
            }
            if((x-1) < 0 || previousDirection==DirType.West){
                //West direction is cut out
                WestCover = -30;
            }
            if((y+1) > 15 || previousDirection==DirType.South){
                //South direction is cut out
                SouthCover = -30;
            }
            if((y-1) < 0 || previousDirection==DirType.North){
                //North direction is cut out
                NorthCover = -30;
            }
            float EastMax = Math.max(EastCover, table[MoveDirectionToNumber(DirType.East)-1][cellType]);
            float WestMax = Math.max(WestCover, table[MoveDirectionToNumber(DirType.West)-1][cellType]);
            float SouthMax = Math.max(SouthCover, table[MoveDirectionToNumber(DirType.South)-1][cellType]);
            float NorthMax = Math.max(NorthCover, table[MoveDirectionToNumber(DirType.North)-1][cellType]);
            float HorizontalMax = Math.max(EastMax, WestMax);
            float VerticalMax = Math.max(SouthMax, NorthMax);
            return Math.max(HorizontalMax, VerticalMax);
        }

        public static void updateGameOver(DirType direction, int StartCell, GameState state, int x, int y, int player_num, boolean WinOrLost){
            float startValue = table[StartCell][MoveDirectionToNumber(direction)];
            GamePiece cell = state.getPiece(x, y);
            int cellType = getCellType(cell, player_num);
            float newValue = (WinOrLost?50:-50) + (discountRate*maxOfSurounding(x, y, cellType, direction)) - startValue;
            newValue = startValue + (learningValue*newValue);
            table[StartCell][MoveDirectionToNumber(direction)] = newValue;
        }

        public void loadFromFile()throws FileNotFoundException{
            File qtable = new File("GameProject3\\HelperClasses\\Reinforcement\\Q-Table.txt");
            Scanner scanner = new Scanner(qtable);
            List<String> lines = new LinkedList<>();
            while(scanner.hasNextLine()){
                lines.add(scanner.nextLine());
            }
            scanner.close();
            String line = lines.get(0);
            table = stringToTable(line);
        }

        public static void writeToFile() throws IOException {
            File qtable = new File("GameProject3\\HelperClasses\\Reinforcement\\Q-Table.txt");
            FileWriter writer = new FileWriter(qtable);
            //System.out.println("writing this to the file: "+Arrays.deepToString(table));
            writer.write(Arrays.deepToString(table));
            writer.close();
        }

        float[][] stringToTable(String line){
            line = line.strip();
            line = line.replace("[","");
            line = line.substring(0, line.length()-2);
            String[] line1 = line.split("],");
            float[][] tempTable = new float[4][5];
            for(int i = 0; i < line1.length; i++){
                line1[i] = line1[i].trim();
                String[] single_float = line1[i].split(", ");
                for(int j = 0; j < single_float.length; j++){
                    tempTable[i][j] = Float.parseFloat(single_float[j]);
                }
            }
            return tempTable;
        }
    }

    public QLearning(Snake agent, GameBoard envo, GameState state, int play_num){
        super(agent, envo, state);
        //Initialize Q-Table
        this.qTable = new QTable();
        try {
            this.qTable.loadFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.previousCellType = -1;
        this.previousDirection = null;
        this.previousGameState = state;
        this.previousX = -1;
        this.previousY = -1;
        this.play_num = play_num;
    }

    public DirType decideMove(GameState state, HeadPiece head){
        //maybe call learner here on the change in gamestate?
        learner(this.previousGameState);

        //select and action to perform
        //remove invalid moves
        List<DirType> validDirections = new LinkedList<>(Arrays.asList(DirType.North, DirType.East,DirType.South,DirType.West));
        int x = head.getX();
        int y = head.getY();
        GamePiece piece = state.getPiece(x, y);
        if((x+1) >= 15 || previousDirection==DirType.West){
            //East direction is cut out
            validDirections.remove(DirType.East);
        }
        if((x-1) < 0 || previousDirection==DirType.East){
            //West direction is cut out
            validDirections.remove(DirType.West);
        }
        if((y+1) >= 15 || previousDirection==DirType.North){
            //South direction is cut out
            validDirections.remove(DirType.South);
        }
        if((y-1) < 0 || previousDirection==DirType.South){
            //North direction is cut out
            validDirections.remove(DirType.North);
        }
        //determine the best direction out of valid ones
        DirType bestDirection = DirType.North;
        float bestValue = -10000;
        int cellType = getCellType(piece, this.play_num);
        for(DirType dirType: validDirections){
            float tempValue = QTable.table[cellType][MoveDirectionToNumber(dirType)];
            float random = (float) Math.floor(Math.random()*6);
            if(tempValue > bestValue && random > 4){
                bestValue = tempValue;
                bestDirection = dirType;
            }
        }
        //update all previous values to update Q-Table next round
        this.previousY = y;
        this.previousX = x;
        this.previousGameState = state;
        this.previousCellType = cellType-1;
        this.previousDirection = bestDirection;
        //perform selected action
        return bestDirection;
    }

    public void learner(GameState state){
        if(this.previousDirection != null && this.previousCellType != -1){
            //System.out.println("Previous Direction: "+this.previousDirection+" Previous Cell Type: "+this.previousCellType);
            this.qTable.update(this.previousDirection, this.previousCellType, state, this.previousX, this.previousY, this.play_num);
            try {
                QTable.writeToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void gameEnded(boolean WinOrLost, DirType previousDirection, int previousCellType, GameState previousGameState,
                                 int previousX, int previousY, int play_num) throws IOException {
        if(previousDirection != null && previousCellType != -1){
            QTable.updateGameOver(previousDirection, previousCellType, previousGameState, previousX, previousY, play_num, WinOrLost);
        }
        QTable.writeToFile();
    }
}
