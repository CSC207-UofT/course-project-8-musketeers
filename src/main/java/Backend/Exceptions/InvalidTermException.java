package Backend.Exceptions;

public class InvalidTermException extends Exception { // TODO: Check whether this looks good (extending "Exception" class)
    public InvalidTermException(String message) {
        super(message);
    }
}
