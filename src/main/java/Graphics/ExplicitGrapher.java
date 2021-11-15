package Graphics;

import Backend.Axes;
import Backend.Expressions.RealValuedExpression;


public class ExplicitGrapher {

    public int[] graph(int[] pixels, int w, int h, Evaluatable func, float[] graphData){
        float scale = graphData[0];
        float xpos = graphData[1];
        float ypos = graphData[2];
        for (int x = 0; x < pixels.length; x++) {
            pixels[x] = (int) Long.parseLong("FFFFFFFF", 16);
        }
        //For each x coordinate, draw the f(x) coordinates that are on the grid approximating it to scale.
        for (int x = 0; x < w; x++) {
            float cx = (x / (float)w) * scale + xpos;
            System.out.println(w * (int) (Math.floor(func.evaluate(cx) * h / scale)) + x);
            if((w * (int) (Math.floor(func.evaluate(cx)) * h / scale) + x) > 0) {
                pixels[w * (h - (int) (Math.floor(func.evaluate(cx) * h / scale))) + x] = (int) Long.parseLong("FF000000", 16);
            }
        }
        return pixels;
    }

    public static void graphExplicit(int[] pixels, int w, int h, Axes ax,
                                     boolean useThreshold) {
        RealValuedExpression e1 = ax.getExpressions().get(0);
        Evaluatable func = new EvalExprAdapter(e1);

        float scale = ax.getScale();
        float xpos = ax.getOrigin()[0];
        float ypos = ax.getOrigin()[1];
        for (int x = 0; x < pixels.length; x++) {
            pixels[x] = (int) Long.parseLong("FFFFFFFF", 16);
        }
        //For each x coordinate, draw the f(x) coordinates that are on the grid approximating it to scale.
        for (int x = 0; x < w; x++) {
            float cx = (x / (float)w) * scale + xpos;
            System.out.println(w * (int) (Math.floor(func.evaluate(cx) * h / scale)) + x);
            if((w * (int) (Math.floor(func.evaluate(cx)) * h / scale) + x) > 0) {
                pixels[w * (h - (int) (Math.floor(func.evaluate(cx) * h / scale))) + x] = (int) Long.parseLong("FF000000", 16);
            }
        }
    }
}