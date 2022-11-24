package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import processing.core.PApplet;

class WakaTest{
    private Waka testPlayer;
    private GameMap testMap;
    private List<Long> testModeLengths;
    private PApplet app;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testMap = new GameMap();
        testMap.parseMap("mapChaserTest.txt");
        testModeLengths = Arrays.asList(2L, 3L);
        testPlayer = new Waka (3L, 1L, testMap);
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testMap = null;
        testModeLengths = null;
        testPlayer= null;
    }

    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new Waka(3L, 3L, testMap));
        assertNotNull(new Waka(3L, 3L, null, testMap));
    }

    //Test to get the x-axis location.
    @Test
    public void testGetX(){
        Long expected = 24L;
        Long actual = testPlayer.getX();
        assertEquals(expected, actual);

    }

    //Test to get the y-axis location.
    @Test
    public void testGetY(){
        Long expected = 280L;
        Long actual = testPlayer.getY();
        assertEquals(expected, actual);

    }
        
    // Tests the current Tile it is currently occupying.
    @Test
    public void testGetCurrentTiles(){
        Tile[] expected= new Tile[5];

        expected[0] = testMap.getGrid().get(17).get(1);
        expected[1] = testMap.getGrid().get(16).get(1);
        expected[2] = testMap.getGrid().get(18).get(1);
        expected[3] = testMap.getGrid().get(17).get(0);
        expected[4] = testMap.getGrid().get(17).get(2);
        Tile[] actual = testPlayer.getCurrentTiles();

        for (int i = 0; i<expected.length; i++){
            assertEquals(expected[i], actual[i]);
        }

    }

    // Tests moving up.
    @Test
    public void testMoveUp(){
        Long expected = 279L;
        testPlayer.move(Direction.UP);
        Long actual = testPlayer.getY();
        assertEquals(expected, actual);

    }

    // Tests moving down.
    @Test
    public void testMoveDown(){
        Long expected = 281L;
        testPlayer.move(Direction.DOWN);
        Long actual = testPlayer.getY();
        assertEquals(expected, actual);

    }

    // Tests moving right.
    @Test
    public void testMoveRight(){
        Long expected = 25L;
        testPlayer.move(Direction.RIGHT);
        Long actual = testPlayer.getX();
        assertEquals(expected, actual);

    }

    // Tests moving left.
    @Test
    public void testMoveLeft(){
        Long expected = 23L;
        testPlayer.move(Direction.LEFT);
        Long actual = testPlayer.getX();
        assertEquals(expected, actual);

    }

    // Tests invalid move.
    @Test
    public void testMoveInvalid(){

        testPlayer.move(null);
        Long expectedX = 24L;
        Long actualX = testPlayer.getX();

        Long expectedY = 280L;
        Long actualY = testPlayer.getY();
        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);

    }

    // Test going bakck to starting position.
    @Test
    public void goBackToStart(){
        testPlayer.setLocation(58L, 43L);
        testPlayer.backToStart();

        Long expectedX = 24L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
        
    }

    // Test passing a tile that has no superfruit.
    @Test
    public void notEatSuperFruit(){
        testPlayer.setLocation(392L, 280L);
        boolean actual = testPlayer.eatSuperfruit();

        assertFalse(actual);
    }

    // Test passing a tile that has superfruit, but not reaching it yet.
    @Test
    public void hasNotReachSuperFruit(){
        testPlayer.setLocation(420L, 280L);
        boolean actual = testPlayer.eatSuperfruit();

        assertFalse(actual);
    }

    //Test passing and eating a superfruit.
    @Test
    public void eatSuperFruit(){
        testPlayer.setLocation(424L, 280L);
        boolean actual = testPlayer.eatSuperfruit();

        assertTrue(actual);
    }

    // Test passing a tile that has no fruit.
    @Test
    public void NotEatFruit(){
        testPlayer.setLocation(200L, 296L);
        testPlayer.eatFruit(testPlayer.getCurrentTiles()[0]);
        String expected = "0";
        String actual = testMap.getGrid().get(18).get(12).getValue();

        assertEquals(expected, actual);
    }

    // Test passing a tile that has fruit.
    @Test
    public void EatFruit(){
        testPlayer.setLocation(40L, 280L);
        testPlayer.eatFruit(testPlayer.getCurrentTiles()[0]);
        String expected = "0";
        String actual = testMap.getGrid().get(17).get(2).getValue();

        assertEquals(expected, actual);
    }

    // Test passing a tile that has fruit, but not reaching it yet.
    @Test
    public void NotReachedFruit(){
        testPlayer.setLocation(35L, 280L);
        testPlayer.eatFruit(testPlayer.getCurrentTiles()[0]);
        String expected = "7";
        String actual = testMap.getGrid().get(17).get(2).getValue();

        assertEquals(expected, actual);
    }

    //Test checking if player is winning, and the player is not winning.
    @Test
    public void CheckNotYetWin(){
        testPlayer.checkPlayerHasWon();
        Boolean actual = testPlayer.hasWon();
        assertFalse(actual);
    
    }

    //Test checking if player is winning, and the player is winning.
    @Test
    public void CheckWin(){
        for(int i = 0; i<testMap.getGrid().size(); i++){
            for (int j = 0; j<testMap.getGrid().get(i).size(); j++){
                Tile current = testMap.getGrid().get(i).get(j);
                testPlayer.setLocation(current.getPosX(), current.getPosY());
                if(current.hasFruit()){
                    testPlayer.eatFruit(current);
                }
            }

        }
        testPlayer.checkPlayerHasWon();
        Boolean expected = true;
        Boolean actual = testPlayer.hasWon();

        assertTrue(actual);
    
    }

    // Tests the available tiles to move, but only right Tile is available.
    @Test
    public void testAvailableTiles1(){

        //Some tiles are wall.
        Tile[] currentTiles = new Tile[5];
        Tile current = new Tile (16L, "p", 24L, 280L);
        Tile above = new Tile (16L, "1", 24L, 272L);
        Tile below = new Tile (16L, "1", 24L, 288L);
        Tile left = new Tile (16L, "2", 8L, 280L);
        Tile right = new Tile (16L, "7", 32L, 280L);

        currentTiles[0] = current;
        currentTiles[1] = above;
        currentTiles[2] = below;
        currentTiles[3] = left;
        currentTiles[4] = right;

        List <Tile> expected = Arrays.asList(right);
        List <Tile> actual = testPlayer.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests the available tiles to move, but current Tiles parameter is null.
    @Test
    public void testAvailableTilesNull(){
        Tile[] currentTiles = null;
        List <Tile> expected = new ArrayList <Tile> ();
        List <Tile> actual = testPlayer.getAvailableTiles(currentTiles);

        assertEquals(expected, actual);
    }

    // Tests the available direction to move, but only right Tile is available.
    @Test
    public void testGetAvailableDirectionRight(){

        Tile current = new Tile (16L, "p", 24L, 280L);
        Tile right = new Tile (16L, "7", 40L, 280L);
        List<Tile> available = new ArrayList <Tile>();
        available.add(right);

        List <Integer> expected = Arrays.asList(4);
        List<Integer> actual = testPlayer.getAvailableDirections(available,current);
        assertEquals(expected, actual);
    }

    // Tests the available direction to move, but only left Tile is available.
    @Test
    public void testGetAvailableDirectionLeft(){

        Tile current = new Tile (16L, "p", 24L, 280L);
        Tile left = new Tile (16L, "7", 10L, 280L);
        List<Tile> available = new ArrayList <Tile>();
        available.add(left);

        List <Integer> expected = Arrays.asList(3);
        List<Integer> actual = testPlayer.getAvailableDirections(available,current);
        assertEquals(expected, actual);
    }

    // Tests the available direction to move, but only above Tile is available.
    @Test
    public void testGetAvailableDirectionAbove(){

        Tile current = new Tile (16L, "p", 24L, 280L);
        Tile above = new Tile (16L, "7", 24L, 270L);
        List<Tile> available = new ArrayList <Tile>();
        available.add(above);

        List <Integer> expected = Arrays.asList(1);
        List<Integer> actual = testPlayer.getAvailableDirections(available,current);
        assertEquals(expected, actual);
    }

    // Tests the available direction to move, but only below Tile is available.
    @Test
    public void testGetAvailableDirectionBelow(){

        Tile current = new Tile (16L, "p", 24L, 280L);
        Tile below = new Tile (16L, "7", 24L, 290L);
        List<Tile> available = new ArrayList <Tile>();
        available.add(below);

        List <Integer> expected = Arrays.asList(2);
        List<Integer> actual = testPlayer.getAvailableDirections(available,current);
        assertEquals(expected, actual);
    }

    // Tests when Waka is moving towards a wall.
    @Test
    public void moveIsInvalid(){
        List <Integer> available = Arrays.asList(1, 2);
        boolean actual = testPlayer.checkMoveIsValid(available);
        assertFalse(actual);
    }

    // Tests when Waka's movement direction is valid.
    @Test
    public void moveIsValid(){
        List <Integer> available = Arrays.asList(1, 2, 4);
        boolean actual = testPlayer.checkMoveIsValid(available);
        assertTrue(actual);
    }

    // Tests when Waka's movement is null.
    @Test
    public void moveIsNull(){
        boolean actual = testPlayer.checkMoveIsValid(null);
        assertFalse(actual);
    }

    // Tests when key input direction is towards a wall.
    @Test
    public void keyIsInvalid(){
        List <Integer> available = Arrays.asList(1, 2);
        boolean actual = testPlayer.checkKeyisValid(available);
        assertFalse(actual);
    }

    // Tests when key input direction is valid.
    @Test
    public void keyIsValid(){
        List <Integer> available = Arrays.asList(0, 2);
        boolean actual = testPlayer.checkKeyisValid(available);
        assertTrue(actual);
    }

    // Tests when key input direction is null.
    @Test
    public void KeyIsNull(){
        boolean actual = testPlayer.checkKeyisValid(null);
        assertFalse(actual);
    }

    // Tests moving waka when both moves and key input is valid (not running towards a wall).
    // and direction is not perpendicular.
    @Test
    public void moveAndKeyValid1(){
        testPlayer.setKeyQueue(Direction.RIGHT);
        testPlayer.move(Direction.RIGHT);
        testPlayer.moveWaka();

        Long expectedX = 26L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka when both moves and key input is valid (not running towards a wall).
    // and direction is perpendicular and has reached the midpoint.
    @Test
    public void moveAndKeyValid2(){
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.move(Direction.RIGHT);
        testPlayer.setLocation(200L, 280L);
        testPlayer.moveWaka();

        Long expectedX = 200L;
        Long expectedY = 279L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka when both moves and key input is valid (not running towards a wall).
    // and direction is perpendicular and has not reached the midpoint.
    @Test
    public void moveAndKeyValid3(){
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.move(Direction.RIGHT);
        testPlayer.setLocation(199L, 280L);
        testPlayer.moveWaka();

        Long expectedX = 200L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka when Waka is not moving and key is not valid.
    @Test
    public void onlyMoveValid1(){
        testPlayer.setLocation(184L, 280L);
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.moveWaka();

        Long expectedX = 184L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka when Waka is moving validly but key is not valid.
    @Test
    public void onlyMoveValid2(){
        testPlayer.move(Direction.RIGHT);
        testPlayer.setLocation(184L, 280L);
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.moveWaka();

        Long expectedX = 185L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka when both moves and key input runs to a wall.
    // but has reached the midpoint of tile so is stops moving.
    @Test
    public void bothInvalid1(){
        testPlayer.setLocation(25L, 280L);
        testPlayer.move(Direction.LEFT);
        testPlayer.setKeyQueue(Direction.LEFT);
        testPlayer.moveWaka();

        Long expectedX = 24L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }
    
    // Tests moving waka when both moves and key input runs to a wall.
    // but has not reach the midpoint of tile so is still moving.
    @Test
    public void bothInvalid2(){
        testPlayer.setLocation(26L, 280L);
        testPlayer.move(Direction.LEFT);
        testPlayer.setKeyQueue(Direction.LEFT);
        testPlayer.moveWaka();

        Long expectedX = 24L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }
    
    // Tests moving waka move is heading towards a wall but key is valid.
    // and movement is not perpendicular.
    @Test
    public void onlyKeyValid1(){
        testPlayer.setLocation(25L, 280L);
        testPlayer.move(Direction.LEFT);
        testPlayer.setKeyQueue(Direction.RIGHT);
        testPlayer.moveWaka();

        Long expectedX = 25L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka move is heading towards a wall but key is valid.
    // and movement is perpendicular and has reached the midpoint.
    @Test
    public void onlyKeyValid2(){
        testPlayer.setLocation(423L, 280L);
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.move(Direction.RIGHT);
        testPlayer.moveWaka();

        Long expectedX = 424L;
        Long expectedY = 279L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }

    // Tests moving waka move is heading towards a wall but key is valid.
    // and movement is perpendicular and has not reached the midpoint.
    @Test
    public void onlyKeyValid3(){
        testPlayer.setLocation(420L, 280L);
        testPlayer.setKeyQueue(Direction.UP);
        testPlayer.move(Direction.RIGHT);
        testPlayer.moveWaka();

        Long expectedX = 422L;
        Long expectedY = 280L;
        Long actualX = testPlayer.getX();
        Long actualY = testPlayer.getY();

        assertEquals(expectedX, actualX);
        assertEquals(expectedY, actualY);
    }
    
    // Tests when Waka loses life.
    @Test
    public void losinglife(){
        testPlayer.loseLife();
        Long expected = 2L;
        Long actual = testPlayer.getLivesLeft();
        assertEquals(expected, actual);
    }
    
    // Tests getting the direction where waka is moving.
    @Test
    public void getMoveDirection(){
        Direction expected = Direction.RIGHT;
        Direction actual = testPlayer.getMoveDirection();

        assertEquals(expected, actual);

    }

    // Tests getting the width of Waka.
    @Test
    public void getWidth(){
        Long expected = 24L;
        Long actual = testPlayer.getWidth();

        assertEquals(expected, actual);

    }

    // Tests getting the height of Waka.
    @Test
    public void getHeight(){
        Long expected = 26L;
        Long actual = testPlayer.getHeight();

        assertEquals(expected, actual);

    }

}