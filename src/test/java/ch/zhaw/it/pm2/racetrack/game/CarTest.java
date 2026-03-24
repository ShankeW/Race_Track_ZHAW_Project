package ch.zhaw.it.pm2.racetrack.game;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarTest {

    @Test
    void getIdReturnsConfiguredId() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertEquals('a', testCar.getId());
    }

    @Test
    void getPositionReturnsCurrentPositionAsCopy() {
        Car testCar = new Car('a', new PositionVector(2, 3));

        PositionVector firstRead = testCar.getPosition();
        PositionVector secondRead = testCar.getPosition();

        assertEquals(new PositionVector(2, 3), firstRead);
        assertEquals(new PositionVector(2, 3), secondRead);
        assertNotSame(firstRead, secondRead);
    }

    @Test
    void getVelocityStartsAtZeroAndReturnsCopy() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        PositionVector firstRead = testCar.getVelocity();
        PositionVector secondRead = testCar.getVelocity();

        assertEquals(new PositionVector(0, 0), firstRead);
        assertEquals(new PositionVector(0, 0), secondRead);
        assertNotSame(firstRead, secondRead);
    }

    @Test
    void nextPositionUsesCurrentVelocityWithoutMovingCar() {
        Car testCar = new Car('a', new PositionVector(1, 1));
        testCar.accelerate(Direction.DOWN_RIGHT);

        assertEquals(new PositionVector(2, 2), testCar.nextPosition());
        assertEquals(new PositionVector(1, 1), testCar.getPosition());
    }

    @Test
    void accelerateUpdatesVelocity() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.accelerate(Direction.RIGHT);
        testCar.accelerate(Direction.DOWN);

        assertEquals(new PositionVector(1, 1), testCar.getVelocity());
    }

    @Test
    void accelerateRejectsNull() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertThrows(IllegalArgumentException.class, () -> testCar.accelerate(null));
    }

    @Test
    void moveAppliesVelocityToPosition() {
        Car testCar = new Car('a', new PositionVector(2, 2));
        testCar.accelerate(Direction.UP_LEFT);
        testCar.accelerate(Direction.LEFT);

        testCar.move();

        assertEquals(new PositionVector(0, 1), testCar.getPosition());
        assertEquals(new PositionVector(-2, -1), testCar.getVelocity());
    }

    @Test
    void updatePositionMovesCarDirectly() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.updatePosition(new PositionVector(4, 5));

        assertEquals(new PositionVector(4, 5), testCar.getPosition());
    }

    @Test
    void crashMarksCarAsCrashedAndStoresCrashPosition() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.crash(new PositionVector(3, 1));

        assertTrue(testCar.isCrashed());
        assertEquals(new PositionVector(3, 1), testCar.getPosition());
    }

    @Test
    void crashedCarNoLongerMoves() {
        Car testCar = new Car('a', new PositionVector(1, 1));
        testCar.accelerate(Direction.RIGHT);
        testCar.crash(new PositionVector(2, 1));

        testCar.accelerate(Direction.DOWN);
        testCar.move();
        testCar.updatePosition(new PositionVector(9, 9));

        assertTrue(testCar.isCrashed());
        assertEquals(new PositionVector(2, 1), testCar.getPosition());
        assertEquals(new PositionVector(1, 0), testCar.getVelocity());
    }

    @Test
    void getMoveReturnsEmptyWhenNoStrategyIsSet() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertTrue(testCar.getMove().isEmpty());
    }

    @Test
    void setMoveStrategyMakesGetMoveReturnStrategyMove() {
        Car testCar = new Car('a', new PositionVector(0, 0));
        testCar.setMoveStrategy(() -> Optional.of(Direction.UP));

        assertEquals(Optional.of(Direction.UP), testCar.getMove());
    }

    @Test
    void isCrashedIsFalseForFreshCar() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertFalse(testCar.isCrashed());
    }
}
