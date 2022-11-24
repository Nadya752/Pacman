package ghost;

import processing.core.PImage;
import processing.core.PApplet;

/** 
* Tile class of type for the map in Waka Waka.
*/
public class Tile{
    private Long size;
    private String value;
    private Long x;
    private Long y;
    private boolean isSuper;

    /** 
    * Constructor for Tile class.
    * A Tile has a size, value, and position.
    * @param size The size of the Tile.
    * @param value The value of the Tile.
    * @param x The position of the Tile in x-axis.
    * @param y The position of the Tile in y-axis.
    */
    public Tile (Long size, String value, Long x, Long y){
        this.size = size;
        this.value = value;
        this.x = x;
        this.y = y;
        this.isSuper= false;

    }

    /** 
    * Sets the Tile to have a superfruit or not.
    * @param isSuper If the Tile has a superfruit or not
    */
    public void setAsSuper(boolean isSuper){
        this.isSuper = isSuper;
    }

    /** 
    * Getter method for the position of the Tile in x-axis.
    * @return The position of the Tile in x-axis.
    */
    public Long getPosX(){
        return x;
    }

    /** 
    * Getter method for the position of the Tile in y-axis.
    * @return The position of the Tile in y-axis.
    */
    public Long getPosY(){
        return y;
    }

    /** 
    * Getter method to get the value of the Tile.
    * @return The value of the Tile.
    */
    public String getValue(){
        return value;
    }

    /** 
    * Getter method to get the size of the Tile.
    * @return The size of the Tile.
    */
    public Long getSize(){
        return size;
    }

    /** 
    * Setter method to set the value of the Tile.
    * @param value The value of the Tile.
    */
    public void setValue(String value){
        this.value = value;
    }

    /** 
    * Getter method to get the row the Tile currently occupies.
    * @return the row the Tile currently occupies.
    */
    public Long getRow(){
        Long row = 0L;
        Long posY = this.y;
        while (posY> this.size){
            posY-= this.size;
            row++;
        }
        return row;
    }

    /** 
    * Getter method to get the column the Tile currently occupies.
    * @return the column the Tile currently occupies.
    */
    public Long getCol(){
        Long col = 0L;
        Long posX = this.x;
        while (posX> this.size){
            posX-= this.size;
            col++;
        }
        return col;
    }

    /** 
    * Checks if the Tile is of a wall Tile.
    * @return If the Tile is a wall or not.
    */
    public boolean isWall(){
        boolean isEmptyCell = value.equals("0");
        boolean isFruitCell = value.equals("7") || value.equals("8");
        boolean isWakaStart = value.equals("p");
        boolean isGhostStart = (value.equals("a") || value.equals("c") || value.equals("i") || value.equals("w"));

        if (isEmptyCell || isFruitCell || isWakaStart || isGhostStart){
            return false;
        }else{
            return true;
        }
    }

    /** 
    * Checks if the Tile has superfruit or not.
    * @return If the Tile has superfruit or not.
    */
    public boolean isSuperfruit(){
        if (isSuper){
            return true;
        }else{
            return false;
        }
    }

    /** 
    * Checks if the Tile has fruit or not.
    * @return If the Tile has fruit or not.
    */
    public boolean hasFruit(){
        if(value.equals("7") ||value.equals("8") ){
            return true;
        }else{
            return false;
        }
    }
}