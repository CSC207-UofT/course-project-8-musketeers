package Graphics;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class RGBA {
    public int r;
    public int g;
    public int b;
    public int a;

    public static void main(String[] args) {
        RGBA color = new RGBA(100, 255, 40);
        //System.out.println(color);
        //System.out.println(color.toInt());

        RGBA gray = new RGBA("FF888888");
        RGBA blend = gray.blend(color, 0.5f);
        System.out.println(blend);
        System.out.println("test " + blend.r + " " + blend.g + " " + blend.b);
        System.out.println(blend.toInt());
    }

    public static String fmtHex255(int i) {
        // String.valueOf is necessary otherwise it treats char as numeric.
        i = max(0, min(255, i));
        return String.valueOf("0123456789ABCDEF".charAt(i>>4))
                + String.valueOf("0123456789ABCDEF".charAt(i&15));
    }

    public RGBA(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public RGBA(String s) {
		long t = Long.parseLong(s, 16);
		this.r = (int)((t >> 16) & 255);
        System.out.println(this.r);
		this.g = (int)((t >> 8) & 255);
		this.b = (int)(t & 255);
	}
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
    public RGBA blend(RGBA other, float c) {
        return new RGBA(
                (int) (this.r * (1 - c) + other.r * c),
                (int) (this.g * (1 - c) + other.g * c),
                (int) (this.b * (1 - c) + other.b * c)
        );
    }
}