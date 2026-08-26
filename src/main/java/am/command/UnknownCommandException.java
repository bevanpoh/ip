package am.command;

/** Indicates that a command word is not supported by the application. */
public class UnknownCommandException extends RuntimeException {
    /** Creates an exception with the supplied command error message. */
    public UnknownCommandException(String message) {
        super(message);
    }
}
