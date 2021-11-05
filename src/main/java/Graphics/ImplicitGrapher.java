package Graphics;

import static Graphics.ImageTest.*;
import static Graphics.RGBA.fmtHex255;

import Backend.Expression;
import Backend.Axes;
import Backend.ExpressionReader;


public class ImplicitGrapher {

   public static void main(String args[]) throws Exception {
      int size = 256;
      int[] mainPixels = new int[size*size];
      int[] dims1 = {size,size};


      Axes axes = new Axes();
      axes.setScale(1f);
      float[] pos = {-1.2f, 0.f};
      axes.setOrigin(pos);
      ExpressionReader er = new ExpressionReader();

      //Expression func = er.read("( cos ( x * y ) + sin ( x + y ) ) * 0.8 - 0.1");
      Expression func = er.read("mandel ( x , y )");
      axes.addExpression(func);
      graphImplicit(mainPixels, dims1[0], dims1[1], axes, false);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOutHmm.png");
      System.out.println("...Done!");
   }

  public static void graphImplicit(int[] pixels, int w, int h, Axes ax,
                                   boolean useThreshold) {
      Expression e1 = ax.getExpressions().get(0);
      Evaluatable func = new EvalExprAdapter(e1);

      float scale = ax.getScale();
      float xpos = ax.getOrigin()[0];
      float ypos = ax.getOrigin()[1];

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            float cx = (x / (float)w - 0.5f) * scale + xpos;
            float cy = -(y / (float)h - 0.5f) * scale + ypos;
            if (useThreshold) {
                if (func.evaluate(cx, cy) > 0) {
                    pixels[y * w + x] = (int) Long.parseLong("FFFFFFFF", 16);
                } else {
                    pixels[y * w + x] = (int) Long.parseLong("FF000000", 16);
                }
            } else {
                float result = func.evaluate(cx, cy);
                String outR = fmtHex255((int) (255 * Math.sqrt(result)));
                pixels[y * w + x] = (int) Long.parseLong("FF" + outR + outR + outR, 16);
            }
         }
      }
  }
}