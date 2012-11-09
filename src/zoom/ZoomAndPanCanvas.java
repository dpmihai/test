package zoom;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 21-Aug-2009
// Time: 13:12:06

//
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

/**
 * Canvas displaying a simple drawing: the coordinate-system axes + some points and their coordinates.
 * It is used to demonstrate the Zoom and Pan functionality.
 *
 * @author Sorin Postelnicu
 * @since July 13, 2009
 */

public class ZoomAndPanCanvas extends Canvas {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Zoom and Pan Canvas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ZoomAndPanCanvas chart = new ZoomAndPanCanvas();

        frame.add(chart, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        chart.createBufferStrategy(2);
    }

    private boolean init = true;

    private Point[] points = {
            new Point(-100, -100),
            new Point(-100, 100),
            new Point(100, -100),
            new Point(100, 100)
    };

    private ZoomAndPanListener zoomAndPanListener;

    public ZoomAndPanCanvas() {
        this.zoomAndPanListener = new ZoomAndPanListener(this);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
    }

    public ZoomAndPanCanvas(int minZoomLevel, int maxZoomLevel, double zoomMultiplicationFactor) {
        this.zoomAndPanListener = new ZoomAndPanListener(this, minZoomLevel, maxZoomLevel, zoomMultiplicationFactor);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 500);
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        if (init) {
            // Initialize the viewport by moving the origin to the center of the window,
            // and inverting the y-axis to point upwards.
            init = false;
            Dimension d = getSize();
            int xc = d.width / 2;
            int yc = d.height / 2;
            g.translate(xc, yc);
            g.scale(1, -1);
            // Save the viewport to be updated by the ZoomAndPanListener
            zoomAndPanListener.setCoordTransform(g.getTransform());
        } else {
            // Restore the viewport after it was updated by the ZoomAndPanListener
            g.setTransform(zoomAndPanListener.getCoordTransform());
        }

        // Draw the axes
        g.drawLine(-1000, 0, 1000, 0);
        g.drawLine(0, -1000, 0, 1000);
        // Create an "upside-down" font to correct for the inverted y-axis
        Font font = g.getFont();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1, -1);
        g.setFont(font.deriveFont(affineTransform));
        // Draw the points and their coordinates
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            g.drawLine((int)p.getX() - 5, (int)p.getY(), (int)p.getX() + 5, (int)p.getY());
            g.drawLine((int)p.getX(), (int)p.getY() -5, (int)p.getX(), (int)p.getY() + 5);
            g.drawString("P"+ i + "(" + p.getX() + "," + p.getY() + ")", (float) p.getX(), (float) p.getY());
        }
    }

}
