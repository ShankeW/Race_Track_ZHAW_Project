package ch.zhaw.it.pm2.racetrack.UI;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

/**
 * UI which uses a console display to show outputs to and get inputs from the user.
 */
public class ConsoleUI implements UserInterface {

    private final TextIO textIO;
    private final TextTerminal<?> terminal;

    /**
     * Creates a new ConsoleUI object.
     */
    public ConsoleUI() {
        this.textIO = TextIoFactory.getTextIO();
        this.terminal = textIO.getTextTerminal();
    }

    @Override
    public int getUserInput(ArrayList<String> options) {
        return getUserInput(options, "Select one of the following options:");
    }

    @Override
    public int getUserInput(ArrayList<String> options, String message) {
        return getUserInput(options, message, "Selection: ");
    }

    /**
     * Returns the index of the chosen selection for the given list of options.
     *
     * @param options The options that will be displayed to the user for selection
     * @param message A message that will be displayed to the user before selection.
     *                Will display "Select one of the following options:" by default.
     * @param selectionMessage An additional prompt that will be displayed.
     *                Will display "Selection: " by default.
     */
    @Override
    public int getUserInput(ArrayList<String> options, String message, String selectionMessage) {
        terminal.println(message);
        String selection = textIO.newStringInputReader()
            .withNumberedPossibleValues(options)
            .read(selectionMessage);
        return options.indexOf(selection);
    }

    /**
     * Prints the given message to the screen.
     *
     * @param message The message stored as a String
     */
    @Override
    public void displayMessage(String message) {
        terminal.println(message);
    }

    /**
     * Prints the given message to the screen, then waits for a confirmation input from the user.
     *
     * @param message The message stored as a String
     */
    @Override
    public void waitForConfirmation(String message) {
        terminal.println(message);
        textIO.newStringInputReader().read("Press Enter to exit");
    }

    @Override
    public void disposeUserInterface() {
        textIO.dispose();
    }
}
