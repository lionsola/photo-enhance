package filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Amaro extends WholeImageFilter {
    final BufferedImage blackboard;
    final BufferedImage overlay;
    final BufferedImage map;

    public Amaro() {
        blackboard = FileAdapter.loadImage("blackboard_1024.png");
        overlay = FileAdapter.loadImage("overlay_map.png");
        map = FileAdapter.loadImage("amaro_map.png");
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        System.out.println("filterPixels: start");
        long tick = System.currentTimeMillis();
        final int INC = map.getHeight()/3;
        //
        BufferedImage scaledBB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) scaledBB.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(blackboard, 0, 0, width, height, null);
        g2.dispose();

        ExecutorService executor = Executors.newWorkStealingPool();
        AtomicInteger ai = new AtomicInteger();

        int[] outPixels = new int[inPixels.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int x0 = x, y0 = y;
                executor.submit(() -> {
                    SColor input1 = new SColor();
                    SColor bb1 = new SColor();
                    input1.setARGB(inPixels[x0 + y0*width]);
                    bb1.setARGB(scaledBB.getRGB(x0, y0));

                    input1.r = SColor.getRed(overlay.getRGB(bb1.r, input1.r));
                    input1.g = SColor.getGreen(overlay.getRGB(bb1.g, input1.g));
                    input1.b = SColor.getBlue(overlay.getRGB(bb1.b, input1.b));

                    input1.r = SColor.getRed(map.getRGB(input1.r, 0));
                    input1.g = SColor.getGreen(map.getRGB(input1.g, INC));
                    input1.b = SColor.getBlue(map.getRGB(input1.b, INC+INC));

                    outPixels[x0 + y0*width] = input1.getARGB();
                    ai.incrementAndGet();
                });
                /*
                input.setARGB(inPixels[x + y*width]);
                bb.setARGB(scaledBB.getRGB(x, y));

                input.r = SColor.getRed(overlay.getRGB(bb.r,input.r));
                input.g = SColor.getGreen(overlay.getRGB(bb.g, input.g));
                input.b = SColor.getBlue(overlay.getRGB(bb.b, input.b));

                input.r = SColor.getRed(map.getRGB(input.r, 0));
                input.g = SColor.getGreen(map.getRGB(input.g, INC));
                input.b = SColor.getBlue(map.getRGB(input.b, INC+INC));

                outPixels[x + y*width] = input.getARGB();
                */
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
