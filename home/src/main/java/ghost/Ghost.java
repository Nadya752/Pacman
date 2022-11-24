package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;
import java.util.Random;

// Enum of the ghost's modes, which comprises of Chase and Scatter.
enum Mode {
    CHASE,
    SCATTER;

    public Mode alter(){
        if (this.equals(CHASE)){
            return SCATTER;
        } else{
            return CHASE;
        }
    }

}

/** 
* Abstract class of ghosts in Waka Waka.
*/
public abstract class Ghost extends Creature{

    /** 
    * The sprite of the ghost.
    */
    protected PImage sprite;

    /** 
    * The sprite of the ghost in frightened state.
    */
    protected PImage frightenedSprite;

    /** 
    * The number of ghosts to differ multiple ghosts of the same type.
    */
    protected int num;

    /** 
    * The player of the game.
    */
    protected Waka player;

    /** 
    * The mode lengths of Chase and Scatter.
    */
    protected static List <Long> modeLengths;

    /** 
    * The current mode of the ghost in the game.
    */
    protected static Mode ghostMode;

    /** 
    * The number of changes of mode in the game.
    */
    protected static int countMode;

    /** 
    * The frame in which the mode should be changed.
    */
    protected static Long targetFrame;

    /** 
    * The frame in which a frightened state should end.
    */
    protected static Long tempTargetFrame;

    /** 
    * The iterator that iterates through mode lengths.
    */
    protected static Iterator<Long> modeIterator;

   /** 
    * If ghost is in frightened state or not.
    */
    protected static boolean isFrightened;

   /** 
    * The length of the frightened state of ghosts.
    */
    protected Long frightenedLength;

   /** 
    * If the ghost has been eaten by Waka or not.
    */
    protected boolean isInvisible;

   /** 
    * If a ghost in the game has collide with Waka.
    */
    protected static boolean hasCollide;

   /** 
    * If the space key is pressed.
    */
    protected static boolean spaceIsPressed;

    /** 
    * If the game is in debug mode.
    */
    protected static boolean isDebugging;

    /** 
    * Overloaded constructor of Ghost that does not receive any sprites. 
    * Used when only the attributes of Ghost are needed.
    * @param map The map of the game.
    * @param speed The speed of a ghost when moving.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param frightenedLength The length for frightened state.
    */
    public Ghost(GameMap map, Long speed, Waka player, List <Long> modeLengths, Long frightenedLength){
        super(map, speed);
        this.player = player;
        this.ghostMode = Mode.SCATTER;
        this.modeLengths= modeLengths;
        this.countMode = 0;
        this.modeIterator =modeLengths.iterator();
        this.targetFrame = 0L;
        this.isFrightened =false;
        this.frightenedLength = frightenedLength;
        this.tempTargetFrame = 0L;
        this.isInvisible = false;
        this.hasCollide = false;
        this.spaceIsPressed = false;
        this.isDebugging = false;
    }    
        
    /** 
    * Overloaded constructor of Ghost that receives sprites. 
    * Used when Ghost needs to be drawn.
    * @param map The map of the game.
    * @param speed The speed of a ghost when moving.
    * @param sprite The sprite of a ghost.
    * @param player The player as Waka.
    * @param modeLengths The mode lengths of Chase and Scatter.
    * @param frightenedLength The length for frightened state.
    * @param frightenedSprite The frightened sprite.
    */

    public Ghost(GameMap map, Long speed, PImage sprite, Waka player, List <Long> modeLengths, Long frightenedLength, PImage frightenedSprite){
        this(map, speed, player, modeLengths,frightenedLength);
        this.sprite = sprite;
        this.frightenedSprite = frightenedSprite;
    }
    
    /** 
    * Abstract method of ghost going back to its starting position.
    */
    public abstract void backToStart();

    /** 
    * Abstract method to get the target tile of the ghost.
    * @return Target tile of the ghost.
    */
    public abstract Tile getTargetTile();
    
