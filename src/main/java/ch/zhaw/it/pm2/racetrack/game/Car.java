package ch.zhaw.it.pm2.racetrack.game;

import ch.zhaw.it.pm2.racetrack.given.CarSpecification;
import ch.zhaw.it.pm2.racetrack.strategy.MoveStrategy;

import java.util.Optional;

/**
 * Class representing a car on the racetrack.<br/>
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.<br/>
 * Each car has an identifier character which represents the car on the racetrack board.<br/>
 * Also keeps the state, if the car is crashed (not active anymore).
 * The state cannot be changed back to uncrashed.<br/>
 * The velocity is changed by providing an acceleration vector.<br/>
 * The car is able to calculate the endpoint of its next position and on request moves to it.<br/>
 */
public class Car implements CarSpecification {

    /**
     * Car identifier used to represent the car on the track.
     */
    private final char id;
    private PositionVector position;
    private PositionVector velocity;
    private boolean crashed = false;
    private MoveStrategy moveStrategy;

    /**
     * Constructor for class Car.
     *
     * @param id            unique Car identification
     * @param startPosition initial position of the Car
     */
    public Car(char id, PositionVector startPosition) {
        if (startPosition == null) {
            throw new IllegalArgumentException("startPosition must not be null");
        }
        this.id = id;
        this.position = startPosition;
        this.velocity = new PositionVector(0, 0);
    }

    /**
     * Returns Identifier of the car, which represents the car on the track.
     *
     * @return the identifier character
     */
    @Override
    public char getId() {
        return this.id;
    }

    /**
     * Returns the current immutable position of the car on the track as a {@link PositionVector}.
     *
     * @return the car's current position
     */
    @Override
    public PositionVector getPosition() {
        return new PositionVector(this.position);
    }

    /**
     * Returns the current immutable velocity vector of the car as a {@link PositionVector}.
     *
     * @return the car's current velocity vector
     */
    @Override
    public PositionVector getVelocity() {
        return new PositionVector(this.velocity);
    }

    /**
     * Return the position that will apply after the next move at the current velocity.
     * Does not complete the move, so the current position remains unchanged.
     *
     * @return expected position after the next move, or current position if crashed
     */
    @Override
    public PositionVector nextPosition() {
        if (crashed) {
            return new PositionVector(this.position);
        }
        return position.add(velocity);
    }

    /**
     * Add the specified amounts to this car's velocity.<br/>
     * The only acceleration values allowed are -1, 0 or 1 in both axis<br/>
     * There are 9 possible acceleration vectors, which are defined in {@link Direction}.<br/>
     * Changes only velocity, not position.<br/>
     *
     * @param acceleration a Direction vector containing the amounts to add to the velocity in x and y dimension
     */
    @Override
    public void accelerate(Direction acceleration) {
        if (acceleration == null) {
            throw new IllegalArgumentException("acceleration must not be null");
        }
        if (crashed) return;
        velocity = velocity.add(acceleration.getVector());
    }

    /**
     * Update this Car's position based on its current velocity.
     */
    @Override
    public void move() {
        if (crashed) {
            return;
        }
        position = nextPosition();
    }

    /**
     * Update this Car's position directly to the Given Position
     * Used when crashing into a Wall or crossing the finish line the wrong way.
     * @param position The Position the Car will move to.
     */
    public void updatePosition(PositionVector position){
        if (position == null) {
            throw new IllegalArgumentException("Position must not be null");
        }
        if (crashed) {
            return;
        }
        this.position = position;
    }

    /**
     * Mark this Car as being crashed at the given position.
     *
     * @param crashPosition position the car crashed.
     */
    @Override
    public void crash(PositionVector crashPosition) {
        if (crashPosition == null) {
            throw new IllegalArgumentException("crashPosition must not be null");
        }
        if (crashed) return;

        position = crashPosition;
        crashed = true;
    }

    /**
     * Returns whether this Car has been marked as crashed.
     *
     * @return true if crash() has been called on this Car, false otherwise.
     */
    @Override
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * Sets this Cars Move strategy
     *
     * @param moveStrategy Chosen move strategy
     */
    public void setMoveStrategy(MoveStrategy moveStrategy){
        if (moveStrategy == null) {
            throw new IllegalArgumentException("MoveStrategy must not be null");
        }
        this.moveStrategy = moveStrategy;
    }
    /**
     * Gets the Next Movement Direction for this Car.
     *
     * @return Next Move from move Strategy, if move strategy is null, return an empty Direction
     */
    public Optional<Direction> getMove(){
        if (moveStrategy == null){
            return Optional.empty();
        }
        return moveStrategy.nextMove();
    }
}
