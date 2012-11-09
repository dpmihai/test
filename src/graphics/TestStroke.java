package graphics;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 24, 2007
 * Time: 12:54:01 PM
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;


public class TestStroke extends JFrame {
      public static void main(String[] args) {
        TestStroke f = new TestStroke();
        f.setTitle("PaintingAndStroking v1.0");
        f.setSize(300, 150);
        //f.center();
        f.setVisible(true);
      }

      public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double x = 15, y = 50, w = 70, h = 70;
        Ellipse2D e = new Ellipse2D.Double(x, y, w, h);
        GradientPaint gp = new GradientPaint(75, 75, Color.white,
            95, 95, Color.gray, true);
        // Fill with a gradient.
        g2.setPaint(gp);
        g2.fill(e);
        // Stroke with a solid color.
        e.setFrame(x + 100, y, w, h);
        g2.setPaint(Color.black);
        //g2.setStroke(new BasicStroke(8));
          g2.setStroke(
                  new ShapeStroke(
                          new Shape[]{
                                  new Ellipse2D.Float(0, 0, 6, 6),
                                  new Ellipse2D.Float(0, 0, 4, 4)
                          },
                          15.0f
                  )
          );
        g2.draw(e);
        // Stroke with a gradient.
        e.setFrame(x + 200, y, w, h);
        g2.setPaint(gp);
        g2.draw(e);
      }
    }

