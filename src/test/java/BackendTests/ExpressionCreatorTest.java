//package BackendTests;
//
//import Backend.*;
//import Backend.Expressions.ComparatorExpression;
//import Backend.Expressions.CustomFunctionExpression;
//import Backend.Expressions.Expression;
//import Backend.Expressions.VariableExpression;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.*;
//
//import static org.junit.Assert.*;
//
//public class ExpressionCreatorTest {
//
//    ExpressionCreator ec;
//    Map<String, Float> varMap = new HashMap<>();
//    double delta = Math.pow(10, -5);
//
//    @Before
//    public void setUp(){
//        ec = new ExpressionCreator();
//    }
//
//    // TEST INPUT PATTERNS
//
//    @Test(timeout = 50)
//    public void testSingleNumber(){
//        Expression exp = ec.create(List.of("2"));
//        assertEquals(exp.evaluate(varMap), 2.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testSingleVariable(){
//        Expression exp = ec.create(List.of("x"));
//        varMap.put("x", 5.0f);
//        assertEquals(exp.evaluate(varMap), 5.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testBinaryOperatorTwoTerms(){
//        Expression exp = ec.create(List.of("2", "+", "3"));
//        assertEquals(exp.evaluate(varMap), 5.0, delta);
//    }
//
//    // Bracket parsing to be implemented
//    @Test(timeout = 50)
//    public void testBinaryOperatorTwoTermsWithBrackets(){
//        Expression exp = ec.create(List.of("2", "*", "(", "3", ")"));
//        assertEquals(exp.evaluate(varMap), 6.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testBinaryOperatorWithOneVariable(){
//        Expression exp = ec.create(List.of("x", "/", "3"));
//        varMap.put("x", 3.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testBinaryOperatorWithTwoVariables(){
//        Expression exp = ec.create(List.of("x", "/", "y"));
//        varMap.put("x", 3.0f);
//        varMap.put("y", 6.0f);
//        assertEquals(exp.evaluate(varMap), 0.5, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testOrderOfOperations1(){
//        Expression exp = ec.create(List.of("1.5", "+", "3", "*", "5"));
//        assertEquals(exp.evaluate(varMap), 16.5, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testOrderOfOperations2(){
//        Expression exp = ec.create(List.of("4", "*", "3", "-", "2"));
//        assertEquals(exp.evaluate(varMap), 10.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testOrderOfOperations3(){
//        Expression exp = ec.create(List.of("x", "*", "3", "-", "2"));
//        varMap.put("x", 4.0f);
//        assertEquals(exp.evaluate(varMap), 10.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testRepeatedOperations(){
//        Expression exp = ec.create(List.of("2", "-", "1", "-", "3"));
//        assertEquals(exp.evaluate(varMap), -2, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testRepeatedOperations2(){
//        Expression exp = ec.create(List.of("2", "/", "4", "/", "10"));
//        assertEquals(exp.evaluate(varMap), 0.05, delta);
//    }
//
//    // TEST SPECIFIC FEATURES, NOT JUST GENERAL PATTERNS
//
//    @Test(timeout = 50)
//    public void testPower(){
//        Expression exp = ec.create(List.of("x", "^", "2"));
//        varMap.put("x", 4.f);
//        assertEquals(exp.evaluate(varMap), 16.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testCos(){
//        Expression exp = ec.create(List.of("cos", "(", "x", "^", "2", ")"));
//        varMap.put("x", 1.f);
//        assertEquals(exp.evaluate(varMap), Math.cos(1), delta);
//    }
//
//    @Test(timeout = 50)
//    public void testCosWithOtherOperators(){
//        Expression exp = ec.create(List.of("2", "*", "cos", "(", "x", "^", "2", ")", "+", "5"));
//        varMap.put("x", 0.f);
//        assertEquals(exp.evaluate(varMap), 7, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testSin(){
//        Expression exp = ec.create(List.of("sin", "(", "x", "^", "2", ")"));
//        varMap.put("x", 1.f);
//        assertEquals(exp.evaluate(varMap), Math.sin(1), delta);
//    }
//
//    @Test(timeout = 50)
//    public void testSinWithOtherOperators(){
//        Expression exp = ec.create(List.of("cos", "(", "x", ")", "^", "2", "+", "sin", "(", "x", ")", "^", "2"));
//        varMap.put("x", 3.f);
//        assertEquals(exp.evaluate(varMap), 1, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testTan(){
//        Expression exp = ec.create(List.of("tan", "(", "x", "^", "2", ")"));
//        varMap.put("x", 1.f);
//        assertEquals(exp.evaluate(varMap), Math.tan(1), delta);
//    }
//
//    @Test(timeout = 50)
//    public void testTanWithOtherOperators(){
//        Expression exp = ec.create(List.of("tan", "(", "x", ")", "^", "2", "+", "3"));
//        varMap.put("x", 1.f);
//        assertEquals(exp.evaluate(varMap), Math.pow(Math.tan(1), 2.0) + 3, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testLessThanOrEqualTo(){
//        Expression exp = ec.create(List.of("x", "<=", "5"));
//        varMap.put("x", 4.9f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.1f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testLessThan(){
//        Expression exp = ec.create(List.of("x", "<", "5"));
//        varMap.put("x", 4.9f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//        varMap.put("x", 5.1f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testLessThanOrEqualToWithOtherOperators(){
//        Expression exp = ec.create(List.of("x", "+", "z", "<=", "5", "-", "y"));
//        varMap.put("x", 5.0f);
//        varMap.put("y", 0.1f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//        varMap.put("x", 5.0f);
//        varMap.put("y", 0.0f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.0f);
//        varMap.put("y", -0.1f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testLessThanWithOtherOperators(){
//        Expression exp = ec.create(List.of("x", "+", "z", "<", "5", "-", "y"));
//        varMap.put("x", 5.0f);
//        varMap.put("y", 0.1f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//        varMap.put("x", 5.0f);
//        varMap.put("y", 0.0f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//        varMap.put("x", 5.0f);
//        varMap.put("y", -0.1f);
//        varMap.put("z", 0.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testMultipleComparators(){
//        // This assumes that chained comparators are treated like regular operations i.e. 1 < 2 < 3 means (1 < 2) < 3.
//        // Should we make chained comparators behave like a logical operator i.e. 1 < 2 < 3 means 1 < 2 & 2 < 3.
//        Expression exp = ec.create(List.of("x", "<=", "y", "<=", "-1"));
//        varMap.put("x", 5.0f);
//        varMap.put("y", 4.9f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.0f);
//        varMap.put("y", 5.1f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testSingleLogicalOperator(){
//        // currently fails
//        Expression exp = ec.create(List.of("x", "&", "y"));
//        varMap.put("x", 1.0f);
//        varMap.put("y", 1.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 1.0f);
//        varMap.put("y", 0.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testLogicalOperatorWithOtherOperators(){
//        // currently fails
//        Expression exp = ec.create(List.of("1", "<", "x", "&", "x", "<=", "5"));
//
//        varMap.put("x", 1.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//        varMap.put("x", 2.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 5.0f);
//        assertEquals(exp.evaluate(varMap), 1.0, delta);
//        varMap.put("x", 6.0f);
//        assertEquals(exp.evaluate(varMap), -1.0, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testSqrtDomain(){
//        Expression exp = ec.create(List.of("sqrt", "(", "x", ")"));
//        varMap.put("x", -1.f);
//        assertTrue(Float.isNaN(exp.evaluate(varMap)));
//    }
//
//    @Test(timeout = 50)
//    public void testCustomFunctions(){
//        String funcName = "f";
//        Expression[] inputs = {new VariableExpression("x")};
//        Expression func = ec.create(List.of("x", "^", "2"));
//        String[] variables = {"x"};
//        Expression myFunc = new CustomFunctionExpression(funcName, inputs, func, variables);
//
//        varMap.put("x", 1.f);
//        assertEquals(myFunc.evaluate(varMap), 1.f, delta);
//    }
//
//    @Test(timeout = 50)
//    public void testCustomFunctionDomain(){
//        String funcName = "f";
//        Expression[] inputs = {new VariableExpression("x")};
//        Expression func = ec.create(List.of("x", "^", "2"));
//        String[] variables = {"x"};
//        ComparatorExpression domain = (ComparatorExpression) ec.create(List.of("x", ">", "0"));
//        Expression myFunc = new CustomFunctionExpression(funcName, inputs, func, variables, domain);
//
//        varMap.put("x", -1.f);
//        assertTrue(Float.isNaN(myFunc.evaluate(varMap)));
//    }
//}
