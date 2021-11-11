package BackendTests;


import Backend.Axes;
import Backend.Expression;
import Backend.NumberExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for the Entity Class Axes
 */


public class AxesTest {



    Axes ax;
    Expression expr5;
    Expression expr0;

    @Before
    public void setUp(){
        ax = new Axes();
        expr5 = new NumberExpression("5");
        expr0 = new NumberExpression("0");

    }

    // TEST INPUT PATTERNS

    @Test(timeout = 50)
    public void testAxesCreation(){
        assertEquals(ax.getScale(),1,0);
        float[] p = {0,0};
        assertArrayEquals(ax.getOrigin(), p, 0.0F);
        assertEquals(ax.getExpressions(), new ArrayList<Expression>() {
        });
    }

    @Test(timeout = 50)
    public void testAxesCreation2(){
        ax = new Axes(5, 6, 7);

        assertEquals(ax.getScale(),5,0);
        assertArrayEquals(ax.getOrigin(), new float[]{6, 7}, 0.0F);
        assertEquals(ax.getExpressions(), new ArrayList<Expression>() {});
    }

    @Test(timeout = 50)
    public void testAxesSetOrigin(){
        assertArrayEquals(ax.getOrigin(), new float[]{0, 0}, 0.0F);

        ax.setOrigin(2,3);
        assertArrayEquals(ax.getOrigin(), new float[]{2, 3}, 0.0F);

        ax.setOrigin(new float[]{-5f,7.55f});
        assertArrayEquals(ax.getOrigin(), new float[]{-5, 7.55f}, 0.0F);
    }

    @Test(timeout = 50)
    public void testAxesSetScale(){
        assertEquals(ax.getScale(), 1f, 0.0001);
        ax.setScale(3f);
        assertEquals(ax.getScale(), 3f, 0.0001);

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
        ax.addExpression(expr5);
        ArrayList<Expression> eList = new ArrayList<>();
        eList.add(expr5);
        assertEquals(ax.getExpressions(), eList);
    }

    @Test(timeout = 50)
    public void testAxesRemoveExpression(){
        ax.addExpression(expr5);
        ax.removeExpression(expr5);

        ArrayList<Expression> eList = new ArrayList<>();

        assertEquals(ax.getExpressions(), eList);
    }

    //Test what happens when we try to remove an expression that is not stored in axes
    @Test(timeout = 50)
    public void testAxesRemoveNonExistentExpression(){
        ax.addExpression(expr5);

        ArrayList<Expression> eList = new ArrayList<>();
        eList.add(expr5);

        ax.removeExpression(expr0);

        assertEquals(ax.getExpressions(), eList);
    }


}
