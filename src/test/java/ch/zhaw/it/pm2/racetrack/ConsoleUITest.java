package ch.zhaw.it.pm2.racetrack;

import ch.zhaw.it.pm2.racetrack.UI.ConsoleUI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ConsoleUITest {
    @Test
    void testReturnsCorrectIndex() {
        ConsoleUI ui = new ConsoleUI();
        ArrayList<String> options = new ArrayList<>();
        options.add("Option A");
        options.add("Option B");
        int result = ui.getUserInput(options);
        assertEquals(0, result);
    }
}
