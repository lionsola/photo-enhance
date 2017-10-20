package filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class FileAdapter {
    public static BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(name));
        } catch (Exception e) {
            System.out.println("Error while loading image " + name);
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    public static void saveImage(BufferedImage image, String name, String format) {
        try {
            ImageIO.write(image, format, new File(name));
        } catch (Exception e) {
            System.err.println("Error while saving image to " + name);
            e.printStackTrace();
        }
    }
}
