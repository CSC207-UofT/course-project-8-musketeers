package Graphics;

import static Graphics.ImageTest.*;
import static Graphics.RGBA.fmtHex255;

import Backend.Expression;
import Backend.Axes;
import Backend.ExpressionReader;


public class ExplicitGrapher {

    public static void main(String[] args) throws Exception {
        int size = 256;
        int[] mainPixels = new int[size*size];
        int[] dims1 = {size,size};


        Axes axes = new Axes();
        axes.setScale(2f);
        float[] pos = {0.f, 0.f};
        axes.setOrigin(pos);
        ExpressionReader er = new ExpressionReader();

        //Expression func = er.read("( cos ( x * y ) + sin ( x + y ) ) * 0.8 - 0.1");
        Expression func = er.read("sin ( x )");
        axes.addExpression(func);
        graphExplicit(mainPixels, dims1[0], dims1[1], axes, false);
        writeImage(mainPixels, dims1[0], dims1[1], "sampleOutHmm.png");
        System.out.println("...Done!");
    }

    public static void graphExplicit(int[] pixels, int w, int h, Axes ax,
                                     boolean useThreshold) {
        Expression e1 = ax.getExpressions().get(0);
        Evaluatable func = new EvalExprAdapter(e1);

        float scale = ax.getScale();
        float xpos = ax.getOrigin()[0];
        float ypos = ax.getOrigin()[1];

        for (int x = 0; x < w; x++) {
            float cx = (x / (float)w) * scale + xpos;
            System.out.println(w * (int) (Math.floor(func.evaluate(cx) * h / scale)) + x);
            for (int y = 0; y < h; y++) {
                pixels[w * (int) (Math.floor(func.evaluate(cx) * h / scale)) + x] = (int) Long.parseLong("FFFFFFFF", 16);
            }
        }
    }
}