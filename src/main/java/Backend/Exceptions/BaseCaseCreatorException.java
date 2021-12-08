package Backend.Exceptions;

public class BaseCaseCreatorException extends InvalidTermException {
    public static final String ERRORMESSAGE_EMPTY_EXPRESSION = "NullExpressionException!";
    public static final String ERRORMESSAGE_INVALID_SINGLE_TERM = "InvalidSingleExpressionException!";

    public BaseCaseCreatorException(String message) {
        super(message);
    }
}