package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.Direction;

import java.util.Optional;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {

    /**
     * {@inheritDoc}
     *
     * @return next direction from move file or {@link Direction#NONE}, if no more moves are available.
     */
    @Override
    public Optional<Direction> nextMove() {
        // TODO: implementation
        throw new UnsupportedOperationException();
    }
}
