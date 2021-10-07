package basic;

import static basic.ImageTest.*;
import static java.lang.Math.max;

public class ImplicitGrapherTest {
   public static void main(String args[]) throws Exception {
       int size = 256;
      int[] mainPixels = new int[size*size];
      int[] dims1 = {size,size};
      //int[] mainPixels = readImage("sample.png");
      //int[] dims1 = getImDims("sample.png");
      mainPixels[5] = (int)Long.parseLong("FFFF00FF", 16);
      mainPixels[6] = (int)Long.parseLong("FF00FFFF", 16);
      raymarch(mainPixels, dims1[0], dims1[1]);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOut.png");
   }

  public static void raymarch(int[] pixels, int w, int h) {
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            float cx = (x - w/2) * 0.1f;
            float cy = (y - h/2) * 0.1f;
            if ((max(0, 16 - cx*cx - cy*cy)
                    + max(0, 9-(cx-3)*(cx-3)-(cy-3)*(cy-3))
                    + Math.sin(cx)*Math.sin(cy) - 0.5f
                    + 0.4f*(Math.sin(cx*cy*cy / 16) - 0.5f)
                    + 0.4f*(Math.cos(-cx*cy / 9) - 0.5f)
                ) > 0) {
                pixels[y*w+x] = (int)Long.parseLong("FFFFFFFF", 16);
            } else {
                pixels[y*w+x] = (int)Long.parseLong("FF000000", 16);
            }
         }
      }
  }
}