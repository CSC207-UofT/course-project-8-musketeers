package BackendTests;


import Backend.Axes;
import Backend.Expression;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        assertEquals(ax.getOrigin(),
                new Point(0,0));
        assertEquals(ax.getExpressions(), new ArrayList<Expression>() {
        });
    }

    @Test(timeout = 50)
    public void testAxesSetOrigin(){
        assertEquals(ax.getOrigin(), new Point(0,0));
        ax.setOrigin(2,3);
        assertEquals(ax.getOrigin(), new Point(2,3));
        Point p = new Point(-5,7);
        ax.setOrigin(p);
        assertEquals(ax.getOrigin(),p);
    }

    @Test(timeout = 50)
    public void testAxesSetScale(){
        assertEquals(ax.getScale(), 1F, 0.0001);
        ax.setScale(3F);
        assertEquals(ax.getScale(), 3F, 0.0001);

    }

}
