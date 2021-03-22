package HelperClasses;

import ProjectTwoEngine.CastleID;
import ProjectTwoEngine.GameState;
import ProjectTwoEngine.PlayerID;

import java.util.*;


@SuppressWarnings("rawtypes")
public class CastleManager {
    HashMap<String, Castle> castles = new HashMap<>();
    PlayerID us;
    PlayerID enemy;

    public CastleManager(GameState state, PlayerID us, PlayerID enemy) {
        this.us = us;
        this.enemy = enemy;
        for (CastleID c : CastleID.values()) {
            castles.put(c.toString(), new Castle(state, c, us, enemy));
        }
    }

    public void updateAll(GameState state) {
        for (Map.Entry m : this.castles.entrySet()) {
            Castle c = (Castle) m.getValue();
            c.update(state);
        }
    }

    public List<Castle> getCastles() {
        ArrayList<Castle> retVal = new ArrayList<>();
        for (Map.Entry m : this.castles.entrySet()) {
            retVal.add((Castle) m.getValue());
        }
        return retVal;
    }

    public void update(CastleID c, GameState state) {
        this.castles.get(c.toString()).update(state);
    }

    public Castle get(CastleID c) {
        return this.castles.get(c.toString());
    }

}
