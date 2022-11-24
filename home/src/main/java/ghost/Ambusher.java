package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/** 
* Ambusher class of type ghost in Waka Waka.
*/
public class Ambusher extends Ghost{

    /** 
    * Overloaded constructor of Ambusher that does not receives sprites. 
    * Used when only the attributes of Ambusher are needed.
    * @param map The map of the game.
    * @param speed The speed of Ambusher when moving.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Ambusher ghost to differentiate between multiple Ambushers (if there are more than one).
    * @param frightenedLength The length for frightened state.
    */
    public Ambusher(GameMap map, Long speed, Waka player, List <Long> modeLengths, int num, Long frightenedLength){
        super(map, speed, player, modeLengths, frightenedLength);
        this.x = map.getGhostStart("a").get(num).get(0);
        this.y = map.getGhostStart("a").get(num).get(1);
    }

    /** 
    * Overloaded constructor of Ambusher that receives sprites. 
    * Used when Ambusherneeds to be drawn.
    * @param map The map of the game.
    * @param speed The speed of Ambusher when moving.
    * @param sprite The sprite of Ambusher.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Ambusher ghost to differentiate between multiple Ambushers (if there are more than one).
    * @param frightenedLength The length for frightened state.
    * @param frightenedSprite The frightened sprite.
    */
    public Ambusher(GameMap map, Long speed, PImage sprite, Waka player, List <Long> modeLengths, int num, Long frightenedLength, PImage frightenedSprite){
        super(map, speed, sprite, player, modeLengths, frightenedLength, frightenedSprite);
        this.x = map.getGhostStart("a").get(num).get(0);
        this.y = map.getGhostStart("a").get(num).get(1);
    }

    /** 
    * Makes Ambusher goes back to its starting position.
    */
    public void backToStart(){
        x  = map.getGhostStart("a").get(num).get(0);
        y = map.getGhostStart("a").get(num).get(1);
    }

    /** 
    * Gets the target tile of Ambusher, that is, four tiles apart from player's current position in Chase Mode.
    * Or the top right corner of map when Scatter mode.
    * @return The target tile of Ambusher.
    */
    public Tile getTargetTile(){
        Tile[] playerTiles = player.getCurrentTiles();
        Tile targetTile = playerTiles[0];
        if (super.ghostMode == Mode.CHASE){
        
            Direction direction = player.getMoveDirection();
            int row = targetTile.getRow().intValue();
            int col = targetTile.getCol().intValue();

            if (direction.equals(Direction.UP)){

                // When the target is out of upper bounds, it sets to the top most tile if the grid instead.
                if (targetTile.getRow()-4 >0){
                    targetTile =this.map.getGrid().get(row-4).get(col);
                }else{
                    targetTile = this.map.getGrid().get(0).get(col);
                }
            } else if (direction.equals(Direction.DOWN)){

                // When the target is out of lower bounds, it sets to the bottom most tile if the grid instead.
                if (targetTile.getRow()+4 <=map.getRowNum()-1){
                    targetTile =this.map.getGrid().get(row+4).get(col);
                }else{
                    targetTile = this.map.getGrid().get(map.getRowNum()-1).get(col);
                }
            } else if (direction.equals(Direction.RIGHT)){

                // When the target is out of right bounds, it sets to the furthest right tile if the grid instead.
                if (targetTile.getCol()+4 <=map.getColNum()-1){
                    targetTile =this.map.getGrid().get(row).get(col+4);
                }else{
                    targetTile = this.map.getGrid().get(row).get(map.getColNum()-1);
                }
            } else if (direction.equals(Direction.LEFT)){

                // When the target is out of left bounds, it sets to the furthest left tile if the grid instead.
                if (targetTile.getCol()-4 >0){
                    targetTile =this.map.getGrid().get(row).get(col-4);
                }else{
                    targetTile = this.map.getGrid().get(row).get(0);
                }
            }
        }else if (super.ghostMode == Mode.SCATTER){
            targetTile = map.getTopRightCorner();
        }

        return targetTile;
    }

}