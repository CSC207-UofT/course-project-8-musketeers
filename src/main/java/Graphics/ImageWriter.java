package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    /** Saves a pixel array as an image
     * @param pixels Array of integers corresponding to the colour of each pixel
     * @param iw Width of image (in pixels)
     * @param ih Height of the image (in pixel)
     * @param fname Name of file to which we save the image
     * @throws IOException If there is an error with saving image
     */
    public void writeImage(int[] pixels, int iw, int ih, String fname) throws IOException {
        BufferedImage outImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);

        outImage.setRGB(0, 0, iw, ih, pixels, 0, iw);

        ImageIO.write(outImage, "png", new File(fname));
    }

}
