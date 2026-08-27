package AM.command;

/**
 * Indicates that a command name is not supported by the application.
 */
public class UnknownCommandException extends RuntimeException {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param msg explanation of the unknown command
     */
    public UnknownCommandException(String msg) {
        super(msg);
    }
}

