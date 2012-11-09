package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * User: mihai.panaitescu
 * Date: 14-Jan-2010
 * Time: 15:18:11
 */
public class ImageColor {


    // get the color from a pixel
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\Public\\next-reports\\src\\images\\splash.png");
        BufferedImage image = ImageIO.read(file);

        System.out.println("width="+image.getWidth());
        System.out.println("height="+image.getHeight());
        int c = image.getRGB(image.getWidth()-1,image.getHeight()-1);
        int  red = (c & 0x00ff0000) >> 16;
        int  green = (c & 0x0000ff00) >> 8;
        int  blue = c & 0x000000ff;

        Color color = new Color(red,green,blue);
        System.out.println("color="+color);

    }
}
