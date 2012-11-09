package loupe;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 20-Aug-2009
// Time: 13:47:53

//

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
 * @author Romain Guy <romain.guy@mac.com>
 */
public class Loupe extends JComponent {
    private BufferedImage loupeImage;
    private Point loupeLocation = new Point(0, 0);
    private JLayeredPane layeredPane;
    private BufferedImage buffer;
    private int zoomLevel = 2;

    public Loupe(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;

        loadImages();

        layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent mouseEvent) {
                Point location = mouseEvent.getPoint();
                location.translate(-getWidth() / 2, -getHeight() / 2);
                setLocation(location);
            }
        });
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent componentEvent) {
                resetBuffer();
            }

            public void componentMoved(ComponentEvent componentEvent) {
            }

            public void componentResized(ComponentEvent componentEvent) {
                resetBuffer();
            }

            public void componentShown(ComponentEvent componentEvent) {
            }
        });
    }

    public int getZoomLevel() {
        return this.zoomLevel;
    }

    public void setZoomLevel(int zoom) {
        if (zoom < 1) {
            zoom = 1;
        }

        int oldZoom = this.zoomLevel;
        this.zoomLevel = zoom;
        firePropertyChange("zoomLevel", oldZoom, this.zoomLevel);

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(loupeImage.getWidth(),
                loupeImage.getHeight());
    }

    public void resetBuffer() {
        buffer = null;
    }

    private void loadImages() {
        try {
            loupeImage = ImageIO.read(getClass().getResource("images/loupe.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null) {
            buffer = createBuffer();
        }

        Graphics2D g2 = buffer.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g2.setComposite(AlphaComposite.Src);

        Point location = getLocation();
        location.translate(getWidth() / 2, getHeight() / 2);

        int myLayer = layeredPane.getLayer(this);
        for (int i = myLayer - 1; i >= 2; i -= 2) {
            Component[] components = layeredPane.getComponentsInLayer(i);
            for (Component c : components) {
                if (c.getBounds().contains(location)) {
                    g2.translate(c.getX(), c.getY());
                    c.paint(g2);
                    g2.translate(-c.getX(), -c.getY());
                }
            }
        }

        g2.dispose();

        if (zoomLevel > 1) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            Shape clip = g.getClip();
            Area newClip = new Area(clip);
            newClip.intersect(new Area(new Ellipse2D.Double(6.0, 6.0, 138.0, 138.0)));

            g.setClip(newClip);
            g.drawImage(buffer,
                    (int) (-getX() * zoomLevel - getWidth() * 0.5 * (zoomLevel - 1)),
                    (int) (-getY() * zoomLevel - getHeight() * 0.5 * (zoomLevel - 1)),
                    buffer.getWidth() * zoomLevel,
                    buffer.getHeight() * zoomLevel, null);
            g.setClip(clip);
        }

        g.drawImage(loupeImage, 0, 0, null);
    }

    private BufferedImage createBuffer() {
        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = local.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();

        Container parent = getParent();
        return config.createCompatibleImage(parent.getWidth(), parent.getHeight(),
                Transparency.TRANSLUCENT);
    }
}
