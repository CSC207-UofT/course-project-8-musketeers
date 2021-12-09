package BackendTests;

import Backend.Axes;
import Backend.AxesUseCase;
import Backend.Exceptions.InvalidTermException;
import Backend.ExpressionBuilders.BooleanValuedExpressionFactory;
import Backend.ExpressionBuilders.RealValuedExpressionFactory;
import Backend.RealBooleanCreatorImp;
import Backend.Expressions.*;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AxesUseCaseTest {

    Axes axes;
    AxesUseCase axesUseCase;
    RealValuedExpression expr5;
    RealValuedExpression expr0;

    @Before
    public void setUp(){
        axes = new Axes();
        axesUseCase = new AxesUseCase();
        expr5 = new NumberExpression("5");
        expr0 = new NumberExpression("0");

    }

    /**
     * test createAxes and make sure the instance of axes has the correct attributes
     */
    @Test(timeout = 50)
    public void testCreateAxes(){
        Axes ax2 = axesUseCase.createAxes();
        assertEquals(axesUseCase.getScale(ax2),5,0);
        float[] p = {0,0};
        assertArrayEquals(axesUseCase.getOrigin(ax2), p, 0.00001f);
        assertEquals(axesUseCase.getExpressions(ax2), new ArrayList<RealValuedExpression>(){});
    }

    /**
     * Test adding expressions to the Axes
     */
    @Test(timeout = 50)
    public void testAxesUseCaseAddExpression(){
        axesUseCase.addExpression(expr5, axes);
        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);
        assertEquals(axes.getExpressions(), eList);
    }


    @Test(timeout = 50)
    public void testAxesUseCaseRemoveExpression(){
        axesUseCase.addExpression(expr5, axes);
        axesUseCase.removeExpression(expr5, axes);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();

        assertEquals(axes.getExpressions(), eList);
    }

    //Test what happens when we try to remove an expression that is not stored in axes
    @Test(timeout = 50)
    public void testAxesUseCaseRemoveNonExistentExpression(){
        axesUseCase.addExpression(expr5, axes);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);

        axesUseCase.removeExpression(expr0, axes);

        assertEquals(axes.getExpressions(), eList);
    }

    @Test(timeout = 50)
    public void testAxesUseCaseSetScale5() {
        axesUseCase.setScale(5, axes);
        assertEquals(axesUseCase.getScale(axes), 5f, 0);
    }


    @Test(timeout = 50)
    public void testAxesUseCaseGetOrigin(){
        axesUseCase.getOrigin(axes);
        assertArrayEquals(axesUseCase.getOrigin(axes), new float[]{0,0}, 0.0F);
    }

    @Test(timeout = 50)
    public void testAxesUseCaseSetOrigin(){
        axesUseCase.setOrigin(new float[]{6f,-4f}, axes);
        assertArrayEquals(axesUseCase.getOrigin(axes), new float[]{6,-4f}, 0.0F);
    }

    @Test(timeout = 100)
    public void testAxesAddCustomFunction() throws InvalidTermException {

        RealBooleanCreatorImp ec = new RealBooleanCreatorImp(axesUseCase.getNamedFunctions(axes));
        String funcName = "f";
        String[] variables = {"x"};
        Expression<?> func = ec.create(List.of("x", "^", "2"));
        FunctionExpression myFunc = new CustomFunctionExpression(funcName, variables, (RealValuedExpression) func);
        axesUseCase.addExpression(myFunc, axes);
        assertEquals(13, axesUseCase.getNamedFunctions(axes).size(),0);
    }

    @Test(timeout = 50)
    public void testAxesUseCaseGetExpr() {
        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        assertEquals(axesUseCase.getExpressions(axes), eList);
    }


}
