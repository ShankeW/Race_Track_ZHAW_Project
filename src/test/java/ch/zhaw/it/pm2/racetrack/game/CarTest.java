package ch.zhaw.it.pm2.racetrack.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarTest {

    Car testCar;

    char testID = 'a';

    @BeforeEach
    void createCar(){
        testCar = new Car(testID, new PositionVector(0,0));
    }

    @Test
    void testGetID(){
        //TODO Create Test
    }

    @Test
    void testGetPosition(){
        //TODO Create Test
    }

    @Test
    void testGetVelocity(){
        //TODO Create Test
    }

    @Test
    void testNextPosition(){
        //TODO Create Test
    }

    @Test
    void testAccelerate(){
        //TODO Create Test
    }

    @Test
    void testMove(){
        //TODO Create Test
    }
    @Test
    void testUpdatePosition(){
        //TODO Create Test
    }

    @Test
    void testIsCrashed(){
        //TODO Create Test
    }

    @Test
    void testCrash(){
        //TODO Create Test
    }

    @Test
    void testSetMoveStrategy(){
        //TODO Create Test
    }
    @Test
    void testGetMove(){
        //TODO Create Test
    }
}
