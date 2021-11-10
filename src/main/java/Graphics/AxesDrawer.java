package Graphics;

import static Graphics.ImageTest.*;
import static Graphics.ImplicitGrapher.*;

import Backend.Expressions.Expression;
import Backend.Axes;
import Backend.ExpressionReader;

public class AxesDrawer {

   public static void main(String args[]) throws Exception {
     int size = 256;
     int[] mainPixels = new int[size*size];
     int[] dims1 = {size,size};

     float scale = 4.5f;
     float xpos = -0.f;
     float ypos = 0.f;

     Axes axes = new Axes();
     axes.setScale(scale);
     float[] pos = {xpos, ypos};
     axes.setOrigin(pos);
     ExpressionReader er = new ExpressionReader();

     Expression func = er.read("mandel ( x , y )");
     axes.addExpression(func);

     graphImplicit(mainPixels, dims1[0], dims1[1], axes, false);
     drawAxes(mainPixels, dims1[0], dims1[1], scale, xpos, ypos);
     drawGrid(mainPixels, dims1[0], dims1[1], scale, xpos, ypos);
     writeImage(mainPixels, dims1[0], dims1[1], "sampleOutMandelAxes.png");
   }

  public static void drawAxes(int[] pixels, int w, int h,
                              float scale, float xpos, float ypos) {

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
  }
  public static void drawGrid(int[] pixels, int w, int h,
                              float scale, float xpos, float ypos) {
      RGBA gridColor = new RGBA("FF888888");
      float spacing = 0.5f;

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
  }
}