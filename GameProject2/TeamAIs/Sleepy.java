package TeamAIs;


import HelperClasses.Castle;
import ProjectTwoEngine.*;

import java.util.Random;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Sleepy extends BaseBot {

    private CastleID choosenCastle;
    private int priorityMult=1000;
    private int prio0=90*priorityMult;
    private int prio1=80*priorityMult;
    private int prio2=70*priorityMult;
    private int prio3=60*priorityMult;
    private int prio4=50*priorityMult;
    private int prio5=40*priorityMult;
    private int prio6=30*priorityMult;

    public Sleepy(String topOrBot) {
        super(topOrBot);

    }



    //This function is called at the start of your opponent's turn
    @Override
    public void startOppTurn(GameState state) {
        System.out.println("\nENEMY -> Doing Stuff and Returning\n");
    }





    //This function is called when the player must select a monster to buy
    @Override
    public BuyMonsterMove getBuyMonster(GameState state) {
        System.out.print("Sleepy -GetBuyMonster()  ->  "); // just for clean printing
        this.update(state);


        List<Monster> monsters= this.currMonsters;
        Monster chosenOne = getBestMonster(monsters, state);

        int myCoins =this.coins/2;
        if(this.enemyCoins+1< this.coins){
            myCoins=this.enemyCoins+1;
        }

        return new BuyMonsterMove(this.self, myCoins, chosenOne);
    }


    public void monsterPlacedAt(CastleID place){
        choosenCastle=place;
    }

    public CastleID getChoosenCastle(){
        return choosenCastle;
    }






    public Integer getMyCastHeur(GameState state, Castle c) {
        Integer castHeur=null;
        c.updatePoints();
        if(state.getCastleWon(c.getID())==null){

            //System.out.println("Goodpoints:" + c.goodPoints +"|| Badpoints:" + c.badPoints);

            int points = c.goodPoints-c.badPoints;
            if(c.goodSlayer==true && c.badDragon==true){points+=6;};
            if(c.badSlayer==true && c.goodDragon==true){points-=6;}
            if(c.hidden==true){points+=6;}
            castHeur=points;
    }
//        System.out.println(castHeur + ": " + c.getID());
        return castHeur;
    }


    public Integer getOppCastHeur(GameState state, Castle c) {
        Integer castHeur=null;
        c.updatePoints();
        if(state.getCastleWon(c.getID())==null){
            int points = c.badPoints-c.goodPoints;
            if(c.goodSlayer==true && c.badDragon==true){points-=6;};
            if(c.badSlayer==true && c.goodDragon==true){points+=6;}
            if(c.hidden==true){points-=6;}
            castHeur=points;
        }
        return castHeur;
    }



    public Integer whichCastHeur(GameState state, Castle c, Boolean good){
        if(good){
            return getMyCastHeur(state, c);
        }
        return getOppCastHeur(state, c);
    }






    public Monster getBestMonster(List<Monster> mon, GameState state) {
        this.update(state);

        Monster monster=null;
        int value=0;
        CastleID choosenCastle=null;

        int i=0;
        while(i<mon.size()){

            Integer testValue=bestChoice(state,mon.get(i),true);
            if(testValue!=null){
                if(value==testValue){
                    if(new Random().nextBoolean()){
                        monster=mon.get(i);
                        value=testValue;
                        choosenCastle=getChoosenCastle();
                    }
                }
                else if((int)(testValue/priorityMult)>(int)(value/priorityMult) || ((int)(testValue/priorityMult)==(int)(value/priorityMult) && testValue<value) ){
                    monster=mon.get(i);
                    value = testValue;
                    choosenCastle=getChoosenCastle();
                }
            }

            i++;
        }

        System.out.println(monster + " and it had a priority value of " + value);
        monsterPlacedAt(choosenCastle);
        return monster;
    }



    public int bestChoice(GameState state, Monster mon, Boolean good){
        int value=0;
        Castle choosenCastle=null;

        for (Castle c : this.cm.getCastles()) {

            if(whichCastHeur(state, c, good)!=null) {

                Castle pseudoCastle= c;
                c.update(state);
                int heur;
                int newHeur;
                List<Monster> monsterList;

                if(good) {
                    heur = getMyCastHeur(state, pseudoCastle);
                    pseudoCastle.goodMonsters.add(mon);
                    newHeur = getMyCastHeur(state, pseudoCastle);
                    monsterList = pseudoCastle.goodMonsters;
                }
                else{
                    heur = getOppCastHeur(state, pseudoCastle);
                    pseudoCastle.badMonsters.add(mon);
                    newHeur = getOppCastHeur(state, pseudoCastle);
                    monsterList = pseudoCastle.badMonsters;
                }


                Integer testValue=bestMove(heur, newHeur, value, monsterList);


                if(testValue!=null){

                    System.out.println(testValue);
                    System.out.println(value==testValue);
                    System.out.println((int)(testValue/priorityMult));

                    if(value==testValue){
                        if(new Random().nextBoolean()){
                            value=testValue;
                            monsterPlacedAt(c.getID());
                        }
                    }
                    else if((int)(testValue/priorityMult)>(int)(value/priorityMult) || ((int)(testValue/priorityMult)==(int)(value/priorityMult) && testValue<value) ){
                        value = testValue;
                        monsterPlacedAt(c.getID());
                    }
                }

            }

        }


        return value;

    }



    public Integer bestMove(int heur, int newHeur, int value, List<Monster> monsters){




        //Only does final when bot has made an increase from 0-6 -> 7 and then wins
        if(newHeur > 6  && monsters.size()>3){
            System.out.println("Got to prio 0");
            if (newHeur < value || value<prio0) {
                return newHeur + prio0;
            }

        }

        //If there is a place where the bot is losing, and with our move, can prevent it from losing the area, and gives it an advantage
        else if (newHeur > 0 && heur < 0) {
            System.out.println("Got to prio 1");
            if ((newHeur < value && value<prio0) || value<prio1) {
                return newHeur + prio1;
            }


        }

        //If there is a place where the bot is losing, and with our move, can prevent it from losing the area, and gives it an advantage
        else if (newHeur > 0 && heur < 0 && monsters.size()>3) {
            System.out.println("Got to prio 2");
            if ((newHeur < value && value<prio1) || value<prio2) {
                return newHeur + prio2;
            }


        }

        //If there is a place where the bot is losing, and with our move, can prevent it from losing the area, but won't let it have advantage
        else if (newHeur == 0 && heur < 0) {
            System.out.println("Got to prio 3");
            if ((newHeur < value && value<prio2) || value<prio3) {
                return newHeur + prio3;
            }


        }

        //If our heuristic is 0.
        else if (heur == 0) {
            System.out.println("Got to prio 4");
            if ((newHeur < value && value<prio3) || value<prio4) {
                return newHeur + prio4;
            }


        }

        //If our place has monsters on it, and it can make the reach from 0-6 to 7 BUT the board isn't full
        else if(newHeur > 6 && heur >= 0 && heur<=6 && monsters.size()<4){
            System.out.println("Got to prio 5");
            if ((newHeur < value && value<prio4)|| value<prio5) {
                return newHeur + prio5;
            }

        }

        else{
            if ((newHeur < value && value<prio5)|| value<prio6) {
                System.out.println("Got to no prio");
                return newHeur + prio6;
            }
        }

        return null;
    }



    
    public boolean stealMonster(GameState state, Monster mon){

        int self=bestChoice(state, mon, true);
        if((self>prio2 && self<prio1) || self>prio0){
            System.out.println("I have stole papas");
            return false;
        }

        int opp=bestChoice(state, mon, false);

        Boolean one=(opp>prio2 && opp<prio1);
        Boolean two= opp>prio0 ;
        Boolean three=(opp>prio5 && opp<prio4);

        System.out.println("opp " + one + " " + two + " " + three);

        if((opp>prio2 && opp<prio1) || opp>prio0 || (opp>prio5 && opp<prio4)){
            System.out.println("it happened");
            bestChoice(state, mon, true);
            return false;
        }

        return true;
    }


    //This function is called when your opponent tried to buy a monster
    //If you steal, you will get the chosen monster
    //... but hand your opponent the price in coins
    @Override
    public RespondMove getRespond(GameState state, Monster mon, int price) {
        System.out.print("Sleepy -GetRespond()  ->  "); // just for clean printing
        this.update(state);
        List<Move> leg_moves = GameRules.getLegalMoves(state);
        RespondMove myMove= new RespondMove(this.self, stealMonster(state, mon), mon);

        if (!leg_moves.contains(myMove)) return new RespondMove(this.self, true, mon);
        return myMove;
    }


    //This function is called when the opponent pays the price to steal
    // ... the monster chosen by the player
    @Override
    public void stolenMonster(GameState state) {
        System.out.print("\nEnemy Stole Our Monster! The Following GameEngine Outputs Are For Enemy! \n");
    }




    //This function is called when the player successfully buys a monster
    //... and needs to place the monster at a castle
    @Override
    public PlaceMonsterMove getPlace(GameState state, Monster mon) {
        System.out.print("Sleepy -GetPlace()  ->  "); // just for clean printing
        this.update(state);

        return new PlaceMonsterMove(this.self, getChoosenCastle(), mon);
    }





    @Override
    public String getPlayName() {
        return "Sleepster";
    }
}
