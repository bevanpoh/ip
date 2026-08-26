package Exceptions;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String msg) {
        super(msg);
    }
}
