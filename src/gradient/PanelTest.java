package gradient;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.swing.*;

public class PanelTest
{
    public static void main(String[] args)
    {
        RoundedShadedPanel rsp = new RoundedShadedPanel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(rsp);
        f.setSize(400,300);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

class RoundedShadedPanel extends JPanel
{
    final int PAD, DIA, BORDER;
    Color colorIn, colorOut;

    public RoundedShadedPanel()
    {
        PAD = 25;                    // padding from panel edge to roundRect edge
        DIA = 75;                    // diameter of corner arcs
        BORDER = 10;                 // border thickness
        colorIn = Color.black;       // color next to roundRect edge
        colorOut = getBackground();  // color out toward panel edge
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        g2.drawRoundRect(PAD,
                         PAD,
                         width - 2*PAD,
                         height - 2*PAD,
                         DIA,
                         DIA);
        // north side
        GradientPaint gradient = new GradientPaint(width/2,
                                                   PAD - BORDER,
                                                   colorOut,
                                                   width/2,
                                                   PAD,
                                                   colorIn);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD + DIA/2, PAD - BORDER,
                                       width - 2*(PAD + DIA/2), BORDER));
        // east side
        gradient = new GradientPaint(width - PAD,
                                     height/2,
                                     colorIn,
                                     width - PAD + BORDER,
                                     height/2,
                                     colorOut);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(width - PAD, PAD + DIA/2, BORDER,
                                       height - 2*(PAD + DIA/2)));
        // south side
        gradient = new GradientPaint(width/2, height - PAD, colorIn,
                                     width/2, height - PAD + BORDER, colorOut);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD + DIA/2, height - PAD,
                                       width - 2*(PAD + DIA/2), BORDER));
        // west side
        gradient = new GradientPaint(PAD - BORDER,
                                     height/2,
                                     colorOut,
                                     PAD,
                                     height/2,
                                     colorIn);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD - BORDER, PAD + DIA/2,
                                       BORDER, height - 2*(PAD + DIA/2)));
        // draw the four corners
        // northeast arc
        int xc = width - PAD - DIA/2;
        int yc = PAD + DIA/2;
        CustomPaint customPaint = new CustomPaint(xc, yc,
                                                  new Point2D.Double(0, DIA/2),
                                                  DIA/2, BORDER,
                                                  colorIn, colorOut);
        g2.setPaint(customPaint);
        Ellipse2D
            eIn  = new Ellipse2D.Double(width - PAD - DIA, PAD, DIA, DIA),
            eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER,
                                        PAD - BORDER,
                                        DIA + 2*BORDER, DIA + 2*BORDER);
        Shape
            arcIn  = new Arc2D.Double(eIn.getBounds2D(),  0.0, 90.0, Arc2D.PIE),
            arcOut = new Arc2D.Double(eOut.getBounds2D(), 0.0, 90.0, Arc2D.PIE);
        Area arcBorder = new Area(arcOut);
        arcBorder.subtract(new Area(arcIn));
        g2.fill(arcBorder);
        // northwest arc
        xc = PAD + DIA/2;
        yc = PAD + DIA/2;
        customPaint = new CustomPaint(xc, yc,
                                      new Point2D.Double(0, DIA/2),
                                      DIA/2, BORDER,
                                      colorIn, colorOut);
        g2.setPaint(customPaint);
        eIn  = new Ellipse2D.Double(PAD, PAD, DIA, DIA);
        eOut = new Ellipse2D.Double(PAD - BORDER, PAD - BORDER,
                                    DIA + 2*BORDER, DIA + 2*BORDER);
        arcIn  = new Arc2D.Double(eIn.getBounds2D(),  90.0, 90.0, Arc2D.PIE);
        arcOut = new Arc2D.Double(eOut.getBounds2D(), 90.0, 90.0, Arc2D.PIE);
        arcBorder = new Area(arcOut);
        arcBorder.subtract(new Area(arcIn));
        g2.fill(arcBorder);
        // southwest arc
        xc = PAD + DIA/2;
        yc = height - PAD - DIA/2;
        customPaint = new CustomPaint(xc, yc,
                                      new Point2D.Double(0, DIA/2),
                                      DIA/2, BORDER,
                                      colorIn, colorOut);
        g2.setPaint(customPaint);
        eIn  = new Ellipse2D.Double(PAD, height - PAD - DIA, DIA, DIA);
        eOut = new Ellipse2D.Double(PAD - BORDER,
                                    height - PAD - DIA - BORDER,
                                    DIA + 2*BORDER, DIA + 2*BORDER);
        arcIn  = new Arc2D.Double(eIn.getBounds2D(),  180.0, 90, Arc2D.PIE);
        arcOut = new Arc2D.Double(eOut.getBounds2D(), 180.0, 90, Arc2D.PIE);
        arcBorder = new Area(arcOut);
        arcBorder.subtract(new Area(arcIn));
        g2.fill(arcBorder);
        // southeast arc
        xc = width  - PAD - DIA/2;
        yc = height - PAD - DIA/2;
        customPaint = new CustomPaint(xc, yc,
                                      new Point2D.Double(0, DIA/2),
                                      DIA/2, BORDER,
                                      colorIn, colorOut);
        g2.setPaint(customPaint);
        eIn  = new Ellipse2D.Double(width - PAD - DIA, height - PAD - DIA, DIA, DIA);
        eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER,
                                    height - PAD - DIA - BORDER,
                                    DIA + 2*BORDER, DIA + 2*BORDER);
        arcIn  = new Arc2D.Double(eIn.getBounds2D(),  270.0, 90.0, Arc2D.PIE);
        arcOut = new Arc2D.Double(eOut.getBounds2D(), 270.0, 90.0, Arc2D.PIE);
        arcBorder = new Area(arcOut);
        arcBorder.subtract(new Area(arcIn));
        g2.fill(arcBorder);
    }
}

