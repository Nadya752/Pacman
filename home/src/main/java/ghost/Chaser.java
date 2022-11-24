package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/** 
* Chaser class of type ghost in Waka Waka.
*/
public class Chaser extends Ghost{

    /** 
    * Overloaded constructor of Chaser that does not receives sprites. 
    * Used when only the attributes of Chaser are needed.
    * @param map The map of the game.
    * @param speed The speed of Chaser when moving.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Chaser ghost to differentiate between multiple Chasers (if there are more than one).
    * @param frightenedLength The length for frightened state.
    */
    public Chaser(GameMap map, Long speed, Waka player, List <Long> modeLengths, int num, Long frightenedLength){
        super(map, speed, player, modeLengths, frightenedLength);
        this.x = map.getGhostStart("c").get(num).get(0);
        this.y = map.getGhostStart("c").get(num).get(1);
    }

    /** 
    * Overloaded constructor of Chaser that receives sprites. 
    * Used when Chaser needs to be drawn.
    * @param map The map of the game.
    * @param speed The speed of Chaser when moving.
    * @param sprite The sprite of Chaser.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Chaser ghost to differentiate between multiple Chasers (if there are more than one).
    * @param frightenedLength The length for frightened state.
    * @param frightenedSprite The frightened sprite.
    */
    public Chaser(GameMap map, Long speed, PImage sprite, Waka player, List <Long> modeLengths, int num, Long frightenedLength, PImage frightenedSprite){
        super(map, speed, sprite, player, modeLengths, frightenedLength, frightenedSprite);
        this.x = map.getGhostStart("c").get(num).get(0);
        this.y = map.getGhostStart("c").get(num).get(1);
    }

    /** 
    * Makes Chaser goes back to its starting position.
    */
    public void backToStart(){
        x = map.getGhostStart("c").get(num).get(0);
        y = map.getGhostStart("c").get(num).get(1);
    }

    /** 
    * Gets the target tile of Chaser, that is, the player's current position in Chase mode.
    * Or the top left corner of the map when in Scatter mode.
    * @return The target tile of Chaser.
    */
    public Tile getTargetTile(){
        Tile[] playerTiles = player.getCurrentTiles();
        Tile targetTile = null;
        
        if (super.ghostMode == Mode.CHASE){
            targetTile = playerTiles[0];
        }else if (super.ghostMode == Mode.SCATTER){
            targetTile = map.getTopLeftCorner();
        }

        return targetTile;
    }

}