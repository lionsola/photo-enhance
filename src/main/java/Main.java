import filter.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("main: start");
        long tick = System.currentTimeMillis();
        if (args.length < 3) {
            System.err.println("Wrong arguments! Format: input_path output_path filter_ID (scale ratio)");
            System.exit(-1);
        }
        BufferedImage input = null;
        try {
            input = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.err.println("Error while loading input file");
            e.printStackTrace();
            System.exit(-1);
        }

        if (args.length > 3) {
            input = PixelUtils.resize(input, Float.valueOf(args[3]));
        }

        WholeImageFilter filter = null;

        switch (Integer.valueOf(args[2])) {
            case -1:
                filter = new Normal();
                break;
            case 0:
                filter = new Amaro();
                break;
            case 1:
                filter = new Earlybird();
                break;
            case 2:
                filter = new Rise();
                break;
            case 3:
                filter = new Valencia();
                break;
            default:
                System.err.println("Undefined filter ID!!!");
                System.exit(-1);
        }


        BufferedImage out = filter.filter(input, null);
        FileAdapter.saveImage(out, args[1], args[0].substring(args[0].lastIndexOf(".") + 1));

        System.out.println("main: finished in " + (System.currentTimeMillis()-tick) + "ms");
        System.out.println(0);
    }
}
