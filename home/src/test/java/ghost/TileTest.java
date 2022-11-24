package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

class TileTest{
    private Tile testTile;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testTile = new Tile(16L, "1", 24L, 24L);
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testTile = null;
    }
 
    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new Tile(null, null, null, null));
        assertNotNull(new Tile(16L, null, null, null));
        assertNotNull(new Tile(null, "2", null, null));
        assertNotNull(new Tile(null, null, 24L, null));
        assertNotNull(new Tile(null, null, null, 24L));
    }
    
    //Test to get the x-axis location.
    @Test
    public void tilePositionX(){
        Long expected = 24L;
        Long actual = testTile.getPosX();
        assertEquals(expected, actual);

    }
        
    //Test to get the y-axis location.
    @Test
    public void tilePositionY(){
        Long expected = 24L;
        Long actual = testTile.getPosX();
        assertEquals(expected, actual);

    }

    // Tests getting the value of the tile.
    @Test
    public void tileValue(){
        String expected = "1";
        String actual = testTile.getValue();
        assertEquals(expected, actual);

    }

    // Tests getting the size of the tile.
    @Test
    public void tileSize(){
        Long expected = 16L;
        Long actual = testTile.getSize();
        assertEquals(expected, actual);

    }


    // Tests setting the value of the tile.
    @Test
    public void tileSetValue(){
        String expected = "4";
        testTile.setValue("4");
        String actual = testTile.getValue();
        assertEquals(expected, actual);

    }

    // Tests setting the value of the tile as a superfruit tile.
    @Test
    public void tileSuperFruit(){
        String expected = "8";
        testTile.setValue("8");
        testTile.setAsSuper(true);
        String actual = testTile.getValue();
        assertEquals(expected, actual);
        assertTrue(testTile.isSuperfruit());
    }

    // Tests getting which row the Tile occupies in the map.
    @Test
    public void tileGetRow(){
        Long expected = 1L;
        Long actual = testTile.getRow();
        assertEquals(expected, actual);
    }

    // Tests getting which column the Tile occupies in the map.
    @Test
    public void tileGetCol(){
        Long expected = 1L;
        Long actual = testTile.getCol();
        assertEquals(expected, actual);
    }

    // Tests checking if empty space is wall.
    @Test
    public void tileCheckWall0(){
        testTile.setValue("0");
        assertFalse(testTile.isWall());
    }

    // Tests checking if horizontal wall is wall.
    @Test
    public void tileCheckWall1(){
        assertTrue(testTile.isWall());
    }

    // Tests checking if vertical wall is wall.
    @Test
    public void tileCheckWall2(){
        testTile.setValue("2");
        assertTrue(testTile.isWall());
    }

    // Tests checking if corner wall is wall.
    @Test
    public void tileCheckWall3(){
        testTile.setValue("3");
        assertTrue(testTile.isWall());
    }

    // Tests checking if corner wall is wall.
    @Test
    public void tileCheckWall4(){
        testTile.setValue("4");
        assertTrue(testTile.isWall());
    }

    // Tests checking if corner wall is wall.
    @Test
    public void tileCheckWall5(){
        testTile.setValue("5");
        assertTrue(testTile.isWall());
    }

    // Tests checking if corner wall is wall.
    @Test
    public void tileCheckWall6(){
        testTile.setValue("6");
        assertTrue(testTile.isWall());
    }

    // Tests checking if fruit Tile is wall.
    @Test
    public void tileCheckWall7(){
        testTile.setValue("7");
        assertFalse(testTile.isWall());
    }

    // Tests checking if superfruit Tile is wall.
    @Test
    public void tileCheckWall8(){
        testTile.setValue("8");
        assertFalse(testTile.isWall());
    }

    // Tests checking if player starting position is wall.
    @Test
    public void tileCheckWallP(){
        testTile.setValue("p");
        assertFalse(testTile.isWall());
    }

    // Tests checking if Ambusher starting position is wall.
    @Test
    public void tileCheckWallA(){
        testTile.setValue("a");
        assertFalse(testTile.isWall());
    }

    // Tests checking if Chaser starting position is wall.
    @Test
    public void tileCheckWallC(){
        testTile.setValue("c");
        assertFalse(testTile.isWall());
    }

    // Tests checking if Ignorant starting position is wall.
    @Test
    public void tileCheckWallI(){
        testTile.setValue("i");
        assertFalse(testTile.isWall());
    }

    // Tests checking if Whim starting position is wall.
    @Test
    public void tileCheckWallW(){
        testTile.setValue("w");
        assertFalse(testTile.isWall());
    }

    // Tests checking if a fruit Tile has fruit.
    @Test
    public void tilehasFruit(){
        testTile.setValue("7");
        assertTrue(testTile.hasFruit());
    }

    // Tests checking if a superfruit Tile has fruit.
    @Test
    public void tilehasFruit2(){
        testTile.setValue("8");
        assertTrue(testTile.hasFruit());
    }

    // Tests checking if not a fruit Tile has fruit.
    @Test
    public void tileNoFruit(){
        assertFalse(testTile.hasFruit());
    }

    // Tests checking a previous fruit tile has fruit.
    @Test
    public void tileHadFruit(){
        testTile.setValue("8");
        testTile.setValue("1");
        assertFalse(testTile.hasFruit());
    }

}