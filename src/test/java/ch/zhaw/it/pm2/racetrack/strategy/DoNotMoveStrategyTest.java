package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.game.Direction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoNotMoveStrategyTest {

    @Test
    void nextMoveReturnsNoneDirectionTest() {
        DoNotMoveStrategy strategy = new DoNotMoveStrategy();

        assertEquals(Optional.of(Direction.NONE), strategy.nextMove());
    }
}
