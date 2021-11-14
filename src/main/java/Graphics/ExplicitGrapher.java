package Graphics;

import static Graphics.ImageTest.*;
import static Graphics.RGBA.fmtHex255;

import Backend.*;
import Backend.Axes;
import Backend.ExpressionReader;
import Backend.Expressions.RealValuedExpression;


public class ExplicitGrapher {

    public static void main(String[] args) throws Exception {
        int size = 256;
        int[] mainPixels = new int[size*size];
        int[] dims1 = {size,size};


        Axes axes = new Axes();
        axes.setScale(10f);
        float[] pos = {0.f, 0.f};
        axes.setOrigin(pos);
        ExpressionReader er = new ExpressionReader(axes.getNamedExpressions());

        //Expression func = er.read("( cos ( x * y ) + sin ( x + y ) ) * 0.8 - 0.1");
//        Expression func = er.read("sin ( x ) + 3.14");
//        axes.addExpression(func);
//        graphExplicit(mainPixels, dims1[0], dims1[1], axes, false);
//        writeImage(mainPixels, dims1[0], dims1[1], "sampleOutHmm.png");
//        System.out.println("...Done!");
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