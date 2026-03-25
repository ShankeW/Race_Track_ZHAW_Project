package ch.zhaw.it.pm2.racetrack.game;

import ch.zhaw.it.pm2.racetrack.given.GameSpecification;
import ch.zhaw.it.pm2.racetrack.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to switch and move the cars, detect if they are crashed
 * and if we have a winner.
 * It also acts as a facade to track and car information, to get game state information.
 */
public class Game implements GameSpecification {

    private final Track track;
    private int currentCarIndex;
    private int winner = NO_WINNER;

    /**
     * Constructor for the Game class.
     * @param track the track to be used for this game
     */
    public Game(final Track track) {
        this.track = track;
    }

    /**
     * Return the number of cars on the track.
     * @return the number of cars
     */
    @Override
    public int getCarCount() {
        return track.getCarCount();
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     * @return the zero-based number of the current car
     */
    @Override
    public int getCurrentCarIndex() {
        return currentCarIndex;
    }

    /**
     * Get the id of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return a char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return track.getCar(carIndex).getId();
    }

    /**
     * Get the position of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return a PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPosition(int carIndex) {
        return track.getCar(carIndex).getPosition();
    }

    /**
     * Get the velocity of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return a PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return track.getCar(carIndex).getVelocity();
    }

    /**
     * Returns a string representation of the Track
     * @return a string representation of the Track
     */
    @Override
    public String toString(){
        return track.toString();
    }

    /**
     * Set the {@link MoveStrategy} for the specified car.
     * @param carIndex The zero-based carIndex number
     * @param moveStrategy the {@link MoveStrategy} to be associated with the specified car
     */
    @Override
    public void setCarMoveStrategy(int carIndex, MoveStrategy moveStrategy) {
        Car car = track.getCar(carIndex);
        car.setMoveStrategy(moveStrategy);
    }

    /**
     * Get the next move for the specified car, depending on its {@link MoveStrategy}.
     * @param carIndex The zero-based carIndex number
     * @return the {@link Optional<Direction>} containing the next move for the specified car,
     * or {@link Optional#empty()} if no move is selected (terminate game)
     */
    @Override
    public Optional<Direction> nextCarMove(int carIndex) {
        Car car = track.getCar(carIndex);
        return car.getMove();
    }

    /**
     * Return the carIndex of the winner.<br/>
     * If the game is still in progress, returns {@link #NO_WINNER}.
     * @return the winning car's index (zero-based, see {@link #getCurrentCarIndex()}),
     * or {@link #NO_WINNER} if the game is still in progress
     */
    @Override
    public int getWinner() {
        return winner;
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link #calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link #NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration a Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     */
    @Override
    public void doCarTurn(Direction acceleration) {
        Car currentCar = track.getCar(currentCarIndex);
        PositionVector startPosition = currentCar.getPosition();
        if (winner != NO_WINNER || currentCar.isCrashed()){
            return;
        }
        currentCar.accelerate(acceleration);
        List<PositionVector> carPath = calculatePath(startPosition, currentCar.nextPosition());
        PositionVector previousStep = startPosition;
        for (PositionVector step : carPath){
            switch (track.getSpaceTypeAtPosition(step)){
                case WALL -> {
                    crashCurrentCarAndResolveWinner(currentCar, step);
                    return;
                }
                case TRACK -> {
                    if (!step.equals(startPosition) && isOccupiedByAnotherCar(step, currentCar)) {
                        crashCurrentCarAndResolveWinner(currentCar, step);
                        return;
                    }
                }
                case FINISH_RIGHT,FINISH_LEFT,FINISH_DOWN,FINISH_UP -> {
                    if (crossedFinishCorrectly(startPosition,step,track.getSpaceTypeAtPosition(step))){
                        winner = currentCarIndex;
                        currentCar.updatePosition(step);
                        return;
                    } else {
                        currentCar.updatePosition(previousStep);
                        return;
                    }
                }
            }
            previousStep = step;
        }
        currentCar.move();
    }

    private void crashCurrentCarAndResolveWinner(Car currentCar, PositionVector crashPosition) {
        currentCar.crash(crashPosition);
        if (crashedCarCount() != (track.getCarCount() - 1)) {
            return;
        }

        for (int i = 0; i < track.getCarCount(); i++) {
            if (!track.getCar(i).isCrashed()){
                winner = i;
                return;
            }
        }

        // Case if all cars are crashed.
        // Use -2 to distinguish from NO_WINNER, so that the main loop can be terminated.
        if (crashedCarCount() >= track.getCarCount()) winner = -2;
    }

    private boolean isOccupiedByAnotherCar(PositionVector position, Car currentCar) {
        for (int i = 0; i < track.getCarCount(); i++) {
            Car otherCar = track.getCar(i);
            if (otherCar == currentCar) {
                continue;
            }
            if (position.equals(otherCar.getPosition())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if, at the given Positions, the Car would pass the finish in the correct direction
     *
     * @param startPosition Holds the start Position of the Car.
     * @param endPosition Holds the end Position of the finish line space.
     * @param finishDirection Spacetype that holds a Finishdirection, giving a non-Finishdirection spacetype will result in false
     * @return true if the given positions are in the correct orientation for the given finishDirection.
     * Returns false if it's not correct, or a proper spacetype hasn't been given.
     */
    private boolean crossedFinishCorrectly(PositionVector startPosition, PositionVector endPosition, SpaceType finishDirection){
        //X goes from left to right.
        //Y goes from up to down.
        switch (finishDirection){
            case FINISH_UP -> {
                return startPosition.getY() < endPosition.getY();
            }
            case FINISH_DOWN -> {
                return startPosition.getY() > endPosition.getY();
            }
            case FINISH_RIGHT -> {
                return startPosition.getX() < endPosition.getX();
            }
            case FINISH_LEFT -> {
                return startPosition.getX() > endPosition.getX();
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    @Override
    public void switchToNextActiveCar() {
        if (crashedCarCount() == track.getCarCount()){
            return;
        }

        do{
            currentCarIndex++;
            if(currentCarIndex == track.getCarCount()){
                currentCarIndex = 0;
            }
        } while(track.getCar(currentCarIndex).isCrashed());
    }

    /**
     * Returns the amount of crashed cars
     * @return the amount of crashed cars
     */
    public int crashedCarCount() {
        int crashedCount = 0;
        for (int i = 0; i < track.getCarCount(); i++) {
            if (track.getCar(i).isCrashed()){
                crashedCount++;
            }
        }
        return crashedCount;
    }

    /**
     * Returns all the grid positions in the path between two positions, for use in determining line of sight. <br>
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (<a href="https://de.wikipedia.org/wiki/Bresenham-Algorithmus">Bresenham-Algorithmus</a>)<br>
     * Basic steps are <ul>
     *   <li>Detect which axis of the distance vector is longer (faster movement)</li>
     *   <li>for each pixel on the 'faster' axis calculate the position on the 'slower' axis.</li>
     * </ul>
     * Direction of the movement has to correctly considered.
     *
     * @param startPosition Starting position as a PositionVector
     * @param endPosition Ending position as a PositionVector
     * @return intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    @Override
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        List<PositionVector> path = new ArrayList<>();

        // Use Bresenham's algorithm to determine positions.
        // Relative Distance (x & y-axis) between end- and starting position
        int diffX = endPosition.getX() - startPosition.getX();
        int diffY = endPosition.getY() - startPosition.getY();
        // Absolute distance (x & y-axis) between end- and starting position
        int distX = Math.abs(diffX);
        int distY = Math.abs(diffY);
        // Direction of vector on x & y-axis (-1: to left/down, 0: none, +1 : to right/up)
        int dirX = Integer.signum(diffX);
        int dirY = Integer.signum(diffY);
        // Determine which axis is the fast direction and set parallel/diagonal step values
        int parallelStepX, parallelStepY;
        int diagonalStepX, diagonalStepY;
        int distanceSlowAxis, distanceFastAxis;
        if (distX > distY) {
            // x-axis is the 'fast' direction
            parallelStepX = dirX; parallelStepY = 0; // parallel step only moves in x direction
            diagonalStepX = dirX; diagonalStepY = dirY; // diagonal step moves in both directions
            distanceSlowAxis = distY;
            distanceFastAxis = distX;
        } else {
            // y-axis is the 'fast' direction
            parallelStepX = 0; parallelStepY = dirY; // parallel step only moves in y direction
            diagonalStepX = dirX; diagonalStepY = dirY; // diagonal step moves in both directions
            distanceSlowAxis = distX;
            distanceFastAxis = distY;
        }
        // initialize path loop
        int x = startPosition.getX();
        int y = startPosition.getY();
        int error = distanceFastAxis/2; // set to half distance to get a good starting value
        // add to the list
        path.add(new PositionVector(x,y));
        // path loop:
        // by default step parallel to the fast axis.
        // if error value gets negative take a diagonal step
        // this happens approximately every (distanceFastAxis / distanceSlowAxis) steps
        for (int step = 0; step < distanceFastAxis; step++) {
            error -= distanceSlowAxis; // update error value
            if (error < 0) {
                error += distanceFastAxis; // correct error value to be positive again
                // step into slow direction; diagonal step
                x += diagonalStepX;
                y += diagonalStepY;
            } else {
                // step into fast direction; parallel step
                x += parallelStepX;
                y += parallelStepY;
            }
            // Print position (add to the list)
            path.add(new PositionVector(x,y));
        }
        return path;
    }
}
