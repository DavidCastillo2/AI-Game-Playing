package HelperClasses;

import ProjectThreeEngine.GamePiece;
import ProjectThreeEngine.GameState;
import ProjectThreeEngine.HeadPiece;
import ProjectThreeEngine.Snake;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameBoard {
    List<List<Tile>> tiles = new ArrayList<>();
    List<Tile> changedTiles = new ArrayList<>();
    List<Tile> us_snake = new ArrayList<>();
    List<Tile> enemy_snake = new ArrayList<>();
    List<Tile> foods = new ArrayList<>();
    HeadPiece us_head;
    HeadPiece enemy_head;
    public List<HeadPiece> heads = new ArrayList<>();
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

    public List<Tile> getEnemySnake() {
        return enemy_snake;
    }

    public List<Tile> getUsSnake() {
        return us_snake;
    }

    public List<Tile> getFoods() {
        return foods;
    }

    public void update(GameState state) {
        // temp? - reset this class's vars
        this.us_snake = new ArrayList<>();
        this.enemy_snake = new ArrayList<>();
        this.heads = new ArrayList<>();
        this.foods = new ArrayList<>();

        // Reset our old tiles
        this.resetTiles();

        // Get our heads
        this.updateHeads(state);

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

    public void updateHeads(GameState state) {
        this.us_head    = this.getMyHead(state, this.us_num);
        this.enemy_head = this.getMyHead(state, this.enemy_num);

        this.heads.add(this.us_head);
        this.heads.add(this.enemy_head);

        this.tiles.get(this.us_head.getX()).get(this.us_head.getY()).is_us = true;
        this.tiles.get(this.enemy_head.getX()).get(this.enemy_head.getY()).is_enemy = true;

        this.changedTiles.add(this.tiles.get(this.us_head.getX()).get(this.us_head.getY()));
        this.changedTiles.add(this.tiles.get(this.enemy_head.getX()).get(this.enemy_head.getY()));
    }

    public Tile get(int x, int y) {
        return this.tiles.get(x).get(y);
    }

    public void setTile(Tile t) {
        this.tiles.get(t.getX()).set(t.getY(), t);
    }

    public HeadPiece getMyHead(GameState state, int play_num){
        for(int x = 0; x < 15; x++){
            for(int y = 0; y < 15; y++){
                GamePiece piece = state.getPiece(x, y);
                if(piece instanceof HeadPiece){
                    HeadPiece headPiece = ((HeadPiece)piece);
                    if(state.getSnake(play_num).isPresent(x, y)) return headPiece;
                }
            }
        }
        return null;
    }

    private void findType(int x, int y, GamePiece gp, GameState state) {
        // Get our tile that has a GamePiece in it
        Tile tile = this.tiles.get(x).get(y);
        this.changedTiles.add(tile);  // add to our list of changed tiles

        // We have a non head piece item
        int player_num = isHead(gp);
        if (player_num == -1) {
            // See if its a snake piece, and whose snake it is
            if (isEnemySnake(x, y, state)) {
                tile.is_enemy = true;
                this.enemy_snake.add(tile);
            } else if (isUsSnake(x, y, state)) {
                tile.is_us = true;
                this.us_snake.add(tile);
            // It's food since that is all that is left
            } else {
                tile.has_food = true;
                this.foods.add(tile);
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
        System.out.print("   ");
        for (int i=0; i < this.max_X; i++) {
            System.out.print(i);
            if (i < 9) System.out.print("   ");
            else System.out.print("  ");
        }
        System.out.println();
        for (int y=0; y < this.max_Y; y++) {
            for (int x = 0; x < this.max_X; x++) {
                System.out.print(" | ");
                Tile t = this.tiles.get(x).get(y);
                if (t.has_food) {
                    System.out.print("f");
                } else if (t.isEmpty()) {
                    System.out.print(" ");
                } else if (t.is_enemy) {
                    if (t.getX() == this.enemy_head.getX() && t.getY() == this.enemy_head.getY())
                        System.out.print("T");
                    else
                        System.out.print("x");
                } else if (t.is_us) {
                    if (t.getX() == this.us_head.getX() && t.getY() == this.us_head.getY())
                        System.out.print("U");
                    else
                        System.out.print("o");
                } else if (t.force_filled) {
                    System.out.print(" ");
                }
            }
            System.out.print(" | ");
            System.out.println(y);
        }
    }

    // Returns nearby tiles that are not full of a snake/snakeHead
    public List<Tile> nearByTiles(int x, int y) {
        return nearByTiles(x, y, 1);
    }

    public List<Tile> nearByTiles(Tile t, int lengthAway) {
        return nearByTiles(t.getX(), t.getY(), lengthAway);
    }

    public List<Tile> nearByTiles(int x, int y, int lengthAway) {
        ArrayList<Tile> retVal = new ArrayList<>();
        try {
            Tile t = this.get(x + lengthAway, y); // Right
            if (t.isEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(x - lengthAway, y); // Left
            if (t.isEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(x, y - lengthAway); // Up
            if (t.isEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(x, y + lengthAway); // Down
            if (t.isEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        return retVal;
    }

    public List<Tile> allTiles() {
        ArrayList<Tile> retVal = new ArrayList<>();
        for (int x=0; x < this.max_X; x++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int y = 0; y < this.max_Y; y++) {
                row.add(new Tile(x, y, this.us_num));
            }
            retVal.addAll(row);
        }
        return retVal;
    }

    public List<Tile> nearByTiles(Tile t) {
        return nearByTiles(t.getX(), t.getY());
    }

    public List<Tile> panicOpenTiles(Tile tile) {
        ArrayList<Tile> retVal = new ArrayList<>();
        try {
            Tile t = this.get(tile.getX() + 1, tile.getY()); // Right
            if (t.hardEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(tile.getX() - 1, tile.getY()); // Left
            if (t.hardEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(tile.getX(), tile.getY() - 1); // Up
            if (t.hardEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        try {
            Tile t = this.get(tile.getX(), tile.getY() + 1); // Down
            if (t.hardEmpty()) retVal.add(t);
        } catch (Exception e) {
            // continue
        }
        return retVal;
    }
}
