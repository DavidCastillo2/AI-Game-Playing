package HelperClasses;

import ProjectThreeEngine.GameState;
import ProjectThreeEngine.Snake;

public class Tile implements Comparable<Tile>{
    int x;
    int y;
    int us_num;
    int enemy_num;
    boolean has_food = false;
    boolean is_enemy = false;
    boolean is_us = false;
    boolean force_filled = false;

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

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public void reset() {
        has_food = false;
        is_enemy = false;
        is_us = false;
    }

    public boolean isEmpty() { return !force_filled && !is_enemy && !is_us; }

    public boolean isFood() { return this.has_food; }

    // Method for strictly checking if something exists
    public boolean hardEmpty() { return !is_enemy && !is_us; }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public void setIsEnemy(boolean b) { this.is_enemy = b; }

    public void setForceFilled(boolean b) { this.force_filled = b; }

    @Override
    public int compareTo(Tile o) {
        if (o.getX() == this.x && o.getY() == this.y)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object v) {
        if (v instanceof Tile){
            Tile ptr = (Tile) v;
            return (ptr.getX() == this.x && ptr.getY() == this.y);
        }

        return false;
    }
}
