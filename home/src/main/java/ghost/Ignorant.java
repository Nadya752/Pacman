package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/** 
* Ignorant class of type ghost in Waka Waka.
*/
public class Ignorant extends Ghost{

    /** 
    * Overloaded constructor of Ignorant that does not receives sprites. 
    * Used when only the attributes of Ignorant  are needed.
    * @param map The map of the game.
    * @param speed The speed of Ignorant when moving.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Ignorant  ghost to differentiate between multiple Ignorants (if there are more than one).
    * @param frightenedLength The length for frightened state.
    */
    public Ignorant(GameMap map, Long speed, Waka player, List <Long> modeLengths, int num, Long frightenedLength){
        super(map, speed, player, modeLengths, frightenedLength);
        this.x = map.getGhostStart("i").get(num).get(0);
        this.y = map.getGhostStart("i").get(num).get(1);
    }

    /** 
    * Overloaded constructor of Ignorant  that receives sprites. 
    * Used when Ignorant needs to be drawn.
    * @param map The map of the game.
    * @param speed The speed of Ignorant when moving.
    * @param sprite The sprite of Ignorant.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Ignorant ghost to differentiate between multiple Ignorants (if there are more than one).
    * @param frightenedLength The length for frightened state.
    * @param frightenedSprite The frightened sprite.
    */
    public Ignorant(GameMap map, Long speed, PImage sprite, Waka player, List <Long> modeLengths, int num, Long frightenedLength, PImage frightenedSprite){
        super(map, speed, sprite, player, modeLengths, frightenedLength, frightenedSprite);
        this.x = map.getGhostStart("i").get(num).get(0);
        this.y = map.getGhostStart("i").get(num).get(1);
    }

    /** 
    * Makes Ignorant goes back to its starting position.
    */
    public void backToStart(){
        x = map.getGhostStart("i").get(num).get(0);
        y = map.getGhostStart("i").get(num).get(1);
    }

    /** 
    * Gets the target tile of Ignorant , that is, the player's current position when within 8 tiles away.
    * In Scatter mode target becomes the bottom left corner of map, or when more than 8 tiles away from player.
    * @return The target tile of Ignorant .
    */
    public Tile getTargetTile(){
        Tile[] playerTiles = player.getCurrentTiles();
        Tile targetTile = playerTiles[0];
        
        if (isWithinBoundary() || super.ghostMode == Mode.SCATTER ){
            targetTile = map.getBottomLeftCorner();
        }


        return targetTile;
    }

    /** 
    * Checks if Ignorant is within a radius of 8 tiles away from the player.
    * @return If Ignorant is within a radius of 8 tiles away from the player.
    */
    public boolean isWithinBoundary(){
        Long playerPosX = player.getX();
        Long playerPosY = player.getY();
        Long currentPosX = this.getX();
        Long currentPosY = this.getY();

        double distanceToPlayer = getDistance(playerPosX, playerPosY, currentPosX, currentPosY);
        double playerRadius = 8* map.getTileSize();

        if (distanceToPlayer<= playerRadius){
            return true;
        }else{
            return false;
        }
    }


}