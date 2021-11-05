package BackendTests;


import Backend.Axes;
import Backend.Expression;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

import static org.junit.Assert.*;




public class AxesTest {



    Axes ax;


    @Before
    public void setUp(){
        ax = new Axes();

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

}
