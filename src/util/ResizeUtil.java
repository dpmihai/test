package util;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Mar 15, 2005 Time: 3:34:32 PM To change this template use File
 * | Settings | File Templates.
 */
public class ResizeUtil {

    /**
         * Centers the component <CODE>c</CODE> on the screen.
         *
         * @param  c  the component to center
         * @see  #centerComponent(java.awt.Component, java.awt.Component)
         */
        public static void centerComponent(Component c) {
            centerComponent(c, null);
        }

        /**
         * Centers the component <CODE>c</CODE> on component <CODE>p</CODE>.
         * If <CODE>p</CODE> is <CODE>null</CODE>, the component <CODE>c</CODE>
         * will be centered on the screen.
         * If width / height of the parent is less than width / height of the component
         * then the component will be just indented from the parent with an amount of pixels.
         *
         * @param  c  the component to center
         * @param  p  the parent component to center on or null for screen
         * @see  #centerComponent(Component)
         */
        public static void centerComponent(Component c, Component p) {
            if(c == null) {
                return;
            }
            Dimension d = (p != null ? p.getSize() :
                Toolkit.getDefaultToolkit().getScreenSize()
            );

            int pWidth = d.getSize().width;
            int pHeight = d.getSize().height;
            int cWidth = c.getSize().width;
            int cHeight = c.getSize().height;

            int x, y;
            if (pWidth >= cWidth) {
                x = pWidth + pWidth/2 - cWidth/2;
            } else {
                x = pWidth + 30;
            }

            if (pHeight >= cHeight) {
                y = pHeight + pHeight/2 - cHeight/2;
            } else {
                y = pHeight + 30;
            }

            c.setLocation(x, y);

        }

    

    /**
     * Sets the JButtons inside a JPanelto be the same size.
     * This is done dynamically by setting each button's preferred and maximum
     * sizes after the buttons are created. This way, the layout automatically
     * adjusts to the locale-specific strings.
     * @param jPanelButtons JPanel containing buttons
     */
    public static void equalizeButtonSizes(JPanel jPanelButtons) {
        ArrayList lbuttons = new ArrayList();
        for (int i = 0; i < jPanelButtons.getComponentCount(); i++) {
            Component c = jPanelButtons.getComponent(i);
            if (c instanceof JButton) {
                lbuttons.add(c);
            }
        }

        // Get the largest width and height
        Dimension maxSize = new Dimension(0, 0);
        for (int i = 0; i < lbuttons.size(); i++) {
            Dimension d = ((JButton) lbuttons.get(i)).getPreferredSize();
            maxSize.width = Math.max(maxSize.width, d.width);
            maxSize.height = Math.max(maxSize.height, d.height);
        }

        for (int i = 0; i < lbuttons.size(); i++) {
            JButton btn = (JButton) lbuttons.get(i);
            btn.setPreferredSize(maxSize);
            btn.setMinimumSize(maxSize);
            btn.setMaximumSize(maxSize);
        }
    }

}
