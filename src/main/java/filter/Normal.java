package filter;

import java.awt.*;

public class Normal extends WholeImageFilter {
    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        return inPixels;
    }
}
