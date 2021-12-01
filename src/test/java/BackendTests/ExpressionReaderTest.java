package BackendTests;

import Backend.Axes;
import Backend.Exceptions.InvalidTermException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
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
        assertEquals(5.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testContainsEquals() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("1 = 2");
        assertEquals(-1f, exp.evaluate(varMap), delta);
    }

    // Bracket parsing to be implemented
    @Test(timeout = 50)
    public void testBinaryOperatorTwoTermsWithBrackets() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 * (3)");
        assertEquals(6.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorWithOneVariable() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("x / 3");
        varMap.put("x", 3.f);
        assertEquals(1.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations1() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("1.5 + 3 * 5");
        assertEquals(16.5, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("4 *  3 - 2");
        assertEquals(10.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 - 1 - 3");
        assertEquals(-2, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 / 2 / 2");
        assertEquals(0.5, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testCos() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("cos(x^2)");
        varMap.put("x", 1.0f);
        assertEquals(Math.cos(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testCosWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("2 * cos ( x^2 ) + 5");
        varMap.put("x", 0.0f);
        assertEquals(7, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testSin() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("sin ( x ^ 2)");
        varMap.put("x", 1.0f);
        assertEquals(Math.sin(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testSinWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("cos (x ) ^ 2+sin ( x )^ 2");
        varMap.put("x", 3.0f);
        assertEquals(1, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testWeirdCombinationsOfOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) er.read("+-++--+1");
        assertEquals(-1f, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testUserDefineFunctions() throws InvalidTermException {

        ExpressionReader er2 = new ExpressionReader(ax);

        RealValuedExpression exp = (RealValuedExpression) er2.read("f(x) = x^2");

        assertEquals("f", exp.getItem());
        assertEquals(4, exp.evaluate(2), delta);

        ax.addExpression(exp);
        RealValuedExpression exp2 = (RealValuedExpression) er2.read("g(x) = f(x) + 1");
        assertEquals(5, exp2.evaluate(2), delta);
    }
    @Test(timeout = 50)
    public void testReadForGraphingLength1() throws InvalidTermException {
        RealValuedExpression exp = er.readForGraphing(new String[]{"1"});
        assertEquals(1, exp.evaluate(varMap), delta);
    }


//    @Test(timeout = 50)
//    public void testTED() throws InvalidTermException {
//        assertEquals("", er.expressionParser("- (x^x) = y"));
//    }

//    @Test(timeout = 100)
//    public void testUserDefineFunctions() throws InvalidTermException{
//        RealValuedExpression exp = (RealValuedExpression) er.read("f(x) = x^2");
//
//        assertEquals("f", exp.getItem());
//        assertEquals(4, exp.evaluate(2), delta);
//
//        ax.addExpression(exp);
//        //System.out.println(er.vc.funcNumInputs);
//        RealValuedExpression exp2 = (RealValuedExpression) er.read("g(x) = f(x) + 1");
//        assertEquals(5, exp2.evaluate(2), delta);
//    }
}
