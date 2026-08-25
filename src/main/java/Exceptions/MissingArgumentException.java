package Exceptions;

public class MissingArgumentException extends RuntimeException {
    public MissingArgumentException(String msg) {
        super(msg);
    }
}
