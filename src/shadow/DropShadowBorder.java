package shadow;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2006
 * Time: 2:51:54 PM
 * To change this template use File | Settings | File Templates.
 */


import java.awt.*;
import javax.swing.border.*;

/**
 * A drop shadow border. Draws a 1 pixel line completely around the component,
 * and a drop shadow effect on the right and bottom sides.
 *
 * @author Dale Anson, 25 Feb 2004
 * @version $Revision: 1.3 $
 */
public class DropShadowBorder extends AbstractBorder {

// the width in pixels of the drop shadow
    private int _width = 3;

// the color of the drop shadow
    private Color _color = Color.BLACK;

    /**
     * Drop shadow with default width of 3 pixels and black color.
     */
    public DropShadowBorder() {
        this(3);
    }

    /**
     * Drop shadow, default shadow color is black.
     *
     * @param width the width of the shadow.
     */
    public DropShadowBorder(int width) {
        this(width, Color.BLACK);
    }

    /**
     * Drop shadow, width and color are adjustable.
     *
     * @param width the width of the shadow.
     * @param color the color of the shadow.
     */
    public DropShadowBorder(int width, Color color) {
        _width = width;
        _color = color;
    }

    /**
     * This implementation returns a new Insets instance where the top and left are 1,
     * the bottom and right fields are the border width + 1.
     *
     * @param c the component for which this border insets value applies
     * @return a new Insets object initialized as stated above.
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, _width + 1, _width + 1);
    }

    /**
     * Reinitializes the <code>insets</code> parameter with this DropShadowBorder's
     * current Insets.
     *
     * @param c      the component for which this border insets value applies
     * @param insets the object to be reinitialized
     * @return the given <code>insets</code> object
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 1;
        insets.left = 1;
        insets.bottom = _width + 1;
        insets.right = _width + 1;
        return insets;
    }

    /**
     * This implementation always returns true.
     *
     * @return true
     */
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * Paints the drop shadow border around the given component.
     *
     * @param c      - the component for which this border is being painted
     * @param g      - the paint graphics
     * @param x      - the x position of the painted border
     * @param y      - the y position of the painted border
     * @param width  - the width of the painted border
     * @param height - the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color old_color = g.getColor();
        int x1, y1, x2, y2;
        g.setColor(_color);

        // outline the component with a 1-pixel wide line
        g.drawRect(x, y, width - _width - 1, height - _width - 1);

        // draw the drop shadow
        for (int i = 0; i <= _width; i++) {
            // bottom shadow
            x1 = x + _width;
            y1 = y + height - i;
            x2 = x + width;
            y2 = y1;
            g.drawLine(x1, y1, x2, y2);

            // right shadow
            x1 = x + width - _width + i;
            y1 = y + _width;
            x2 = x1;
            y2 = y + height;
            g.drawLine(x1, y1, x2, y2);
        }

        // fill in the corner rectangles with the background color of the parent
        // container
        if (c.getParent() != null) {
            g.setColor(c.getParent().getBackground());
            for (int i = 0; i <= _width; i++) {
                x1 = x;
                y1 = y + height - i;
                x2 = x + _width;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
                x1 = x + width - _width;
                y1 = y + i;
                x2 = x + width;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }
            // add some slightly darker colored triangles
            g.setColor(g.getColor().darker());
            for (int i = 0; i < _width; i++) {
                // bottom left triangle
                x1 = x + i + 1;
                y1 = y + height - _width + i;
                x2 = x + _width;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);

                // top right triangle
                x1 = x + width - _width;
                y1 = y + i + 1;
                x2 = x1 + i;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.setColor(old_color);
    }
}


