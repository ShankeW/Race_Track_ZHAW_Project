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

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @TempDir
    Path tempDir;

    @Test
    void calculatePathIncludesStartAndEndPosition() throws Exception {
        Game game = createGame(
            "#####",
            "#a  #",
            "#####"
        );

        List<PositionVector> path = game.calculatePath(new PositionVector(3, 3), new PositionVector(5, 5));

        assertEquals(3, path.size());
        assertEquals(new PositionVector(3, 3), path.getFirst());
        assertEquals(new PositionVector(4, 4), path.get(1));
        assertEquals(new PositionVector(5, 5), path.getLast());
    }

    @Test
    void doCarTurnDoesNotTreatTheOwnStartFieldAsCollision() throws Exception {
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
    void doCarTurnCrashesIntoOccupiedTrackFieldAndAwardsLastRemainingCar() throws Exception {
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

    private Game createGame(String... lines) throws IOException, InvalidFileFormatException {
        Path trackFile = Files.createTempFile(tempDir, "track", ".txt");
        Files.writeString(trackFile, String.join(System.lineSeparator(), lines));
        return new Game(new Track(trackFile.toFile()));
    }
}
