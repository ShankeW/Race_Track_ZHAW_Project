package ch.zhaw.it.pm2.racetrack.UI;
import org.beryx.textio.*;

import java.util.ArrayList;

public class ConsoleUI implements UserInterface{

    TextIO textIO = TextIoFactory.getTextIO();
    TextTerminal<?> terminal = textIO.getTextTerminal();

    @Override
    public int getUserInput(ArrayList<String> options) {
        return getUserInput(options,"Select one of the following options:");
    }

    @Override
    public int getUserInput(ArrayList<String> options, String message) {
        return getUserInput(options,message, "Selection: ");
    }

    /**
     * Returns the index for the chosen selection for the given String Arraylist.
     * @param options The Options that will be displayed to the user for selection
     * @param message A message the will be displayed to the user before selection.
     *                Will display "Select one of the following options:" by default.
     * @param selectionMessage An extra message that will display.
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
     * Prints the given trackString onto the Screen
     * @param trackString The Track stored as a String
     */
    @Override
    public void displayTrack(String trackString) {
        //Needs check for proper display
        terminal.print(trackString);
    }

    @Override
    public void disposeUserInterface() {
        textIO.dispose();
    }
}
