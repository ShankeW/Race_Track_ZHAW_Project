package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests below are based on the following equivalence classes for MoveListStrategy:
 * 
 * Equivalence class for valid inputs:
 *  1: Selected move file exists and contains valid directions -> nextMove returns first direction.
 *  2: Selected move file exists and contains multiple valid directions -> order is preserved and consumed.
 *  3: Selected move file exists but is empty -> nextMove returns Optional.empty().
 * 
 * Equivalence class for invalid inputs:
 *  4: Move file contains an invalid direction object -> nextMove throws IllegalArgumentException.
 *  5: Selected move file cannot be read (I/O error) -> constructor throws UncheckedIOException.
 */
public class MoveListStrategyTest {
    @TempDir
    Path tempDir; // JUnit will create and clean up a temporary directory for test files

    @Test
    void challengeCarAtest() {
        File[] moveFiles = {
            new File("moves/challenge-car-a.txt"),
            new File("moves/challenge-car-b.txt")
        };
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(0), moveFiles);

        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.NONE, strategy.nextMove().orElseThrow());
    }

    @Test
    void challengeCarBtest() {
        File[] moveFiles = {
            new File("moves/challenge-car-a.txt"),
            new File("moves/challenge-car-b.txt")
        };
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(1), moveFiles);

        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.NONE, strategy.nextMove().orElseThrow());
    }

    @Test
    void ifNextMoveConsumesTheFirstDirectionTest() throws IOException{
        File moveFile = createMoveFile("DOWN_RIGHT");
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(0), new File[]{moveFile});

        assertEquals(Direction.DOWN_RIGHT, strategy.nextMove().orElseThrow());
        assertTrue(strategy.nextMove().isEmpty());
    }

    @Test
    void ifNextMoveConsumesDirectionsInOrderTest() throws IOException {
        File moveFile = createMoveFile("left", "up_right", "none");
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(0), new File[]{moveFile});

        assertEquals(Direction.LEFT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.UP_RIGHT, strategy.nextMove().orElseThrow());
        assertEquals(Direction.NONE, strategy.nextMove().orElseThrow());
        assertTrue(strategy.nextMove().isEmpty());
    }

    @Test
    void ifNextMoveReturnsEmptyForEmptyMoveFileTest() throws IOException {
        File moveFile = createMoveFile();
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(0), new File[]{moveFile});

        assertTrue(strategy.nextMove().isEmpty());
    }

    @Test
    void ifExceptionIsThrownForInvalidDirectionObjectTest() throws IOException {
        File moveFile = createMoveFile("invalid-direction");
        MoveListStrategy strategy = new MoveListStrategy(new FixedIndexUserInterface(0), new File[]{moveFile});

        assertThrows(IllegalArgumentException.class, strategy::nextMove);
    }

    @Test
    void ifExceptionIsThrownWhenMoveFileCouldNotBeReadTest() {
        File missingMoveFile = new File(tempDir.toFile(), "does-not-exist.txt");
        assertThrows(UncheckedIOException.class,
            () -> new MoveListStrategy(new FixedIndexUserInterface(0), new File[]{missingMoveFile}));
    }


    /**
     * Helper method that creates a move File for testing
     * @param lines the directions to write to the file
     * @return the temporary move file for testing
     * @throws IOException if the file cannot be created
     */
    private File createMoveFile(String... lines) throws IOException {
        Path moveFile = Files.createTempFile(tempDir, "moves", ".txt");
        Files.write(moveFile, java.util.List.of(lines));
        return moveFile.toFile();
    }

    /**
     * A helper class that implements UserInterface. It is used for choosing between the 
     * movelist files challenge-car-a and challenge-car-b.
     */
    private static final class FixedIndexUserInterface implements UserInterface {
        private final int index;

        private FixedIndexUserInterface(int index) {
            this.index = index;
        }

        @Override
        public int getUserInput(ArrayList<String> options) {
            return index;
        }

        @Override
        public int getUserInput(ArrayList<String> options, String message) {
            return index;
        }

        @Override
        public int getUserInput(ArrayList<String> options, String message, String optionMessage) {
            return index;
        }

        @Override
        public void displayMessage(String message) {
            // not needed in tests
        }

        @Override
        public void waitForConfirmation(String message) {
            // not needed in tests
        }

        @Override
        public void disposeUserInterface() {
            // not needed in tests
        }
    }
}
