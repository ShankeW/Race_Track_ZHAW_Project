package ch.zhaw.it.pm2.racetrack.UI;

import java.util.ArrayList;

/**
 * Interface for the User Interface. Displays information to the user and can get input from the user.
 */
public interface UserInterface {
    /**
     * Displays a list of options to the user. One of which is to be picked.
     * Without aditional parameters displays: "Select one of the following options:" and "Selection: " by default.
     * @param options The options to be displayed.
     * @return the index of the chosen option.
     */
    int getUserInput(ArrayList<String> options);

    /**
     * Displays a list of options to the user. One of which is to be picked.
     * Without aditional parameters displays: "Select one of the following options:" and "Selection: " by default.
     * @param options The options to be displayed.
     * @param message Message that will replace "Select one of the following options:"
     * @return the index of the chosen option.
     */
    int getUserInput(ArrayList<String> options, String message);

    /**
     * Displays a list of options to the user. One of which is to be picked.
     * Without aditional parameters displays: "Select one of the following options:" and "Selection: " by default.
     * @param options The options to be displayed.
     * @param message Message that will replace "Select one of the following options:"
     * @param optionMessage Message that will replace "Selection: "
     * @return the index of the chosen option.
     */
    int getUserInput(ArrayList<String> options, String message, String optionMessage);

    /**
     * Displays a basic message to the user.
     * @param message the message to be displayed.
     */
    void displayMessage(String message);

    /**
     * Displays a basic message to the user, message will display until interrupted by user input.
     * @param message the message to be displayed
     */
    void waitForConfirmation(String message);

    /**
     * Shuts down the User Interface
     */
    void disposeUserInterface();
}
