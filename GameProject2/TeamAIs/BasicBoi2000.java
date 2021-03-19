package TeamAIs;

import HelperClasses.Castle;
import ProjectTwoEngine.*;

import java.util.List;


public class BasicBoi2000 extends BaseBot {
    //This function is called at the start of your opponent's turn
    @Override
    public void startOppTurn(GameState state) {

    }

    //This function is called when the player must select a monster to buy
    @Override
    public BuyMonsterMove getBuyMonster(GameState state) {
        System.out.print("\nUs -  GetBuyMonster  ->  "); // just for clean printing
        this.update(state);

        // Look at the monsters
        int biggestMonster = -1;
        Monster chosenOne = null;
        for (Monster m : this.currMonsters) {
            if (m.value > biggestMonster) chosenOne = m;
        }

        // Create our Move - Chose biggest monster and set the price to half of our coins
        return new BuyMonsterMove(this.self, this.coins/2, chosenOne);
    }



    //This function is called when your opponent tried to buy a monster
    //If you steal, you will get the chosen monster
    //... but hand your opponent the price in coins
    @Override
    public RespondMove getRespond(GameState state, Monster mon, int price) {
        System.out.print("\nUs -  GetRespond  ->  "); // just for clean printing
        this.update(state);

        // Only buy it if the price is less than the value of the card itself and we can afford it
        if (mon.value > price && state.getCoins(this.self) >= price) return new RespondMove(this.self, false, mon);
        return new RespondMove(this.self, true, mon);
    }


    //This function is called when the opponent pays the price to steal
    // ... the monster chosen by the player
    @Override
    public void stolenMonster(GameState state) {
        // this.update(state);
    }

    //This function is called when the player successfully buys a monster
    //... and needs to place the monster at a castle
    @Override
    public PlaceMonsterMove getPlace(GameState state, Monster mon) {
        System.out.print("\nUs -  GetPlace()  ->  "); // just for clean printing
        this.update(state);

        int lowestNum = Integer.MAX_VALUE;
        CastleID chosenOne = CastleID.CastleA;

        // Chose the castle with the lowest number of points
        for (Castle c : this.cm.getCastles()) {
            if (c.goodPoints < lowestNum) {
                lowestNum = c.goodPoints;
                chosenOne = c.getID();
            }
        }
        return new PlaceMonsterMove(this.self, chosenOne, mon);
    }


    @Override
    public String getPlayName() {
        return "BasicBoi2000";
    }
}
