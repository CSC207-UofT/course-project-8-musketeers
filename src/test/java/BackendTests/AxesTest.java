package BackendTests;


import Backend.Axes;
import Backend.Expression;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
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
        assertEquals(ax.getOrigin(), new Point(0,0));
        assertEquals(ax.getExpressions(), new HashSet<Expression>());
    }

}
