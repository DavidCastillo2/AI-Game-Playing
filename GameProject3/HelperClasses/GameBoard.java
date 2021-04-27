package HelperClasses;

import ProjectThreeEngine.GamePiece;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.HeadPiece;
import ProjectThreeEngine.Snake;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    List<List<Tile>> tiles = new ArrayList<>();
    List<Tile> changedTiles = new ArrayList<>();
    int max_X = 15;
    int max_Y = 15;
    int us_num;
    int enemy_num;

    public GameBoard(GameState state, int usNum, int enemyNum) {
        us_num = usNum;
        enemy_num = enemyNum;
        this.fillBoard();
    }

    // Just part of the init things to fill our double array
    // this.tiles.get(x).get(y) is the proper format
    private void fillBoard() {
        for (int x=0; x < this.max_X; x++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int y = 0; y < this.max_Y; y++) {
                row.add(new Tile(x, y, this.us_num));
            }
            this.tiles.add(row);
        }
    }

    public void update(GameState state) {
        // Reset our old tiles
        this.resetTiles();

        // Iterate over the board looking for any game piece
        for (int x=0; x < this.max_X; x++) {
            for (int y=0; y < this.max_Y; y++) {
                GamePiece gp = state.getPiece(x, y);

                // We have a game piece, see what it is
                if (gp != null) {
                    this.findType(x, y, gp, state);
                }
            }
        }
    }

    private void findType(int x, int y, GamePiece gp, GameState state) {
        Tile tile = this.tiles.get(x).get(y);
        this.changedTiles.add(tile);  // add to our list of changed tiles
        if (isEnemySnake(x, y, state)) {
            tile.is_enemy = true;
        } else if (isUsSnake(x, y, state)) {
            tile.is_us = true;
        } else {

            // Head Shenanigans???? The Headpiece is a different object so we cannot tell what it is.
            int player_num = isHead(gp);
            if (player_num == -1) {
                tile.has_food = true;
            } else if (player_num == this.us_num) {
                tile.is_us = true;
            } else {
                tile.is_enemy = true;
            }
        }
    }

    private int isHead(GamePiece gp) {
        try {
            return ((HeadPiece)gp).getNum();
        } catch (Exception e) {
            return -1;  // This means its a food piece
        }
    }

    private void resetTiles() {
        for (Tile t : this.changedTiles) {
            t.reset();
        }
    }

    private boolean isEnemySnake(int x, int y, GameState state) {
        Snake enemy = state.getSnake(this.enemy_num);
        return enemy.isPresent(x, y);
    }

    private boolean isUsSnake(int x, int y, GameState state) {
        Snake enemy = state.getSnake(this.us_num);
        return enemy.isPresent(x, y);
    }

    public void printBoard() {
        for (int y=0; y < this.max_Y; y++) {
            for (int x = 0; x < this.max_X; x++) {
                System.out.print("|");
                if (this.tiles.get(x).get(y).isEmpty()) {
                    System.out.print(" ");
                } else {
                    if (this.tiles.get(x).get(y).is_enemy) {
                        System.out.print("x");
                    } else if (this.tiles.get(x).get(y).is_us) {
                        System.out.print("o");
                    } else {
                        System.out.print("f");
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("\n\n");
    }
}
