package ch.zhaw.it.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameTest {

    Config config = new Config();
    Game game;

    @BeforeEach
    void setup() throws InvalidFileFormatException, IOException {
        File[] tracks = config.getTrackDirectory().listFiles();
        Assertions.assertNotNull(tracks);
        Game game = new Game(new Track(tracks[0]));
    }

    @Test
    void testCalculatePath() {
        PositionVector start = new PositionVector(3,3);
        PositionVector end = new PositionVector(5,5);

        List<PositionVector> path = game.calculatePath(start,end);
        Assertions.assertEquals(3, path.size());
        Assertions.assertEquals(new PositionVector(4,4), path.get(1));
    }
}
