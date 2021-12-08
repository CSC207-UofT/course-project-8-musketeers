package BackendTests;

import Backend.*;
import Backend.ExpressionBuilders.BooleanValuedExpressionFactory;
import Backend.ExpressionBuilders.RealValuedExpressionFactory;
import Backend.Expressions.*;
import Backend.Exceptions.InvalidTermException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ExpressionCreatorTest {

    Axes axes = new Axes();
    ExpressionCreator expressionCreator;
    RealValuedExpressionFactory realValuedExpressionFactory = new RealValuedExpressionFactory();
    BooleanValuedExpressionFactory booleanValuedExpressionFactory = new BooleanValuedExpressionFactory();
    Map<String, Float> varMap = new HashMap<>();
    double delta = Math.pow(10, -5);

    @Before
    public void setUp(){
        expressionCreator = new ExpressionCreator(axes.getNamedExpressions(), realValuedExpressionFactory,
                booleanValuedExpressionFactory);
    }

    // TEST INPUT PATTERNS

    @Test(timeout = 50)
    public void testSingleNumber() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2"));
        assertEquals(2.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testSingleVariable() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("x"));
        varMap.put("x", 5.0f);
        assertEquals(5.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorTwoTerms() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2", "+", "3"));
        assertEquals(5.0, exp.evaluate(varMap), delta);
    }

    // Bracket parsing to be implemented
    @Test(timeout = 50)
    public void testBinaryOperatorTwoTermsWithBrackets() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2", "*", "(", "3", ")"));
        assertEquals(6.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorWithOneVariable() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("x", "/", "3"));
        varMap.put("x", 3.0f);
        assertEquals(1.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorWithTwoVariables() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("x", "/", "y"));
        varMap.put("x", 3.0f);
        varMap.put("y", 6.0f);
        assertEquals(0.5, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations1() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("1.5", "+", "3", "*", "5"));
        assertEquals(16.5, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("4", "*", "3", "-", "2"));
        assertEquals(10.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations3() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("x", "*", "3", "-", "2"));
        varMap.put("x", 4.0f);
        assertEquals(10.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2", "-", "1", "-", "3"));
        assertEquals(-2, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations2() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2", "/", "4", "/", "10"));
        assertEquals(0.05, exp.evaluate(varMap), delta);
    }

    // TEST SPECIFIC FEATURES, NOT JUST GENERAL PATTERNS

    @Test(timeout = 50)
    public void testPower() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("x", "^", "2"));
        varMap.put("x", 4.f);
        assertEquals(16.0, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testCos() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("cos", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.cos(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testCosWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("2", "*", "cos", "(", "x",
                "^", "2", ")", "+", "5"));
        varMap.put("x", 0.f);
        assertEquals(7, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testSin() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("sin", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.sin(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testSinWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("cos", "(", "x", ")", "^",
                "2", "+", "sin", "(", "x", ")", "^", "2"));
        varMap.put("x", 3.f);
        assertEquals(1, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testTan() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("tan", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.tan(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testTanWithOtherOperators() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("tan", "(", "x", ")",
                "^", "2", "+", "3"));
        varMap.put("x", 1.f);
        assertEquals(Math.pow(Math.tan(1), 2.0) + 3, exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testLessThanOrEqualTo() throws InvalidTermException {
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("x", "<=", "5"));
        varMap.put("x", 4.9f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 5.1f);
        assertEquals(false, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testLessThan() throws InvalidTermException {
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("x", "<", "5"));
        varMap.put("x", 4.9f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 5.1f);
        assertEquals(false, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testLessThanOrEqualToWithOtherOperators() throws InvalidTermException {
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("x", "+", "z",
                "<=", "5", "-", "y"));
        varMap.put("x", 5.0f);
        varMap.put("y", 0.1f);
        varMap.put("z", 0.0f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        varMap.put("y", 0.0f);
        varMap.put("z", 0.0f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        varMap.put("y", -0.1f);
        varMap.put("z", 0.0f);
        assertEquals(true, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testLessThanWithOtherOperators() throws InvalidTermException {
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("x", "+", "z",
                "<", "5", "-", "y"));
        varMap.put("x", 5.0f);
        varMap.put("y", 0.1f);
        varMap.put("z", 0.0f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        varMap.put("y", 0.0f);
        varMap.put("z", 0.0f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        varMap.put("y", -0.1f);
        varMap.put("z", 0.0f);
        assertEquals(true, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testMultipleComparators() throws InvalidTermException {
        // This assumes that chained comparators are treated like regular operations i.e. 1 < 2 < 3 means (1 < 2) < 3.
        // Should we make chained comparators behave like a logical operator i.e. 1 < 2 < 3 means 1 < 2 & 2 < 3.
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("0", "<", "x",
                "<=", "y", "<=", "1"));
        varMap.put("x", 0.0f);
        varMap.put("y", 0.9f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 0.1f);
        varMap.put("y", 0.9f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 0.1f);
        varMap.put("y", 1.0f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 0.1f);
        varMap.put("y", 1.1f);
        assertEquals(false, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testLogicalOperatorWithOtherOperators() throws InvalidTermException {
        // currently fails
        BooleanValuedExpression exp = (BooleanValuedExpression) expressionCreator.create(List.of("1", "<", "x", "&",
                "x", "<=", "4", "+", "1"));
        varMap.put("x", 1.0f);
        assertEquals(false, exp.evaluate(varMap));
        varMap.put("x", 2.0f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 5.0f);
        assertEquals(true, exp.evaluate(varMap));
        varMap.put("x", 6.0f);
        assertEquals(false, exp.evaluate(varMap));
    }

    @Test(timeout = 50)
    public void testSqrtDomain() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("sqrt", "(", "x", ")"));
        varMap.put("x", -1.f);
        assertTrue(Float.isNaN(exp.evaluate(varMap)));
    }

    @Test(timeout = 50)
    public void testCustomFunctions() throws InvalidTermException {
        String funcName = "f";
        String[] variables = {"x"};
        RealValuedExpression func = (RealValuedExpression) expressionCreator.create(List.of("x", "^", "2"));
        RealValuedExpression myFunc = new CustomFunctionExpression(funcName, variables, func);

        varMap.put("x", 1.f);
        assertEquals(1.f, myFunc.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testCustomFunctionDomain() throws InvalidTermException {
        String funcName = "f";
        String[] variables = {"x"};
        RealValuedExpression func = (RealValuedExpression) expressionCreator.create(List.of("x", "^", "2"));

        ComparatorExpression domain = (ComparatorExpression) expressionCreator.create(List.of("x", ">", "0"));
        RealValuedExpression myFunc = new CustomFunctionExpression(funcName, variables, func, domain);

        varMap.put("x", -1.f);
        assertTrue(Float.isNaN(myFunc.evaluate(varMap)));
    }

    @Test(timeout = 50)
    public void testCustomFunctionMultivariable() throws InvalidTermException {
        String funcName = "divide";
        String[] variables = {"x", "y"};
        RealValuedExpression func = (RealValuedExpression) expressionCreator.create(List.of("x", "/", "y"));
        RealValuedExpression myFunc = new CustomFunctionExpression(funcName, variables, func);

        varMap.put("x", 2f);
        varMap.put("y", 1f);
        assertEquals(2f, myFunc.evaluate(varMap), delta);

        varMap.put("x", 1f);
        varMap.put("y", 0f);
        assertFalse(Float.isFinite(myFunc.evaluate(varMap)));
    }

    @Test(timeout = 50)
    public void testCustomFunctionComposition() throws InvalidTermException {
        String funcName = "f";
        String[] variables = {"x"};
        RealValuedExpression func = (RealValuedExpression) expressionCreator.create(List.of("x", "^", "2"));

        FunctionExpression myFunc = new CustomFunctionExpression(funcName, variables, func);
        RealValuedExpression f2 = new BuiltinFunctionExpression("cos", new String[]{"x"});
        myFunc.setInputs(new RealValuedExpression[] {f2});

        varMap.put("x", 0f);
        assertEquals(1, myFunc.evaluate(varMap), delta);
    }



    @Test(timeout = 50)
    public void testCompositionOfCustomFunctions() throws InvalidTermException {
        Axes axes = new Axes();
        ExpressionCreator ec2 = new ExpressionCreator(axes.getNamedExpressions(), realValuedExpressionFactory,
                booleanValuedExpressionFactory);
        ExpressionReader er2 = new ExpressionReader(axes);

        String funcName = "f";
        String[] variables = {"x"};
        RealValuedExpression func = (RealValuedExpression) ec2.create(List.of("x", "^", "2"));
        FunctionExpression myFunc = new CustomFunctionExpression(funcName, variables, func);
        axes.addExpression(myFunc);

        String funcName2 = "g";
        String[] variables2 = {"x", "y"};
        RealValuedExpression func2 = (RealValuedExpression) ec2.create(List.of("x", "*", "y"));
        FunctionExpression myFunc2 = new CustomFunctionExpression(funcName2, variables2, func2);
        axes.addExpression(myFunc2);

        RealValuedExpression composeFunc = (RealValuedExpression) er2.read("f(g(x, y))");
        varMap.put("x", 2f);
        varMap.put("y", 3f);
        assertEquals(func2.evaluate(varMap), 6, delta);

        assertEquals(composeFunc.evaluate(varMap), 36, delta);
    }

    @Test(timeout = 50)
    public void testExp() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("exp", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.exp(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testArcsin() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("arcsin", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.asin(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testArccos() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("arccos", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.acos(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testArctan() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("arctan", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.atan(1), exp.evaluate(varMap), delta);
    }

    @Test(timeout = 50)
    public void testLogarithm() throws InvalidTermException {
        RealValuedExpression exp = (RealValuedExpression) expressionCreator.create(List.of("log", "(", "x",
                "^", "2", ")"));
        varMap.put("x", 1.f);
        assertEquals(Math.log(1), exp.evaluate(varMap), delta);
    }


}
