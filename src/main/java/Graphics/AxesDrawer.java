package Graphics;


public class AxesDrawer {

    public int[] drawAxes(int[] pixels, int w, int h, float[] graphData) {

	  float scale = graphData[0];
      float xpos = graphData[1];
      float ypos = graphData[2];

      int xOrigin = (int)((xpos / scale + 0.5f) * w);
	  int yOrigin = (int)((-ypos / scale + 0.5f) * h);

      RGBA xColor = new RGBA("FFFF0000");
      RGBA yColor = new RGBA("FF0044FF");

      // Y axis
      for (int y = 0; y < h; y++) {
        pixels[y * w + xOrigin] = yColor.blend(new RGBA(pixels[y * w + xOrigin]), 0.6f).toInt();
      }
      // X axis
      for (int x = 0; x < w; x++) {
        pixels[yOrigin * w + x] = xColor.blend(new RGBA(pixels[yOrigin * w + x]), 0.6f).toInt();
      }
      return pixels;
  }

  public int[] drawGrid(int[] pixels, int w, int h, float[] graphData) {
      RGBA gridColor = new RGBA("FF888888");
      float spacing = 0.5f;

      float scale = graphData[0];
      float xpos = graphData[1];
      float ypos = graphData[2];

      // vertical lines
      float xLeft = (xpos - scale/2);
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
      float yLeft = (ypos - scale/2);
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