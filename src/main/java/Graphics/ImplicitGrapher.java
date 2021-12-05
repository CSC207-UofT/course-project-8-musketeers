package Graphics;

import static Graphics.RGBA.fmtHex255;

/**
 * ImplicitGrapher is responsible for the core 2D graphing functionality.
 *   It writes data to an array representing pixels of an image.
 */

public class ImplicitGrapher {
    public final int WHITE = (int) Long.parseLong("FFFFFFFF", 16);
    public final int BLACK = (int) Long.parseLong("FF000000", 16);

    /**
     * Writes the graph of a function onto the image represented by pixels.
     * @param pixels array representing an ARGB image of dimensions (w,h)
     * @param w width of the image represented by pixels
     * @param h height of the image represented by pixels
     * @param func the function to be graphed
     * @param graphData array of {scale, x position, y position}
     * @param gtype the type of graph to generate
     */
    public void graph(int[] pixels, int w, int h, Evaluatable func, float[] graphData, GraphType gtype){
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                writePixel(pixels, w, h, func, graphData, gtype, x, y);
            }
        }
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

      // Convert pixel coordinate to graphing-space coordinate
      float cx = (x / (float)w - 0.5f) * scale + xpos;
      float cy = -(y / (float)h - 0.5f) * scale + ypos;

      if (gtype == GraphType.REGION) {
          if (func.evaluate(cx, cy) < 0){
              pixels[y * w + x] = BLACK;
          }
      }
      else if (gtype == GraphType.GRAYSCALE) {
          float result = func.evaluate(cx, cy);
          String outR = fmtHex255((int) (255 * Math.sqrt(result)));
          pixels[y * w + x] = (int) Long.parseLong("FF" + outR + outR + outR, 16);
      }
      else if (gtype == GraphType.BOUNDARY) {
          float currVal = func.evaluate(cx, cy);
          float xVal = func.evaluate(cx + pixelSize, cy);
          float yVal = func.evaluate(cx, cy + pixelSize);

          // If any are NaN then the sum will be NaN
          if (Float.isNaN(currVal + xVal + yVal)) {
              return;
          }
          if ((currVal > 0) ^ (xVal > 0)) {
              pixels[y * w + x] = BLACK;
          }
          if ((currVal > 0) ^ (yVal > 0)) {
              pixels[y * w + x] = BLACK;
          }
      }
    }
}