    /** 
    * Drives the whole logic of a ghost.
    * This includes checking the modes of ghost, turning on and off debug mode,
    * moving the ghost, and changing its state, such as frightened or getting eaten by Waka.
    * @param app Used to get framecount to alter the modes of ghost.
    */
    public void tick(PApplet app){
        Tile[] currentTiles = getCurrentTiles();
        List <Tile> availableTiles = getAvailableTiles(currentTiles);
        Tile currentTile = currentTiles[0];
        Tile targetTile = getTargetTile();
        Tile nextTile = getNextTile(availableTiles, targetTile);
        Direction nextDirection= getNextDirection(nextTile);

        checkMode(app);
        checkDebug(app);
        if (isDebugging){
            debugMode(app, targetTile, nextTile);
        }

        moveGhost(currentTile, nextDirection);
        if(isFrightened){
            if (isCollide()){
                backToStart();
                isInvisible = true;
            }
        }else{
            if (hasCollide){
                isInvisible = false;
                backToStart();
            }

        }

    }

    /** 
    * Sets the target frame when mode needs to be changed.
    * @param frameCount The current frameCount of the game.
    */
    public void setTargetFrame(int frameCount){
        targetFrame = frameCount + modeIterator.next()*60;
    }

    /** 
    * Sets debug mode on or off.
    * @param debug If debug mode is on or off.
    */
    public void setDebug(boolean debug){
        isDebugging = debug;
    }

    /** 
    * Checks if the space key has been pressed.
    * @param app To check for space inputs.
    */
    public void checkDebug(PApplet app){

        // Means the space bar is being pressed;
        if (app.keyPressed && app.key== ' '){
            spaceIsPressed = true;
        
        }else{
            spaceIsPressed = false;
        }

    }

    /** 
    * Changes mode from Chase to Scatter or vice versa.
    */
    public void changeMode(){
        ghostMode = ghostMode.alter();
    }

    /** 
    * Sets if a ghost in the game has collided with player.
    * @param collide If a ghost in the game has collided with player.
    */
    public void setHasCollide(boolean collide){
        hasCollide = collide;
    }

    /** 
    * Checks if the ghost should be frightened or not.
    * If a superfruit has been eaten by player, it becomes frightened.
    * @param app To count the frameCount of the game.
    */
    public void checkFrightened(PApplet app){
        // If player eats super fruit and ghost has not been frightened already, 
        // then the tempTargetFrame is set as countdown of frightened mode.
        if (player.eatSuperfruit()){
            if (!isFrightened){
                tempTargetFrame = app.frameCount + (frightenedLength*60);
                targetFrame += (frightenedLength*60);
                isFrightened = true;
            }
        }
    }

    /** 
    * Makes the ghost invisible, the state it goes after being eaten by player.
    * Or makes the ghost visible again.
    * @param invisibility If the ghost is visible or not.
    */
    public void setInvisibility(boolean invisibility){
        isInvisible = invisibility;
    }

    /** 
    * Sets which direction the ghost is facing as it moves.
    * @param facing The direction that is set to where the ghost is facing as it moves.
    */
    public void setFacing(Direction facing){
        this.facing = facing;
    }

    /** 
    * Turns ghost into frightened state or the other way around.
    * @param frightened If ghost becomes frightened or not.
    */
    public void setFrightened(boolean frightened){
        this.isFrightened = frightened; 
    }

    /** 
    * Tells if ghost is frightened or not.
    * @return If ghost is frightened or not.
    */
    public boolean isFrightened(){
        return isFrightened;
    }

    /** 
    * Tells if the space key has been pressed or not.
    * @return If the space key has been pressed or not.
    */
    public boolean spaceIsPressed(){
        return spaceIsPressed;
    }

    /** 
    * Tells if the space key has not been pressed.
    */
    public void spaceNotPressed(){
        spaceIsPressed = false;
    }

