package ghost;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/** 
* App runs the whole game Waka Waka.
*/
public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;

    private ConfigReader config;
    private  Game wakaWaka;
    private boolean gameEnded;
    private static final int restartCount = 600;
    private int endCount;



    /** 
    * Sets up the game and reads the config.json file.
    */
    public App() {
        //Set up your objects
        config = new ConfigReader();
        wakaWaka = new Game(config);
        gameEnded= false;

    }

    /** 
    * Sets the frameRate, loads the sprite images, and sets up the game.
    */
    public void setup() {
        frameRate(60);
        wakaWaka.loadSprites(this);
        wakaWaka.setUp(this);

    }

    /** 
    * Sets the width and height.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /** 
    * Evokes Game class's method for keyReleased.
    */
    public void keyReleased(){
        wakaWaka.keyReleased(this);
    }

    /** 
    * Draws the whole game.
    * This includes the map, player, ghosts, 
    * and also the "GAME OVER" and "YOU WIN" texts should a player lose or win respectively.
    */
    public void draw() { 
        background(0, 0, 0);
        imageMode(CENTER);

        // If the player wins or the player loses, the relevant texts will be on screen.
        if(wakaWaka.getStatus() == Status.WIN || wakaWaka.getStatus() == Status.LOSE){
            if (wakaWaka.getStatus() == Status.WIN ){
                wakaWaka.displayText(this, "YOU WIN");
            }else if (wakaWaka.getStatus() == Status.LOSE ){
                wakaWaka.displayText(this, "GAME OVER");   
            }

            //If the game is won or lost, it ends and restarts after 10 seconds with a new game.
            if (!gameEnded){
                gameEnded = true;
                endCount= frameCount + restartCount;
            } else if (gameEnded){
                if (frameCount == endCount){
                    config = new ConfigReader();
                    wakaWaka = new Game(config);
                    gameEnded= false;
                    this.setup();
                }
            }
        }else{
            wakaWaka.draw(this);
        }

    }

    
    /** 
    * The main method of the app class.
    */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
