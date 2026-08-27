package am.command;

/** Indicates that a command has invalid syntax or arguments. */
public class InvalidCommandException extends RuntimeException {
    /** Creates an exception with the supplied validation message. */
    public InvalidCommandException(String message) {
        super(message);
    }
}
