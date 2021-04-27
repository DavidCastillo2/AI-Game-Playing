package HelperClasses;

import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Snake;

public class Tile {
    int x;
    int y;
    int us_num;
    int enemy_num;
    boolean has_food = false;
    boolean is_enemy = false;
    boolean is_us = false;

    // All variables not made here should be updated in batch
    public Tile(int X, int Y, int usNum) {
        x = X;
        y = Y;
        us_num = usNum;
        if (this.us_num == 0) {
            this.enemy_num = 1;
        } else {
            this.enemy_num = 0;
        }
    }

    public void reset() {
        has_food = false;
        is_enemy = false;
        is_us = false;
    }

    public boolean isEmpty() {
        return !has_food && !is_enemy && !is_us;
    }
}
