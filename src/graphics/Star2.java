package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 2, 2007
 * Time: 3:26:29 PM
 */
public class Star2 extends JComponent {

    private double x;             // center x
    private double y;             // center y
    private double innerRadius;
    private double outerRadius;
    private int branches;

    public Star2(double x, double y, double innerRadius, double outerRadius, int branches) {
        this.x = x;
        this.y = y;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.branches = branches;
        setPreferredSize(new Dimension( 400, 400));
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GeneralPath path = new GeneralPath();

        double outerAngleIncrement = 2 * Math.PI / branches;
        double outerAngle = 0.0;
        double innerAngle = outerAngleIncrement / 2.0;

        x += outerRadius;
        y += outerRadius;

        float x1 = (float) (Math.cos(outerAngle) * outerRadius + x);
        float y1 = (float) (Math.sin(outerAngle) * outerRadius + y);

        float x2 = (float) (Math.cos(innerAngle) * innerRadius + x);
        float y2 = (float) (Math.sin(innerAngle) * innerRadius + y);

        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        outerAngle += outerAngleIncrement;
        innerAngle += outerAngleIncrement;

        for (int i=1; i<branches; i++) {
            x1 = (float) (Math.cos(outerAngle) * outerRadius + x);
            y1 = (float) (Math.sin(outerAngle) * outerRadius + y);

            path.lineTo(x1, y1);

            x2 = (float) (Math.cos(innerAngle) * innerRadius + x);
            y2 = (float) (Math.sin(innerAngle) * innerRadius + y);

            path.lineTo(x2, y2);

            outerAngle += outerAngleIncrement;
            innerAngle += outerAngleIncrement;
        }

        path.closePath();

        AffineTransform tr = new AffineTransform();        
        Shape shape = tr.createTransformedShape(path);
        g2.draw(shape);
        g2.fill(shape);

    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("General Path");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent comp = new Star2(100, 100, 20, 50, 9);
        frame.add(comp);
        frame.pack();
        frame.setVisible(true);
    }
}
