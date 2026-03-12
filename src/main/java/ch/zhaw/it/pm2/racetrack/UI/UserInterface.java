package ch.zhaw.it.pm2.racetrack.UI;

import java.util.ArrayList;

public interface UserInterface {
    int getUserInput(ArrayList<String> options);
    int getUserInput(ArrayList<String> options, String message);
    int getUserInput(ArrayList<String> options, String message, String optionMessage);
    void displayMessage(String message);
    void waitForConfirmation(String message);
    void disposeUserInterface();
}
