package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/** 
* Player class of Waka Waka.
*/
public class Waka extends Creature{

    private Long lives;
    private Direction face;
    private Direction keyQueue;
    private HashMap <Direction, PImage> sprites;
    private Long livesLeft;
    private boolean hasWon;
    private Long fruitsEaten;
    private boolean keyIsReleased;

    /** 
    * Overloaded constructor of Waka that does receive any sprites. 
    * Used when only the attributes of Waka are needed.
    * @param lives The lives of Waka.
    * @param speed The speed of Waka when moving.
    * @param map The map of the game.
    */
    public Waka(Long lives, Long speed, GameMap map){
        super(map, speed);
        this.lives = lives;
        this.face = Direction.RIGHT;
        this.x = map.getWakaStart().get(0);
        this.y = map.getWakaStart().get(1);
        this.hasWon = false;
        this.fruitsEaten = 0L;
        this.livesLeft = lives;
        this.keyQueue = Direction.NEUTRAL;
        this.keyIsReleased = false;
    }

    /** 
    * Overloaded constructor of Waka that receives sprites. 
    * Used when Waka needs to be drawn.
    * @param lives The lives of Waka.
    * @param speed The speed of Waka when moving.
    * @param map The map of the game.
    * @param sprites The sprites of Waka.
    */
    public Waka(Long lives, Long speed, HashMap <Direction, PImage> sprites, GameMap map){
        this(lives, speed, map);
        //super(map, speed);
        this.sprites = sprites;

    }

    /** 
    * Makes player goes back to start.
    * This method is called when player hits a ghost.
    */
    public void backToStart(){
        x = map.getWakaStart().get(0);
        y = map.getWakaStart().get(1);
        isMoving = false;
        facing = Direction.RIGHT;
        keyQueue = Direction.NEUTRAL;
        keyIsReleased = false;
    }

    /** 
    * Drives the whole logic of Waka.
    * This includes moving according to key input, moving the mouth, 
    * eating fruits, and checking if the player has won.
    * This method is called when player hits a ghost.
    * @param app Used to get framecount to alter the images of open and closing mouth.
    */
    public void tick(PApplet app){
        moveMouth(app);
        Tile[] currentTiles = getCurrentTiles();
        Tile currentTile = currentTiles[0];

        // If keyboard is pressed and released, then Waka moves.
        setKeyQueue(app);
        if (keyIsReleased){
            moveWaka();
        }

        // Eating fruit if Waka passes fruit.
        if(currentTile.hasFruit()){
            eatFruit(currentTile);
        }  
        checkPlayerHasWon();
    }

    /** 
    * Sets if a key input has been given.
    * @param isReleased Boolean if a key input has been given or not.
    */
    public void setKeyIsReleased(boolean isReleased){
        keyIsReleased = isReleased;
    }

    /** 
    * Tells if the player has just eaten a superfruit or not.
    * @return if the player has just eaten a superfruit or not.
    */
    public boolean eatSuperfruit(){
        Tile[] currentTiles = getCurrentTiles();
        Tile currentTile = currentTiles[0];
        Long tilePosX = currentTile.getPosX();
        Long tilePosY = currentTile.getPosY();
        boolean isMidPoint = tilePosX.equals(this.x) && tilePosY.equals(this.y);

        // If player is in a Tile that has a superfruit and has reached the midpoint of the tile.
        if (currentTile.isSuperfruit() && isMidPoint){
            currentTile.setAsSuper(false);
            return true;
        }else{
            return false;
        }
        
    }

    /** 
    * Makes the player loose one life.
    */
    public void loseLife(){
        livesLeft--;
    }

    /** 
    * Getter method for how many lives Waka has left.
    *@return the lives left that Waka has.
    */
    public Long getLivesLeft(){
        return livesLeft;
    }

    /** 
    * Draws the sprites of Waka, and also the sprites that portrays the lives left.
    *@param app To draw the image of sprites.
    */
    public void draw(PApplet app){

        // Draws the sprite of player.
        PImage sprite = sprites.get(face);
        app.image(sprite, this.x, this.y);
        Long lifePosY = map.getGridHeight() - (map.getTileSize() + map.getTileSize() + map.getTileSize()/2)/2;
        Long lifePosX = map.getTileSize();

        // Draws the sprites that portrays the lives left that Waka has.
        for(int i = 0; i<livesLeft; i++){
            PImage lifeSprite = sprites.get(Direction.RIGHT);
            app.image(lifeSprite, lifePosX, lifePosY);
            lifePosX+= 26;
        }
    }

