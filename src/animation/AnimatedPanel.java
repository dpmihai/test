package animation;

/**
 * 18:05 17/02/2005
 * Romain Guy <romain.guy@jext.org>
 * Subject to the BSD license.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Display an animated panel. The panel contains a picture and a text message.
 * As soon as <code>start()</code> is called, the pictures and the text glow
 * in cycles. The animation can be stopped at anytime by calling
 * <code>stop()</code>. You can set the font and its color by calling
 * <code>setFont()</code> and <code>setForeground()</code>.
 *
 * @author Romain Guy
 */
public class AnimatedPanel extends JPanel {

    protected float gradient;
    protected String message;
    protected Thread animator;
    protected BufferedImage convolvedImage;
    protected BufferedImage originalImage;
    protected static AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

    /**
     * Creates an animated panel with a message and a picture.
     *
     * @param message The message to display, cannot be null nor empty.
     * @param icon The picture to display
     */
    public AnimatedPanel(String message, ImageIcon icon) {
        this.message = message;

        Image image = icon.getImage();
        originalImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        convolvedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = originalImage.createGraphics();
        g.drawImage(image, 0, 0, this);
        g.dispose();

        setBrightness(1.0f);
        setOpaque(false);
    }

    /**
     * Changes the displayed message at runtime.
     *
     * @param text The message to be displayed. Can be null or empty.
     */
    public void setText(String text)
    {
        this.message = text;
        repaint();
    }

    /**
     * Returns the current displayed message.
     */
    public String getText()
    {
        return message;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (convolvedImage != null) {
            int width = getWidth();
            int height = getHeight();

            synchronized (convolvedImage) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                FontRenderContext context = g2.getFontRenderContext();
                TextLayout layout = new TextLayout(message, getFont(), context);
                Rectangle2D bounds = layout.getBounds();

                int x = (width - convolvedImage.getWidth(null)) / 2;
                int y = (int) (height - (convolvedImage.getHeight(null) + bounds.getHeight() + layout.getAscent())) / 2;

                g2.drawImage(convolvedImage, x, y, this);
                Color foreground = getForeground();
                g2.setColor(new Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(),
                                      (int) (gradient * 255)));
                layout.draw(g2, (float) (width - bounds.getWidth()) / 2,
                    (float) (y + convolvedImage.getHeight(null) + bounds.getHeight() + layout.getAscent()));
            }
        }
    }

    /**
     * Changes the image luminosity.
     */
    private void setBrightness(float multiple) {
        float[] brightKernel = { multiple };
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImageOp bright = new ConvolveOp(new Kernel(1, 1, brightKernel), ConvolveOp.EDGE_NO_OP, hints);
        bright.filter(originalImage, convolvedImage);
        repaint();
    }

    /**
     * Changes the text gradient control value.
     */
    private void setGradientFactor(float gradient) {
        this.gradient = gradient;
    }

    /**
     * Starts the animation. A thread called "Highlighter" is spawned and can be
     * interrupted at anytime by invoking <code>stop()</code>.
     */
    public void start() {
        this.animator = new Thread(new HighlightCycler(), "Highlighter");
        this.animator.start();
    }

    /**
     * Safely stops the animation.
     */
    public void stop() {
        if (this.animator != null)
            this.animator.interrupt();
        this.animator = null;
    }

    /**
     * Makes the image luminosity and the text gradient to cycle.
     */
    class HighlightCycler implements Runnable {

        private int way = 1;
        private final int LOWER_BOUND = 10;
        private final int UPPER_BOUND = 35;
        private int value = LOWER_BOUND;

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 / (UPPER_BOUND - LOWER_BOUND));
                } catch (InterruptedException e) {
                    return;
                }

                value += this.way;
                if (value > UPPER_BOUND) {
                    value = UPPER_BOUND;
                    this.way = -1;
                } else if (value < LOWER_BOUND) {
                    value = LOWER_BOUND;
                    this.way = 1;
                }

                synchronized (convolvedImage) {
                    setBrightness((float) value / 10);
                    setGradientFactor((float) value / UPPER_BOUND);
                }
            }
        }
    }
}
