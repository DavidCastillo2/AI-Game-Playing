package HelperClasses.Reinforcement;

import HelperClasses.GameBoard;
import ProjectThreeEngine.*;

public abstract class ReinforcmentLearner {
    Snake agent;
    GameBoard enviroment;
    Move action;
    GameState state;
    //Heuristic reward;

    public ReinforcmentLearner(Snake agent, GameBoard envo, GameState state){
        this.agent = agent;
        this.enviroment = envo;
        this.state = state;
    }

    public static int MoveDirectionToNumber(DirType direction){
        if(direction == DirType.North) return 1;
        if(direction == DirType.South) return 3;
        if(direction == DirType.East) return 2;
        if(direction == DirType.West) return 4;
        else return 0;
    }

    public static int MoveDirectionToNumber(Move action){
        return MoveDirectionToNumber(action.getDir());
    }

    public static int getCellType(GamePiece piece, int player_num){
        if(piece instanceof SnakePiece){
            if(((SnakePiece) piece).getNum() == player_num) return 1;
            return 2;
        }
        if(piece instanceof HeadPiece){
            if(((HeadPiece) piece).getNum() == player_num) return 1;
            return 3;
        }
        if(piece instanceof FoodPiece){
            return 4;
        }
        return 5;
    }
}
