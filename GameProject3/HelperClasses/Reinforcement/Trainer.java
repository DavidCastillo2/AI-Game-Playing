package HelperClasses.Reinforcement;

import ProjectThreeEngine.AIGameText;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Trainer {
    public static void main(String[] argv) throws IOException {
        for(int i = 0; i < 500; i++) {
            AIGameText.run_game("GameProject3\\HelperClasses\\Reinforcement\\Q-Learning.txt");
            System.out.println("finished game "+i);
            DirType[] previousDirection = new DirType[2];
            int[] previousCellType = new int[2];
            GameState[] previousGameState = new GameState[2];
            int[] previousX = new int[2];
            int[] previousY = new int[2];
            Scanner scanner = new Scanner(new File("GameProject3\\HelperClasses\\Reinforcement\\Q-Learning.txt"));
            int winner = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineSplit = line.split(":");
                if (lineSplit.length > 1) {
                    if (lineSplit[1].trim().equals("Winner")) {
                        winner = Integer.parseInt(lineSplit[0].trim());
                        break;
                    }
                }
            }
            if (winner == 1) {
                QLearning.gameEnded(false, previousDirection[0], previousCellType[0], previousGameState[0], previousX[0], previousY[0], 0);
                QLearning.gameEnded(true, previousDirection[1], previousCellType[1], previousGameState[1], previousX[1], previousY[1], 1);
            } else {
                QLearning.gameEnded(true, previousDirection[0], previousCellType[0], previousGameState[0], previousX[0], previousY[0], 0);
                QLearning.gameEnded(false, previousDirection[1], previousCellType[1], previousGameState[1], previousX[1], previousY[1], 1);
            }
        }
    }
}
