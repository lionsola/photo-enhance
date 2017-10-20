package filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rise extends WholeImageFilter {
    BufferedImage blackboard;
    BufferedImage overlay;
    BufferedImage map;

    public Rise() {
        blackboard = FileAdapter.loadImage("blackboard_1024.png");
        overlay = FileAdapter.loadImage("overlay_map.png");
        map = FileAdapter.loadImage("rise_map.png");
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        final int INC = map.getHeight()/3;
        System.out.println(INC);
        SColor input = new SColor();
        SColor bb = new SColor();

        BufferedImage scaledBB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) scaledBB.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(blackboard, 0, 0, width, height, null);
        g2.dispose();

        int[] outPixels = new int[inPixels.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                input.setARGB(inPixels[x + y*width]);
                bb.setARGB(scaledBB.getRGB(x, y));

                input.r = SColor.getRed(overlay.getRGB(bb.r,input.r));
                input.g = SColor.getGreen(overlay.getRGB(bb.g, input.g));
                input.b = SColor.getBlue(overlay.getRGB(bb.b, input.b));

                input.r = SColor.getRed(map.getRGB(input.r, 0));
                input.g = SColor.getGreen(map.getRGB(input.g, INC));
                input.b = SColor.getBlue(map.getRGB(input.b, INC+INC));

                outPixels[x + y*width] = input.getARGB();
            }
        }
        return outPixels;
    }
}
