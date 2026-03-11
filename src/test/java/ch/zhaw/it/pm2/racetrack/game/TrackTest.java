package ch.zhaw.it.pm2.racetrack.game;

import ch.zhaw.it.pm2.racetrack.InvalidFileFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrackTest {

    @TempDir
    Path tempDir;

    @Test
    void constructorParsesTrackBlockAndExposesTrackMetadata() throws Exception {
        Track track = createTrack(
            "",
            "",
            "####",
            "#a>#",
            "####",
            "",
            "ignored"
        );

        assertEquals(3, track.getHeight());
        assertEquals(4, track.getWidth());
        assertEquals(1, track.getCarCount());
        assertEquals('a', track.getCar(0).getId());
        assertEquals(SpaceType.TRACK, track.getSpaceTypeAtPosition(new PositionVector(1, 1)));
        assertEquals(SpaceType.FINISH_RIGHT, track.getSpaceTypeAtPosition(new PositionVector(2, 1)));
    }

    @Test
    void constructorRejectsTracksWithInconsistentRowLength() {
        assertThrows(InvalidFileFormatException.class, () -> createTrack(
            "#####",
            "#a #",
            "#####"
        ));
    }

    @Test
    void constructorRejectsTracksWithoutCars() {
        assertThrows(InvalidFileFormatException.class, () -> createTrack(
            "#####",
            "# > #",
            "#####"
        ));
    }

    @Test
    void constructorRejectsDuplicateCarIds() {
        assertThrows(InvalidFileFormatException.class, () -> createTrack(
            "####",
            "#aa#",
            "####"
        ));
    }

    @Test
    void constructorRejectsMoreCarsThanAllowed() {
        assertThrows(InvalidFileFormatException.class, () -> createTrack(
            "############",
            "#abcdefghij#",
            "############"
        ));
    }

    @Test
    void getSpaceTypeAtPositionReturnsWallOutsideGrid() throws Exception {
        Track track = createTrack(
            "#####",
            "#a >#",
            "#####"
        );

        assertEquals(SpaceType.WALL, track.getSpaceTypeAtPosition(new PositionVector(-1, 1)));
        assertEquals(SpaceType.WALL, track.getSpaceTypeAtPosition(new PositionVector(99, 99)));
    }

    @Test
    void getCharRepresentationAndToStringOverlayCarsOntoGrid() throws Exception {
        Track track = createTrack(
            "###",
            "#a#",
            "#>#"
        );
        replaceCars(track, List.of(
            new TestCar('a', new PositionVector(1, 1), false),
            new TestCar('b', new PositionVector(1, 2), true)
        ));

        assertEquals('a', track.getCharRepresentationAtPosition(1, 1));
        assertEquals(Track.CRASH_INDICATOR, track.getCharRepresentationAtPosition(2, 1));
        assertEquals(
            String.join(System.lineSeparator(), "###", "#a#", "#X#"),
            track.toString()
        );
    }

    private Track createTrack(String... lines) throws IOException, InvalidFileFormatException {
        Path trackFile = tempDir.resolve("track.txt");
        Files.writeString(trackFile, String.join(System.lineSeparator(), lines));
        return new Track(trackFile.toFile());
    }

    private void replaceCars(Track track, List<Car> replacementCars) throws ReflectiveOperationException {
        Field carsField = Track.class.getDeclaredField("cars");
        carsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Car> cars = (List<Car>) carsField.get(track);
        cars.clear();
        cars.addAll(replacementCars);
    }

    private static final class TestCar extends Car {
        private final PositionVector position;
        private final boolean crashed;

        private TestCar(char id, PositionVector position, boolean crashed) {
            super(id, position);
            this.position = position;
            this.crashed = crashed;
        }

        @Override
        public PositionVector getPosition() {
            return position;
        }

        @Override
        public PositionVector getVelocity() {
            return new PositionVector(0, 0);
        }

        @Override
        public PositionVector nextPosition() {
            return position;
        }

        @Override
        public void accelerate(Direction acceleration) {
        }

        @Override
        public void move() {
        }

        @Override
        public void crash(PositionVector crashPosition) {
        }

        @Override
        public boolean isCrashed() {
            return crashed;
        }
    }
}
