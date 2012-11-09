package graphics;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 19, 2007
 * Time: 3:14:59 PM
 */

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

// draw text along a curve
public class RollingText extends JPanel {
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String s = "Java Source and Support.";
        Font font = new Font("Serif", Font.PLAIN, 24);
        FontRenderContext frc = g2.getFontRenderContext();
        g2.translate(40, 80);

        GlyphVector gv = font.createGlyphVector(frc, s);
        int length = gv.getNumGlyphs();
        for (int i = 0; i < length; i++) {
            Point2D p = gv.getGlyphPosition(i);
            double theta = (double) i / (double) (length - 1) * Math.PI / 4;
            AffineTransform at = AffineTransform.getTranslateInstance(p.getX(),  p.getY());
            at.rotate(theta);
            Shape glyph = gv.getGlyphOutline(i);
            Shape transformedGlyph = at.createTransformedShape(glyph);
            g2.fill(transformedGlyph);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("RollingText v1.0");
        f.getContentPane().add(new RollingText());
        f.setSize(600, 300);
        f.setVisible(true);
    }
}