package zoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * User: mihai.panaitescu
 * Date: 02-Nov-2009
 * Time: 09:42:18
 */
class ScalablePanel extends JScrollPane implements MouseWheelListener {
    final double ZOOM_IN_FACTOR = 1.1;
    final double ZOOM_OUT_FACTOR = 0.9;
    private int counter = 0;

    private JPanel zoomPanel;


    public ScalablePanel(JPanel zoomPanel) {
        this.zoomPanel = zoomPanel;
        getViewport().setView(zoomPanel);
        setPreferredSize(new Dimension(300, 300));
        addMouseWheelListener(this);
    }

    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                if (counter <= 0) {
                    counter = 0;
                } else {
                    zoomOut(e);
                    counter += e.getWheelRotation();
                }
            } else {
                zoomIn(e);
                counter += e.getWheelRotation();
            }
            e.consume();
        }
    }

    private void scaleFonts(MouseWheelEvent me, Component[] components) {
        int clicks = me.getWheelRotation();
        for (Component c : components) {
            if (c instanceof JPanel) {
                scaleFonts(me, ((JPanel)c).getComponents());
            }
            Font f = c.getFont();
            f = f.deriveFont(f.getSize2D() + 2 * clicks);
            c.setFont(f);
            c.invalidate();
        }
        repaint();
    }


    public void zoomIn(final MouseWheelEvent e) {

        scaleFonts(e, zoomPanel.getComponents());

        // Get the mouse position with respect to the zoomPanel
        final Point pointOnZoomPanel = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), zoomPanel);

        // Resize panel
        final Dimension currSize = zoomPanel.getSize();
        zoomPanel.setPreferredSize(new Dimension(
                (int) (currSize.width * ZOOM_IN_FACTOR),
                (int) (currSize.height * ZOOM_IN_FACTOR)));

        // Find out where our point on the zoom panel is now that we've resized it
        final Point newViewPos = new Point();

        newViewPos.x = (int) ((ZOOM_IN_FACTOR * currSize.width) * (e.getPoint().x / (double) currSize.width));
        newViewPos.y = (int) ((ZOOM_IN_FACTOR * currSize.height) * (e.getPoint().y / (double) currSize.height));

//        newViewPos.x = (int) (ZOOM_IN_FACTOR * pointOnZoomPanel.x - e.getPoint().x);
//        newViewPos.y = (int) (ZOOM_IN_FACTOR * pointOnZoomPanel.y - e.getPoint().y);
        System.out.println("newViewPos=" + newViewPos);

        // Move the viewport to the new position to keep the area our mouse was in the same spot
        getViewport().setViewPosition(newViewPos);

        zoomPanel.revalidate();
    }

    public void zoomOut(final MouseWheelEvent e) {

        scaleFonts(e, zoomPanel.getComponents());

        // Get the mouse position with respect to the zoomPanel
        final Point pointOnZoomPanel = SwingUtilities.convertPoint(
                e.getComponent(), e.getPoint(), zoomPanel);

        // Resize panel
        final Dimension currSize = zoomPanel.getSize();
        zoomPanel.setPreferredSize(
                new Dimension(
                        (int) (currSize.width * ZOOM_OUT_FACTOR),
                        (int) (currSize.height * ZOOM_OUT_FACTOR)));

        // Find out where our point on the zoom panel is now that we've resized it
        final Point newViewPos = new Point();

        newViewPos.x = (int) ((ZOOM_OUT_FACTOR * currSize.width) * (e.getPoint().x / (double) currSize.width));
        newViewPos.y = (int) ((ZOOM_OUT_FACTOR * currSize.height) * (e.getPoint().y / (double) currSize.height));

//        newViewPos.x = (int) (ZOOM_OUT_FACTOR * pointOnZoomPanel.x - e.getPoint().x);
//        newViewPos.y = (int) (ZOOM_OUT_FACTOR * pointOnZoomPanel.y - e.getPoint().y);

        System.out.println("newViewPos=" + newViewPos);

        // Move the viewport to the new position to keep the area our mouse was in the same spot
        getViewport().setViewPosition(newViewPos);

        zoomPanel.revalidate();
    }
}
