package TeamAIs;


import HelperClasses.Castle;
import ProjectTwoEngine.*;

import java.util.List;

public class CardCounter extends BaseBot {
    List<Monster> FakeDeck = DeckFactory.createDeck();
    /*
    the idea of this model is to count cards and probabilities
    as cards are used they are removed from the fake deck and
    it will prioritize winning a castle over anything else (maybe it focus on one castle first?)
    try to get away with the smallest monster value for a castle and still win with the smallest margin possible
    */

    @Override
    public void startOppTurn(GameState state) {
        System.out.println("\nENEMY -> Doing Stuff and Returning\n");
    }

    @Override
    public BuyMonsterMove getBuyMonster(GameState state) {
        System.out.print("Us -GetBuyMonster()  ->  "); // just for clean printing
        this.update(state);
        if (winningCastle(CastleID.CastleA)){ //winning castleA

        }
        else if (winningCastle(CastleID.CastleB)) { //winning castleB

        }
        else if (winningCastle(CastleID.CastleC)) { //winning castleB

        }
        return null;
    }

    private boolean winningCastle(CastleID id){
        Castle castle = cm.get(id);
        int value = castle.goodPoints - castle.badPoints;
        if (castle.hidden) value += 6;
        if (castle.badSlayer && (castle.goodDragon || castle.hidden)) value -= 6;
        if (castle.goodSlayer && castle.badDragon) value += 6;
        return value > 0;
    }

    @Override
    public RespondMove getRespond(GameState state, Monster mon, int price) {
        return null;
    }

    @Override
    public void stolenMonster(GameState state) {
        System.out.print("\nEnemy Stole Our Monster! The Following GameEngine Outputs Are For Enemy! \n");
    }

    @Override
    public PlaceMonsterMove getPlace(GameState state, Monster mon) {
        return null;
    }

    @Override
    public String getPlayName() {
        return "CardCounter";
    }
}
