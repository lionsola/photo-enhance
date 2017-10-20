package filter;

import java.awt.image.BufferedImage;

public class SColor {
    int a;
    int r;
    int g;
    int b;

    public SColor() {}

    public SColor(int argb) {
        setARGB(argb);
    }

    public void setARGB(int argb) {
        a = (argb >> 24) & 0xff;
        r = (argb >> 16) & 0xff;
        g = (argb >> 8) & 0xff;
        b = argb & 0xff;
    }

    public void map(BufferedImage mapImage) {
        r = SColor.getRed(mapImage.getRGB(r,0));
        g = SColor.getGreen(mapImage.getRGB(g, 0));
        b = SColor.getBlue(mapImage.getRGB(b, 0));
    }

    public void multiply(float[] matrix) {

    }

    public void mix(SColor c, double ratio) {
        r = (int)Math.round(r + (c.r - r)*ratio);
        g = (int)Math.round(g + (c.g - g)*ratio);
        b = (int)Math.round(b + (c.b - b)*ratio);
    }

    public int getARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int combineRGB(int r, int g, int b) {
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    public static int getAlpha(int argb) {
        return (argb >> 24) & 0xff;
    }

    public static int getRed(int argb) {
        return (argb >> 16) & 0xff;
    }

    public static int getGreen(int argb) {
        return (argb >> 8) & 0xff;
    }

    public static int getBlue(int argb) {
        return argb & 0xff;
    }
}
