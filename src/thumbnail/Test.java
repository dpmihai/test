package thumbnail;

import javax.imageio.ImageIO;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 15, 2006
 * Time: 5:59:47 PM
 */
public class Test {

//    public static Image createThumbnail(Image image) {
//        int sourceWidth = image.getWidth();
//        int sourceHeight = image.getHeight();
//
//        int thumbWidth = 64;
//        int thumbHeight = -1;
//
//        if (thumbHeight == -1)
//            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
//
//        Image thumb = Image.createImage(thumbWidth, thumbHeight);
//        Graphics g = thumb.getGraphics();
//
//        for (int y = 0; y < thumbHeight; y++) {
//            for (int x = 0; x < thumbWidth; x++) {
//                g.setClip(x, y, 1, 1);
//                int dx = x * sourceWidth / thumbWidth;
//                int dy = y * sourceHeight / thumbHeight;
//                g.drawImage(image, x - dx, y - dy,
//                        Graphics.LEFT | Graphics.TOP);
//            }
//        }
//
//        Image immutableThumb = Image.createImage(thumb);
//
//        return immutableThumb;
//    }
}
