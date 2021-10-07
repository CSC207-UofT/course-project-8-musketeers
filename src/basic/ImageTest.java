package basic;

import java.awt.image.BufferedImage;

import java.io.File;
import javax.imageio.ImageIO;

public class ImageTest {
   public ImageTest() {

   }
   public static void main(String args[]) throws Exception {
      int[] mainPixels = readImage("sample.png");
      int[] dims1 = getImDims("sample.png");
      mainPixels[5] = (int)Long.parseLong("FFFF00FF", 16);
      mainPixels[6] = (int)Long.parseLong("FF00FFFF", 16);
      writeImage(mainPixels, dims1[0], dims1[1], "sampleOut.png");
   }

   public static int[] getImDims(String fname) throws Exception {
      BufferedImage bImage = ImageIO.read(new File(fname));
      int[] dims = {bImage.getWidth(), bImage.getHeight()};
      return dims;
   }

   public static int[] readImage(String fname) throws Exception {
      BufferedImage bImage = ImageIO.read(new File(fname));

      int width = bImage.getWidth();
      int height = bImage.getHeight();
      int[] pixels = bImage.getRGB(0, 0, width, height, null, 0, width);

      // ARGB
      //System.out.println(Integer.toHexString(pixels[0]));

      return pixels;
   }
   public static void writeImage(int[] pixels, int iw, int ih, String fname) throws Exception {
      BufferedImage outImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);

      outImage.setRGB(0, 0, iw, ih, pixels, 0, iw);
      ImageIO.write(outImage, "png", new File(fname));
   }
}