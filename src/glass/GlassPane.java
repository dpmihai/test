package glass;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 23, 2005 Time: 11:16:48 AM To change this template use
 * File | Settings | File Templates.
 */
public class GlassPane extends JPanel {
    public GlassPane() {
        // intercept mouse and keyboard events and do nothing
        this.addMouseListener(new MouseAdapter() {
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
        });
        this.addKeyListener(new KeyAdapter() {
        });
        this.setOpaque(false);
    }

//    public static final Color mainUltraLightColor = new Color(128, 192, 255);
//    public static final Color mainLightColor = new Color(0, 128, 255);
//    public static final Color mainMidColor = new Color(0, 64, 196);
//    public static final Color mainDarkColor = new Color(0, 0, 128);

    public static final Color mainUltraLightColor = new Color(255, 255, 255);
    public static final Color mainLightColor = new Color(255, 255, 255);
    public static final Color mainMidColor = new Color(255, 255, 255);
    public static final Color mainDarkColor = new Color(192, 192, 192);

    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        BufferedImage oddLine = createGradientLine(
                this.getWidth(), mainLightColor,
                mainDarkColor, 0.6);
        BufferedImage evenLine = createGradientLine(this
                .getWidth(), mainUltraLightColor,
                mainMidColor, 0.6);

        int width = this.getWidth();
        int height = this.getHeight();
        for (int row = 0; row < height; row++) {
            if ((row % 2) == 0)
                graphics.drawImage(evenLine, 0, row, null);
            else
                graphics.drawImage(oddLine, 0, row, null);
        }
    }


    public static BufferedImage createGradientLine(int width, Color leftColor,
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
}