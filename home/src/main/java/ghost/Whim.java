package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

/** 
* Whim class of type ghost in Waka Waka.
*/
public class Whim extends Ghost{
    private Chaser chaser;

    /** 
    * Overloaded constructor of Whim that does not receives sprites. 
    * Used when only the attributes of Whim  are needed.
    * @param map The map of the game.
    * @param speed The speed of Whim when moving.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Whim  ghost to differentiate between multiple Whims (if there are more than one).
    * @param chaser A chaser in the game that Whim uses to acquire its target.
    * @param frightenedLength The length for frightened state.
    */
    public Whim(GameMap map, Long speed, Waka player, List <Long> modeLengths, int num, Chaser chaser, Long frightenedLength){
        super(map, speed, player, modeLengths, frightenedLength);
        this.chaser = chaser;
        this.x = map.getGhostStart("w").get(num).get(0);
        this.y = map.getGhostStart("w").get(num).get(1);
    }

    /** 
    * Overloaded constructor of Whim that receives sprites. 
    * Used when Whim needs to be drawn.
    * @param map The map of the game.
    * @param speed The speed of Whim when moving.
    * @param sprite The sprite of Whim.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param num The number of the Whim ghost to differentiate between multiple Whims (if there are more than one).
    * @param chaser A chaser in the game that Whim uses to acquire its target.
    * @param frightenedLength The length for frightened state.
    * @param frightenedSprite The mode lengths of Chase and Scatter.
    */
    public Whim(GameMap map, Long speed, PImage sprite, Waka player, List <Long> modeLengths, int num,  Chaser chaser, Long frightenedLength, PImage frightenedSprite){
        super(map, speed, sprite, player, modeLengths, frightenedLength, frightenedSprite);
        this.chaser = chaser;
        this.x = map.getGhostStart("w").get(num).get(0);
        this.y = map.getGhostStart("w").get(num).get(1);
    }

    /** 
    * Makes Whim goes back to its starting position.
    */
    public void backToStart(){
        x = map.getGhostStart("w").get(num).get(0);
        y = map.getGhostStart("w").get(num).get(1);
    }

    /** 
    * Gets the target tile of Whim, that is, double the vector from Chaser to two Tiles ahead of player.
    * Or the bottom left right of map when in Scatter mode our more than 8 tiles away from player.
    * @return The target tile of Whim.
    */   
    public Tile getTargetTile(){
        Tile targetTile = null;

        if (super.ghostMode == Mode.CHASE){
            Tile wakaTarget = getWakaTarget();
            Tile chaserPos = getChaserPos();
            Long chaserPosX = chaserPos.getRow();
            Long chaserPosY = chaserPos.getCol();
            Long wakaPosX = wakaTarget.getRow();
            Long wakaPosY = wakaTarget.getCol();
    
            Long vectorX = wakaPosX - chaserPosX;
            Long vectorY = wakaPosY - chaserPosY;
            Long targetX = wakaPosX + vectorX;
            Long targetY = wakaPosY + vectorY;
    
            int targetPosX = targetX.intValue();
            int targetPosY = targetY.intValue();
            
            // Checks if the target is out of bounds or not.
            // If it is, the target position will be the nearest existent Tile.
            if (targetPosX>map.getRowNum()-1){
                targetPosX = map.getRowNum()-1;
            } else if (targetPosX<0){
                targetPosX = 0;
            }
    
            if (targetPosY>map.getColNum()-1){
                targetPosY = map.getColNum()-1;
            }else if (targetPosY <0){
                targetPosY = 0;
            }
            targetTile = this.map.getGrid().get(targetPosX).get(targetPosY);
        }else if (ghostMode == Mode.SCATTER){
            targetTile = map.getBottomRightCorner();
        }

        return targetTile;
    }

   /** 
    * Gets the position of Chaser it is assigned to.
    * @return The tile in which the Chaser currently occupies.
    */  
    public Tile getChaserPos(){
        Tile[] chaserTiles = chaser.getCurrentTiles();
        Tile targetTile = chaserTiles[0];
        return targetTile;
    }

    /** 
    * Gets the Tile that is two Tiles ahead of the player's current position.
    * @return The Tile that is two Tiles ahead of the player's current position.
    */  
    public Tile getWakaTarget(){
        Tile[] playerTiles = player.getCurrentTiles();
        Tile targetTile = playerTiles[0];
        Direction direction = player.getMoveDirection();
        int row = targetTile.getRow().intValue();
        int col = targetTile.getCol().intValue();

        if (direction.equals(Direction.UP)){

            // Makes sure that it is not out of upper bounds.
            if (targetTile.getRow()-2 >0){
                targetTile =this.map.getGrid().get(row-2).get(col);
            }
        } else if (direction.equals(Direction.DOWN)){

            // Makes sure that it is not out of lower bounds.
            if (targetTile.getRow()+2 <=map.getRowNum()-1){
                targetTile =this.map.getGrid().get(row+2).get(col);
            }
        } else if (direction.equals(Direction.RIGHT)){

            // Makes sure that it is not out of right bounds.
            if (targetTile.getCol()+2 <=map.getColNum()-1){
                targetTile =this.map.getGrid().get(row).get(col+2);
            }
        } else if (direction.equals(Direction.LEFT)){

            // Makes sure that it is not out of left bounds.
            if (targetTile.getCol()-2 >0){
                targetTile =this.map.getGrid().get(row).get(col-2);
            }
        }


        return targetTile;

    }

}