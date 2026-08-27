package AM.command;

/**
 * Indicates that a recognized command has invalid arguments or formatting.
 */
public class InvalidCommandException extends RuntimeException {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param msg explanation of the invalid command
     */
    public InvalidCommandException(String msg) {
        super(msg);
    }
}
