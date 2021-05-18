package HelperClasses.Reinforcement;

import ProjectThreeEngine.AIGameText;
import ProjectThreeEngine.DirType;
import ProjectThreeEngine.GameState;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Trainer {
    public static void main(String[] argv) throws IOException {
        for(int i = 0; i < 500; i++) {
            AIGameText.run_game("GameProject3\\HelperClasses\\Reinforcement\\Q-Learning.txt");
            System.out.println("finished game "+i);
            Scanner scanner = new Scanner(new File("GameProject3\\HelperClasses\\Reinforcement\\Q-Learning.txt"));
            LinkedList<String> possibleLastGameStage = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("----")) {
                    if(possibleLastGameStage != null){
                        FakeGameState gameState = new FakeGameState(possibleLastGameStage);
                        QLearningImproved.self1.update(gameState);
                    }
                    possibleLastGameStage = new LinkedList<>();
                    continue;
                }
                if (possibleLastGameStage != null){
                    possibleLastGameStage.add(line);
                }
            }

        }
    }
}
