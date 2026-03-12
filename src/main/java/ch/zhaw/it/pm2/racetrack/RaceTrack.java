package ch.zhaw.it.pm2.racetrack;

import ch.zhaw.it.pm2.racetrack.UI.ConsoleUI;
import ch.zhaw.it.pm2.racetrack.UI.UserInterface;
import ch.zhaw.it.pm2.racetrack.game.Game;
import ch.zhaw.it.pm2.racetrack.game.Track;
import ch.zhaw.it.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.it.pm2.racetrack.strategy.UserMoveStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RaceTrack {
    UserInterface UI;
    Config config;
    Game game;
    boolean gameActive = true;

    /**
     * Runs the Racetrack Game.
     * Requires Picking a Track and Picking the move strategies for the cars present on the track.
     */
    public static void main(String[] args) throws InterruptedException {
        RaceTrack racetrack = new RaceTrack();
        racetrack.game = new Game(racetrack.selectTrack());
        for (int i = 0; i < racetrack.game.getCarCount(); i++) {
            racetrack.game.setCarMoveStrategy(i, racetrack.selectStrategyForCar(racetrack.game.getCarId(i)));
        }
        /**
        while (racetrack.gameActive){
            racetrack.processTurn();
        }
         */
        racetrack.processTurn();
    }

    /**
     * Creates a Racetrack Object
     * Creates a UI, defaults to ConsoleUI
     * Creates a Config file with Paths to the required directories
     */
    public RaceTrack(){
        this.UI = new ConsoleUI();
        this.config = new Config();
    }

    /**
     * Selects a Track from the Track Directory using the chosen UI
     */
    public Track selectTrack(){
        File[] tracks = config.getTrackDirectory().listFiles();
        ArrayList<String> trackNames = new ArrayList<>();
        assert tracks != null;
        for (File trackFile : tracks){
            trackNames.add(trackFile.getName());
        }
        try {
            return new Track(tracks[UI.getUserInput(trackNames)]);
        } catch (IOException | InvalidFileFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Selects a Strategy from the List of StrategyType Enums in MoveStrategy
     * If the Chosen MoveStrategy does not exist, defaults to Do Not Move Strategy
     */
    public MoveStrategy selectStrategyForCar(char carID){
        MoveStrategy.StrategyType[] strategyTypes= MoveStrategy.StrategyType.values();
        ArrayList<String> typeNames = new ArrayList<>();
        for (MoveStrategy.StrategyType type : strategyTypes){
            typeNames.add(type.toString());
        }
        String message = "Select Move Strategy for: " + carID;
        MoveStrategy.StrategyType chosenType = strategyTypes[UI.getUserInput(typeNames,message,"Move Strategies:")];

        //Potential Implicit Coupling?
        return switch (chosenType) {
            case MoveStrategy.StrategyType.USER -> new UserMoveStrategy(UI);
            case MoveStrategy.StrategyType.MOVE_LIST -> new MoveListStrategy();
            default -> new DoNotMoveStrategy();
        };
    }

    /**
     * Runs a Single turn of the game
     */
    public void processTurn(){
        UI.displayMessage(game.toString());
    }

    public void quitGame(){
        UI.disposeUserInterface();
    }
}
