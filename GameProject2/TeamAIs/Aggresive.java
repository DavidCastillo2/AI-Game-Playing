package TeamAIs;


import HelperClasses.Castle;
import ProjectTwoEngine.*;

import java.util.Comparator;
import java.util.List;

public class Aggresive extends BaseBot {
    public Aggresive(String topOrBot) {
        super(topOrBot);
    }

    private class MonsterValue implements Comparator<Monster>{
        public int compare(Monster a, Monster b){
            return a.value - b.value;
        }
    }
    private class Ids {
        CastleID enemy, us, closest;
        public Ids (CastleID enemy, CastleID us, CastleID closest){
            this.enemy = enemy;
            this.us = us;
            this.closest = closest;
        }

        public String toString() {
            return "Enemy: " + this.enemy + "\t\tUs: " + this.us + "\t\tClosest: " + this.closest;
        }
    }
    CastleID castleID = null;
    /*
    the idea of this model is to count cards and probabilities
    as cards are used they are removed from the fake deck and
    it will prioritize winning a castle over anything else (maybe it focus on one castle first?)
    try to get away with the smallest monster value for a castle and still win with the smallest margin possible
    */

    private Ids getWinningAndClosest(){
        CastleID enemyid = null;
        CastleID usid = null;
        CastleID closest = null;
        int closestValue = 100; //this is the absolute value of the difference in points of the closest castle
        for (CastleID c: CastleID.values()){
            int enemyValue = winningCastleEnemy(c);
            int usValue = winningCastle(c);
            if (enemyValue > 0 && !cm.get(c).isTaken) enemyid = c;
            if (usValue > 0 && !cm.get(c).isTaken) usid = c;
            //here you find which is closest (basically abs(goodPoints1-badPoint1) < abs(goodPoints2-badPoints2))
            int difference = Math.abs(usValue-enemyValue);
            if (difference < closestValue && !cm.get(c).isTaken) {
                closest = c;
                closestValue = difference;
            }
        }
        return new Ids(enemyid, usid, closest);
    }

    @Override
    public void startOppTurn(GameState state) {
        System.out.println("\nENEMY -> Doing Stuff and Returning\n");
    }

    @Override
    public BuyMonsterMove getBuyMonster(GameState state) {
        Monster m;
        int price;
        System.out.print("Us -GetBuyMonster()  ->  "); // just for clean printing
        this.update(state);
        Ids ids = getWinningAndClosest();
        CastleID id = ids.us;
        CastleID closest = ids.closest;

        if (id != null){
            Castle castle = cm.get(id);
            this.castleID = id;
            if (castle.goodMonsters.size() == 3){ //we are about to win this fight and all we need to do is start it
                if (this.enemyCoins < this.coins){
                    m = BiggestMonster();
                    price = this.enemyCoins+1;
                }
                else {
                    m = SmallestMonster();
                    price = this.coins;
                }
            }
            else { //get middle powerful monster and put it there for cost
                m = MiddleMonster();
                price = Math.min(this.coins, m.value);
            }
        }
        else { //this is the case in which we are not winning any castles, so buy the largest monster at price
            this.castleID = closest;
            m = BiggestMonster();
            price = Math.min(this.coins, m.value);
        }

        return new BuyMonsterMove(this.self, price, m);
    }

    private Monster BiggestMonster(){
        int biggestMonster = -1;
        Monster chosenOne = null;
        for (Monster m : this.currMonsters) {
            if (m.value > biggestMonster) chosenOne = m;
        }
        return chosenOne;
    }

    private Monster MiddleMonster(){
        List<Monster> list = this.currMonsters;
        list.sort(new MonsterValue());
        int middle = list.size()/2;
        return list.get(middle);
    }

    private Monster SmallestMonster(){
        int smallestMonster = 7;
        Monster chosenOne = null;
        for (Monster m : this.currMonsters) {
            if (m.value < smallestMonster) chosenOne = m;
        }
        return chosenOne;
    }

    private int winningCastle(CastleID id){
        Castle castle = cm.get(id);
        if (castle.isTaken) return -100;
        int value = castle.goodPoints - castle.badPoints;
        return value;
    }

    private int winningCastleEnemy(CastleID id){
        Castle castle = cm.get(id);
        if (castle.isTaken) return -100;
        int value = castle.badPoints - castle.goodPoints;
        return value;
    }

    @Override
    public RespondMove getRespond(GameState state, Monster mon, int price) {
        System.out.print("Us -GetRespond()  ->  "); // just for clean printing
        this.update(state);

        if (price > this.coins) return new RespondMove(this.self, true, mon); //automatically pass if we can't afford it
        boolean stealing;
        Ids ids = getWinningAndClosest();
        CastleID enemyid = ids.enemy;
        CastleID usid = ids.us;
        CastleID closest = ids.closest;

        if (usid != null){
            this.castleID = usid;
            stealing = false;
        } else {
            stealing = (enemyid == null);
            this.castleID = closest;
        }
        return new RespondMove(this.self, stealing, mon);
    }

    @Override
    public void stolenMonster(GameState state) {
        System.out.print("\nEnemy Stole Our Monster! The Following GameEngine Outputs Are For Enemy! \n");
    }

    @Override
    public PlaceMonsterMove getPlace(GameState state, Monster mon) {
        System.out.print("Us -GetPlace()  ->  "); // just for clean printing
        this.update(state);
        CastleID castle = this.castleID;
        this.castleID = null;
        return new PlaceMonsterMove(this.self, castle, mon);
    }

    @Override
    public String getPlayName() {
        return "Aggresive";
    }
}
