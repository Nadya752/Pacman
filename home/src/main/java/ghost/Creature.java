package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

// Enum for direction constants such as up, down, left, right, and not moving.
enum Direction {
    NEUTRAL,
    UP,
    DOWN,
    LEFT,
    RIGHT;

    private final int value;

    private Direction() {
        this.value = ordinal();
    }

    public int getValue(){
        return value;
    }

    // When direction is up or down.
    public boolean isVertical(){
        if (this.equals(DOWN) || this.equals(UP)){
            return true;
        }else{
            return false;
        }
    }

    // When direction is left or right.
    public boolean isHorizontal(){
        if (this.equals(LEFT) || this.equals(RIGHT)){
            return true;
        }else{
            return false;
        }
    }

    // Returns the opposite direction.
    public Direction opposite(){
        if (this.equals(UP)){
            return DOWN;
        } else if (this.equals(DOWN)){
            return UP;
        }else if (this.equals(RIGHT)){
            return LEFT;
        }else if (this.equals(LEFT)){
            return RIGHT;
        }
        return NEUTRAL;
    }
}

/** 
* An abstract class for creatures that moves in the map such as the Waka (player) and the ghost(s).
*/
public abstract class Creature{
    /** 
    * Map of the game.
    */
    protected GameMap map;
    /** 
    * Speed of movement.
    */
    protected Long speed;
    /** 
    * Coordinates of x-axis.
    */
    protected Long x;
    /** 
    * Coordinates of y-axis.
    */
    protected Long y;
    /** 
    * Direction it faces according to movement.
    */
    protected Direction facing;
    /** 
    * Whether it moves or not.
    */
    protected boolean isMoving;

    /** 
    * An abstract class for creatures that moves in the map such as the waka (player)  and the ghost(s).
    * @param map The map of the game.
    * @param speed The speed of the movement of the Creature.
    */
    public Creature(GameMap map, Long speed){
        this.map = map;
        this.speed = speed;
        this.facing = Direction.RIGHT;
        this.isMoving = false;
    }

    /** 
    * An abstract method to run the whole logic of the Creature.
    * @param app To access frameCount and key inputs. 
    */
    public abstract void tick(PApplet app);

    /** 
    * An abstract method to draw the creature.
    * @param app To draw the sprite. 
    */
    public abstract void draw(PApplet app);

    /** 
    * Abstract method of Creature going back to its starting position.
    */
    public abstract void backToStart();

    /** 
    * To get the coordinates of x-axis of the Creature.
    * @return The coordinates of x-axis of the Creature.
    */
    public Long getX(){
        return this.x;
    }

    /** 
    * To get the coordinates of y-axis of the Creature.
    * @return The coordinates of y-axis of the Creature.
    */
    public Long getY(){
        return this.y;
    }

   /** 
    * To get the coordinates location of the Creature.
    * @param x The coordinates of y-axis of the Creature.
    */
    public void setLocation (Long x, Long y){
        this.x = x;
        this.y = y;
    }

    /** 
    * To get the current position of the Creature in Tiles.
    * @return The tile the creature is situated in and also the surrounding tiles such as
    *         the tiles above, below, left and right.
    */
    public Tile[] getCurrentTiles(){
        Long posX = x;
        Long posY = y;
        int tileRow= 0;
        int tileCol = 0;
        Long tileSize = map.getTileSize();

        // Check the row of current tile.
        while (posX> tileSize){
            posX-= tileSize;
            tileCol++;
        }
        // Check the column of current tile.
        while (posY> tileSize){
            posY-= tileSize;
            tileRow++;
        }

        Tile currentTile= this.map.getGrid().get(tileRow).get(tileCol);
        Tile aboveTile = this.map.getGrid().get(tileRow-1).get(tileCol);
        Tile belowTile = this.map.getGrid().get(tileRow+1).get(tileCol);
        Tile rightTile = this.map.getGrid().get(tileRow).get(tileCol+1);
        Tile leftTile = this.map.getGrid().get(tileRow).get(tileCol-1);
        Tile[] currentTiles= {currentTile, aboveTile, belowTile, leftTile, rightTile};
        return currentTiles;
    }

    /** 
    * Moving the Creature according to the specified Direction.
    * Also sets where its facing according to which direction it moved.
    * @param direction The direction of movement.
    */
    public void move(Direction direction){
        if (direction != null){
            isMoving = true;
            if (direction.equals(Direction.UP)){
                this.y-=this.speed;
                this.facing = Direction.UP;
            } else if (direction.equals(Direction.DOWN)){
                this.y+=this.speed;
                this.facing = Direction.DOWN;
            } else if (direction.equals(Direction.RIGHT)){
                this.x+=this.speed;
                this.facing = Direction.RIGHT;
            } else if (direction.equals(Direction.LEFT)){
                this.x-=this.speed;
                this.facing = Direction.LEFT;
            }
        }

    }



}