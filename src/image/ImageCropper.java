package image;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 29, 2008
 * Time: 10:33:30 AM
 */
import com.jhlabs.image.CompoundFilter;
import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.GrayscaleFilter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.imageio.ImageIO;

/**
 * @author Christophe Le Besnerais
 * http://swing-fx.blogspot.com/2008/04/swing-component-to-crop-images.html
 */
public class ImageCropper extends JLayeredPane {

    private BufferedImage image;
    private BufferedImage thumbnail;
    private BufferedImage croppedImage;
    private JPanel background;
    private JPanel croppingPanel;
    private JPanel resizePanel;
    private Rectangle crop;
    private float zoom = 1.0f;
    private int whellZoomAmount = 5;
    private BufferedImageOp filter;
    private transient ChangeEvent changeEvent;


    public ImageCropper(BufferedImage image) {
        InnerListener listener = new InnerListener();
        this.addComponentListener(listener);
        this.image = image;
        this.crop = new Rectangle(image.getWidth() / 5, image.getHeight() / 5,
                                  3 * image.getWidth() / 5, 3 * image.getHeight() / 5);
        this.croppedImage = image.getSubimage(crop.x, crop.y, crop.width, crop.height);

        this.background = new JPanel(true) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(getThumbnail(), (this.getWidth() - getThumbnail().getWidth()) / 2,
                            (this.getHeight() - getThumbnail().getHeight()) / 2, null);
            }
        };
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        this.croppingPanel = new JPanel(true) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(croppedImage, 0, 0, this.getWidth(), this.getHeight(), null);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            }
        };
        this.croppingPanel.addMouseListener(listener);
        this.croppingPanel.addMouseMotionListener(listener);
        this.croppingPanel.addMouseWheelListener(listener);
        this.croppingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        this.add(croppingPanel, JLayeredPane.PALETTE_LAYER, 2);

        this.resizePanel = new JPanel(true) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
                g.drawLine(2, 4, 4, 2);
                g.drawLine(2, 7, 7, 2);
                g.drawLine(5, 7, 7, 5);
            }
        };
        this.resizePanel.addMouseListener(listener);
        this.resizePanel.addMouseMotionListener(listener);
        this.resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        this.resizePanel.setSize(10, 10);
        this.add(resizePanel, JLayeredPane.PALETTE_LAYER, 0);
    }


    public BufferedImageOp getFilter() {
        return filter;
    }

    public void setFilter(BufferedImageOp filter) {
        this.filter = filter;
    }

    public BufferedImage getCroppedImage() {
        return croppedImage;
    }

    private BufferedImage getThumbnail() {
        if (thumbnail == null) {
            int width = ImageCropper.this.image.getWidth();
            int height = ImageCropper.this.image.getHeight();
            float ratio = (float) width / (float) height;
            if ((float) this.getWidth() / (float) this.getHeight() > ratio) {
                height = this.getHeight();
                width = (int) (height * ratio);
            } else {
                width = this.getWidth();
                height = (int) (width / ratio);
            }

            thumbnail = getGraphicsConfiguration().createCompatibleImage(width, height,
                                            ImageCropper.this.image.getTransparency());
            Graphics2D g2d = thumbnail.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(ImageCropper.this.image, 0, 0, width, height, null);
            g2d.dispose();

            if (filter != null)
                thumbnail = filter.filter(thumbnail, null);
        }

        return thumbnail;
    }

    private void changePosition() {
        if (crop.x < 0)
            crop.x = 0;
        if (crop.x > image.getWidth())
            crop.x = image.getWidth();
        if (crop.y < 0)
            crop.y = 0;
        if (crop.y > image.getHeight())
            crop.y = image.getHeight();
        if (crop.x + crop.width > image.getWidth())
            crop.x = image.getWidth() - crop.width;
        if (crop.y + crop.height > image.getHeight())
            crop.y = image.getHeight() - crop.height;

        changeCrop(true);
    }

    private void changeSize() {
        if (crop.width <= 0)
            crop.width = 1;
        if (crop.height <= 0)
            crop.height = 1;
        float ratio = (float) crop.width / (float) crop.height;
        if (crop.x + crop.width > image.getWidth()) {
            crop.width = image.getWidth() - crop.x;
            crop.height = (int) (crop.width / ratio);
        }
        if (crop.y + crop.height > image.getHeight()) {
            crop.height = image.getHeight() - crop.y;
            crop.width = (int) (crop.height * ratio);
        }
        if (crop.width >= image.getWidth()) {
            crop.width = image.getWidth();
            crop.height = (int) (crop.width / ratio);
        }
        if (crop.height >= image.getHeight()) {
            crop.height = image.getHeight();
            crop.width = (int) (crop.height * ratio);
        }

        changeCrop(true);
    }

    private void changeZoom() {
        if (crop.x < 0)
            crop.x = 0;
        if (crop.y < 0)
            crop.y = 0;
        if (crop.width <= 0)
            crop.width = 1;
        if (crop.height <= 0)
            crop.height = 1;
        float ratio = (float) crop.width / (float) crop.height;
        if (crop.x + crop.width > image.getWidth()) {
            crop.width = image.getWidth() - crop.x;
            crop.height = (int) (crop.width / ratio);
        }
        if (crop.y + crop.height > image.getHeight()) {
            crop.height = image.getHeight() - crop.y;
            crop.width = (int) (crop.height * ratio);
        }
        if (crop.width >= image.getWidth()) {
            crop.width = image.getWidth();
            crop.height = (int) (crop.width / ratio);
        }
        if (crop.height >= image.getHeight()) {
            crop.height = image.getHeight();
            crop.width = (int) (crop.height * ratio);
        }

        changeCrop(false);
    }

    private void changeCrop(boolean updatePanel) {
        this.croppedImage = image.getSubimage(crop.x, crop.y, crop.width, crop.height);
        this.fireStateChanged();

        if (updatePanel) {
            float ratio = (float) image.getWidth() / (float) getThumbnail().getWidth();
            this.croppingPanel.setSize((int) (zoom * crop.width / ratio), (int) (zoom * crop.height / ratio));
            this.croppingPanel.setLocation(
                    (int) ((crop.x / ratio) - ((crop.width / ratio) * (zoom - 1) / 2)) +
                            (getWidth() - getThumbnail().getWidth()) / 2,
                    (int) ((crop.y / ratio) - ((crop.height / ratio) * (zoom - 1) / 2)) +
                            (getHeight() - getThumbnail().getHeight()) / 2);
            this.resizePanel.setLocation(
                    croppingPanel.getX() + croppingPanel.getWidth() - resizePanel.getWidth() / 2,
                    croppingPanel.getY() + croppingPanel.getHeight() - resizePanel.getHeight() / 2);
        } else {
            this.croppingPanel.repaint();
        }
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])(listenerList.getListeners(
            ChangeListener.class));
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }


    private class InnerListener extends MouseAdapter implements
            ComponentListener, MouseMotionListener, MouseWheelListener {

        private Point cursorPosition;

        public void componentResized(ComponentEvent e) {
            thumbnail = null;
            background.setSize(ImageCropper.this.getSize());
            float ratio = (float) getThumbnail().getWidth() / (float) image.getWidth();
            croppingPanel.setSize((int) (zoom * crop.width * ratio), (int) (zoom * crop.height * ratio));
            croppingPanel.setLocation(
                    (int) ((crop.x * ratio) - (crop.width * ratio * (zoom - 1) / 2)) +
                            (getWidth() - getThumbnail().getWidth()) / 2,
                    (int) ((crop.y * ratio) - (crop.height * ratio * (zoom - 1) / 2)) +
                            (getHeight() - getThumbnail().getHeight()) / 2);
            resizePanel.setLocation(
                    croppingPanel.getX() + croppingPanel.getWidth() - resizePanel.getWidth() / 2,
                    croppingPanel.getY() + croppingPanel.getHeight() - resizePanel.getHeight() / 2);
            thumbnail = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            cursorPosition = e.getPoint();
            setLayer(e.getComponent(), JLayeredPane.DRAG_LAYER);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            cursorPosition = null;
            setLayer(e.getComponent(), JLayeredPane.PALETTE_LAYER);
            setPosition(croppingPanel, 1);
            setPosition(resizePanel, 0);
        }

        public void mouseDragged(MouseEvent e) {
            if (cursorPosition != null) {
                if (e.getComponent().equals(croppingPanel)) {
                    float ratio = (float) image.getWidth() / (float) getThumbnail().getWidth();
                    Point pos = SwingUtilities.convertPoint(croppingPanel, e.getPoint(), background);
                    pos.translate(- cursorPosition.x, - cursorPosition.y);
                    pos.translate(croppingPanel.getWidth() / 2, croppingPanel.getHeight() / 2);
                    pos.translate(- (getWidth() - getThumbnail().getWidth()) / 2,
                                  - (getHeight() - getThumbnail().getHeight()) / 2);
                    pos.setLocation(pos.x * ratio, pos.y * ratio);
                    pos.translate(- crop.width / 2, - crop.height / 2);
                    crop.setLocation(pos.x, pos.y);
                    changePosition();

                } else if (e.getComponent().equals(resizePanel)) {
                    float ratio = (float) image.getWidth() / (float) getThumbnail().getWidth();
                    Point start = SwingUtilities.convertPoint(resizePanel, cursorPosition, background);
                    Point end = SwingUtilities.convertPoint(resizePanel, e.getPoint(), background);
                    crop.setSize(crop.width + (int) ((end.x - start.x) * ratio),
                                 crop.height + (int) ((end.y - start.y) * ratio));
                    changeSize();
                }
            }
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            if ((e.getWheelRotation() > 0 && crop.x > 0 && crop.x + crop.width < image.getWidth() &&
                    crop.y > 0 && crop.y + crop.height < image.getHeight())
                    || (e.getWheelRotation() < 0 && crop.width > 1 && crop.height > 1)) {
                float ratio = (float) crop.height / (float)crop.width;
                int newWidth = (int) (crop.width + (e.getWheelRotation() * whellZoomAmount));
                int newHeight = (int) (newWidth * ratio);
                crop.setBounds(crop.x - ((newWidth - crop.width) / 2),
                               crop.y - ((newHeight - crop.height) / 2), newWidth, newHeight);
                zoom = (float) (croppingPanel.getWidth() * image.getWidth()) /
                       (float) (crop.width * getThumbnail().getWidth());
                changeZoom();
            }
        }

        public void componentMoved(ComponentEvent e) {}
        public void componentShown(ComponentEvent e) {}
        public void componentHidden(ComponentEvent e) {}
        public void mouseMoved(MouseEvent e) {}

    }

    public static void main(String[] args) {
        try {
         JFrame f = new JFrame();
         f.setSize(600, 800);
         BufferedImage i = ImageIO.read(new File("E:\\Private\\Poze\\healing dance.JPG"));
         ImageCropper c = new ImageCropper(i);

         // http://www.jhlabs.com/ip/filters/index.html
         BufferedImageOp filter = new CompoundFilter(new BoxBlurFilter(3, 3, 3), new GrayscaleFilter());
         c.setFilter(filter);

         c.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
           ImageCropper c2 = (ImageCropper) e.getSource();
           System.out.println(c2.getCroppedImage().getWidth() + " * " + c2.getCroppedImage().getHeight());
          }
         });
         f.add(c);
         f.setVisible(true);

        } catch (Exception e) {
         e.printStackTrace();
        }
        
    }

}
