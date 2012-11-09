package tabbedpanel3;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 7, 2008
 * Time: 12:02:07 PM
 */
public class CloseIcon implements Icon {
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.RED);
        g.drawLine(6, 6, getIconWidth() - 7, getIconHeight() - 7);
        g.drawLine(getIconWidth() - 7, 6, 6, getIconHeight() - 7);
    }

    public int getIconWidth() {
        return 17;
    }

    public int getIconHeight() {
        return 17;
    }
}