    /** 
    * Checks if a mode should be changed in the frameCount the game is currently at.
    * Also checks if ghost has been frightened.
    * @param app To count the frameCount.
    */
    public void checkMode(PApplet app){
        checkFrightened(app);

        if (isFrightened){
            // After 7 seconds it becomes not frightened again.
            if (app.frameCount == tempTargetFrame){
                isFrightened = false;
            }

        }else{
            // Changes mode according to modeLengths.
            if (app.frameCount == targetFrame){
                changeMode();
                countMode++;
                targetFrame += modeIterator.next()*60;
            }
        }

        // Reiterates through modeLengths after it has been iterated until the end.
        if (countMode+1 == modeLengths.size()){
            modeIterator = modeLengths.iterator();
        }
    }

    /** 
    * Draws a line from the ghost's position to its target Tile in real-time.
    * If it is frightened, then the target is the next random tile it will end.
    * @param app To count the frameCount.
    * @param targetTile To draw the line to target when not frightened.
    * @param nextTile To draw the line to next tile it lands when frightened.
    */
    public void debugMode(PApplet app, Tile targetTile, Tile nextTile){
        Long targetPosX = 0L;
        Long targetPosY = 0L;

        if (isFrightened){
            targetPosX = nextTile.getPosX();
            targetPosY = nextTile.getPosY();
        }else{
            targetPosX = targetTile.getPosX();
            targetPosY = targetTile.getPosY();
        }

        // Draws a white line.
        app.line(x, y, targetPosX, targetPosY);
        app.stroke(255);

    }

    /** 
    * Moves ghost according to the next direction.
    * @param currentTile Current position to know if has reached mid point of the Tile or not.
    * @param nextDirection The next direction the ghost is heading.
    */
    public void moveGhost(Tile currentTile, Direction nextDirection){
        if(currentTile != null && nextDirection != null){

            // If it is invisible, the ghost will not move.
            if (!isInvisible){
                Long tilePosX = currentTile.getPosX();
                Long tilePosY = currentTile.getPosY();
                boolean isMidPoint = tilePosX.equals(this.x) && tilePosY.equals(this.y);

                //If it has not reach the mid point of the tile,
                // the ghost will not move to the next
                if (!isMidPoint){
                    move(facing);
                }else{
                    move(nextDirection);
                    facing = nextDirection;
                }
            }
        }

    }

    /** 
    * Sets the next Tile the ghost will be moving towards.
    * If it is not frightened, then it will move according to the Tile with closest distance to target.
    * If it is frightened, the next Tile is chosen randomly according to the available tiles.
    * @param availableTiles The available Tiles that is not wall or the opposite direction that the ghost is moving.
    * @param target The target Tile.
    * @return The next Tile the ghost will be moving towards.
    */
    public Tile getNextTile(List <Tile> availableTiles, Tile target){
        Tile nextTile = null;

        if (availableTiles != null && target != null){
            double shortest = 0.0d;
            Long targetRow = target.getRow();
            Long targetCol = target.getCol();

            // If it is frightened the next tile is random.
            if (isFrightened){
                Random randomObj = new Random();
                int max = availableTiles.size()-1;
                int min = 0;
                int randomNum = randomObj.nextInt((max-min)+1) + min;
                nextTile = availableTiles.get(randomNum);

            // If not frightened it finds the tile with closest distance to target.
            }else{
                for (int i= 0; i< availableTiles.size(); i++){
                    Tile potentialTile = availableTiles.get(i);
                    Long tileRow = potentialTile.getRow();
                    Long tileCol = potentialTile.getCol();
        
                    double currentDistance = getDistance(targetCol, targetRow, tileCol, tileRow);
        
                    if (nextTile != null){
                        if (currentDistance<shortest){
                            shortest = currentDistance;
                            nextTile = potentialTile;
                        }
        
                    }else{
                        shortest = currentDistance;
                        nextTile = potentialTile;
                    }
        
                }
            }
        }

        return nextTile;
    }

