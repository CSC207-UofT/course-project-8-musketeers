package BackendTests;

import Backend.Axes;
import Backend.Exceptions.BaseCaseCreatorException;
import Backend.Exceptions.CompoundCaseCreatorException;
import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionReader;
import Backend.Expressions.Expression;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ValidityCheckerTest {
    Axes ax = new Axes();
    ExpressionReader er;

    // TODO: store error messages in a better place e.g. using ResourceBundle
    private final String ERRORMESSAGE_EMPTY_EXPRESSION = "NullExpressionException!";
    private final String ERRORMESSAGE_UNMATCHED_BRACKETS = "UnmatchedBracketsException!";
    private final String ERRORMESSAGE_INVALID_SINGLE_CHARACTER = "InvalidSingleExpressionException!";
    private final String ERRORMESSAGE_INVALID_TERM = "InvalidTermException!";
    private final String ERRORMESSAGE_OPERAND_TYPE = "OperandTypeException!";
    private final String ERRORMESSAGE_OPERAND = "Invalid Operand Exception!";
    private final String ERRORMESSAGE_FUNCTION_INPUT_TYPE = "InvalidFunctionInputsException!";
    private final String ERRORMESSAGE_FUNCTION_INPUT_SIZE = "FunctionInputsSizeException!";
    private final String ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS = "CommasNotWithinFunctions!";

    @Before
    public void setUp(){
        er = new ExpressionReader(ax.getNamedExpressions());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // TEST INPUT PATTERNS

    // Todo: add a test for the errors that ExpressionValidityChecker.checkMultipleTermsConnection checks

    @Test(timeout = 50)
    public void testEmptyExpression() throws InvalidTermException {
        thrown.expect(BaseCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_EMPTY_EXPRESSION);
        Expression<?> exp = er.read("");
    }

    @Test(timeout = 50)
    public void testInvalidSingleCharacter() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("$");
    }

    @Test(timeout = 50)
    public void testInvalidCharacter() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("1+$2");
    }

    @Test(timeout = 50)
    public void testInvalidNumber() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("1.2.1");
    }

    @Test(timeout = 50)
    public void testInvalidNumber2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("sin(1.2.1)");
    }

    @Test(timeout = 50)
    public void testUnmatchingBrackets() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_UNMATCHED_BRACKETS);
        Expression<?> exp = er.read("(((()()))()");
    }

    @Test(timeout = 50)
    public void testInvalidOperator() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("1 ? 2");
    }

    @Test(timeout = 50)
    public void testInvalidOperand() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("(1 & 3) < 3");
    }

    @Test(timeout = 50)
    public void testInvalidFunction() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("shinch(x)");
    }

    // Currently, there's no point having this test because if there's anything in the expression that could create
    // a boolean-valued function at any point in production, ExpressionCreator.createExpressionBuilder calls
    // booleanValuedCreator as the first step in construction, so we can never create a RealValuedExpression
    // with BooleanValuedExpression subtrees.
    // Not sure if this is best practice though, because we might incorporate types other than BooleanValuedExpressions,
    // so the current system might have limited extendibility.
//    @Test(timeout = 50)
//    public void testInvalidArithmeticOperatorInputs() throws InvalidTermException {
//        thrown.expect(CompoundCaseCreatorException.class);
//        thrown.expectMessage(this.ERRORMESSAGE_OPERAND_TYPE);
//        Expression<?> exp = er.read("(1 < 2) + 2");
//    }

    @Test(timeout = 50)
    public void testInvalidLogicalOperatorInputs() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_OPERAND_TYPE);
        Expression<?> exp = er.read("(1 < 2) & 2");
    }

    @Test(timeout = 50)
    public void testInvalidComparatorInputs() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_OPERAND_TYPE);
        Expression<?> exp = er.read("(1 < 2) < 2");
    }

    // TODO: decide whether the error message here should be ERRORMESSAGE_FUNCTION_INPUT_SIZE
    @Test(timeout = 50)
    public void testWrongFunctionInputLength() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_FUNCTION_INPUT_TYPE);
        Expression<?> exp = er.read("sin()");
    }

    @Test(timeout = 50)
    public void testWrongFunctionInputLength2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_FUNCTION_INPUT_SIZE);
        Expression<?> exp = er.read("cos(x,y)");
    }

    @Test(timeout = 50)
    public void testInvalidNumberofOperands() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_OPERAND);
        Expression<?> exp = er.read("2<");
    }

    @Test(timeout = 50)
    public void testWrongCommas() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS);
        Expression<?> exp = er.read("x+y,");
    }

    @Test(timeout = 50)
    public void testWrongCommas2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS);
        Expression<?> exp = er.read("(x+y,)");
    }

    @Test(timeout = 50)
    public void testWrongCommas3() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("f(x+y,)");
    }

    @Test(timeout = 50)
    public void testWrongCommas4() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(this.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = er.read("f(,,)");
    }

}
