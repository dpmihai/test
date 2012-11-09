package vista;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 1:52:41 PM
 */
public class VistaUtil {

    public static final Color TEXT_FOREGROUND = Color.BLUE.darker();
    public static final Color BACKGROUND = new Color(185, 208, 228);

    public static boolean isHtml(String text) {
        return (text != null) && text.toLowerCase().startsWith("<html>");
    }
}
