package menu;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 28, 2006
 * Time: 2:56:53 PM
 * To change this template use File | Settings | File Templates.
 */
class JMenuExpandIcon implements Icon {

    private final static int size = 16;
    private int maxWidth;

    public JMenuExpandIcon(int maxWidth) {
        super();
        // all menu items invisible or too small
        if (maxWidth <= size+20) {
           maxWidth = size +20;
        }
        this.maxWidth = maxWidth;
    }

    public void paintIcon(Component c, Graphics gr, int x, int y) {

        Graphics2D g = (Graphics2D) gr;
        int unitW = size/4;
        int unitH = size/4;
        int h = size/3;
        int w = size/3;
        int dH = size/3;
        int dW = (maxWidth-size)/2; // center icon horizontally on the menu item

        Stroke oldS = g.getStroke();
        g.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);
        g.drawLine(unitW+dW, unitH, unitW+dW + w, unitH + h);
        g.drawLine(unitW+dW, unitH + dH, unitW+dW + w, unitH + h + dH);
        g.drawLine(unitW+dW + w, unitH + h, unitW+dW + 2 * w, unitH);
        g.drawLine(unitW+dW + w, unitH + h + dH, unitW+dW + 2 * w, unitH + dH);

        g.setColor(Color.WHITE);
        g.setStroke(oldS);
        g.drawLine(unitW+dW-1, unitH, unitW+dW + w -1, unitH + h);
        g.drawLine(unitW+dW-1, unitH + dH, unitW+dW + w-1, unitH + h + dH);
        g.drawLine(unitW+dW + w+1, unitH + h, unitW+dW + 2 * w+1, unitH);
        g.drawLine(unitW+dW + w+1, unitH + h + dH, unitW+dW + 2 * w+1, unitH + dH);
    }

    public int getIconWidth() {
        return size;
    }

    public int getIconHeight() {
        return size;
    }
}
