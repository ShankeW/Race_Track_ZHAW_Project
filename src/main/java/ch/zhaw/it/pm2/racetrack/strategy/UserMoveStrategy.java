package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Let the user decide the next move.
 */
public class UserMoveStrategy implements MoveStrategy {

    UserInterface UI;

    /**
     * Movestrategy that gets the next move from the user through a User Interface.
     * @param ui the user interface that gets input from the user
     */

    public UserMoveStrategy(UserInterface ui){
        this.UI = ui;
    }

    /**
     * {@inheritDoc}
     * Asks the user for the direction vector.
     *
     * @return next direction, {Optional#empty()} if the user terminates the game.
     */
    @Override
    public Optional<Direction> nextMove() {
        Direction[] directions = Direction.values();
        ArrayList<String> directionNames = new ArrayList<>();
        for (Direction type : directions){
            directionNames.add(type.toString());
        }
        Direction chosenDirection = directions[UI.getUserInput(directionNames)];
        return Optional.of(chosenDirection);
    }
}
