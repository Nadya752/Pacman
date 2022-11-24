package ghost;

import processing.core.PApplet;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.List;
import java.util.ArrayList;

/** 
* Config file parser class of Waka Waka.
*/
public class ConfigReader{

    private String mapFile;
    private Long lives;
    private Long speed;
    private Long frightenedLength;
    private List<Long> modeLengths;

    /** 
    * Config file parser class of Waka Waka.
    */
    public ConfigReader(){
        modeLengths = new ArrayList <Long> ();
    }   
    /** 
    * Parses a JSON file to get the game's map filename, lives, speed, and also the ghosts' frightened and mode lengths.
    * @param fileName The JSON file that will be parsed.
    */
    public void parseJSON (String fileName){
        JSONParser parseJ = new JSONParser();
        try{
            Object obj = parseJ.parse(new FileReader(fileName));
            JSONObject JSONObj = (JSONObject) obj;
            mapFile = (String) JSONObj.get("map");
            lives = (Long) JSONObj.get("lives");
            speed = (Long) JSONObj.get("speed");
            frightenedLength = (Long) JSONObj.get("frightenedLength");
    
            JSONArray jArray = (JSONArray) JSONObj.get("modeLengths");
            for (int i = 0; i<jArray.size(); i++){
                modeLengths.add(Long.parseLong(jArray.get(i).toString()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** 
    * To get the String name of map file of the game.
    * @return The String name of map file of the game.
    */
    public String getMapFile(){
        return mapFile;
    }

    /** 
    * To get the lives of player in the game.
    * @return The lives of player in the game.
    */
    public Long getLives(){
        return lives;
    }

    /** 
    * To get the speed of player and ghost(s) in the game.
    * @return The speed of player and ghost(s) in the game.
    */
    public Long getSpeed(){
        return speed;
    }

    /** 
    * To get the frightened length of the ghost(s) in the game.
    * @return The frightened length of the ghost(s) in the game.
    */
    public Long getFrightenedLength(){
        return frightenedLength;
    }

    /** 
    * To get the mode lengths of the ghost(s) in the game.
    * @return The mode lengths of the ghost(s) in the game.
    */
    public List <Long> getModeLengths(){
        return modeLengths;

    }


}