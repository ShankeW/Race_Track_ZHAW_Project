package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
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
    private UserInterface UI;

    /**
     * Instantiate the MoveListStrategy by reading the txt file containing all predefined moves.
     * User can choose via userinterface UI which move file to use.
     *
     * @param ui        UI that requests a chosen Movelist file from the user.
     * @param moveFiles The list of available Movelist files.
     */
    public MoveListStrategy(UserInterface ui, File[] moveFiles){
        this.UI = ui;
        ArrayList<String> moveFileNames = new ArrayList<>();
        for (File currMoveFile : moveFiles) {
            moveFileNames.add(currMoveFile.getName());
        }
        int userInput = UI.getUserInput(moveFileNames);
        if (userInput < 0 || userInput >= moveFiles.length) {
            throw new IllegalArgumentException("Invalid move file selection.");
        }

        if (userInput < 0 || userInput >= moveFiles.length) {
            throw new IllegalArgumentException("Selected move file index is out of bounds: " + userInput);
        }

        try{
            Path path = Paths.get(moveFiles[userInput].getPath());
            moveList = Files.readAllLines(path);
        } catch (IOException e){
            throw new UncheckedIOException("Could not read move list file: " + moveFiles[userInput].getPath(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from move file or {@link Direction#NONE}, if no more moves are available.
     */
    @Override
    public Optional<Direction> nextMove() {
        if (moveList.isEmpty()) {
            return Optional.empty();
        }
        try {
            Direction currDirection = Direction.valueOf(moveList.get(0).toUpperCase());
            moveList.remove(0);
            return Optional.of(currDirection);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction in move file.", e);
        }
    }
}
