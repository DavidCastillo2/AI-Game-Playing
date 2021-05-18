package HelperClasses.Reinforcement;

import ProjectThreeEngine.*;

import java.util.LinkedList;

public class FakeGameState {
    GamePiece[][] board = new GamePiece[15][15];
    HeadPiece head1, head2 = null;

    public FakeGameState(LinkedList<String> gameState){
        int play_num = -1;
        boolean makingSnake = false;
        boolean foundHead = false;
        boolean foodPieces = false;
        String direction = "";
        DirType dirType = null;
        for(String line: gameState){
            if(line.split(" ")[0].equals("Snake")){
                play_num = Integer.parseInt(line.split(" ")[1]);
                foodPieces = false;
                makingSnake = true;
                foundHead = false;
                continue;
            }
            if(line.split(" ")[0].equals("Foods")){
                play_num = Integer.parseInt(line.split(" ")[1]);
                foodPieces = true;
                makingSnake = false;
                foundHead = false;
                continue;
            }
            if(makingSnake){
                if(!line.split(" ")[0].equals("(")){
                    direction = line;
                    if(direction.equals("North")) dirType = DirType.North;
                    if(direction.equals("South")) dirType = DirType.South;
                    if(direction.equals("East")) dirType = DirType.East;
                    if(direction.equals("West")) dirType = DirType.West;
                    continue;
                }
                if(!foundHead){
                    foundHead = true;
                    String[] splitLine = line.trim().split(",");
                    int x = Integer.parseInt(splitLine[0].substring(1));
                    int y = Integer.parseInt(splitLine[0].substring(0,1));
                    HeadPiece tempHead = new HeadPiece(x, y, play_num, dirType);
                    if(play_num == 0) head1 = tempHead;
                    if(play_num == 1) head2 = tempHead;
                    board[x][x] = tempHead;
                }
                else{
                    String[] splitLine = line.trim().split(",");
                    int x = Integer.parseInt(splitLine[0].substring(1));
                    int y = Integer.parseInt(splitLine[0].substring(0,1));
                    SnakePiece tempPiece = new SnakePiece(x, y, play_num);
                    board[x][x] = tempPiece;
                }
            }
            if(foodPieces){
                String[] splitLine = line.trim().split(",");
                int x = Integer.parseInt(splitLine[0].substring(1));
                int y = Integer.parseInt(splitLine[0].substring(0,1));
                FoodPiece tempPiece = new FoodPiece(x, y);
                board[x][x] = tempPiece;
            }
        }
    }

    public GamePiece getPiece(int x, int y){
        return board[x][y];
    }

    public HeadPiece getHead(int play_num){
        if(play_num == 0) return head1;
        return head2;
    }
}
