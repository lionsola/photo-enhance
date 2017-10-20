import filter.FileAdapter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TestImageGenerator {
    public static void main(String[] args) {
        BufferedImage scaledBB = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = scaledBB.createGraphics();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int i = y*16 + x;
                g2D.setColor(new Color(i,i,i,255));
                g2D.fillRect(x*16,y*16,16,16);
            }
        }
        g2D.dispose();
        FileAdapter.saveImage(scaledBB, "insta_input.png","png");
        
    }
}
