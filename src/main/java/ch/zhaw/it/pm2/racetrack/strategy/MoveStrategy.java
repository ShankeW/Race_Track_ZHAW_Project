package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.Direction;

import java.util.Optional;

/**
 * Move Strategy, the different cars may choose to determine the next direction to accelerate.
 */
public interface MoveStrategy {
    /**
     * Determine the direction to accelerate in the next move.
     *
     * @return Direction vector to accelerate in the next move. {@link Optional#empty()} will terminate the game.
     */
    Optional<Direction> nextMove();

    /**
     * Possible Move Strategies which can be selected. This shall not be altered!
     */
    enum StrategyType {
        /** the car does not move. */
        DO_NOT_MOVE,
        /** the user selects the next move. */
        USER,
        /** the car follows a list of moves. */
        MOVE_LIST,
        /** the car follows a path of points. */
        PATH_FOLLOWER,
        /** the car finds a path to the finish line. */
        PATH_FINDER;

        @Override
        public String toString() {
            String rawString = super.toString();
            rawString = rawString.replace("_", " ");
            return rawString;
        }
    }
}
