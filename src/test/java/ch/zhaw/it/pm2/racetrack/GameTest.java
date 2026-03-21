package ch.zhaw.it.pm2.racetrack;

import ch.zhaw.it.pm2.racetrack.game.Direction;
import ch.zhaw.it.pm2.racetrack.game.Game;
import ch.zhaw.it.pm2.racetrack.game.PositionVector;
import ch.zhaw.it.pm2.racetrack.game.Track;
import ch.zhaw.it.pm2.racetrack.given.GameSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @TempDir
    Path tempDir;

    /*
     * Equivalence classes for Game:
     * EC-GAME-1: calculatePath with diagonal movement -> includes start and end, with diagonal intermediates.
     * EC-GAME-2: calculatePath with axis-aligned movement -> all intermediate points stay on that axis.
     * EC-GAME-3: doCarTurn on own start field -> no self-collision at start position.
     * EC-GAME-4: doCarTurn into occupied track field -> current car crashes, last remaining car becomes winner.
     * EC-GAME-5: doCarTurn crosses finish in correct direction -> winner is set and car stops at finish field.
     * EC-GAME-6: doCarTurn crosses finish in wrong direction -> no winner, car remains at previous path step.
     * EC-GAME-7: doCarTurn after winner already exists -> turn is ignored.
     * EC-GAME-8: doCarTurn for already crashed current car -> turn is ignored.
     * EC-GAME-9: switchToNextActiveCar with crashed cars -> skips crashed cars and wraps around.
     * EC-GAME-10: switchToNextActiveCar when all cars crashed -> current index remains unchanged.
     * EC-GAME-11: nextCarMove without strategy -> Optional.empty().
     * EC-GAME-12: nextCarMove with strategy -> returns strategy-provided move.
     */

    /**
     * Tests for calculatePath() based on the equivalence classes 1 and 2.
     * @throws Exception
     */
    @Test
    void testCalculatePathDiagonalMovementIfContainsAllInterveningVectors() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#   #",
            "#   #",
            "#####"
        );

        List<PositionVector> path = game.calculatePath(new PositionVector(1, 1), new PositionVector(5, 5));

        assertEquals(5, path.size());
        assertEquals(new PositionVector(1, 1), path.getFirst());
        assertEquals(new PositionVector(2, 2), path.get(1));
        assertEquals(new PositionVector(3, 3), path.get(2));
        assertEquals(new PositionVector(4, 4), path.get(3));
        assertEquals(new PositionVector(5, 5), path.getLast());
    }

    @Test
    void testCalculatePathAxisAlignedMovementIfContainsAllInterveningVectors() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        List<PositionVector> horizontalPath = game.calculatePath(new PositionVector(1, 1), new PositionVector(4, 1));
        List<PositionVector> verticalPath = game.calculatePath(new PositionVector(2, 1), new PositionVector(2, 4));

        List<PositionVector> expectedHorizontalPath = List.of(
            new PositionVector(1, 1),
            new PositionVector(2, 1),
            new PositionVector(3, 1),
            new PositionVector(4, 1)
        );
        List<PositionVector> expectedVerticalPath = List.of(
            new PositionVector(2, 1),
            new PositionVector(2, 2),
            new PositionVector(2, 3),
            new PositionVector(2, 4)
        );
        assertEquals(expectedHorizontalPath, horizontalPath);
        assertEquals(expectedVerticalPath, verticalPath);
    }

    /**
     * Tests for doCarTurn() based on the equivalence classes 3 - 8.
     */
    @Test
    void testDoCarTurnDoesNotTreatTheOwnStartFieldAsCollision() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        game.doCarTurn(Direction.NONE);

        assertEquals(new PositionVector(1, 1), game.getCarPosition(0));
        assertEquals(GameSpecification.NO_WINNER, game.getWinner());
        assertEquals(String.join(System.lineSeparator(), "#####", "#a  #", "#####"), game.toString());
    }

    @Test
    void testDoCarTurnCrashesIntoOccupiedTrackFieldAndAwardsLastRemainingCar() throws Exception {
        Game game = createGame(
            "#######",
            "#a b  #",
            "#######"
        );

        game.doCarTurn(Direction.RIGHT);
        game.doCarTurn(Direction.NONE);

        assertEquals(new PositionVector(3, 1), game.getCarPosition(0));
        assertEquals(new PositionVector(3, 1), game.getCarPosition(1));
        assertEquals(1, game.getWinner());
        assertEquals(String.join(System.lineSeparator(), "#######", "#  b  #", "#######"), game.toString());
    }

    @Test
    void testDoCarTurnAwardsWinnerWhenCrossingFinishLineCorrectly() throws Exception {
        Game game = createGame(
            "#####",
            "#a> #",
            "#####"
        );

        game.doCarTurn(Direction.RIGHT);

        assertEquals(0, game.getWinner());
        assertEquals(new PositionVector(2, 1), game.getCarPosition(0));
    }

    @Test
    void testDoCarTurnDoesNotAwardWinnerWhenCrossingFinishLineWrongDirection() throws Exception {
        Game game = createGame(
            "######",
            "# >a #",
            "######"
        );

        game.doCarTurn(Direction.LEFT);

        assertEquals(-1, game.getWinner());
        assertEquals(new PositionVector(3, 1), game.getCarPosition(0));
    }

    @Test
    void testDoCarTurnReturnsImmediatelyWhenWinnerAlreadyExists() throws Exception {
        Game game = createGame(
            "######",
            "#a>b #",
            "######"
        );

        game.doCarTurn(Direction.RIGHT);
        PositionVector positionAfterWinningTurn = game.getCarPosition(0);
        PositionVector velocityAfterWinningTurn = game.getCarVelocity(0);

        game.doCarTurn(Direction.LEFT);

        assertEquals(0, game.getWinner());
        // Position and velocity should remain unchanged since a has won the game already.
        assertEquals(positionAfterWinningTurn, game.getCarPosition(0));
        assertEquals(velocityAfterWinningTurn, game.getCarVelocity(0));
    }

    @Test
    void testDoCarTurnReturnsImmediatelyWhenCurrentCarAlreadyCrashed() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        game.doCarTurn(Direction.LEFT);
        PositionVector crashedPosition = game.getCarPosition(0);
        PositionVector velocityAfterCrash = game.getCarVelocity(0);

        game.doCarTurn(Direction.RIGHT);

        // Position and velocity should remain unchanged since the car has already crashed.
        assertEquals(crashedPosition, game.getCarPosition(0));
        assertEquals(velocityAfterCrash, game.getCarVelocity(0));
    }

    /**
     * Tests for switchToNextActiveCar() based on the equivalence classes 9 and 10.
     * @throws Exception
     */
    @Test
    void testSwitchToNextActiveCarSkipsCrashedCarsAndWrapsAround() throws Exception {
        Game game = createGame(
            "#######",
            "#a b  #",
            "#######"
        );

        game.switchToNextActiveCar();
        assertEquals(1, game.getCurrentCarIndex());

        game.doCarTurn(Direction.UP); // b crashes into wall
        game.switchToNextActiveCar();
        game.doCarTurn(Direction.RIGHT);
        assertEquals(0, game.getCurrentCarIndex());
        game.switchToNextActiveCar(); // turn for b is skipped
        assertEquals(0, game.getCurrentCarIndex());
    }

    @Test
    void testSwitchToNextActiveCarDoesNothingWhenAllCarsAreCrashed() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        game.doCarTurn(Direction.LEFT);
        game.switchToNextActiveCar();

        assertEquals(0, game.getCurrentCarIndex());
    }

    /**
     * Tests for nextCarMove() based on the equivalence classes 11 and 12.
     */
    @Test
    void testNextCarMoveReturnsEmptyIfNoStrategyIsSet() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        assertTrue(game.nextCarMove(0).isEmpty());
    }

    @Test
    void nextCarMoveReturnsMoveProvidedByStrategy() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );
        game.setCarMoveStrategy(0, () -> Optional.of(Direction.DOWN_RIGHT));

        assertEquals(Optional.of(Direction.DOWN_RIGHT), game.nextCarMove(0));
    }

    /**
     * Helper method to create a Game instance for testing
     * @param lines the track file content as lines of text
     * @return a new Game instance using given parameter as track file content 
     * @throws IOException
     * @throws InvalidFileFormatException
     */
    private Game createGame(String... lines) throws IOException, InvalidFileFormatException {
        Path trackFile = Files.createTempFile(tempDir, "track", ".txt");
        Files.writeString(trackFile, String.join(System.lineSeparator(), lines));
        return new Game(new Track(trackFile.toFile()));
    }
}
