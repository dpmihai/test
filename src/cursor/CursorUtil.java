package cursor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 5, 2006
 * Time: 1:50:01 PM
 */
public class CursorUtil {

    public static void setImageCursor(JComponent component, Image image) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null!");
        }
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null!");
        }
        Toolkit toolkit = component.getToolkit();
        Cursor c = toolkit.createCustomCursor(image, new Point(1, 1), "image-cursor");
        component.setCursor(c);
    }

    public static void setDefaultCursor(JComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null!");
        }
        Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        component.setCursor(c);
    }

    public static void setWaitCursor(JComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null!");
        }
        Cursor c = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        component.setCursor(c);
    }

    public static void hideCursor(JComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null!");
        }
        int[] pixels = new int[16 * 16];
        Toolkit toolkit = component.getToolkit();
        Image image = toolkit.createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor = toolkit.createCustomCursor(image, new Point(0, 0), "invisiblecursor");
        component.setCursor(transparentCursor);
    }
}
