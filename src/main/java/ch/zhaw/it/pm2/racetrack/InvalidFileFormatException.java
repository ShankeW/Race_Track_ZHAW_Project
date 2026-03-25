package ch.zhaw.it.pm2.racetrack;

public class InvalidFileFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an InvalidFileFormatException
     */
    public InvalidFileFormatException() {
        super();
    }

    /**
     * Creates an InvalidFileFormatException
     *
     * @param message creates exception with this message.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }

    /**
     * Creates an InvalidFileFormatException
     *
     * @param message creates exception with this message.
     * @param cause   why the exception was caused.
     */
    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an InvalidFileFormatException
     *
     * @param cause why the exception was caused.
     */
    public InvalidFileFormatException(Throwable cause) {
        super(cause);
    }
}
