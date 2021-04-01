package HelperClasses;

import ProjectTwoEngine.CastleID;
import ProjectTwoEngine.GameState;
import ProjectTwoEngine.Monster;
import ProjectTwoEngine.PlayerID;

import java.util.List;

public class Castle {
    public List<Monster> badMonsters;
    public List<Monster> goodMonsters;
    public int goodDragon= 0;
    public int badDragon= 0;
    public int goodSlayer= 0;
    public int badSlayer= 0;
    public boolean isTaken = false;
    public boolean hidden = false;
    public int goodPoints = 0;
    public int badPoints = 0;
    PlayerID enemy;
    PlayerID us;
    CastleID c;

    public Castle(GameState gs, CastleID c, PlayerID us, PlayerID enemy) {
        this.c = c;
        this.us = us;
        this.enemy = enemy;
        this.goodMonsters = gs.getMonsters(this.c, us);
        this.badMonsters = gs.getMonsters(this.c, enemy);
        if (gs.getHidden(us) == c) this.hidden = true;

        this.update(gs);
    }

    // TODO update the fact that Slayers kill dragons for points value.
    public void update(GameState gs) {

        // Our side of the game
        int tempLength = this.goodMonsters.size();
        this.goodMonsters = gs.getMonsters(this.c, this.us);
        goodDragon= 0;
        badDragon= 0;
        goodSlayer= 0;
        badSlayer= 0;

        if (tempLength != this.goodMonsters.size()) {  // New Monsters update our stuff
            int total = 0;
            for (Monster m : this.goodMonsters) {
                if (m.name.equals("Slayer")) this.goodSlayer+= 1;
                if (m.name.equals("Dragon")) this.goodDragon+= 1;
                total += m.value;
            }
            //            System.out.println("Good total:" + total);
            this.goodPoints = total;
        }

        // Enemy side of the game
        tempLength = this.badMonsters.size();
        this.badMonsters = gs.getMonsters(this.c, this.enemy);

        if (tempLength != this.badMonsters.size()) {  // New Monsters update our stuff
            int total = 0;
            for (Monster m : this.badMonsters) {
                if (m.name.equals("Slayer")) this.badSlayer+= 1;
                if (m.name.equals("Dragon")) this.badDragon+= 1;
                total += m.value;
            }
//            System.out.println("Bad total:" + total);
            this.badPoints = total;
        }

        if(this.hidden){
            System.out.println("Has hidden");
            this.goodPoints+=6;
            this.goodDragon+=1;
        }

        int goodS=this.goodSlayer;
        int badD=this.badDragon;
        while(goodS>0 && badD>0){
            System.out.println("Eyy");
            this.badPoints-=6;
            goodS-=1;
            badD-=1;
        };

        int badS=this.badSlayer;
        int goodD=this.goodDragon;
        while(badS>0 && goodD>0){
            System.out.println("Lamal");
            this.goodPoints-=6;
            badS-=1;
            goodD-=1;
        }


        // Check to see if the Castle has been Taken
        if (this.goodMonsters.size() >=4 || this.badMonsters.size() >= 4) {
            this.isTaken = true;
        }
    }

    public CastleID getID() {
        return this.c;
    }



    public void updatePoints(){

    }



}