    /** 
    * Getter method for which direction Waka is moving.
    *@return The direction that Waka is moving and facing.
    */
    public Direction getMoveDirection(){
        return facing;
    }

    /** 
    * Getter method for the height of the player's sprite.
    *@return The height of the player's sprite.
    */
    public Long getHeight(){

        //Default height when there is no sprite.
        Long spriteHeight =26L;
    
        if (sprites!= null){
            PImage sprite = sprites.get(Direction.RIGHT);
            int height = sprite.height;
            spriteHeight = Long.valueOf(height);
        }

        return spriteHeight;
    }

    /** 
    * Getter method for the width of the player's sprite.
    *@return The width of the player's sprite.
    */
    public Long getWidth(){

        //Default width when there is no sprite.
        Long spriteWidth = 24L;

        if (sprites != null){
            PImage sprite = sprites.get(Direction.RIGHT);
            int width = sprite.width;
            spriteWidth = Long.valueOf(width);
        }

        return spriteWidth;
    }

    /** 
    * Tells if a player has won the game.
    *@return Boolean if a player has won the game.
    */
    public boolean hasWon(){
        if (hasWon){
            return true;
        }

        return false;
    }

    /** 
    * Check the available tiles, that is, the surrounding tiles of Waka that is not a wall.
    * @param currentTiles The surrounding Tiles of Waka, such as the tile it is currently in,
    *                     the tiles above, below, left and right.
    *@return The surrounding tiles of Waka that is not a wall.
    */
    public List <Tile> getAvailableTiles (Tile[] currentTiles){
        List <Tile> availableTiles = new ArrayList<Tile>();
        if (currentTiles != null){
            // Check for available tiles (available moves) from current Tile the player occupies.
            for (int i = 1; i<currentTiles.length; i++){
                if (!(currentTiles[i].isWall())){
                    availableTiles.add(currentTiles[i]);
                }
            }
        }

        return availableTiles;
    }

    /** 
    * Overloaded method sets the key input as keyQueue.
    * @param app To get the input from keys.
    */
    public void setKeyQueue(PApplet app){

        // Key inputs are added to keyQueue.
        if (app.key ==app.CODED){
            if (app.keyCode == app.UP){
                this.keyQueue = Direction.UP;
            }else if (app.keyCode == app.DOWN){
                this.keyQueue = Direction.DOWN;
            }else if (app.keyCode == app.LEFT){
                this.keyQueue = Direction.LEFT;
            }else if(app.keyCode == app.RIGHT){
                this.keyQueue = Direction.RIGHT;
            }
        }

    }

   /** 
    * Overloaded method sets keyQueue according to specified direction.
    * @param direction Specified direction to set keyQueue of Waka.
    */
    public void setKeyQueue(Direction direction){
        keyQueue = direction;
    }

    /** 
    * Checks if the movement of Waka can still be continued.
    * When it is moving in a certain direction, it checks whether the next Tile it will land
    * according to the direction it is moving is a wall or not.
    * @param availableDirections To compare whether the current direction of movement is an available direction.
    * @return Boolean if the movement of Waka can still be continued. 
    */
    public boolean checkMoveIsValid(List <Integer> availableDirections){
        if(availableDirections != null){
            for (int i = 0; i<availableDirections.size(); i++){
                int tilePos = availableDirections.get(i);
    
                if (facing.getValue() == tilePos){
                    return true;
                }
            }
        }
        return false;
    }

    /** 
    * Checks if the key input can be take account to Waka's movement.
    * Checks whether the next Tile it will land
    * according to the direction of key input is a wall or not.
    * @param availableDirections To compare whether the current direction of key input is an available direction.
    * @return Boolean if the key input can be take account to Waka's movement.
    */
    public boolean checkKeyisValid(List <Integer> availableDirections){
        if (availableDirections != null){
            for (int i = 0; i<availableDirections.size(); i++){
                int tilePos = availableDirections.get(i);
                if (keyQueue.getValue() == tilePos){
                    return true;
                }
            }
    
        }

        return false;
    }

