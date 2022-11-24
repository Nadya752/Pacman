package ghost;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

// Enum for constants of the status of the game.
enum Status{
    PLAYING,
    LOSE,
    WIN;
}

/** 
* Game driver class of Waka Waka.
*/
public class Game{

    private GameMap gameMap;
    private Waka player;
    private List <Ambusher> ambushers;
    private List <Chaser> chasers;
    private List <Ignorant> ignorants;
    private List <Whim> whims;
    private List <Ghost> allGhosts;
    private HashMap <String, PImage> mapContents;
    private HashMap <Direction, PImage> wakaFaces;
    private Long lives;
    private Long speed;
    private String mapFile;
    private List <Long> modeLengths;
    private Long frightenedLength;
    private ConfigReader config;
    private PImage[] ghostSprites;
    private Status gameStatus;
    private boolean isDebug;

    /** 
    * Game driver class of Waka Waka. 
    * It has all the objects of the game, namely the player, ghost(s), and game map.
    * @param config The config file for the game.
    *               It sets the map filename, lives of player,
    *               speed of player and ghost(s), and also mode and frightened lengths 
    *               of the ghosts.
    */
    public Game(ConfigReader config){
        config.parseJSON("config.json");
        this.mapFile = config.getMapFile();
        this.lives = config.getLives();
        this.speed = config.getSpeed();
        this.modeLengths = config.getModeLengths();
        this.frightenedLength = config.getFrightenedLength();

        this.gameStatus = Status.PLAYING;
        this.isDebug = false;

        this.ambushers = new ArrayList <Ambusher>();
        this.chasers = new ArrayList <Chaser>();
        this.ignorants = new ArrayList <Ignorant>();
        this.whims = new ArrayList <Whim>();
        this.allGhosts = new ArrayList <Ghost>();

        this.mapContents = new HashMap<String, PImage> ();
        this.wakaFaces = new HashMap<Direction, PImage> ();

    }

    /** 
    * Loads all the sprite images for the game.
    * @param app The app for calling loadImage method.
    */
    public void loadSprites(PApplet app){

        // Loading the images of the map Tiles.
        PImage horizontal = app.loadImage("src/main/resources/horizontal.png");
        PImage vertical = app.loadImage("src/main/resources/vertical.png");
        PImage downLeft = app.loadImage("src/main/resources/downLeft.png");
        PImage downRight = app.loadImage("src/main/resources/downRight.png");
        PImage upLeft = app.loadImage("src/main/resources/upLeft.png");
        PImage upRight = app.loadImage("src/main/resources/upRight.png");
        PImage fruit = app.loadImage("src/main/resources/fruit.png");
        PImage superfruit = app.loadImage("src/main/resources/superfruit.png");

        mapContents.put("1", horizontal);
        mapContents.put("2", vertical);
        mapContents.put("3", upLeft);
        mapContents.put("4", upRight);
        mapContents.put("5", downLeft);
        mapContents.put("6", downRight);
        mapContents.put("7", fruit);
        mapContents.put("8", superfruit);

        //Loading images of Waka's faces.
        PImage wakaDown = app.loadImage("src/main/resources/playerDown.png");
        PImage wakaUp = app.loadImage("src/main/resources/playerUp.png");
        PImage wakaLeft = app.loadImage("src/main/resources/playerLeft.png");
        PImage wakaRight = app.loadImage("src/main/resources/playerRight.png");
        PImage wakaClosed = app.loadImage("src/main/resources/playerClosed.png");

        wakaFaces.put(Direction.DOWN, wakaDown);
        wakaFaces.put(Direction.UP, wakaUp);
        wakaFaces.put(Direction.LEFT, wakaLeft);
        wakaFaces.put(Direction.RIGHT, wakaRight);
        wakaFaces.put(Direction.NEUTRAL, wakaClosed);

        //Loading the images of the four types of ghosts.
        PImage ambusherSprite = app.loadImage("src/main/resources/ambusher.png");
        PImage chaserSprite = app.loadImage("src/main/resources/chaser.png");
        PImage ignorantSprite = app.loadImage("src/main/resources/ignorant.png");
        PImage whimSprite = app.loadImage("src/main/resources/whim.png");
        PImage frightenedSprite = app.loadImage("src/main/resources/frightened.png");

        PImage[] ghostImages = {ambusherSprite, chaserSprite, ignorantSprite, whimSprite, frightenedSprite};
        ghostSprites = ghostImages;
    }

