package Backend.Exceptions;

public class CompoundCaseCreatorException extends InvalidTermException {
    public static final String ERRORMESSAGE_UNMATCHED_BRACKETS = "UnmatchedBracketsException!";
    public static final String ERRORMESSAGE_INVALID_TERM = "InvalidTermException!";
    public static final String ERRORMESSAGE_INVALID_OPERAND = "Invalid Operand Exception!";
    public static final String ERRORMESSAGE_INVALID_OPERAND_TYPE = "OperandTypeException!";
    public static final String ERRORMESSAGE_MISSING_OPERATOR = "NoOperatorToBuildException";
    public static final String ERRORMESSAGE_INVALID_FUNCTION_INPUT = "InvalidFunctionInputsException!";
    public static final String ERRORMESSAGE_FUNCTION_INPUT_SIZE = "FunctionInputsSizeException!";
    public static final String ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS = "CommasNotWithinFunctions!";

    public CompoundCaseCreatorException(String message) {
        super(message);
    }
}