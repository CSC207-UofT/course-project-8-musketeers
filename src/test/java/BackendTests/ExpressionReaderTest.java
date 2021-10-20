package BackendTests;

import Backend.Expression;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import Backend.ExpressionReader;
import Backend.Expression;

public class ExpressionReaderTest {

    ExpressionReader er;
    Map<String, Double> varMap = new HashMap<>();
    double delta = Math.pow(10, -8);

    @Before
    public void setUp(){
        er = new ExpressionReader();
    }

    @Test(timeout = 50)
    public void testBinaryOperatorTwoTerms(){
        Expression exp = er.read("2 + 3");
        assertEquals(exp.evaluate(varMap), 5.0, delta);
    }

    // Bracket parsing to be implemented
    @Test(timeout = 50)
    public void testBinaryOperatorTwoTermsWithBrackets(){
        Expression exp = er.read("2 * (3)");
        assertEquals(exp.evaluate(varMap), 6.0, delta);
    }

    @Test(timeout = 50)
    public void testBinaryOperatorWithOneVariable(){
        Expression exp = er.read("x / 3");
        varMap.put("x", 3.0);
        assertEquals(exp.evaluate(varMap), 1.0, delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations1(){
        Expression exp = er.read("1.5 + 3 * 5");
        assertEquals(exp.evaluate(varMap), 16.5, delta);
    }

    @Test(timeout = 50)
    public void testOrderOfOperations2(){
        Expression exp = er.read("4 * 3 - 2");
        assertEquals(exp.evaluate(varMap), 10.0, delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations(){
        Expression exp = er.read("2 - 1 - 3");
        assertEquals(exp.evaluate(varMap), -2, delta);
    }

    @Test(timeout = 50)
    public void testRepeatedOperations2(){
        Expression exp = er.read("2 / 2 / 2");
        assertEquals(exp.evaluate(varMap), 0.5, delta);
    }

    @Test(timeout = 50)
    public void testCos(){
        Expression exp = er.read("cos ( x ^ 2 )");
        varMap.put("x", 1.0);
        assertEquals(exp.evaluate(varMap), Math.cos(1), delta);
    }

    @Test(timeout = 50)
    public void testCosWithOtherOperators(){
        Expression exp = er.read("2 * cos ( x ^ 2 ) + 5");
        varMap.put("x", 0.0);
        assertEquals(exp.evaluate(varMap), 7, delta);
    }

    @Test(timeout = 50)
    public void testSin(){
        Expression exp = er.read("sin ( x ^ 2 )");
        varMap.put("x", 1.0);
        assertEquals(exp.evaluate(varMap), Math.sin(1), delta);
    }

    @Test(timeout = 50)
    public void testSinWithOtherOperators(){
        Expression exp = er.read("cos ( x ) ^ 2 + sin ( x ) ^ 2");
        varMap.put("x", 3.0);
        assertEquals(exp.evaluate(varMap), 1, delta);
    }

}
