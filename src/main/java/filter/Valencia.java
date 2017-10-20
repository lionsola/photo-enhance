package filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Valencia extends WholeImageFilter {
    BufferedImage valenciaGradientMap;
    BufferedImage valenciaMap;

    private static final float[] sat = {1.1402f, -0.0598f, -0.061f, -0.1174f, 1.0826f, -0.1186f, -0.0228f, -0.0228f, 1.1772f};
    private static final float[] luma = {.3f, .59f, .11f};

    public Valencia() {
        valenciaGradientMap = FileAdapter.loadImage("valencia_gradient_map.png");
        valenciaMap = FileAdapter.loadImage("valencia_map.png");
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        SColor texel = new SColor();
        SColor temp = new SColor();
        int lumi;
        double value;

        int[] outPixels = new int[inPixels.length];

        int d;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                texel.setARGB(inPixels[x + y*width]);

                temp.r = SColor.getRed(valenciaMap.getRGB(texel.r, 0));
                temp.g = SColor.getGreen(valenciaMap.getRGB(texel.g, 1));
                temp.b = SColor.getBlue(valenciaMap.getRGB(texel.b, 2));

                texel.r = PixelUtils.clamp(Math.round(sat[0]*temp.r + sat[3]*temp.g + sat[6]*temp.b));
                texel.g = PixelUtils.clamp(Math.round(sat[1]*temp.r + sat[4]*temp.g + sat[7]*temp.b));
                texel.b = PixelUtils.clamp(Math.round(sat[2]*temp.r + sat[5]*temp.g + sat[8]*temp.b));

                lumi = Math.round(luma[0]*texel.r + luma[1]*texel.g + luma[2]*texel.b);

                texel.r = SColor.getRed(valenciaGradientMap.getRGB(lumi, texel.r));
                texel.g = SColor.getGreen(valenciaGradientMap.getRGB(lumi, texel.g));
                texel.b = SColor.getBlue(valenciaGradientMap.getRGB(lumi, texel.b));

                outPixels[x + y*width] = texel.getARGB();
            }
        }
        return outPixels;
    }
}
