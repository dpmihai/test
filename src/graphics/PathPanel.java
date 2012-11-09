package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 18, 2007
 * Time: 2:05:32 PM
 */
public class PathPanel extends JPanel {

    public PathPanel() {
        setPreferredSize(new Dimension(640, 480));
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        float x0 = 1.0f;
        float y0 = 0.0f;
        float x1 = (float)Math.cos(2*Math.PI/5.0);
        float y1 = (float)Math.sin(2*Math.PI/5.0);
        float x2 = (float)Math.cos(4*Math.PI/5.0);
        float y2 = (float)Math.sin(4*Math.PI/5.0);
        float x3 = (float)Math.cos(6*Math.PI/5.0);
        float y3 = (float)Math.sin(6*Math.PI/5.0);
        float x4 = (float)Math.cos(8*Math.PI/5.0);
        float y4 = (float)Math.sin(8*Math.PI/5.0);
        path.moveTo(x2, y2);
        path.lineTo(x0, y0);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        path.lineTo(x4, y4);
        path.closePath();
        AffineTransform tr = new AffineTransform();
        tr.setToScale(100, 100);
        g2.translate(120, 120);
        Shape shape = tr.createTransformedShape(path);
        g2.draw(shape);
        g2.fill(shape);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("General Path");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new PathPanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
