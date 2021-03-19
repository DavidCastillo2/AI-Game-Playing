package TeamAIs;

import HelperClasses.CastleManager;
import ProjectTwoEngine.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBot implements Player {
    CastleManager cm;
    List<Monster> currMonsters;
    Monster nextMonster;
    PlayerID enemy;
    int enemyCoins;
    Move lastMove;
    PlayerID self;
    int coins;

    // This is a setup method called only at the start
    @Override
    public void begin(GameState init_state) {
        // Defining us Versus Them
        this.self = init_state.getCurPlayer();
        if (this.self ==  PlayerID.BOT) this.enemy = PlayerID.TOP;
        else this.enemy = PlayerID.BOT;

        // Setting up one off updates
        this.cm = new CastleManager(init_state, this.self, this.enemy);

        // Update a lot of variables - This should be called by every method basically
        this.update(init_state);

    }

    // Just pulls out the data from state that we need
    public void update(GameState gs) {
        this.currMonsters = gs.getPublicMonsters();
        this.nextMonster = gs.getNextMonster();
        this.coins = gs.getCoins(this.self);
        this.lastMove = gs.getLastMove();
        this.enemyCoins = gs.getCoins(this.enemy);

        this.cm.updateAll(gs);
    }


    //This function is called when the player must select a monster to buy
    @Override
    public BuyMonsterMove getBuyMonster(GameState state) {
        return null;
    }


    //This function is called at the start of your opponent's turn
    @Override
    public void startOppTurn(GameState state) {

    }


    //This function is called when your opponent tried to buy a monster
    //If you steal, you will get the chosen monster
    //... but hand your opponent the price in coins
    @Override
    public RespondMove getRespond(GameState state, Monster mon, int price) {
        return null;
    }


    //This function is called when the opponent pays the price to steal
    // ... the monster chosen by the player
    @Override
    public void stolenMonster(GameState state) {

    }

    //This function is called when the player successfully buys a monster
    //... and needs to place the monster at a castle
    @Override
    public PlaceMonsterMove getPlace(GameState state, Monster mon) {
        return null;
    }


    @Override
    public String getPlayName() {
        return "Base_Bot";
    }
}
