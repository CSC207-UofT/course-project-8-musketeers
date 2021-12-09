package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TextDrawer {
    private int[] pixels1;
    private int[] pixels2;
    private int[] pixels3;
    private int[] pixels4;
    private int[] pixels5;
    private int[] pixels6;
    private int[] pixels7;
    private int[] pixels8;
    private int[] pixels9;
    private int[] pixels0;


    public TextDrawer() throws IOException {
        BufferedImage numbers = ImageIO.read(new File("numbers.png"));
        int width = numbers.getWidth();
        int height = numbers.getHeight();
        int[] allNumPixels = numbers.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < width - 8; i += 8) {
            int[] pixels = Arrays.copyOfRange(allNumPixels, i, i + 80);
            switch (i) {
                case 0:
                    pixels1 = pixels;
                    break;
                case 1:
                    pixels2 = pixels;
                    break;
                case 2:
                    pixels3 = pixels;
                    break;
                case 3:
                    pixels4 = pixels;
                    break;
                case 4:
                    pixels5 = pixels;
                    break;
                case 5:
                    pixels6 = pixels;
                    break;
                case 6:
                    pixels7 = pixels;
                    break;
                case 7:
                    pixels8 = pixels;
                case 8:
                    pixels9 = pixels;
                    break;
                case 9:
                    pixels0 = pixels;
                    break;
            }
        }

    }

    public void writeNum(int startX, int startY, String number, int[] pixels) {
        switch (number) {
            case "0":
                for (int i = 0; i < pixels0.length; i++) {
                    pixels[startX * startY + i] = pixels0[i];
                }
                break;
            case "1":
                for (int i = 0; i < pixels1.length; i++) {
                    pixels[startX * startY + i] = pixels1[i];
                }
                break;
            case "2":
                for (int i = 0; i < pixels2.length; i++) {
                    pixels[startX * startY + i] = pixels2[i];
                }
                break;
            case "3":
                for (int i = 0; i < pixels3.length; i++) {
                    pixels[startX * startY + i] = pixels3[i];
                }
                break;
            case "4":
                for (int i = 0; i < pixels4.length; i++) {
                    pixels[startX * startY + i] = pixels4[i];
                }
                break;
            case "5":
                for (int i = 0; i < pixels5.length; i++) {
                    pixels[startX * startY + i] = pixels5[i];
                }
                break;
            case "6":
                for (int i = 0; i < pixels6.length; i++) {
                    pixels[startX * startY + i] = pixels6[i];
                }
                break;
            case "7":
                for (int i = 0; i < pixels7.length; i++) {
                    pixels[startX * startY + i] = pixels7[i];
                }
            case "8":
                for (int i = 0; i < pixels8.length; i++) {
                    pixels[startX * startY + i] = pixels8[i];
                }
                break;
            case "9":
                for (int i = 0; i < pixels9.length; i++) {
                    pixels[startX * startY + i] = pixels9[i];
                }
                break;
        }
    }
}
