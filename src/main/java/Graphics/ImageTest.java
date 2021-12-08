package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageTest {
    public ImageTest() {

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

        return pixels;
    }

    public static void writeImage(int[] pixels, int iw, int ih, String fname) throws Exception {
        BufferedImage outImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);

        outImage.setRGB(0, 0, iw, ih, pixels, 0, iw);

        ImageIO.write(outImage, "png", new File(fname));
    }
}