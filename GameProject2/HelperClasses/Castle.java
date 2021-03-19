package HelperClasses;

import ProjectTwoEngine.CastleID;
import ProjectTwoEngine.GameState;
import ProjectTwoEngine.Monster;
import ProjectTwoEngine.PlayerID;

import java.util.List;

public class Castle {
    List<Monster> badMonsters;
    List<Monster> goodMonsters;
    public boolean goodDragon = false;
    public boolean badDragon = false;
    public boolean goodSlayer = false;
    public boolean badSlayer = false;
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
        this.badMonsters  = gs.getMonsters(this.c, enemy);
        this.update(gs);
    }

    public void update(GameState gs) {

        // Our side of the game
        if (this.goodMonsters != null) {
            int tempLength = this.goodMonsters.size();
            this.goodMonsters = gs.getMonsters(this.c, this.us);

            if (tempLength != this.goodMonsters.size()) {  // New Monsters update our stuff
                int total = 0;
                for (Monster m : this.goodMonsters) {
                    if (m.name.equals("Slayer")) this.goodSlayer = true;
                    if (m.name.equals("Dragon")) this.goodDragon = true;
                    total += m.value;
                }
                this.goodPoints = total;
            }
        }

        // Enemy side of the game
        if (this.badMonsters != null) {
            int tempLength = this.badMonsters.size();
            this.badMonsters = gs.getMonsters(this.c, this.enemy);

            if (tempLength != this.badMonsters.size()) {  // New Monsters update our stuff
                int total = 0;
                for (Monster m : this.badMonsters) {
                    if (m.name.equals("Slayer")) this.badSlayer = true;
                    if (m.name.equals("Dragon")) this.badDragon = true;
                    total += m.value;
                }
                this.badPoints = total;
            }
        }
    }

    public CastleID getID() {
        return this.c;
    }

}
