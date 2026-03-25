package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {
    private final List<String> moveList;

    /**
     * Instantiates the MoveListStrategy by reading the selected text file containing predefined moves.
     * The user chooses the file through the provided user interface.
     *
     * @param ui UI that requests a move-list file from the user
     * @param moveFiles list of available move-list files
     */
    public MoveListStrategy(UserInterface ui, File[] moveFiles) {
        Objects.requireNonNull(ui, "ui must not be null");
        Objects.requireNonNull(moveFiles, "moveFiles must not be null");
        if (moveFiles.length == 0) {
            throw new IllegalArgumentException("moveFiles must not be empty");
        }

        ArrayList<String> moveFileNames = new ArrayList<>();
        for (File currMoveFile : moveFiles) {
            moveFileNames.add(currMoveFile.getName());
        }
        int userInput = ui.getUserInput(moveFileNames);
        if (userInput < 0 || userInput >= moveFiles.length) {
            throw new IllegalArgumentException("Invalid move file selection.");
        }

        try {
            Path path = Paths.get(moveFiles[userInput].getPath());
            moveList = Files.readAllLines(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read move list file: " + moveFiles[userInput].getPath(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from the move file, or {@link Optional#empty()} if no more moves are available
     */
    @Override
    public Optional<Direction> nextMove() {
        if (moveList.isEmpty()) {
            return Optional.empty();
        }

        String directionName = moveList.remove(0).strip();
        try {
            Direction currDirection = Direction.valueOf(directionName.toUpperCase(Locale.ROOT));
            return Optional.of(currDirection);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction in move file: " + directionName, e);
        }
    }
}
