package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {
    List<String> moveList = List.of("");

    /**
     * Instantiate the MoveListStrategy by reading the txt file containing all predefined moves.
     * User can choose via userinterface UI which move file to use.
     */
    public MoveListStrategy(UserInterface ui, File[] moveFiles){
        ArrayList<String> moveFileNames = new ArrayList<>();
        for (File currMoveFile : moveFiles){
            moveFileNames.add(currMoveFile.getName());
        }
        int userInput = ui.getUserInput(moveFileNames);

        try{
            Path path = Paths.get(moveFiles[userInput].getPath());
            moveList = Files.readAllLines(path);
        } catch (IOException e){
            System.out.println("File not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from move file or {@link Direction#NONE}, if no more moves are available.
     */
    @Override
    public Optional<Direction> nextMove() {
        // TODO: implementation
        if (!Objects.equals(moveList.getFirst(), "")){
            Direction currDirection = Direction.valueOf(moveList.getFirst().toUpperCase());
            moveList.removeFirst();
            return Optional.of(currDirection);
        } else {
            return Optional.empty();
        }
    }
}
