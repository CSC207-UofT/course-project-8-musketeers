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

public class ValidityCheckerTest {
    Axes axes = new Axes();
    ExpressionReader expressionReader;

    @Before
    public void setUp(){
        expressionReader = new ExpressionReader(axes.getNamedExpressions());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // TEST INPUT PATTERNS

    // Todo: add a test for the errors that ExpressionValidityChecker.checkMultipleTermsConnection checks

    @Test(timeout = 50)
    public void testEmptyExpression() throws InvalidTermException {
        thrown.expect(BaseCaseCreatorException.class);
        thrown.expectMessage(BaseCaseCreatorException.ERRORMESSAGE_EMPTY_EXPRESSION);
        Expression<?> exp = expressionReader.read("");
    }

    @Test(timeout = 50)
    public void testUnmatchingBrackets() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_UNMATCHED_BRACKETS);
        Expression<?> exp = expressionReader.read("(((()()))()");
    }

    @Test(timeout = 50)
    public void testInvalidSingleCharacter() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("$");
    }

    @Test(timeout = 50)
    public void testInvalidCharacter() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("1+$2");
    }

    @Test(timeout = 50)
    public void testInvalidOperator() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("1 ? 2");
    }

    @Test(timeout = 50)
    public void testInvalidNumber() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("1.2.1");
    }

    @Test(timeout = 50)
    public void testInvalidNumber2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("sin(1.2.1)");
    }

    @Test(timeout = 50)
    public void testInvalidFunction() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_TERM);
        Expression<?> exp = expressionReader.read("shinch(x)");
    }

    // TODO: the program currently fails on this test (the test is correct)
    @Test(timeout = 50)
    public void testInvalidArithmeticOperatorInputs() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_OPERAND_TYPE);
        Expression<?> exp = expressionReader.read("(1 < 2) + 2");
    }

    @Test(timeout = 50)
    public void testInvalidLogicalOperatorInputs() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_OPERAND_TYPE);
        Expression<?> exp = expressionReader.read("(1 < 2) & 2");
    }

    @Test(timeout = 50)
    public void testInvalidComparatorInputs() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_OPERAND_TYPE);
        Expression<?> exp = expressionReader.read("(1 < 2) < 2");
    }

    // TODO: decide whether the error message here should be ERRORMESSAGE_FUNCTION_INPUT_SIZE
    @Test(timeout = 50)
    public void testWrongFunctionInputLength() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_FUNCTION_INPUT);
        Expression<?> exp = expressionReader.read("sin()");
    }

    @Test(timeout = 50)
    public void testWrongFunctionInputLength2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_FUNCTION_INPUT_SIZE);
        Expression<?> exp = expressionReader.read("cos(x,y)");
    }

    @Test(timeout = 50)
    public void testWrongCommas() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS);
        Expression<?> exp = expressionReader.read("x+y,");
    }

    @Test(timeout = 50)
    public void testWrongCommas2() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_COMMAS_OUTSIDE_FUNCTIONS);
        Expression<?> exp = expressionReader.read("(x+y,)");
    }

    @Test(timeout = 50)
    public void testWrongCommas3() throws InvalidTermException {
        thrown.expect(CompoundCaseCreatorException.class);
        thrown.expectMessage(CompoundCaseCreatorException.ERRORMESSAGE_INVALID_FUNCTION_INPUT);
        Expression<?> exp = expressionReader.read("cos((x+y,))");
    }

}
