package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

class GameMapTest{
    private GameMap testMap;
    private String normalMap;
    private String nonExistentMap;
    private String badMap;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testMap = new GameMap();
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testMap = null;
    }
 
    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new GameMap());
    }

    // Test parsing a map.
    @Test
    public void parseNormalMap(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <List<Tile>> actual = testMap.getGrid();
        List<List<String>> expected = new ArrayList <List<String>>();


        try{
            File f = new File (normalMap);
            Scanner scan = new Scanner(f);

            while (scan.hasNext()){
                String line = scan.nextLine();
                String[] values = line.split("");
                List <String> row = new ArrayList <String>();
                for (String value: values){
                    row.add(value);
                }
                expected.add(row);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        
        for (int i = 0; i<expected.size(); i++){
            for (int j = 0;j<expected.get(i).size(); j++){
                assertEquals(expected.get(i).get(j), actual.get(i).get(j).getValue());
            }
        }
    }

    // Test parsing a non existent map.
    @Test
    public void parseNonExistentMap(){
        nonExistentMap = "notExist.txt";
        try{
            testMap.parseMap(nonExistentMap);
        }catch(Exception e){
            assertEquals(FileNotFoundException.class, e.getClass());
        }
    }

    // Tests getting the bottom left corner tile of the map.
    @Test
    public void testGetBottomLeftCorner(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Tile actual = testMap.getBottomLeftCorner();
        Tile expected = testMap.getGrid().get(35).get(0);

        assertEquals(expected, actual);
    }

    // Tests getting the bottom right corner tile of the map.
    @Test
    public void testGetBottomRightCorner(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Tile actual = testMap.getBottomRightCorner();
        Tile expected = testMap.getGrid().get(35).get(27);

        assertEquals(expected, actual);
    }

    // Tests getting the top right corner tile of the map.
    @Test
    public void testGetTopRightCorner(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Tile actual = testMap.getTopRightCorner();
        Tile expected = testMap.getGrid().get(0).get(27);

        assertEquals(expected, actual);
    }

    // Tests getting the top left corner tile of the map.
    @Test
    public void testGetTopLeftCorner(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Tile actual = testMap.getTopLeftCorner();
        Tile expected = testMap.getGrid().get(0).get(0);

        assertEquals(expected, actual);
    }

    // Tests getting column number of the map.
    @Test
    public void testGetColNum(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        int actual = testMap.getColNum();
        int expected = 28;

        assertEquals(expected, actual);
    }

    // Tests getting row number of the map.
    @Test
    public void testGetRowlNum(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        int actual = testMap.getRowNum();
        int expected = 36;

        assertEquals(expected, actual);
    }

    // Tests getting the number of ghosts in the map.
    @Test
    public void testCountGhost(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        int actual = testMap.getCountGhost();
        int expected = 4;

        assertEquals(expected, actual);
    }

    // Tests getting the lists of ghosts in the map.
    @Test
    public void testGetGhostList(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <String> actual = testMap.getGhostList();
        List <String> expected = Arrays.asList("a", "c", "i", "w");

        assertEquals(expected, actual);
    }

    // Tests getting the starting positions of Ambusher in the map.
    @Test
    public void testAmbusherStart(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <List<Long>> expected = new ArrayList <List<Long>>();
        List <Long> ambusherStart = Arrays.asList(24L, 344L);
        expected.add(ambusherStart);
        List <List<Long>> actual = testMap.getGhostStart("a");

        assertEquals(expected, actual);
    }


    // Tests getting the starting positions of Chaser in the map.
    @Test
    public void testChaserStart(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <List<Long>> expected = new ArrayList <List<Long>>();
        List <Long> chaserStart = Arrays.asList(104L, 344L);
        expected.add(chaserStart);
        List <List<Long>> actual = testMap.getGhostStart("c");
        
        assertEquals(expected, actual);
    }

    // Tests getting the starting positions of Ignorant in the map.
    @Test
    public void testIgnorantStart(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <List<Long>> expected = new ArrayList <List<Long>>();
        List <Long> ignorantStart = Arrays.asList(184L, 344L);
        expected.add(ignorantStart);
        List <List<Long>> actual = testMap.getGhostStart("i");
        
        assertEquals(expected, actual);
    }

    // Tests getting the starting positions of Whim in the map.
    @Test
    public void testWhimStart(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        List <List<Long>> expected = new ArrayList <List<Long>>();
        List <Long> whimStart = Arrays.asList(264L, 344L);
        expected.add(whimStart);
        List <List<Long>> actual = testMap.getGhostStart("w");
        
        assertEquals(expected, actual);
    }

    // Tests getting the height of the map.
    @Test
    public void testGridHeight(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Long actual = testMap.getGridHeight();
        Long expected = 576L;
    
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTotalFruits(){
        normalMap = "testMap1.txt";
        testMap.parseMap(normalMap);
        Long actual = testMap.getTotalFruits();
        Long expected = 28L;
    
        assertEquals(expected, actual);
    }

}