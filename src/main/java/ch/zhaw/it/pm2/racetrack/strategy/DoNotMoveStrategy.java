package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.Direction;

import java.util.Optional;

/**
 * Do not accelerate in any direction.
 */
public class DoNotMoveStrategy implements MoveStrategy {
    /**
     * {@inheritDoc}
     *
     * @return always {@link Direction#NONE}
     */
    @Override
    public Optional<Direction> nextMove() {
        // TODO: implementation
        throw new UnsupportedOperationException();
    }
}
