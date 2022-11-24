package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import processing.core.PApplet;

class IgnorantTest{
    private Ignorant testIgnorant;
    private Waka testPlayer;
    private GameMap testMap;
    private List<Long> testModeLengths;
    private PApplet app;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testMap = new GameMap();
        testMap.parseMap("mapIgnorantTest.txt");
        testModeLengths = Arrays.asList(2L, 3L);
        testPlayer = new Waka (3L, 1L, testMap);
        testIgnorant = new Ignorant(testMap, 1L, testPlayer, testModeLengths, 0, 3L);
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testMap = null;
        testModeLengths = null;
        testPlayer = null;
        testIgnorant = null;
    }
 
    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new Ignorant(testMap, 7L, testPlayer, testModeLengths, 0, 3L));
        assertNotNull(new Ignorant(testMap, 7L, null, testPlayer, testModeLengths, 0, 3L, null));
    }

    // Tests ghost becomes frightened after player eating superfruit.
    @Test
    public void testCheckFrightened(){
        app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        //player eats superfruit
        testPlayer.setLocation(424L, 280L);
        testPlayer.eatFruit(testPlayer.getCurrentTiles()[0]);
        testIgnorant.checkFrightened(app);
        assertTrue(testIgnorant.isFrightened());
    }

    // Tests ghost is not frightened because player doesnt eat superfruit.
    @Test
    public void testCheckFrightened2(){
        app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        testIgnorant.checkFrightened(app);
        assertFalse(testIgnorant.isFrightened());
    }

    //Test to get the x-axis location.
    @Test
    public void testGetX(){
        Long expected = 24L;
        Long actual = testIgnorant.getX();
        assertEquals(expected, actual);

    }

    //Test to get the y-axis location.
    @Test
    public void testGetY(){
        Long expected = 344L;
        Long actual = testIgnorant.getY();
        assertEquals(expected, actual);

    }
 
    // Tests the current Tile it is currently occupying.       
    @Test
    public void testGetCurrentTiles(){
        Tile[] expected= new Tile[5];

        expected[0] = testMap.getGrid().get(21).get(1);
        expected[1] = testMap.getGrid().get(20).get(1);
        expected[2] = testMap.getGrid().get(22).get(1);
        expected[3] = testMap.getGrid().get(21).get(0);
        expected[4] = testMap.getGrid().get(21).get(2);
        Tile[] actual = testIgnorant.getCurrentTiles();

        for (int i = 0; i<expected.length; i++){
            assertEquals(expected[i], actual[i]);
        }

    }

    // Tests moving up.
    @Test
    public void testMoveUp(){
        Long expected = 343L;
        testIgnorant.move(Direction.UP);
        Long actual = testIgnorant.getY();
        assertEquals(expected, actual);

    }

    // Tests moving down.
    @Test
    public void testMoveDown(){
        Long expected = 345L;
        testIgnorant.move(Direction.DOWN);
        Long actual = testIgnorant.getY();
        assertEquals(expected, actual);

    }

    // Tests moving right.
    @Test
    public void testMoveRight(){
        Long expected = 25L;
        testIgnorant.move(Direction.RIGHT);
        Long actual = testIgnorant.getX();
        assertEquals(expected, actual);

    }

    // Tests moving left.
    @Test
    public void testMoveLeft(){
        Long expected = 23L;
        testIgnorant.move(Direction.LEFT);
        Long actual = testIgnorant.getX();
        assertEquals(expected, actual);

    }

    // Tests invalid move.
    @Test
    public void testMoveInvalid(){

        testIgnorant.move(null);
        Long expectedX = 24L;
        Long actualX = testIgnorant.getX();

        Long expectedY = 344L;
        Long actualY = testIgnorant.getY();
        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);

    }

    // Tests colliding with player but is invisible.
    @Test
    public void testCollideInvisible(){

        testIgnorant.setInvisibility(true);
        testPlayer.setLocation(30L, 344L);
        assertFalse(testIgnorant.isCollide());
    }

    // Tests colliding with player from the right side.
    @Test
    public void testCollideWithPlayer1(){
        testPlayer.setLocation(30L, 344L);
        assertTrue(testIgnorant.isCollide());

    }

    // Tests colliding with player from the left side.
    @Test
    public void testCollideWithPlayer2(){
        testPlayer.setLocation(20L, 344L);
        assertTrue(testIgnorant.isCollide());

    }

    // Tests colliding with player from above.
    @Test
    public void testCollideWithPlayer3(){
        testPlayer.setLocation(24L, 338L);
        assertTrue(testIgnorant.isCollide());

    }

    // Tests colliding with player from below.
    @Test
    public void testCollideWithPlayer4(){
        testPlayer.setLocation(24L, 350L);
        assertTrue(testIgnorant.isCollide());

    }

    // Tests not colliding with player.
    @Test
    public void testCollideWithPlayer5(){
        testPlayer.setLocation(60L, 344L);
        assertFalse(testIgnorant.isCollide());

    }

    // Tests the available tiles to move, but only right Tile is available.
    @Test
    public void testAvailableTiles1(){

        //Some tiles are wall.
        Tile[] currentTiles = new Tile[5];
        Tile current = new Tile (16L, "c", 24L, 344L);
        Tile above = new Tile (16L, "1", 24L, 328L);
        Tile below = new Tile (16L, "1", 24L, 360L);
        Tile left = new Tile (16L, "2", 8L, 344L);
        Tile right = new Tile (16L, "0", 40L, 344L);

        currentTiles[0] = current;
        currentTiles[1] = above;
        currentTiles[2] = below;
        currentTiles[3] = left;
        currentTiles[4] = right;

        List <Tile> expected = Arrays.asList(right);
        List <Tile> actual = testIgnorant.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests the available tiles to move, but no Tile is available.
    @Test
    public void testAvailableTiles2(){
        Tile[] currentTiles = new Tile[5];
        Tile current = new Tile (16L, "1", 24L, 344L);
        Tile above = new Tile (16L, "1", 24L, 328L);
        Tile below = new Tile (16L, "1", 24L, 360L);
        Tile left = new Tile (16L, "2", 8L, 344L);
        Tile right = new Tile (16L, "1", 40L, 344L);

        currentTiles[0] = current;
        currentTiles[1] = above;
        currentTiles[2] = below;
        currentTiles[3] = left;
        currentTiles[4] = right;

        List <Tile> expected = new ArrayList <Tile> ();
        List <Tile> actual = testIgnorant.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests the available tiles to move, but only Left Tile is available.
    @Test
    public void testAvailableTiles3(){
        Tile[] currentTiles = new Tile[5];
        Tile current = new Tile (16L, "1", 24L, 344L);
        Tile above = new Tile (16L, "1", 24L, 328L);
        Tile below = new Tile (16L, "1", 24L, 360L);
        Tile left = new Tile (16L, "0", 8L, 344L);
        Tile right = new Tile (16L, "1", 40L, 344L);

        currentTiles[0] = current;
        currentTiles[1] = above;
        currentTiles[2] = below;
        currentTiles[3] = left;
        currentTiles[4] = right;

        List <Tile> expected = Arrays.asList(left);
        List <Tile> actual = testIgnorant.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests the available tiles to move, but current Tiles parameter is null.
    @Test
    public void testAvailableTiles4(){
        Tile[] currentTiles = null;
        List <Tile> expected = new ArrayList <Tile> ();
        List <Tile> actual = testIgnorant.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests get distance between 2 points.
    @Test
    public void testGetDistance(){
        double expected = 80.0D;
        double actual = testIgnorant.getDistance(24L, 264L, 24L, 344l);
        assertEquals(expected, actual, 0.01);
    }

    // Tests get distance between 2 negative points.
    @Test
    public void testGetNegativeDistance(){
        double expected = 609.89D;
        double actual = testIgnorant.getDistance(-24L, 264L, 24L, -344l);
        assertEquals(expected, actual, 0.01);
    }

    // Tests getting the next direction that is closes to target, and the direction is right.
    @Test
    public void testGetNextDirectionRight(){
        Tile next = testMap.getGrid().get(21).get(2);
        Direction expected = Direction.RIGHT;
        Direction actual = testIgnorant.getNextDirection(next);
        assertEquals(expected, actual);
    }

    // Tests getting the next direction that is closes to target, and the direction is left.
    @Test
    public void testGetNextDirectionLeft(){
        Tile next = testMap.getGrid().get(21).get(0);
        Direction expected = Direction.LEFT;
        Direction actual = testIgnorant.getNextDirection(next);
        assertEquals(expected, actual);
    }

    // Tests getting the next direction that is closes to target, and the direction is up.
    @Test
    public void testGetNextDirectionUp(){
        Tile next = testMap.getGrid().get(20).get(1);
        Direction expected = Direction.UP;
        Direction actual = testIgnorant.getNextDirection(next);
        assertEquals(expected, actual);
    }

    // Tests getting the next direction that is closes to target, and the direction is down.
    @Test
    public void testGetNextDirectionDown(){
        Tile next = testMap.getGrid().get(22).get(1);
        Direction expected = Direction.DOWN;
        Direction actual = testIgnorant.getNextDirection(next);
        assertEquals(expected, actual);
    }

    // Tests getting the next direction that is closes to target, and the direction is null.
    @Test
    public void testGetNextDirectionNull(){
        Tile next = null;
        Direction expected = Direction.NEUTRAL;
        Direction actual = testIgnorant.getNextDirection(next);
        assertEquals(expected, actual);
    }

    // Tests getting the target tile during Scatter mode.
    @Test
    public void testGetNextTileScatter(){
        List <Tile> availableTiles = new ArrayList <Tile>();
        Tile above =testMap.getGrid().get(16).get(12);
        Tile left = testMap.getGrid().get(17).get(11);
        Tile right = testMap.getGrid().get(17).get(13);

        availableTiles.add(above);
        availableTiles.add(left);
        availableTiles.add(right);

        //on Scatter Mode
        Tile target = testMap.getGrid().get(0).get(0);
        testIgnorant.setFacing(Direction.UP);

        Tile expected = above;
        Tile actual = testIgnorant.getNextTile(availableTiles, target);
        assertEquals(expected, actual);
    }

    // Tests getting the target tile during Chase mode.
    @Test
    public void testGetNextTileChase(){
        List <Tile> availableTiles = new ArrayList <Tile>();
        Tile above =testMap.getGrid().get(16).get(12);
        Tile left = testMap.getGrid().get(17).get(11);
        Tile right = testMap.getGrid().get(17).get(13);

        availableTiles.add(above);
        availableTiles.add(left);
        availableTiles.add(right);

        //on Chase Mode
        Tile target = testMap.getGrid().get(17).get(1);

        Tile expected = left;
        Tile actual = testIgnorant.getNextTile(availableTiles, target);
        assertEquals(expected, actual);
    }

    // Tests getting the next Tile to move towards but is Frightened so it is picked at random.
    @Test
    public void testGetNextTileFrightened(){
        List <Tile> availableTiles = new ArrayList <Tile>();
        Tile above =testMap.getGrid().get(16).get(12);
        Tile left = testMap.getGrid().get(17).get(11);
        Tile right = testMap.getGrid().get(17).get(13);

        availableTiles.add(above);
        availableTiles.add(left);
        availableTiles.add(right);

        //on Chase Mode but is frightened.
        testIgnorant.setFrightened(true);
        Tile target = testMap.getGrid().get(17).get(1);

        Tile expected = left;
        Tile actual = testIgnorant.getNextTile(availableTiles, target);
        assertTrue(actual.equals(left) || actual.equals(right) || actual.equals(above));
    }

    // Tests getting the next Tile but parameters are null.
    @Test
    public void testGetNextTileNull(){
        Tile actual = testIgnorant.getNextTile(null, null);
        assertNull(actual);
    }

    // Tests move the ghost but is invisible so it should stay in place.
    @Test
    public void moveGhostInvisible(){
        testIgnorant.setInvisibility(true);

        Tile current = new Tile (16L, "c", 24L, 344L);
        Direction nextDirection = Direction.RIGHT;
        testIgnorant.setLocation(24L, 344L);
        testIgnorant.moveGhost(current, nextDirection);
        Long expectedX = 24L;
        Long expectedY = 344L;
        Long actualX = testIgnorant.getX();
        Long actualY = testIgnorant.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests move the ghost when the ghost has not reach the midpoint of Tile.
    @Test
    public void moveGhostNotMidpoint(){
        Tile current = new Tile (16L, "c", 24L, 344L);
        Direction nextDirection = Direction.UP;
        testIgnorant.setLocation(22L, 344L);
        testIgnorant.moveGhost(current, nextDirection);
        Long expectedX = 23L;
        Long expectedY = 344L;
        Long actualX = testIgnorant.getX();
        Long actualY = testIgnorant.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests move the ghost up when the ghost has reach the midpoint of Tile.
    @Test
    public void moveGhostMidpoint(){
        Tile current = new Tile (16L, "c", 24L, 344L);
        Direction nextDirection = Direction.UP;
        testIgnorant.setLocation(24L, 344L);
        testIgnorant.moveGhost(current, nextDirection);
        Long expectedX = 24L;
        Long expectedY = 343L;
        Long actualX = testIgnorant.getX();
        Long actualY = testIgnorant.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests move the ghost but the parameters are null.
    @Test
    public void moveGhostNull(){
        testIgnorant.setLocation(24L, 344L);
        testIgnorant.moveGhost(null, null);
        Long expectedX = 24L;
        Long expectedY = 344L;
        Long actualX = testIgnorant.getX();
        Long actualY = testIgnorant.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    
    // Tests going back to starting position.
    @Test
    public void goBackToStart(){
        testIgnorant.setLocation(37L, 34L);
        testIgnorant.backToStart();

        Long expectedX = 24L;
        Long expectedY = 344L;
        Long actualX = testIgnorant.getX();
        Long actualY = testIgnorant.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
        
    }

    // Tests getting the Target Tile during Scatter Mode.
    @Test
    public void testTargetTileScatter(){
        Tile expected = testMap.getBottomLeftCorner();
        Tile actual = testIgnorant.getTargetTile();
        assertEquals(expected, actual);
    }


    // Tests getting the Target Tile during second Scatter Mode.
    @Test
    public void testTargetTileScatter2(){
        testIgnorant.changeMode();
        testIgnorant.changeMode();
        Tile expected = testMap.getBottomLeftCorner();
        Tile actual = testIgnorant.getTargetTile();
        assertEquals(expected, actual);
    }

    // Tests getting the Target Tile during Chase mode and it is more than 8 tiles from player.
    @Test
    public void testTargetTileChaseFar(){
        testPlayer.setLocation(420L, 280L);
        testIgnorant.changeMode();
        Tile expected = testMap.getGrid().get(17).get(26);
        Tile actual = testIgnorant.getTargetTile();
        assertEquals(expected, actual);
    }
    // Tests getting the Target Tile during Chase mode and it is less than 8 tiles from player.
    @Test
    public void testTargetTileChaseClose(){
        testIgnorant.changeMode();
        Tile expected = testMap.getBottomLeftCorner();
        Tile actual = testIgnorant.getTargetTile();
        assertEquals(expected, actual);
        
    }

    // Tests getting the Target Tile during Scatter Mode but is far from player.
    @Test
    public void testScatterNotBoundary(){
        testPlayer.setLocation(420L, 280L);
        Tile expected = testMap.getBottomLeftCorner();
        Tile actual = testIgnorant.getTargetTile();
        assertEquals(expected, actual);
    }

    // Tests when ghost is not frightened.
    @Test
    public void notFrightened(){
        Boolean actual = testIgnorant.isFrightened();
        assertFalse(actual);
    }

    // Tests when a space key is pressed.
    @Test
    public void spacePressed(){
        testIgnorant.spaceNotPressed();
        Boolean actual = testIgnorant.spaceIsPressed();
        assertFalse(actual);
    }
}