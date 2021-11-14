package BackendTests;

import Backend.Axes;
import Backend.Exceptions.InvalidTermException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import Backend.ExpressionReader;
import Backend.Expressions.*;

public class ExpressionReaderTest {

    Axes ax = new Axes();
    ExpressionReader er;
    Map<String, Float> varMap = new HashMap<>();
    double delta = Math.pow(10, -5);

    @Before
    public void setUp(){
        er = new ExpressionReader(ax.getNamedExpressions());
    }

    @Test(timeout = 50)
    public void testBinaryOperatorTwoTerms() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 + 3");
        assertEquals(exp.evaluate(varMap), 5.0, delta);
    }

    // Bracket parsing to be implemented
    @Test(timeout = 50)
    public void testBinaryOperatorTwoTermsWithBrackets() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 * (3)");
        assertEquals(exp.evaluate(varMap), 6.0, delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorWithOneVariable() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("x /    3");
        varMap.put("x", 3.f);
        assertEquals(exp.evaluate(varMap), 1.0, delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations1() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("1.5 + 3 * 5");
        assertEquals(exp.evaluate(varMap), 16.5, delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("4 *  3 - 2");
        assertEquals(exp.evaluate(varMap), 10.0, delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 - 1 - 3");
        assertEquals(exp.evaluate(varMap), -2, delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 / 2 / 2");
        assertEquals(exp.evaluate(varMap), 0.5, delta);
    }

    @Test(timeout = 50)
    public void testCos() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("cos(x^2)");
        varMap.put("x", 1.0f);
        assertEquals(exp.evaluate(varMap), Math.cos(1), delta);
    }

    @Test(timeout = 50)
    public void testCosWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 * cos ( x^2 ) + 5");
        varMap.put("x", 0.0f);
        assertEquals(exp.evaluate(varMap), 7, delta);
    }

    @Test(timeout = 50)
    public void testSin() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("sin ( x ^ 2)");
        varMap.put("x", 1.0f);
        assertEquals(exp.evaluate(varMap), Math.sin(1), delta);
    }

    @Test(timeout = 50)
    public void testSinWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("cos (x ) ^ 2+sin ( x )^ 2");
        varMap.put("x", 3.0f);
        assertEquals(exp.evaluate(varMap), 1, delta);
    }

    @Test(timeout = 50)
    public void testWeirdCombinationsOfOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("+-++--+1");
        assertEquals(exp.evaluate(varMap), -1f, delta);
    }
}
