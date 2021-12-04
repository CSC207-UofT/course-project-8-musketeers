package Graphics;

/**
 * AxesDrawer is responsible for drawing the Axes and the gridlines
 */
public class AxesDrawer {

    /**
     * drawAxes draws Axes
     * @param pixels  array of ints representing pixels in image
     * @param w  width of image (in pixels)
     * @param h  height of image (in pixels)
     * @param graphData  array of floats that contains relevant information for our image
     *                   scale, x-position of origin, y-position of origin
     * @return an array of ints, whose individual elements represent the pixels of our image after
     *                   the axes have been drawn.
     */
    public int[] drawAxes(int[] pixels, int w, int h, float[] graphData) {

	  float scale = graphData[0];
      float xpos = graphData[1];
      float ypos = graphData[2];

      int xOrigin = (int)((-xpos / scale + 0.5f) * w);
	  int yOrigin = (int)((ypos / scale + 0.5f) * h);

      RGBA xColor = new RGBA("FFFF0000");
      RGBA yColor = new RGBA("FF0044FF");

      if ((xOrigin > 0) && (xOrigin < w)) {
        // Y axis
        for (int y = 0; y < h; y++) {
            pixels[y * w + xOrigin] = yColor.blend(new RGBA(pixels[y * w + xOrigin]), 0.6f).toInt();
        }
      }
      if ((yOrigin > 0) && (yOrigin < h)) {
        // X axis
        for (int x = 0; x < w; x++) {
            pixels[yOrigin * w + x] = xColor.blend(new RGBA(pixels[yOrigin * w + x]), 0.6f).toInt();
        }
      }
      return pixels;
  }

    /**
     * Draws gridlines in our image
     * @param pixels  array of integers representing pixels in image
     * @param w  width of image (in pixels)
     * @param h  height of image (in pixels)
     * @param graphData  array of floats that contains relevant information for our image
     *                   scale, x-position of origin, y-position of origin
     * @return an array of ints, whose individual elements represent the pixels of our image after
     *                   the gridlines have been drawn.
     */
  public int[] drawGrid(int[] pixels, int w, int h, float[] graphData) {
      RGBA gridColor = new RGBA("FF888888");

      // How far apart the gridlines are
      float spacing = 0.5f;

      float scale = graphData[0];
      float xpos = graphData[1];
      float ypos = graphData[2];

      // vertical lines
      float xLeft = (xpos - scale/2);
      // round to the nearest spacing
      xLeft = (float)Math.ceil(xLeft / spacing) * spacing;
      float xRight = (xpos + scale/2);
      for (float xc = xLeft; xc < xRight; xc += spacing) {
          if (xc == 0) continue;
          int xp = (int)((xc - xpos) / scale * w + w/2);
          for (int y = 0; y < h; y++) {
              pixels[y * w + xp] = gridColor.blend(new RGBA(pixels[y * w + xp]), 0.8f).toInt();
          }
      }

      // horizontal lines
      ypos = -ypos;
      float yLeft = (ypos - scale/2);
      // round to nearest spacing
      yLeft = (float)Math.ceil(yLeft / spacing) * spacing;
      float yRight = (ypos + scale/2);
      for (float yc = yLeft; yc < yRight; yc += spacing) {
          if (yc == 0) continue;
          int yp = (int)((yc - ypos) / scale * h + h/2);
          for (int x = 0; x < w; x++) {
              pixels[yp * w + x] = gridColor.blend(new RGBA(pixels[yp * w + x]), 0.8f).toInt();
          }
      }
      return pixels;
  }
}