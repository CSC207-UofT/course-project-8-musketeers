package Backend.Exceptions;

public class InvalidCommandArguments extends Exception {
    /**
     * Constructor for the InvalidCommandArguments Exception. This exception occurs when one (or more) inputs
     * from the user is not valid.
     * @param errorMessage An error message to be printed.
     */
    public InvalidCommandArguments(String errorMessage) {
        super(errorMessage);
    }
}
