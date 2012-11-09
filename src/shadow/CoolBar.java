package shadow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2006
 * Time: 2:53:32 PM
 */

public class CoolBar extends JToolBar {

    public CoolBar() {
        setBorder(new DropShadowBorder(/* bottom only */));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int bottom = getHeight() - getInsets().bottom;
        GradientPaint gp =
                new GradientPaint(0, 0, Color.GRAY, 0, bottom, Color.WHITE);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), bottom);
        g2.setColor(Color.BLUE);
        g2.drawLine(0, bottom - 1, getWidth(), bottom - 1);
        g2.dispose();
    }
}