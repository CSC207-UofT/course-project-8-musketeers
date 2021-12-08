package BackendTests;


import Backend.*;


import org.junit.Before;
import org.junit.Test;
import Backend.Expressions.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for the Entity Class Axes
 */


public class AxesTest {



    Axes axes;
    RealValuedExpression expr5;
    RealValuedExpression expr0;

    @Before
    public void setUp(){
        axes = new Axes();
        expr5 = new NumberExpression("5");
        expr0 = new NumberExpression("0");

    }

    // TEST INPUT PATTERNS

    @Test(timeout = 50)
    public void testAxesCreation(){
        assertEquals(axes.getScale(),5,0.0001f);
        float[] p = {0,0};
        assertArrayEquals(axes.getOrigin(), p, 0.00001f);
        assertEquals(axes.getExpressions(), new ArrayList<RealValuedExpression>() {
        });
    }

    @Test(timeout = 100)
    public void testAxesCreation2(){
        axes = new Axes(5, 6, 7);

        assertEquals(axes.getScale(),5,0);
        assertArrayEquals(axes.getOrigin(), new float[]{6, 7}, 0.0F);
        assertEquals(axes.getExpressions(), new ArrayList<RealValuedExpression>() {});
    }

    @Test(timeout = 50)
    public void testAxesSetOrigin(){
        assertArrayEquals(axes.getOrigin(), new float[]{0, 0}, 0.0F);

        axes.setOrigin(2,3);
        assertArrayEquals(axes.getOrigin(), new float[]{2, 3}, 0.0F);

        axes.setOrigin(new float[]{-5f,7.55f});
        assertArrayEquals(axes.getOrigin(), new float[]{-5, 7.55f}, 0.0F);
    }

    @Test(timeout = 50)
    public void testAxesSetScale(){
        assertEquals(axes.getScale(), 5f, 0.0001);
        axes.setScale(3f);
        assertEquals(axes.getScale(), 3f, 0.0001);

    }

    @Test(timeout = 50)
    public void testAxesCreation3(){
        Axes ax2 = new Axes(5, new float[]{1,0,-1});
        assertArrayEquals(ax2.getOrigin(), new float[]{1,0,-1}, 0);
    }


    /**
     * Test adding and removing expressions from the Axes
     */
    @Test(timeout = 50)
    public void testAxesAddExpression(){
        axes.addExpression(expr5);
        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);
        assertEquals(axes.getExpressions(), eList);
    }

    @Test(timeout = 50)
    public void testAxesRemoveExpression(){
        axes.addExpression(expr5);
        axes.removeExpression(expr5);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();

        assertEquals(axes.getExpressions(), eList);
    }

    //Test what happens when we try to remove an expression that is not stored in axes
    @Test(timeout = 50)
    public void testAxesRemoveNonExistentExpression(){
        axes.addExpression(expr5);

        ArrayList<RealValuedExpression> eList = new ArrayList<>();
        eList.add(expr5);

        axes.removeExpression(expr0);

        assertEquals(axes.getExpressions(), eList);
    }


}