    /** 
    * Sets the direction according to the next Tile.
    * @param next The next tile the ghost is heading towards.
    * @return The next direction the ghost will be moving towards.
    */
    public Direction getNextDirection(Tile next){
        Tile currentTile = getCurrentTiles()[0];
        Tile nextTile = next;
        Direction nextDirection = Direction.NEUTRAL;

        if (nextTile!= null){
            if(nextTile.getRow()>currentTile.getRow()){
                nextDirection = Direction.DOWN;
            }else if (nextTile.getRow()<currentTile.getRow()){
                nextDirection = Direction.UP;
            } else if(nextTile.getCol()>currentTile.getCol()){
                nextDirection = Direction.RIGHT;
            } else if(nextTile.getCol()<currentTile.getCol()){
                nextDirection = Direction.LEFT;
            }
        }

        return nextDirection;
    }

    /** 
    * To get the distance between two points.
    * @param targetX the x-axis of first point (target tile).
    * @param targetY the y-axis of first point (target tile).
    * @param posX the x-axis of second point (ghost current location).
    * @param posY the y-axis of second point (ghost current location).
    * @return The distance between two points.
    */
    public double getDistance(Long targetX, Long targetY, Long posX, Long posY){
            double base = Math.abs(targetX.doubleValue() - posX.doubleValue());
            double altitude = Math.abs(targetY.doubleValue()-posY.doubleValue());

            double distance = Math.sqrt(Math.pow(base, 2) + Math.pow(altitude, 2));

            return distance;

    }

    /** 
    * To get the available tiles that surrounds the ghost as a potential for the next Tile it is heading.
    * @param currentTiles The current tile the ghost occupies and its surrounding tiles.
    * @return List of available tiles
    */
    public List<Tile> getAvailableTiles(Tile[] currentTiles){

        List <Tile> availableTiles = new ArrayList<Tile>();
        if (currentTiles != null){
            int countAvailable = 0;

            // Check for available tiles(available moves) from current Tile.
            for (int i = 1; i<currentTiles.length; i++){

                // The tile is available if it is not a wall and not the opposite direction it is currently facing.
                // But if its not currently moving, the only thing to avoid is wall.
                if (!(currentTiles[i].isWall())){
                    if (isMoving && !(facing.opposite().getValue() == i)){
                        availableTiles.add(currentTiles[i]);
                        countAvailable++;
                    }else if (!isMoving){
                        availableTiles.add(currentTiles[i]);
                        countAvailable++;
                    }
       
                }
            
            }
            
            // Last resort when stuck and the only way is the opposite direction, 
            // then it moves to the opposite direction.
            if (countAvailable == 0){
                if (!currentTiles[facing.opposite().getValue()].isWall()){
                    availableTiles.add(currentTiles[facing.opposite().getValue()]);
                }

            }
        }

        return availableTiles;
    }

    /** 
    * Checks if the ghost is colliding with player.
    * In the event that the ghost is invisible, then it is not colliding with the player,
    * however near the player is with the ghost.
    * @return If the ghost is colliding with player.
    */
    public boolean isCollide(){
        if (!isInvisible){
            Long playerX = player.getX();
            Long playerY = player.getY();
            Long playerHeight = player.getHeight();
            Long playerWidth = player.getWidth();
            Long ghostHeight = 28L;
            Long ghostWidth = 28L;

            // Make sure the sprite is the same size of default width and height;
            if (sprite != null){
                ghostHeight = Long.valueOf(sprite.height);
                ghostWidth = Long.valueOf(sprite.width);
            }

            Long distanceX = playerWidth/2 + ghostWidth/2;
            Long distanceY = playerHeight/2 + ghostHeight/2;
            Long currentDistX = Math.abs(playerX - this.x);
            Long currentDistY = Math.abs(playerY - this.y);
            
            // The distance between player and ghost is less than the height and width of both,
            // then it is colliding.
            if(currentDistX < distanceX && currentDistY<distanceY){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


    /** 
    * Draws the ghost.
    * When ghost is invisible, it is not drawn.
    * @param app To draw the sprite.
    */
    public void draw(PApplet app){
        if (!isInvisible){

            // Different sprites for when the ghost is frightened and not frightened.
            if (isFrightened){
                app.image(frightenedSprite, this.x, this.y);
            }else{
                app.image(sprite, this.x, this.y);
            }
        }
    }
    
}
