package basic;

import static basic.ImageTest.*;
import static basic.RaymarcherTest.fmtHex255;

public class ImplicitGrapherTest {
    public static class CoolFunc implements Evaluatable {
        public float evaluate(float cx, float cy) {
            return(float)(Math.cos(cx*cy)+Math.sin(cx+cy)-0.4);
        }
    }
    public static class MandelFunc implements Evaluatable {
        public float evaluate(float cx, float cy) {
            float x = 0;
            float y = 0;
            int i;
            for (i = 0; i < 100; i++) {
                if (x*x + y*y > 4) break;
                float xtemp = x*x - y*y + cx;
                y = 2*x*y + cy;
                x = xtemp;
            }
            return (float)i / 100.f;
        }
    }


   public static void main(String args[]) throws Exception {
      int size = 256;
      int[] mainPixels = new int[size*size];
      int[] dims1 = {size,size};

      Evaluatable f1 = new MandelFunc();
      graphImplicit(mainPixels, dims1[0], dims1[1], f1, 0.01f, 0.6f, 0.f, false);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOutMandel.png");

      Evaluatable f2 = new CoolFunc();
      graphImplicit(mainPixels, dims1[0], dims1[1], f2, 0.1f, 0.f, 0.f, true);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOutCool.png");
   }

  public static void graphImplicit(int[] pixels, int w, int h, Evaluatable func,
                                   float scale, float xpos, float ypos,
                                   boolean useThreshold) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            float cx = (x - w/2) * scale - xpos;
            float cy = (y - h/2) * scale - ypos;
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