package Graphics;

import static Graphics.ImageTest.*;
import static Graphics.RGBA.fmtHex255;

import Backend.*;
import Backend.Axes;
import Backend.ExpressionReader;
import Backend.Expressions.RealValuedExpression;


public class ImplicitGrapher {
    public static final int WHITE = (int) Long.parseLong("FFFFFFFF", 16);
    public static final int BLACK = (int) Long.parseLong("FF000000", 16);

//   public static void main(String args[]) throws Exception {
//      int size = 256;
//      int[] mainPixels = new int[size*size];
//      int[] dims1 = {size,size};
//
//
//      Axes axes = new Axes();
//      axes.setScale(2f);
//      float[] pos = {-0.8f, 0.f};
//      axes.setOrigin(pos);
//      ExpressionReader er = new ExpressionReader(axes.getNamedExpressions());
//
//      // Expression func = er.read("( cos ( x * y ) + sin ( x + y ) ) * 0.8 - 0.1");
//      RealValuedExpression func = (RealValuedExpression) er.read("cos(10 * x) - y");
//      axes.addExpression(func);
//      graphImplicit(mainPixels, dims1[0], dims1[1], axes, GraphType.BOUNDARY);
//      writeImage(mainPixels, dims1[0], dims1[1], "sampleOutHmm.png");
//      System.out.println("...Done!");
//   }

//    public static void graphImplicit(int[] pixels, int w, int h, Axes ax,
//                                     boolean useThreshold) {
//        GraphType gtype = GraphType.GRAYSCALE;
//        if (useThreshold) gtype = GraphType.REGION;
//       graphImplicit(pixels, w, h, ax, gtype);
//    }

    public int[] graph(int[] pixels, int w, int h, Evaluatable func, float[] graphData, GraphType gtype){
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                writePixel(pixels, w, h, func, graphData, gtype, x, y);
            }
        }
        return pixels;
    }

private void writePixel(int[] pixels, int w, int h, Evaluatable func,
                        float[] graphData, GraphType gtype,
                        int x, int y) {

      // This can be moved outside the double-for loop
      // but "premature optimization is the root of all evil"
      // currently this avoids long parameter list and data clump
      float scale = graphData[0];
      float xpos = graphData[1];
      float ypos = graphData[2];
      float pixelSize = scale / (float)w;

      float cx = (x / (float)w - 0.5f) * scale + xpos;
      float cy = -(y / (float)h - 0.5f) * scale + ypos;
      if (gtype == GraphType.REGION) {
          if (func.evaluate(cx, cy) > 0) {
              pixels[y * w + x] = WHITE;
          } else {
              pixels[y * w + x] = BLACK;
          }
      }
      else if (gtype == GraphType.GRAYSCALE) {
          float result = func.evaluate(cx, cy);
          String outR = fmtHex255((int) (255 * Math.sqrt(result)));
          pixels[y * w + x] = (int) Long.parseLong("FF" + outR + outR + outR, 16);
      }
      else if (gtype == GraphType.BOUNDARY) {
          pixels[y * w + x] = WHITE;

          if ((func.evaluate(cx, cy) > 0) ^ (func.evaluate(cx + pixelSize, cy) > 0)) {
              pixels[y * w + x] = BLACK;
          }
          if ((func.evaluate(cx, cy) > 0) ^ (func.evaluate(cx, cy + pixelSize) > 0)) {
              pixels[y * w + x] = BLACK;
          }
      }
  }
}
