package Graphics;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * RGBA represents a pixel with RGB (color) and Alpha (transparency).
 * It uses the standard 8-bit per channel precision.
 */

public class RGBA {
    public int r;
    public int g;
    public int b;
    public int a;

    /**
     * Converts byte number (0 to 255) into hexadecimal string.
     * @param i between 0 and 255
     * @return hexadecimal of input
     */
    public static String fmtHex255(int i) {
        // String.valueOf is necessary otherwise it treats char as numeric.
        i = max(0, min(255, i));
        return String.valueOf("0123456789ABCDEF".charAt(i>>4))
                + String.valueOf("0123456789ABCDEF".charAt(i&15));
    }

    /**
     * Construct a pixel from R,G,B components
     * @param r red (0 to 255)
     * @param g green (0 to 255)
     * @param b blue (0 to 255)
     */
    public RGBA(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construct a pixel from hex string
     * @param s hex value of all channels
     */
    public RGBA(String s) {
		long t = Long.parseLong(s, 16);
		this.r = (int)((t >> 16) & 255);
        this.g = (int)((t >> 8) & 255);
		this.b = (int)(t & 255);
	}

    /**
     * Construct pixel from bytes
     * @param t raw bytes of RGB channels
     */
    public RGBA(int t) {
        this.r = t >> 16 & 255;
        this.g = (t >> 8) & 255;
        this.b = t & 255;
    }

    public int toInt() {
		return (int)((255L << 24) + (this.r << 16) + (this.g << 8) + (this.b));
	}
    @Override
    public String toString() {
        return "FF" + fmtHex255(this.r) + fmtHex255(this.g) + fmtHex255(this.b);
    }

    /**
     * Blends 2 colors together
     * @param other other color
     * @param c proportion of other in the output
     * @return blended color
     */
    public RGBA blend(RGBA other, float c) {
        return new RGBA(
                (int) (this.r * (1 - c) + other.r * c),
                (int) (this.g * (1 - c) + other.g * c),
                (int) (this.b * (1 - c) + other.b * c)
        );
    }
}