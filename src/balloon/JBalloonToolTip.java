package balloon;

import java.awt.*;
import javax.swing.*;

/*
 * @(#)JBalloonToolTip.java         1.00
 *
 * User: sv
 * Date: Oct 3, 2004
 * Time: 10:23:39 PM
 *
 * Copyright 2004 jballoon.net.  All rights reserved.
 */


/**
 * The  <code>JBalloonToolTip</code>
 *
 *
 *
 *
 * @author  Serge S. Vasiljev
 * @version 1.00, Oct 3, 2004
 */
public class JBalloonToolTip extends JToolTip {

    private static boolean balloonDefault = true;

    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), 6);

        g.translate(0, 5);
        g.setClip(0, 1, getWidth(), getHeight() - 6);
        super.paint(g);
        g.setClip(0, -5, getWidth(), getHeight());

        if (getBorder().getClass().getName().startsWith("com.intellij.ui")) {
            // IDEA specific borders
            getBorder().paintBorder(this, g, 0, -5, getWidth(), getHeight() - 5);
        } else {
            getBorder().paintBorder(this, g, 0, -5, getWidth(), getHeight());
        }
    }

    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, d.height + 10);
    }

    public static boolean isBalloonDefault() {
        return JBalloonToolTip.balloonDefault;
    }

    public static void setBalloonDefault(boolean balloonDefault) {
        JBalloonToolTip.balloonDefault = balloonDefault;
    }


    public static JToolTip createToolTip(JComponent c) {
        JToolTip tip = new JBalloonToolTip();
        tip.setComponent(c);
        return tip;
    }
}
