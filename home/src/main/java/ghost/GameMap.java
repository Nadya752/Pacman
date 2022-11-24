package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.HashMap;

/** 
* Map class of the game that parses and draws the map. 
* It parses a text file into a list of Tiles and draws based on the sprite and each Tile's value.
*/
public class GameMap{

    private List <List<Tile>> grid;
    private Long gridHeight;
    private HashMap <String, PImage> sprites;
    private List <String> ghostList;
    private List <List<Long>> ambusherStart;
    private List <List<Long>> chaserStart;
    private List <List<Long>> ignorantStart;
    private List <List<Long>> whimStart;
    private List <Long> wakaStart;
    private Long tileSize;
    private Long countFruits;
    private int countGhost;

    /** 
    * Overloaded constructor of Map that does receive any sprites. 
    * Used when only the attributes of the Map are needed.
    */
    public GameMap (){
        ambusherStart = new ArrayList <List<Long>>();
        chaserStart = new ArrayList <List<Long>>();
        ignorantStart = new ArrayList <List<Long>>();
        whimStart = new ArrayList <List<Long>>();
        wakaStart = new ArrayList <Long>();
        ghostList = new ArrayList <String>();
        this.tileSize = 16L;
        countFruits = 0L;
        this.countGhost = 0;
    }

    /** 
    * Overloaded constructor of Map that receives sprites. 
    * Used when Map needs to be drawn.
    * @param sprites The sprites for the map such as walls, fruits and superfruits.
    */
    public GameMap(HashMap <String, PImage> sprites){
        this();
        this.sprites = sprites;
    }
    
    /** 
    * To get the bottom left corner of the Map.
    * @return The bottom left corner Tile of map grid.
    */
    public Tile getBottomLeftCorner(){
        return grid.get(getRowNum()-1).get(0);
    }

    /** 
    * To get the bottom right corner of the Map.
    * @return The bottom right corner Tile of the map grid.
    */
    public Tile getBottomRightCorner(){
        return grid.get(getRowNum()-1).get(getColNum()-1);
    }

    /** 
    * To get the top right corner of the Map.
    * @return The top right corner Tile of the map grid.
    */
    public Tile getTopRightCorner(){
        return grid.get(0).get(getColNum()-1);
    }

    /** 
    * To get the top left corner of the Map.
    * @return The top left corner Tile of the map grid.
    */
    public Tile getTopLeftCorner(){
        return grid.get(0).get(0);
    }

    /** 
    * To get the number of rows of the Map.
    * @return The number of rows of the map grid.
    */
    public int getRowNum(){
        return grid.size();
    }

    /** 
    * To get the number of columns of the Map.
    * @return The number of columns of the map grid.
    */
    public int getColNum(){
        return grid.get(0).size();
    }

    /** 
    * To get the number of ghost(s) in the map.
    * @return The number of ghost(s) in the map.
    */
    public int getCountGhost(){
        return countGhost;
    }

    /** 
    * To get the List of all ghost(s) in the map.
    * @return The List of all ghost(s) in the map.
    */
    public List <String> getGhostList(){
        return ghostList;
    }
    
    /** 
    * To get the List of starting position coordinates of a specified ghost type in the map.
    * @param ghostType The specified ghost type.
    * @return The List of starting position coordinates of a specified ghost type in the map.
    */
    public List <List<Long>> getGhostStart(String ghostType){
        if (ghostType.equals("a")){
            return ambusherStart;
        }else if (ghostType.equals("c")){
            return chaserStart;
        }else if (ghostType.equals("i")){
            return ignorantStart;
        }else if(ghostType.equals("w")){
            return whimStart;
        }
        return null;
    }

    /** 
    * To get the List of starting position coordinates of player in the map.
    * @return The List of starting position coordinates of player in the map.
    */
    public List <Long> getWakaStart(){
        return wakaStart;
    }

    /** 
    * To get the grid (a 2d List of Tiles) of the map.
    * @return The grid (a 2d List of Tiles) of the map.
    */
    public List <List<Tile>> getGrid(){
        return grid;
    }

    /** 
    * To get the size of the Tiles of the map.
    * @return The size of the Tiles of the map.
    */
    public Long getTileSize(){
        return tileSize;
    }

    /** 
    * To get the number of total fruits inside the map.
    * @return The number of total fruits inside the map.
    */
    public Long getTotalFruits(){
        return countFruits;
    }

