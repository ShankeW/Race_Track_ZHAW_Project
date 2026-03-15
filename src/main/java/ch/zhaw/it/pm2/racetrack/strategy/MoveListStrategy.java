package ch.zhaw.it.pm2.racetrack.strategy;

import ch.zhaw.it.pm2.racetrack.game.Direction;

import java.util.Optional;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {
    List<String> moveList;

    /**
     * Instantiate the MoveListStrategy by reading the txt file containing all predefined moves.
     */
    public MoveListStrategy(){
        try{
            Path path = Paths.get("move_list.txt");
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
        if (!moveList.isEmpty()){
            Direction currDirection = Direction.valueOf(moveList.get(0).toUpperCase());
            moveList.remove(0);
            return Optional.of(currDirection);
        } else {
            return Optional.empty();
        }
    }
}
