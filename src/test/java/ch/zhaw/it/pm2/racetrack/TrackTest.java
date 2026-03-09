package ch.zhaw.it.pm2.racetrack;

import ch.zhaw.it.pm2.racetrack.game.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


class TrackTest {
    private Track track0;
    private Track track1;
    private Track track2;
    private Track track3;

    @BeforeEach
    void setUp() {
        File trackFile0 = new File("tracks/challenge.txt");
        File trackFile1 = new File("tracks/oval-anticlock-right.txt");
        File trackFile2 = new File("tracks/oval-clock-up.txt");
        File trackFile3 = new File("tracks/quarter-mile.txt");
        try{
            track0 = new Track(trackFile0);
            track1 = new Track(trackFile1);
            track2 = new Track(trackFile2);
            track3 = new Track(trackFile3);
        } catch (IOException | InvalidFileFormatException e){
            System.out.println("File not found.");
        }
    }

    /**
     * Test for invalid file should consider following files as invalid:
     *  - the file contains no track lines (grid height is 0)
     *  - not all track lines have the same length
     *  - the file contains no cars
     *  - the file contains more than max number of cars
     */
    @Test
    void InvalidFileFormatTest() {
    }

    /**
     * Test for getHeightTest method checks if the height is read correctly.
     * If the file is ending with an empty or multiple empty lines, then these 
     * lines should not be counted.
     */
    @Test
    void getHeightTest() {
    }

    /**
     * Test for getSpaceTypeAtPosition method checks the position and returns 
     * the correct SpaceType, while cars are being considered as TRACK.
     */
    @Test
    void getSpaceTypeAtPositionTest1(){
        SpaceType spaceType0 = track0.getSpaceTypeAtPosition(new PositionVector(18,1));  // TRACK
        SpaceType spaceType1 = track0.getSpaceTypeAtPosition(new PositionVector(5,5));   // WALL
        SpaceType spaceType2 = track0.getSpaceTypeAtPosition(new PositionVector(25,22)); // Car 'a'
        SpaceType spaceType3 = track0.getSpaceTypeAtPosition(new PositionVector(22,23)); // FINISH_RIGHT

        assertEquals(SpaceType.TRACK, spaceType0);
        assertEquals(SpaceType.WALL, spaceType1);
        assertEquals(SpaceType.TRACK, spaceType2);
        assertEquals(SpaceType.FINISH_RIGHT, spaceType3);
    }


    // Following are the Tests that works only after Car class is implemented, because the CRASH_INDICATOR
    // must be used for the test.

    /**
     * Test for getCharRepresentationAtPosition method checks if this method returns 
     * the correct character on corresponding position. Especially, the CRASH_INDICATOR 
     * for car crash should be returned correctly.
     */
    @Test
    void getCharRepresentationAtPositionTest(){
        assertEquals(' ', track1.getCharRepresentationAtPosition(6, 5));    // TRACK
        assertEquals('#', track1.getCharRepresentationAtPosition(6, 1));    // WALL
        assertEquals('>', track1.getCharRepresentationAtPosition(11, 20));  // FINISH_RIGHT
        // TODO after Car class is implemented
        // case for cars when crashed and not crashed
        //assertEquals('a', track1.getCharRepresentationAtPosition(9, 22));
    }

    /**
     * Test for toString method checks if the Track class can correctly output the desired
     * track based on the File input.
     */
    // @Test
    // void toStringTest() {
    //     String expected =
    //                     "##################################################\r\n" + //
    //                     "##################################################\r\n" + //
    //                     "##############                       #############\r\n" + //
    //                     "##########                              ##########\r\n" + //
    //                     "#######                                    #######\r\n" + //
    //                     "######  a   b   #################           ######\r\n" + //
    //                     "#####^^^^^^^^^^###################           #####\r\n" + //
    //                     "#####          ###################           #####\r\n" + //
    //                     "######          #################           ######\r\n" + //
    //                     "#######                                    #######\r\n" + //
    //                     "##########                              ##########\r\n" + //
    //                     "##############                      ##############\r\n" + //
    //                     "##################################################\r\n" + //
    //                     "##################################################\r\n" + //
    //                     "";
    //     assertEquals(expected, track2.toString());
    // }
}
