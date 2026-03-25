package ch.zhaw.it.pm2.racetrack;

import ch.zhaw.it.pm2.racetrack.UI.ConsoleUI;
import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Direction;
import ch.zhaw.it.pm2.racetrack.game.Game;
import ch.zhaw.it.pm2.racetrack.game.Track;
import ch.zhaw.it.pm2.racetrack.given.GameSpecification;
import ch.zhaw.it.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.UserMoveStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Main class that runs and controls the gameplay loop.
 */
public class RaceTrack {
    private final UserInterface ui;
    private final Config config;
    private Game game;
    private boolean gameActive = true;

    /**
     * Runs the Racetrack Game.
     * Requires Picking a Track and Picking the move strategies for the cars present on the track.
     * @param args Arguments
     */
    public static void main(String[] args) {
        RaceTrack racetrack = new RaceTrack();
        racetrack.game = new Game(racetrack.selectTrack());
        for (int i = 0; i < racetrack.game.getCarCount(); i++) {
            racetrack.game.setCarMoveStrategy(i, racetrack.selectStrategyForCar(racetrack.game.getCarId(i)));
        }
        while (racetrack.gameActive) {
            racetrack.processTurn();
        }
        racetrack.ui.displayMessage(racetrack.game.toString());
        if (racetrack.game.getWinner() == GameSpecification.NO_WINNER) {
            racetrack.ui.waitForConfirmation("Game ended without a winner.");
        } else {
            racetrack.ui.waitForConfirmation("Winner is: " + racetrack.game.getCarId(racetrack.game.getWinner()));
        }

        racetrack.quitGame();
    }

    /**
     * Creates a Racetrack Object
     * Creates a UI, defaults to ConsoleUI
     * Creates a Config file with Paths to the required directories
     */
    public RaceTrack() {
        this.ui = new ConsoleUI();
        this.config = new Config();
    }

    /**
     * Selects a Track from the Track Directory using the chosen UI.
     * @return the track selected by the user
     */
    public Track selectTrack() {
        File[] tracks = listFilesOrThrow(config.getTrackDirectory(), "track files");
        ArrayList<String> trackNames = new ArrayList<>();
        for (File trackFile : tracks) {
            trackNames.add(trackFile.getName());
        }
        try {
            return new Track(tracks[ui.getUserInput(trackNames)]);
        } catch (IOException | InvalidFileFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and delivers the actual MoveStrategy object for a car. DO_NOT_MOVE strategy object
     * is created and returned by default.
     * @param strategyType the desired MoveStrategy Enum Type.
     * @return the actual MoveStrategy object needed.
     */
    public MoveStrategy selectMoveStrategy(MoveStrategy.StrategyType strategyType) {
        Objects.requireNonNull(strategyType, "strategyType must not be null");
        switch (strategyType) {
            case MoveStrategy.StrategyType.USER -> {
                return new UserMoveStrategy(ui);
            }
            case MoveStrategy.StrategyType.MOVE_LIST -> {
                return new MoveListStrategy(ui, listFilesOrThrow(config.getMoveDirectory(), "move list files"));
            }
            default -> {
                return new DoNotMoveStrategy();
            }
        }
    }

    /**
     * Selects a Strategy from the List of StrategyType Enums in MoveStrategy
     * If the Chosen MoveStrategy does not exist, defaults to Do Not Move Strategy
     * @param carID the Char ID for the given Car
     * @return the move strategy chosen by the user
     */
    public MoveStrategy selectStrategyForCar(char carID) {
        MoveStrategy.StrategyType[] strategyTypes = MoveStrategy.StrategyType.values();
        ArrayList<String> typeNames = new ArrayList<>();
        for (MoveStrategy.StrategyType type : strategyTypes) {
            typeNames.add(type.toString());
        }
        // Exclude optional move strategies that are not implemented in this project.
        typeNames.removeLast();
        typeNames.removeLast();

        String message = "Select Move Strategy for: " + carID;
        MoveStrategy.StrategyType chosenType = strategyTypes[ui.getUserInput(typeNames, message, "Move Strategies:")];
        return selectMoveStrategy(chosenType);
    }

    /**
     * Runs a Single turn of the game
     * Steps Taken:
     *      Display Current Track State.
     *      Get the Next Move of the Current Car.
     *      Do the Cars turn with the parsed direction.
     *      Check if there is a Winner after the Turn
     */
    public void processTurn() {
        ui.displayMessage(game.toString());
        ui.displayMessage("Current Car: " + game.getCarId(game.getCurrentCarIndex()));
        Optional<Direction> direction = game.nextCarMove(game.getCurrentCarIndex());

        if (direction.isEmpty()) {
            gameActive = false;
            return;
        }

        Direction parsedDirection = direction.get();
        game.doCarTurn(parsedDirection);
        game.switchToNextActiveCar();
        if (game.getWinner() != GameSpecification.NO_WINNER
            || game.crashedCarCount() == game.getCarCount()) {
            gameActive = false;
        }
    }
    /**
     * Shuts down the game and UI
     */
    public void quitGame() {
        ui.disposeUserInterface();
    }

    private File[] listFilesOrThrow(File directory, String description) {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalStateException("No %s available in %s".formatted(description, directory.getAbsolutePath()));
        }
        return files;
    }
}