    /** 
    * To get the height of the map grid in pixels.
    * @return The height of the map grid in pixels.
    */
    public Long getGridHeight(){
        return gridHeight;
    }

    /** 
    * Draws the map according to the grid's (a 2d list of Tiles) Tile values.
    * @param app To draw the sprite images.
    */
    public void draw(PApplet app){

        Long countY = 0L;
        for (int i = 0; i< grid.size(); i++){
            Long countX = 0L;

            for (int j = 0; j< grid.get(i).size(); j++){
                Tile tile = grid.get(i).get(j);
                String value = tile.getValue();
                Long middleX = countX + (tile.getSize()/2);
                Long middleY = countY + (tile.getSize()/2);

                // Draws a horizontal wall.
                if (value.equals("1")){
                    PImage sprite = sprites.get("1");
                    app.image(sprite, middleX, middleY);

                // Draws a vertical wall.
                } else if (value.equals("2")){
                    PImage sprite = sprites.get("2");
                    app.image(sprite, middleX, middleY);

                // Draws a corner wall (up + left).
                } else if (value.equals("3")){
                    PImage sprite = sprites.get("3");
                    app.image(sprite, middleX, middleY);

                // Draws a corner wall (up + right).
                } else if (value.equals("4")){
                    PImage sprite = sprites.get("4");
                    app.image(sprite, middleX, middleY);

                // Draws a corner wall (down + left).
                } else if (value.equals("5")){
                    PImage sprite = sprites.get("5");
                    app.image(sprite, middleX, middleY);

                // Draws a corner wall (down + right).
                } else if (value.equals("6")){
                    PImage sprite = sprites.get("6");
                    app.image(sprite, middleX, middleY);
                
                // Draws fruit.
                } else if (value.equals("7")){
                    PImage sprite = sprites.get("7");
                    app.image(sprite, middleX, middleY);

                // Draws superfruit.
                }else if (value.equals("8")){
                    PImage sprite = sprites.get("8");
                    app.image(sprite, middleX, middleY);
                }
                countX += 16;
            }
            countY += 16;
        }
    }

    /** 
    * Parses a txt file into a grid (a 2d list of Tiles).
    * @param filename The txt file that will be parsed into a 2d list of Tiles.
    */
    public void parseMap(String filename){
        List <List<Tile>> map = new ArrayList <List<Tile>> ();
        try{
            File f = new File (filename);
            Scanner scan = new Scanner(f);

            Long countY = 0L;
            while (scan.hasNext()){
                String line = scan.nextLine();
                String[] values = line.split("");
                Long countX = 0L;
                List <Tile> row = new ArrayList <Tile>();
                for (String value: values){
                    Long middleX = countX + (tileSize/2);
                    Long middleY = countY + (tileSize/2);
                    
                    Tile tile = new Tile (tileSize, value, middleX, middleY);
                    List <Long> ghostPos = new ArrayList<Long>();

                    // Adds player's starting position coordinates.
                    if (value.equals("p")){
                        wakaStart.add(middleX);
                        wakaStart.add(middleY);
                    }
                    // Adds ambusher's starting position coordinates.
                    if (value.equals("a")){
                        ghostPos.add(middleX);
                        ghostPos.add(middleY);
                        ambusherStart.add(ghostPos);

                        ghostList.add(value);
                        countGhost++;

                    // Adds chaser's starting position coordinates.
                    } else if (value.equals("c")){
                        ghostPos.add(middleX);
                        ghostPos.add(middleY);
                        chaserStart.add(ghostPos);

                        ghostList.add(value);
                        countGhost++;

                    // Adds ignorant's starting position coordinates.
                    }else if (value.equals("i")){
                        ghostPos.add(middleX);
                        ghostPos.add(middleY);
                        ignorantStart.add(ghostPos);

                        ghostList.add(value);
                        countGhost++;

                    // Adds whim's starting position coordinates.
                    }else if (value.equals("w")){
                        ghostPos.add(middleX);
                        ghostPos.add(middleY);
                        whimStart.add(ghostPos);

                        ghostList.add(value);
                        countGhost++;
                    
                    // Sets the tile as a superfruit tile.
                    }else if (value.equals("8")){
                        tile.setAsSuper(true);
                    }
                    // Counts the fruits when the value is7 or 8.
                    if (value.equals("7") || value.equals("8")){
                        countFruits++;
                    }

                    row.add(tile);
                    countX+=16;
                }
                map.add(row);
                countY+=16;
            }
            gridHeight = countY;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
            this.grid = map;
    }

}