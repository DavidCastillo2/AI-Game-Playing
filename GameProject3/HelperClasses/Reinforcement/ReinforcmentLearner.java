package HelperClasses.Reinforcement;

import HelperClasses.GameBoard;
import ProjectThreeEngine.*;

public abstract class ReinforcmentLearner {
    Snake agent;
    GameBoard enviroment;
    Move action;
    GameState state;
    //Heuristic reward;

    public ReinforcmentLearner(){
        //this.agent = agent;
        //this.enviroment = envo;
        //this.state = state;
    }

    public static int MoveDirectionToNumber(DirType direction){
        if(direction == DirType.North) return 0;
        if(direction == DirType.South) return 2;
        if(direction == DirType.East) return 1;
        if(direction == DirType.West) return 3;
        else return -1;
    }

    public static int MoveDirectionToNumber(Move action){
        return MoveDirectionToNumber(action.getDir());
    }

    public static int getCellType(GamePiece piece, int player_num){
        if(piece instanceof SnakePiece){
            if(((SnakePiece) piece).getNum() == player_num) return 0;
            return 1;
        }
        if(piece instanceof HeadPiece){
            if(((HeadPiece) piece).getNum() == player_num) return 0;
            return 2;
        }
        if(piece instanceof FoodPiece){
            return 3;
        }
        return 4;
    }
}