/**
* Code adapted from http://www.oreilly.com/catalog/java2d/chapter/ch04.html
*/
class CustomPaint implements Paint
{
    Point2D originP, radiusP;
    int radius, border;
    Color colorIn, colorOut;

    public CustomPaint(int x, int y, Point2D radiusP,
                       int radius, int border,
                       Color colorIn, Color colorOut)
    {
        originP = new Point2D.Double(x,y);
        this.radiusP = radiusP;
        this.radius = radius;
        this.border = border;
        this.colorIn = colorIn;
        this.colorOut = colorOut;
    }

    public PaintContext createContext(ColorModel cm,
                                      Rectangle deviceBounds,
                                      Rectangle2D userBounds,
                                      AffineTransform xform,
                                      RenderingHints hints)
    {
        Point2D
        xformOrigin = xform.transform(originP, null),
        xformRadius = xform.deltaTransform(radiusP, null);
        return new CustomPaintContext(xformOrigin, xformRadius,
                                      radius, border,
                                      colorIn, colorOut);
    }

    public int getTransparency()
    {
        int alphaIn = colorIn.getAlpha();
        int alphaOut = colorOut.getAlpha();
        return (((alphaIn & alphaOut) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
}

/**
* Code adapted from http://www.oreilly.com/catalog/java2d/chapter/ch04.html
*/
class CustomPaintContext implements PaintContext
{
    Point2D originP, radiusP;
    Color colorIn, colorOut;
    int radius, border;

    public CustomPaintContext(Point2D originP, Point2D radiusP,
                              int radius, int border,
                              Color colorIn, Color colorOut)
    {
        this.originP = originP;
        this.radiusP = radiusP;
        this.radius = radius;
        this.border = border;
        this.colorIn = colorIn;
        this.colorOut = colorOut;
    }

    public void dispose() {}

    public ColorModel getColorModel()
    {
        return ColorModel.getRGBdefault();
    }

    public Raster getRaster(int x, int y, int w, int h)
    {
        WritableRaster raster = getColorModel().createCompatibleWritableRaster(w,h);
        int[] data = new int[w * h * 4];
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
            {
                double distance = originP.distance(x + i, y + j);
                double r = radiusP.distance(radius, radius);
                double ratio = distance - r < 0 ? 0.0 : (distance - r)/border;
                if(ratio > 1.0)
                    ratio = 1.0;
                int base = (j * w + i) * 4;
                data[base + 0] = (int)(colorIn.getRed() +
                ratio * (colorOut.getRed() - colorIn.getRed()));
                data[base + 1] = (int)(colorIn.getGreen() +
                ratio * (colorOut.getGreen() - colorIn.getGreen()));
                data[base + 2] = (int)(colorIn.getBlue() +
                ratio * (colorOut.getBlue() - colorIn.getBlue()));
                data[base + 3] = (int)(colorIn.getAlpha() +
                ratio * (colorOut.getAlpha() - colorIn.getAlpha()));
            }
            raster.setPixels(0, 0, w, h, data);
            return raster;
    }
}

