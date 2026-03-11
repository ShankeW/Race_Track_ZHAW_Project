package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.Optional;

/**
 * Let the user decide the next move.
 */
public class UserMoveStrategy implements MoveStrategy {

    /**
     * {@inheritDoc}
     * Asks the user for the direction vector.
     *
     * @return next direction, {Optional#empty()} if the user terminates the game.
     */
    @Override
    public Optional<Direction> nextMove() {
        // TODO: implementation
        throw new UnsupportedOperationException();
    }
}
