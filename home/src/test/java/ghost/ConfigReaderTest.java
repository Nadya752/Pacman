package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

class ConfigReaderTest{
    private ConfigReader testConfig;
    private String normalconfigFile;
    private String nonExistentConfigFile;

    // Setting the objects.
    @BeforeEach
    public void setUp(){
        testConfig = new ConfigReader();
    }

    // Tearing down the objects.
    @AfterEach
    public void tearDown(){
        testConfig= null;
    }
 
    // Tests the constructor.
    @Test
    public void testConstructor(){
        assertNotNull(new ConfigReader());
    }
    
    // Tests getting the mode lengths from the config file.
    @Test
    public void parseModeLengths(){
        normalconfigFile= "testConfig.json";
        testConfig.parseJSON(normalconfigFile);
        List<Long> expectedModeLengths = Arrays.asList( 7L, 10L, 7L, 10L, 5L, 10L, 5L, 1000L);
        List <Long> actualModeLengths = testConfig.getModeLengths();

        assertEquals(expectedModeLengths, actualModeLengths);

    }
    
    // Tests getting the map file name from the config file.
    @Test
    public void parseMapFile(){
        normalconfigFile= "testConfig.json";
        testConfig.parseJSON(normalconfigFile);
        String expectedMap = "testMap1.txt";
        String actualMap = testConfig.getMapFile();

        assertEquals(expectedMap, actualMap);
    }

    // Tests getting the frightened length from the config file.
    @Test
    public void parseFrightenedLength(){
        normalconfigFile= "testConfig.json";
        testConfig.parseJSON(normalconfigFile);
        Long expectedFrightenedLength = 5L;
        Long actualFrightenedLength = testConfig.getFrightenedLength();

        assertEquals(expectedFrightenedLength, actualFrightenedLength);
    }

    // Tests getting the speed from the config file.
    @Test
    public void parseSpeed(){
        normalconfigFile= "testConfig.json";
        testConfig.parseJSON(normalconfigFile);
        Long expectedSpeed = 2L;
        Long actualSpeed = testConfig.getSpeed();

        assertEquals(expectedSpeed, actualSpeed);
    }

    // Tests getting the lives from the config file.
    @Test
    public void parseLives(){
        normalconfigFile= "testConfig.json";
        testConfig.parseJSON(normalconfigFile);
        Long expectedLives = 3L;
        Long actualLives = testConfig.getLives();
        
        assertEquals(expectedLives, actualLives);

    }

    // Tests nonexistent config file.
    @Test
    public void parseNonExistentConfigFile(){
        nonExistentConfigFile = "notExist.json";
        try{
            testConfig.parseJSON(nonExistentConfigFile);
        }catch(Exception e){
            assertEquals(FileNotFoundException.class, e.getClass());
        }
    }


}