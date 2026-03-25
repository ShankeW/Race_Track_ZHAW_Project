package ch.zhaw.it.pm2.racetrack.game;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarTest {

    /*
     * Equivalence classes covered by existing tests:
     * 1: Construction invariants: configured `char` id is preserved, initial position is copied, velocity starts at zero and is copied.
     * 2: Predictive movement: `nextPosition()` reflects current velocity without mutating position.
     * 3: Acceleration handling: valid accelerations accumulate deltas, null accelerations are rejected.
     * 4: Movement execution: `move()` applies velocity to position and keeps velocity, `updatePosition()` jumps directly to given coordinates.
     * 5: Crash lifecycle: `crash()` flags the car and freezes position, further movement or updates after a crash leave position unchanged while velocity stays at last value.
     * 6: Crash state reporting: freshly constructed car reports `isCrashed() == false`.
     * 7: Strategy integration: absent strategy yields `Optional.empty()`, configured strategy delegates its move.
     */

    /**
     * Tests based on the equivalence class 1: ensures that the Car constructor correctly sets the id, initializes velocity to zero, and returns copies of position and velocity to prevent external mutation.
     */
    // Equivalence class: Construction invariants - id is set, position is copied, velocity starts at zero and is copied.
    @Test
    void getIdReturnsConfiguredIdTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertEquals('a', testCar.getId());
        assertEquals(new PositionVector(0, 0), testCar.getVelocity());
    }

    @Test
    void getPositionReturnsCurrentPositionAsCopyTest() {
        Car testCar = new Car('a', new PositionVector(2, 3));

        PositionVector firstRead = testCar.getPosition();
        PositionVector secondRead = testCar.getPosition();

        assertEquals(new PositionVector(2, 3), firstRead);
        assertEquals(new PositionVector(2, 3), secondRead);
        assertNotSame(firstRead, secondRead);
    }

    @Test
    void getVelocityStartsAtZeroAndReturnsCopyTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        PositionVector firstRead = testCar.getVelocity();
        PositionVector secondRead = testCar.getVelocity();

        assertEquals(new PositionVector(0, 0), firstRead);
        assertEquals(new PositionVector(0, 0), secondRead);
        assertNotSame(firstRead, secondRead);
    }

    /**
     * Tests based on equivalenceclasses 2 -3
     */
    // Equivalence class: Predictive movement - `nextPosition()` reflects current velocity without mutating position.
    @Test
    void nextPositionUsesCurrentVelocityWithoutMovingCarTest() {
        Car testCar = new Car('a', new PositionVector(1, 1));
        testCar.accelerate(Direction.DOWN_RIGHT);

        assertEquals(new PositionVector(2, 2), testCar.nextPosition());
        assertEquals(new PositionVector(1, 1), testCar.getPosition());
    }

    @Test
    void accelerateUpdatesVelocityTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.accelerate(Direction.RIGHT);
        testCar.accelerate(Direction.DOWN);

        assertEquals(new PositionVector(1, 1), testCar.getVelocity());
    }

    @Test
    void accelerateRejectsNullTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertThrows(IllegalArgumentException.class, () -> testCar.accelerate(null));
    }

    /**
     * Tests based on equivalenceclass 4
     */
    // Movement execution - movement APIs apply velocity or override position deterministically.
    @Test
    void moveAppliesVelocityToPositionTest() {
        Car testCar = new Car('a', new PositionVector(2, 2));
        testCar.accelerate(Direction.UP_LEFT);
        testCar.accelerate(Direction.LEFT);

        testCar.move();

        assertEquals(new PositionVector(0, 1), testCar.getPosition());
        assertEquals(new PositionVector(-2, -1), testCar.getVelocity());
    }

    @Test
    void updatePositionMovesCarDirectlyTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.updatePosition(new PositionVector(4, 5));

        assertEquals(new PositionVector(4, 5), testCar.getPosition());
    }

    /**
     * Tests based on equivalence class 5 - crash lifecycle and crash state reporting
     */
    // Equivalence class: Crash lifecycle - crash flag freezes position regardless of later inputs.
    @Test
    void crashMarksCarAsCrashedAndStoresCrashPositionTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        testCar.crash(new PositionVector(3, 1));

        assertTrue(testCar.isCrashed());
        assertEquals(new PositionVector(3, 1), testCar.getPosition());
    }

    @Test
    void crashedCarNoLongerMovesTest() {
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
    // Equivalence class: Crash state reporting - freshly constructed cars report non-crashed status.
    @Test
    void isCrashedIsFalseForFreshCarTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertFalse(testCar.isCrashed());
    }

    /**
     * Test based on equivalence class 6 - strategy integration
     */
    // Equivalence class: Strategy integration - strategy presence controls Optional move outcomes.
    @Test
    void getMoveReturnsEmptyWhenNoStrategyIsSetTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));

        assertTrue(testCar.getMove().isEmpty());
    }

    @Test
    void setMoveStrategyMakesGetMoveReturnStrategyMoveTest() {
        Car testCar = new Car('a', new PositionVector(0, 0));
        testCar.setMoveStrategy(() -> Optional.of(Direction.UP));

        assertEquals(Optional.of(Direction.UP), testCar.getMove());
    }


}
