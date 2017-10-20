package filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Earlybird extends WholeImageFilter {
    BufferedImage earlybirdCurves;
    BufferedImage earlybirdOverlayMap;
    BufferedImage vignetteMap;
    BufferedImage earlybirdBlowout;
    BufferedImage earlybirdMap;
    private static final float[] sat = {1.210300f, -0.089700f, -0.091000f, -0.176100f, 1.123900f, -0.177400f, -0.034200f, -0.034200f, 1.268400f};
    private static final float[] rgbPrime = {0.25098f, 0.14640522f, 0.0f};
    private static final float[] desat = {.3f, .59f, .11f};

    public Earlybird() {
        earlybirdCurves = FileAdapter.loadImage("early_bird_curves.png");
        earlybirdOverlayMap = FileAdapter.loadImage("earlybird_overlay_map.png");
        vignetteMap = FileAdapter.loadImage("vignette_map.png");
        earlybirdBlowout = FileAdapter.loadImage("earlybird_blowout.png");
        earlybirdMap = FileAdapter.loadImage("earlybird_map.png");
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        System.out.println("filterPixels: start");
        long tick = System.currentTimeMillis();

        ExecutorService executor = Executors.newWorkStealingPool();
        AtomicInteger ai = new AtomicInteger();

        int[] outPixels = new int[inPixels.length];

        for (int y0 = 0; y0 < height; y0++) {
            for (int x0 = 0; x0 < width; x0++) {
                final int x = x0, y = y0;
                executor.submit(() -> {
                    SColor texel = new SColor();
                    SColor temp = new SColor();

                    texel.setARGB(inPixels[x + y * width]);

                    texel.map(earlybirdCurves);

                    int lumi = Math.round(desat[0] * texel.r + desat[1] * texel.g + desat[2] * texel.b);
                    temp.setARGB(earlybirdOverlayMap.getRGB(lumi, 0));

                    temp.mix(texel, 0.5f);

                    texel.r = PixelUtils.clamp(Math.round(sat[0] * temp.r + sat[3] * temp.g + sat[6] * temp.b));
                    texel.g = PixelUtils.clamp(Math.round(sat[1] * temp.r + sat[4] * temp.g + sat[7] * temp.b));
                    texel.b = PixelUtils.clamp(Math.round(sat[2] * temp.r + sat[5] * temp.g + sat[8] * temp.b));

                    double value = Math.pow(2.0 * x / width - 1, 2) + Math.pow(2.0 * y / height - 1, 2);
                    int d = (int) Math.round(PixelUtils.clamp(value, 0, 1)
                            * 254);
                    try {
                        texel.r = SColor.getRed(vignetteMap.getRGB(d, texel.r));
                        texel.g = SColor.getGreen(vignetteMap.getRGB(d, texel.g));
                        texel.b = SColor.getBlue(vignetteMap.getRGB(d, texel.b));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }


                    value = PixelUtils.smoothstep(0, 1.25f, Math.pow(d, 1.35) / 1.65f);

                    temp.r = SColor.getRed(earlybirdBlowout.getRGB(texel.r, 0));
                    temp.g = SColor.getGreen(earlybirdBlowout.getRGB(texel.g, 0));
                    temp.b = SColor.getBlue(earlybirdBlowout.getRGB(texel.b, 0));

                    texel.mix(temp, value);

                    texel.map(earlybirdMap);

                    outPixels[x + y * width] = texel.getARGB();
                    ai.incrementAndGet();
                });
            }
        }
        while (ai.intValue() < inPixels.length) {
            try {
                Thread.sleep(10);
                System.out.println("sleeping...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("filterPixels: finished in " + (System.currentTimeMillis()-tick) + "ms");
        return outPixels;
    }
}
