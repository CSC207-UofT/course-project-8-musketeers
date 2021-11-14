package BackendTests;

import Backend.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import java.util.*;
import static org.junit.Assert.*;

public class AxesUseCaseTest {

    Axes ax;
    AxesUseCase auc;
    Expression expr5;
    Expression expr0;

    @Before
    public void setUp(){
        ax = new Axes();
        auc = new AxesUseCase();
        expr5 = new NumberExpression("5");
        expr0 = new NumberExpression("0");

    }

    /**
     * Test adding expressions to the Axes
     */
    @Test(timeout = 50)
    public void testAxesAddExpression(){
        auc.addExpression(expr5, ax);
        ArrayList<Expression> eList = new ArrayList<>();
        eList.add(expr5);
        assertEquals(ax.getExpressions(), eList);
    }

    /**
     * test adding and removing the same expression to and from axes
     */
    @Test(timeout = 50)
    public void testAxesRemoveExpression(){
        auc.addExpression(expr5, ax);
        auc.removeExpression(expr5, ax);

        ArrayList<Expression> eList = new ArrayList<>();

        assertEquals(ax.getExpressions(), eList);
    }

    //Test what happens when we try to remove an expression that is not stored in axes
    @Test(timeout = 50)
    public void testAxesRemoveNonExistentExpression(){
        auc.addExpression(expr5, ax);

        ArrayList<Expression> eList = new ArrayList<>();
        eList.add(expr5);

        auc.removeExpression(expr0, ax);

        assertEquals(ax.getExpressions(), eList);
    }

    @Test(timeout = 50)
    public void testAxesAddCustomFunction(){
        assertEquals(ax.getNamedExpressions().size(), 5);

        ExpressionCreator ec = new ExpressionCreator(ax);
        String funcName = "f";
        String[] variables = {"x"};
        Expression func = ec.create(List.of("x", "^", "2"));
        FunctionExpression myFunc = new CustomFunctionExpression(funcName, variables, func);
        auc.addExpression(myFunc, ax);

        assertEquals(ax.getNamedExpressions().size(), 6);
    }
}