    /** 
    * Checks the available directions, that is, which direction Waka can move according to the available Tiles.
    * @param availableTiles The available surrounding tiles that is not a wall.
    * @param currentTile The current tile player occupies.
    * @return List of Integer to compare with key input and movement.
    */
    public List<Integer> getAvailableDirections(List<Tile> availableTiles, Tile currentTile){
        List <Integer> availableDirections = new ArrayList <Integer> ();
        if (availableTiles != null && currentTile != null){
            for (int i = 0; i<availableTiles.size(); i++){
                Tile nextTile = availableTiles.get(i);
                if(nextTile.getRow()>currentTile.getRow()){
                    availableDirections.add(2);
                }else if (nextTile.getRow()<currentTile.getRow()){
                    availableDirections.add(1);
                } else if(nextTile.getCol()>currentTile.getCol()){
                    availableDirections.add(4);
                } else if(nextTile.getCol()<currentTile.getCol()){
                    availableDirections.add(3);
                }
            }
        }

        return availableDirections;
    }

    /** 
    * Moves waka according to current movement or key input.
    */
    public void moveWaka(){
        Tile[] currentTiles = getCurrentTiles();
        Tile currentTile = currentTiles[0];
        List<Tile> availableTiles = getAvailableTiles(currentTiles);
        List <Integer> availableDirections = getAvailableDirections(availableTiles, currentTile);

        Long tilePosX = currentTile.getPosX();
        Long tilePosY = currentTile.getPosY();
        boolean isMidPoint = tilePosX.equals(this.x) && tilePosY.equals(this.y);

        boolean moveIsValid= checkMoveIsValid(availableDirections);
        boolean keyIsValid = checkKeyisValid(availableDirections);

        boolean keyIsVertical = keyQueue.isVertical();
        boolean keyIsHorizontal = keyQueue.isHorizontal();
        boolean moveIsVertical = facing.isVertical();
        boolean moveIsHorizontal = facing.isHorizontal();

        // Moving Waka
        if (moveIsValid && keyIsValid){
            // If movement is perpendicular Waka waits until it reaches the midpoint of the Tile.
            // or else it moves according to the key input.
            if ((keyIsVertical && moveIsHorizontal) || (keyIsHorizontal && moveIsVertical)){
                if (!isMidPoint){
                    move(facing);
                }else{
                    move(keyQueue);
                }
            }else{
                move(keyQueue);
            }
        // If Waka is moving but the new key input is not valid then the key is ignored.
        }else if (moveIsValid && !keyIsValid){
            if (isMoving){
                move(facing);
            }

        // If Waka reaches the last Tile it can move before a wall.
        // and theres no new key input or new key input is invalid.
        // It will still move until the midpoint of the Tile, and then it will stop moving.
        } else if (!moveIsValid && !keyIsValid){
            if (!isMidPoint){
                move(facing);
            }

        // If Waka is in the last Tile it can move before a wall.
        // and there is a new key input that is valid. 
        // If movement is perpendicular Waka waits until it reaches the midpoint of the Tile.
        // or else it moves according to the key input.
        }else if (!moveIsValid && keyIsValid){
            if((keyIsVertical&& moveIsHorizontal) || (keyIsHorizontal && moveIsVertical)){
                if (!isMidPoint){
                    move(facing);
                }else{
                    move(keyQueue);
                }
            }else{
                move(keyQueue);
            }
        }
    }

    /** 
    * Makes Waka eat the fruit at the middle of the Tile.
    * @param currentTile The current tile player occupies.
    */
    public void eatFruit(Tile currentTile){
        Long tilePosX = currentTile.getPosX();
        Long tilePosY = currentTile.getPosY();
        boolean isMidPoint = tilePosX.equals(this.x) && tilePosY.equals(this.y);

        // Waka only eats when it reaches the middle of Tile, where the fruit is currently situated.
        if(isMidPoint){
            currentTile.setValue("0");
            fruitsEaten++;
        }
    }

    /** 
    * Checks if player has won the game.
    */
    public void checkPlayerHasWon(){

        // If Waka has eaten all fruit, then player wins.
        if (fruitsEaten.equals(map.getTotalFruits())){
            hasWon = true;
        }
    }

    /** 
    * Alters the mouth of Waka for every 8 frames.
    * @param app To count the frames.
    */
    public void moveMouth(PApplet app){
        if ((app.frameCount) % 8 == 0){
            if (!(face.equals(Direction.NEUTRAL))){
                face = Direction.NEUTRAL;
            }else{
                face = facing;
            }
        } 
    }

}