    /** 
    * Sets up the game.
    * All the objects of the game, such as the map, the player, and ghosts are initialised.
    * @param app To access the app frameCount to count for ghost modes.
    */
    public void setUp(PApplet app){
        PImage ambusherSprite = ghostSprites[0];
        PImage chaserSprite = ghostSprites[1];
        PImage ignorantSprite = ghostSprites[2];
        PImage whimSprite = ghostSprites[3];
        PImage frightenedSprite = ghostSprites[4];

        this.gameMap = new GameMap (mapContents);
        gameMap.parseMap(mapFile);
        this.player = new Waka(lives, speed, wakaFaces, gameMap);
        for (int i=0; i<gameMap.getGhostList().size(); i++){
            String ghostType = gameMap.getGhostList().get(i);
            if(ghostType.equals("a")){
                Ambusher ambusher = new Ambusher(gameMap, speed, ambusherSprite, player, modeLengths, ambushers.size(), frightenedLength, frightenedSprite);
                ambushers.add(ambusher);
                allGhosts.add(ambusher);
                
            }else if(ghostType.equals("c")){
                Chaser chaser = new Chaser(gameMap, speed, chaserSprite, player, modeLengths, chasers.size(), frightenedLength, frightenedSprite);
                chasers.add(chaser);
                allGhosts.add(chaser);
            }else if(ghostType.equals("i")){
                Ignorant ignorant =new Ignorant(gameMap, speed, ignorantSprite, player, modeLengths, ignorants.size(), frightenedLength, frightenedSprite);
                ignorants.add(ignorant);
                allGhosts.add(ignorant);
            }
        }

        for (int i = 0; i<gameMap.getGhostList().size(); i++){
            String ghostType = gameMap.getGhostList().get(i);
            int countChaser = 0;
            Chaser chaserGhost = chasers.get(countChaser);
            if(ghostType.equals("w")){
                Whim whim = new Whim(gameMap, speed, whimSprite, player, modeLengths, whims.size(), chaserGhost, frightenedLength, frightenedSprite);
                whims.add(whim);
                allGhosts.add(whim);
                countChaser++;
            }
        }

        
        allGhosts.get(0).setTargetFrame(app.frameCount);
    
    }

    /** 
    * Called when any keys are pressed and then released.
    * Used for debug mode and moving Waka.
    * @param app To access the app frameCount to count for ghost modes.
    */
    public void keyReleased(PApplet app){
        for (Ghost ghost : allGhosts){
            // When a space key was pressed,
            // then the debug mode commences. But if debug mode is on already,
            // then it is turned off.
            if (ghost.spaceIsPressed()){

                if (!isDebug){
                    ghost.setDebug(true);
                    isDebug = true;
                    break;
                }else if (isDebug){
                    ghost.setDebug(false);
                    isDebug = false;
                    ghost.spaceNotPressed();
                    break;
                }
            }
        }
        // Waka also starts moving according to the key that was pressed and released.
        player.setKeyIsReleased(true);
    }

    /** 
    * Getter method to get the status of the game.
    * @return The status of the game, whether it is playing, win, or lose.
    */
    public Status getStatus(){
        return gameStatus;
    }

    /** 
    * Draws the text "YOU WIN" or "GAME OVER".
    * @param app To draw the texts.
    * @param message The message that the text displays.
    */
    public void displayText(PApplet app, String message){
        PFont mono;

        mono = app.createFont("PressStart2P-Regular.ttf", 32);
        app.textFont(mono);
        app.textAlign(app.CENTER, app.CENTER);
        app.text(message, 448/2, 576/2);
    }

    /** 
    * Draws the whole game, which includes the player, ghosts, and map.
    * It is called in App draw method in order to 
    * display movements and visuals of the game.
    * @param app To access frameCount and displaying sprites.
    */
    public void draw(PApplet app){
        boolean isCollision = false;
        boolean isFrightened = false;

        // Checks if the game is won or lost.
        if (player.getLivesLeft() <0){
            gameStatus = Status.LOSE;
        }else if (player.hasWon()){
            gameStatus = Status.WIN;
        }

        gameMap.draw(app);
        player.tick(app);
        player.draw(app);


        // Check if any ghost collided, or if a superfruit has been eaten.
        for (Ghost ghost : allGhosts){
            if (ghost.isCollide()){
                isCollision = true;
            }
            if (ghost.isFrightened()){
                isFrightened = true;
            }
        }

        // Player loses life when it collides with a ghost, and the ghost becomes not frightened.
        if (isCollision && !isFrightened){
            player.loseLife();
            player.backToStart();       
        }
  
        // Tells the other ghosts that a ghost has collided, if a ghost has collided.
        for (Ghost ghost : allGhosts){
            if (isCollision){
                ghost.setHasCollide(true);
            }

            ghost.tick(app);
            ghost.draw(app);
            ghost.setHasCollide(false);
        }

    }
}