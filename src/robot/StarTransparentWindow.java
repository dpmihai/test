package robot;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 19, 2007
 * Time: 3:00:15 PM
 */
public class StarTransparentWindow extends TransparentWindow {

    public StarTransparentWindow(int width, int height, boolean closeOnClick) {
        super(width, height, closeOnClick);
    }

    protected void paintImage(Graphics2D tig) {

        GradientPaint gp = new GradientPaint(0, 0, Color.white,
                getWidth() / 3, getHeight() / 3, new Color(210, 210, 50), true);
        tig.setPaint(gp);

        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        float x0 = 1.0f;
        float y0 = 0.0f;
        float x1 = (float) Math.cos(2 * Math.PI / 5.0);
        float y1 = (float) Math.sin(2 * Math.PI / 5.0);
        float x2 = (float) Math.cos(4 * Math.PI / 5.0);
        float y2 = (float) Math.sin(4 * Math.PI / 5.0);
        float x3 = (float) Math.cos(6 * Math.PI / 5.0);
        float y3 = (float) Math.sin(6 * Math.PI / 5.0);
        float x4 = (float) Math.cos(8 * Math.PI / 5.0);
        float y4 = (float) Math.sin(8 * Math.PI / 5.0);
        path.moveTo(x3, y3);
        path.lineTo(x0, y0);
        path.lineTo(x2, y2);
        path.lineTo(x4, y4);
        path.lineTo(x1, y1);
        path.closePath();

        AffineTransform tr = new AffineTransform();
        tr.setToScale(width / 2, height / 2);
        Shape shape = tr.createTransformedShape(path);

        // if we need to translate the shape
        // we must call translate inverse after shape drawing! (to have the correct background)
        tig.translate(width / 2, height / 2);

        //tig.rotate(Math.PI/2);
        tig.draw(shape);
        tig.fill(shape);
        //tig.rotate(-Math.PI/2);

        // some text
        String message = "Indy \u2122";
        tig.setColor(Color.black);
        Font oldFont = tig.getFont();
        Font font = oldFont.deriveFont(Font.BOLD, 22.0f);
        tig.setFont(font);
        FontRenderContext frc = tig.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(message, frc);
        LineMetrics metrics = font.getLineMetrics(message, frc);
        float linewidth = (float) bounds.getWidth();     // The linewidth of our text
        float lineheight = metrics.getHeight();          // Total line height
        float ascent = metrics.getAscent();              // Top of text to baseline
        //System.out.println("lineWidth="+linewidth);
        //System.out.println("lineHeight="+lineheight);
        //System.out.println("ascent="+ascent);
        // Now display the message centered horizontally and vertically
        float cw = (float)  ( (shape.getBounds().getWidth() -width  - linewidth) / 2) +15;
        float ch = (float) ( (shape.getBounds().getHeight() -height - lineheight) / 2 + ascent);
        tig.drawString(message, cw, ch);
        Font font2 = oldFont.deriveFont(10.0f);
        tig.setFont(font2);
        tig.drawString("Ver 1.0 \u00A92007", cw, ch + 15);

        
        tig.translate(-width / 2, -height / 2);
    }

    public static void main(String[] args) {
        new StarTransparentWindow(250, 250, false);
    }
}
