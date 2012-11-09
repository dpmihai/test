package glass;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 7, 2005 Time: 11:21:58 AM
 */
public class WaitGlassPane extends JComponent implements MouseListener {

    protected Animator animation = null;
    protected boolean active = false;
    protected int alphaLevel = 0;
    protected float shield = 0.70f;
    protected String text = "";

    protected RenderingHints hints = null;
    private JLabel2D lbl;
    private boolean glowing = false;

    public static final Color mainUltraLightColor = new Color(255, 255, 255);
    public static final Color mainLightColor = new Color(255, 255, 255);
    public static final Color mainMidColor = new Color(255, 255, 255);
    public static final Color mainDarkColor = new Color(192, 192, 192);

    public WaitGlassPane() {
        this("", false);
    }

    public WaitGlassPane(String text, boolean glowing) {
        this(text, glowing, 0.70f);
    }

    public WaitGlassPane(String text, boolean glowing, float shield) {
        this.text = text;
        this.glowing = glowing;
        this.shield = shield >= 0.0f ? shield : 0.0f;

        this.hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        this.hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        lbl = new JLabel2D(text, JLabel.CENTER);
        //lbl.setFont(bigFont);
        lbl.setEffectIndex(JLabel2D.EFFECT_COLOR_ANIMATION);
        GradientPaint gp = new GradientPaint(0, 0, Color.BLACK, 100, 50, Color.LIGHT_GRAY, true);
        lbl.setGradient(gp);
        lbl.setOutlineColor(Color.black);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public String getText() {
        return text;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void start() {
        addMouseListener(this);
        setVisible(true);
        animation = new Animator();
        animation.start();
    }

    public void stop() {
        if (animation != null) {
            animation.finish();
            animation = null;
            removeMouseListener(WaitGlassPane.this);
            setVisible(false);
        }
    }

    private void interrupt() {
        if (animation != null) {
            animation.interrupt();
            animation = null;
            removeMouseListener(this);
            setVisible(false);
        }
    }

    public void paintComponent(Graphics g) {
        if (active) {
            int width = getWidth();
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(hints);

//            g2.setColor(new Color(255, 255, 255, (int) (alphaLevel * shield)));
//            g2.fillRect(0, 0, getWidth(), getHeight());

            BufferedImage oddLine = createGradientLine(this.getWidth(), mainLightColor,
                    mainDarkColor, 0.6);
            BufferedImage evenLine = createGradientLine(this
                    .getWidth(), mainUltraLightColor,
                    mainMidColor, 0.6);

            for (int row = 0; row < height; row++) {
                if ((row % 2) == 0) {
                    g2.drawImage(evenLine, 0, row, null);
                } else {
                    g2.drawImage(oddLine, 0, row, null);
                }
            }

            FontRenderContext frc = g2.getFontRenderContext();
            TextLayout tl = new TextLayout(text, getFont(), frc);
            AffineTransform shift = AffineTransform.getTranslateInstance((width - tl.getBounds().getWidth()) / 2,
                    (height - tl.getBounds().getHeight()) / 2);
            Shape shp = tl.getOutline(shift);
            g2.setColor(lbl.getForeground());
            g2.fill(shp);
        }
    }

    private static BufferedImage createGradientLine(int width, Color leftColor,
            Color rightColor, double opacity) {
        BufferedImage image = new BufferedImage(width, 1,
                BufferedImage.TYPE_INT_ARGB);
        int iOpacity = (int)(255*opacity);

        for (int col = 0; col < width; col++) {
            double coef = (double) col / (double) width;
            int r = (int) (leftColor.getRed() + coef
                    * (rightColor.getRed() - leftColor.getRed()));
            int g = (int) (leftColor.getGreen() + coef
                    * (rightColor.getGreen() - leftColor.getGreen()));
            int b = (int) (leftColor.getBlue() + coef
                    * (rightColor.getBlue() - leftColor.getBlue()));

            int color = (iOpacity << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(col, 0, color);
        }
        return image;
    }

    private class Animator extends Thread {
        private boolean started = true;

        protected Animator() {
            started = true;
        }

        public void run() {
            active = true;
            if (started && glowing) {
                lbl.startAnimation(150);
            }

            while (!Thread.interrupted()) {
                repaint();
                if (started) {
                    alphaLevel = 255;
                } else {
                    alphaLevel = 0;
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    break;
                }
                Thread.yield();
            }

            if (!started) {
                active = false;
                if (glowing) {
                    lbl.stopAnimation();
                }
                repaint();
            }
        }

        public void finish() {
            started = false;
            this.interrupt();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
