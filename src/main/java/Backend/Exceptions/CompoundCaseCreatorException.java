package Backend.Exceptions;

public class CompoundCaseCreatorException extends InvalidTermException {
    public static final String ERRORMESSAGE_INVALID_OPERAND = "Invalid Operand Exception!";
    public static final String ERRORMESSAGE_MISSING_OPERATOR = "NoOperatorToBuildException";
    public static final String ERRORMESSAGE_INVALID_FUNCTION_INPUT = "InvalidFunctionInputsException!";

    public CompoundCaseCreatorException(String message) {
        super(message);
    }
}