package AM.command;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String msg) {
        super(msg);
    }
}

