package am.command;

/** Indicates that a recognized command has invalid arguments or formatting. */
public class InvalidCommandException extends RuntimeException {
    /** Creates an exception with a user-facing explanation. */
    public InvalidCommandException(String message) {
        super(message);
    }
}
