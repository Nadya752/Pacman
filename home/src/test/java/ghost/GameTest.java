package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import processing.core.PApplet;

class GameTest{
    private ConfigReader testConfig;
    private Game testGame;
    private PApplet app;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testConfig = new ConfigReader();
        testConfig.parseJSON("testConfig.JSON");
        testGame = new Game(testConfig);
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testConfig = null;
        testGame = null;
    }

    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new Game(testConfig));
    }

    // Tests setting up the game.
    @Test
    public void testLoadSprites(){
        app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        testGame.loadSprites(app);
        testGame.setUp(app);
    
    }

    // Tests the status of the game.
    @Test
    public void testGetStatus(){
        Status expected = Status.PLAYING;
        Status actual = testGame.getStatus();
        assertEquals(expected, actual);
    }

}