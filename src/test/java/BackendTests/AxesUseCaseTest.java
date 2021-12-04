package BackendTests;

import Backend.*;

import Backend.Exceptions.InvalidCommandArguments;
import Backend.Exceptions.InvalidTermException;
import Backend.Expressions.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class AxesUseCaseTest {

    Axes ax;
    AxesUseCase auc;
    RealValuedExpression expr5;
    RealValuedExpression expr0;

    @Before
    public void setUp(){
        ax = new Axes();
        auc = new AxesUseCase();
        expr5 = new NumberExpression("5");
        expr0 = new NumberExpression("0");

    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test adding expressions to the Axes
     */
    @Test(timeout = 50)
    public void testAxesUseCaseAddExpression(){
        auc.addExpression(expr5, ax);
        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);
        assertEquals(ax.getExpressions(), eList);
    }


    @Test(timeout = 50)
    public void testAxesUseCaseRemoveExpression(){
        auc.addExpression(expr5, ax);
        auc.removeExpression(expr5, ax);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();

        assertEquals(ax.getExpressions(), eList);
    }

    //Test what happens when we try to remove an expression that is not stored in axes
    @Test(timeout = 50)
    public void testAxesUseCaseRemoveNonExistentExpression(){
        auc.addExpression(expr5, ax);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);

        auc.removeExpression(expr0, ax);

        assertEquals(ax.getExpressions(), eList);
    }

    @Test(timeout = 50)
    public void testAxesUseCaseSetScale5() throws InvalidCommandArguments {
        auc.setScale(5, ax);
        assertEquals(auc.getScale(ax), 5f, 0);
    }
//    @Test(timeout = 50)
//    public void testAxesUseCaseSetScale0() throws InvalidCommandArguments {
//        auc.setScale(0, ax);
//
//    }


    @Test
    public void testAxesUseCaseSetScale0() throws InvalidCommandArguments {

        thrown.expect(InvalidCommandArguments.class);
        auc.setScale(0, ax);
    }


    @Test(timeout = 50)
    public void testAxesUseCaseGetOrigin(){
        auc.getOrigin(ax);
        assertArrayEquals(auc.getOrigin(ax), new float[]{0,0}, 0.0F);
    }

    @Test(timeout = 50)
    public void testAxesUseCaseSetOrigin(){
        auc.setOrigin(new float[]{6f,-4f}, ax);
        assertArrayEquals(auc.getOrigin(ax), new float[]{6,-4f}, 0.0F);
    }

    @Test(timeout = 100)
    public void testAxesAddCustomFunction() throws InvalidTermException {

        ExpressionCreator ec = new ExpressionCreator(auc.getNamedFunctions(ax));
        String funcName = "f";
        String[] variables = {"x"};
        Expression<?> func = ec.create(List.of("x", "^", "2"));
        FunctionExpression myFunc = new CustomFunctionExpression(funcName, variables, (RealValuedExpression) func);
        auc.addExpression(myFunc, ax);

        assertEquals(13, auc.getNamedFunctions(ax).size(),0);

    }

    @Test(timeout = 50)
    public void testAxesUseCaseGetExpr(){
        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        assertEquals(auc.getExpressions(ax), eList);
    }


}
