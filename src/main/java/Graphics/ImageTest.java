package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageTest {
    public ImageTest() {

    }

    public static int[] getImDims(String fname) throws Exception {
        BufferedImage bImage = ImageIO.read(new File(fname));
        return new int[]{bImage.getWidth(), bImage.getHeight()};
    }

    public static int[] readImage(String fname) throws Exception {
        BufferedImage bImage = ImageIO.read(new File(fname));

        int width = bImage.getWidth();
        int height = bImage.getHeight();

        return bImage.getRGB(0, 0, width, height, null, 0, width);
    }

}