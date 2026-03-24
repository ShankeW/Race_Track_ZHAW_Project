package ch.zhaw.it.pm2.racetrack;

/**
 * Used for invalid formatted files for Track, MoveStrategy, ...
 */
public class InvalidFileFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidFileFormatException() {
        super();
    }

    public InvalidFileFormatException(String message) {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileFormatException(Throwable cause) {
        super(cause);
    }
}
