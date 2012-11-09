package animation;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 30, 2005 Time: 2:49:50 PM
 */
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class Glow extends JPanel
{
    String text;
    Font font;
    double scale;

    public Glow()
    {
        text = "hello world";
        // sdk1.5/jre/lib/fonts
        font = new Font("lucida sans demibold", Font.PLAIN, 48);
        scale = 1.25;
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2.setFont(font);
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        Rectangle2D r2 = textLayout.getBounds();
        double x = (w - scale*r2.getWidth())/2;
        double y = (h + scale*r2.getHeight())/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        Shape outline = textLayout.getOutline(at);
        Rectangle r = outline.getBounds();
        float x1 = r.x + r.width/2;
        float y1 = r.y + r.height/2;
        float y2 = h*5/8;
        GradientPaint gradient = new GradientPaint(x1, y1, Color.red,
                                                   x1, y2, Color.yellow, true);
        g2.setPaint(gradient);
        g2.fill(outline);
    }
    
     public static void main(String[] args) {
        String[] lines = { "Mihai Dinca-Panaitescu", "Senior Developer" };
        Glow panel = new Glow();
        panel.setPreferredSize(new Dimension(200,200));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(panel, new GridBagConstraints(0,0, 1,1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 0,0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(400,400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}
