package gradient;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Panel4Test
{
    public static void main(String[] args)
    {
        OffsetShadePanel osp = new OffsetShadePanel();
        Shadow4Selector selector = new Shadow4Selector(osp);
        AdjustmentPanel adjuster = new AdjustmentPanel(osp);
        JFrame f = new JFrame("Shaded Border");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(selector, "North");
        f.getContentPane().add(osp);
        f.getContentPane().add(adjuster, "South");
        f.setSize(500,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

/**
 * For consistency do not shift gradients of any sides or arcs
 * shift: width  + 1 for north and south sides
 *        height + 1 for west  and east  sides
 *        x + 1 for eIn and eOut in northeast and southeast arcs
 *              for rrClip       in northeast and southeast arcs
 *        y + 1 for eIn and eOut in southwest and southeast arcs
 *              for rrClip       in southwest and southeast arcs
 */
class OffsetShadePanel extends JPanel
{
    int PAD, DIA, BORDER;
    Point2D RADIUS;
    int xc, yc;
    Color colorIn, colorOut;
    int alpha;
    GradientPaint gradient;
    CustomPaint customPaint;
    Ellipse2D eIn, eOut;
    Arc2D arcIn, arcOut;
    Area arcArea;
    RoundRectangle2D rrClip;
    int shadowVertex = 3;
    final static int
        NORTHEAST = 0,
        NORTHWEST = 1,
        SOUTHWEST = 2,
        SOUTHEAST = 3,
        COMPARE   = 4;

    public OffsetShadePanel()
    {
        PAD = 30;
        DIA = 40;
        BORDER = 30;
        colorOut = getBackground();
        alpha = 175;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        RADIUS = new Point2D.Double(0, DIA/2);
        colorIn = new Color(0, 0, 0, alpha);
        colorOut = new Color(colorOut.getRed(), colorOut.getGreen(),
                             colorOut.getBlue(), alpha);
        g2.drawRoundRect(PAD, PAD, width - 2*PAD, height - 2*PAD, DIA, DIA);

        switch(shadowVertex)
        {
            case SOUTHEAST:
                // east side
                gradient = new GradientPaint(width - PAD, height/2, colorIn,
                                             width - PAD + BORDER, height/2, colorOut);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(width - PAD + 1, PAD + DIA/2 + BORDER,
                                               BORDER, height - 2*PAD - DIA - BORDER + 1));
                // south side
                gradient = new GradientPaint(width/2, height - PAD, colorIn,
                                             width/2, height - PAD + BORDER, colorOut);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD + DIA/2 + BORDER, height - PAD + 1,
                                               width - 2*PAD - DIA - BORDER + 1, BORDER));
                // vertex arc - southeast
                xc = width - PAD - DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width  - PAD - DIA + 1,
                                            height - PAD - DIA + 1, DIA, DIA);
                eOut = new Ellipse2D.Double(width  - PAD - DIA - BORDER + 1,
                                            height - PAD - DIA - BORDER + 1,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  270.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 270.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                g2.fill(arcArea);

                // northeast arc
                xc = width - PAD - DIA/2;
                yc = PAD + DIA/2 + BORDER;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA + 1, PAD + BORDER, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER + 1, PAD,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  0.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 0.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(width - PAD - DIA, PAD,
                                                     DIA, DIA + BORDER, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);

                // southwest arc
                xc = PAD + DIA/2 + BORDER;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD + BORDER, height - PAD - DIA, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD, height - PAD - DIA - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  180.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 180.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(PAD, height - PAD - DIA,
                                                     DIA + BORDER, DIA, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);
                break;

            case SOUTHWEST:
                // west side
                gradient = new GradientPaint(PAD - BORDER, height/2, colorOut,
                                             PAD, height/2, colorIn);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD - BORDER, PAD + DIA/2 + BORDER,
                                               BORDER, height - 2*PAD - DIA - BORDER + 1));
                // south side
                gradient = new GradientPaint(width/2, height - PAD, colorIn,
                                             width/2, height - PAD + BORDER, colorOut);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD + DIA/2, height - PAD + 1,
                                               width - 2*PAD - DIA - BORDER + 1, BORDER));
                // vertex arc - southwest
                xc = PAD + DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, height - PAD - DIA + 1, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER, height - PAD - DIA - BORDER + 1,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  180.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 180.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                g2.fill(arcArea);

                // northwest arc
                xc = PAD + DIA/2;
                yc = PAD + DIA/2 + BORDER;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, PAD + BORDER, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER, PAD,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  90.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 90.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(PAD, PAD, DIA,
                                                     DIA + BORDER, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);

                // southeast arc
                xc = width - PAD - DIA/2 - BORDER;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA - BORDER + 1,
                                            height - PAD - DIA, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - 2*BORDER + 1,
                                            height - PAD - DIA - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  270.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 270.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(width - PAD - DIA - BORDER + 1,
                                                     yc - DIA/2, DIA + BORDER + 1,
                                                     DIA, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);
                break;

            case NORTHWEST:
                // draw west side
                gradient = new GradientPaint(PAD - BORDER, height/2, colorOut,
                                             PAD, height/2, colorIn);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD - BORDER, PAD + DIA/2, BORDER,
                                               height - 2*PAD - DIA - BORDER + 1));
                // north side
                gradient = new GradientPaint(width/2, PAD - BORDER, colorOut,
                                             width/2, PAD, colorIn);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD + DIA/2, PAD - BORDER,
                                               width - 2*PAD - DIA - BORDER + 1, BORDER));
                // vertex arc - northwest
                xc = PAD + DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  90.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 90.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                g2.fill(arcArea);

                // southwest arc
                xc = PAD + DIA/2;
                yc = height - PAD - DIA/2 - BORDER;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, height - PAD - DIA - BORDER + 1,
                                            DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER,
                                            height - PAD - DIA - 2*BORDER + 1,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  180.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 180.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(PAD, yc - DIA/2 + 1, DIA,
                                                     DIA + BORDER, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);

                // northeast arc
                xc = width - PAD - DIA/2 - BORDER;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA - BORDER + 1, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - 2*BORDER + 1, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  0.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 0.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(width - PAD - DIA - BORDER + 1,
                                                     PAD, DIA + BORDER, DIA, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);
                break;

            case NORTHEAST:
                // north side
                gradient = new GradientPaint(width/2, PAD - BORDER, colorOut,
                                             width/2, PAD, colorIn);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(PAD + DIA/2 + BORDER, PAD - BORDER,
                                               width - 2*PAD - DIA - BORDER + 1, BORDER));
                // east side
                gradient = new GradientPaint(width - PAD, height/2, colorIn,
                                             width - PAD + BORDER, height/2, colorOut);
                g2.setPaint(gradient);
                g2.fill(new Rectangle2D.Double(width - PAD + 1, PAD + DIA/2,
                                               BORDER, height - 2*PAD - DIA - BORDER + 1));
                // vertex arc - northeast
                xc = width - PAD - DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA + 1, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER + 1, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  0.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 0.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                g2.fill(arcArea);

                // northwest arc
                xc = PAD + DIA/2 + BORDER;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD + BORDER, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  90.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 90.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(PAD, PAD, DIA + BORDER,
                                                     DIA, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);

                // southeast arc
                xc = width  - PAD - DIA/2;
                yc = height - PAD - DIA/2 - BORDER;
                customPaint = new CustomPaint(
                        xc, yc, RADIUS, DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA + 1,
                                            height - PAD - DIA - BORDER + 1, DIA, DIA);
                eOut = new Ellipse2D.Double(width  - PAD - DIA - BORDER + 1,
                                            height - PAD - DIA - 2*BORDER + 1,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcIn  = new Arc2D.Double(eIn.getBounds2D(),  270.0, 90.0, Arc2D.PIE);
                arcOut = new Arc2D.Double(eOut.getBounds2D(), 270.0, 90.0, Arc2D.PIE);
                arcArea = new Area(arcOut);
                arcArea.subtract(new Area(arcIn));
                rrClip = new RoundRectangle2D.Double(width - PAD - DIA + 1,
                                                     height - PAD - DIA - BORDER + 1, DIA,
                                                     DIA + BORDER, DIA, DIA);
                arcArea.subtract(new Area(rrClip));
                g2.fill(arcArea);
                break;

            case COMPARE:
                g2.setPaint(colorIn);
                g2.fillRoundRect(PAD + BORDER, PAD + BORDER,
                                 width - 2*PAD, height - 2*PAD, DIA, DIA);
                g2.setPaint(getBackground());
                g2.fillRoundRect(PAD, PAD, width - 2*PAD, height - 2*PAD, DIA, DIA);
                g2.setPaint(Color.black);
                g2.drawRoundRect(PAD, PAD, width - 2*PAD, height - 2*PAD, DIA, DIA);
        }
        // diagnostic markers used to highlight constructions
        //g2.setPaint(Color.red);
        //g2.fill(new Ellipse2D.Double(xc - 1, yc - 1, 2, 2));
        //g2.draw(eIn);
        //g2.draw(eOut);
        //g2.draw(rrClip);
        //g2.draw(clipArea);
        //g2.drawRoundRect(PAD, PAD, width - 2*PAD, height - 2*PAD, DIA, DIA);
    }
}

/**
 * JSpinner to select the type of shadow for the ShadeOptionsPanel.
 * The direction is the fully shaded corner, adjacent corners being
 * partially shaded and the opposite corner unshaded.
 * Loaded into the north section of the content pane in main method.
 */
class Shadow4Selector extends JPanel
{
    OffsetShadePanel osPanel;

    public Shadow4Selector(OffsetShadePanel osp)
    {
        osPanel = osp;
        String[] directions = {
            "northeast", "northwest", "southwest", "southeast", "compare"
        };
        final SpinnerListModel model = new SpinnerListModel(directions);
        model.setValue(directions[3]);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(90, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                String value = (String)model.getValue();
                osPanel.shadowVertex = model.getList().indexOf(value);
                osPanel.repaint();
            }
        });
        add(new JLabel("shadow vertex", JLabel.RIGHT));
        add(spinner);
    }
}

/**
 * JSpinners for adjusting the values of PAD, DIA, BORDER and alpha
 * in OffsetShadePanel.
 * Loaded into south section of content pane in main method.
 */
class AdjustmentPanel extends JPanel
{
    OffsetShadePanel osPanel;

    public AdjustmentPanel(OffsetShadePanel osp)
    {
        osPanel = osp;
        final SpinnerNumberModel padModel = new SpinnerNumberModel(30,10,50,1);
        final JSpinner padSpinner = new JSpinner(padModel);
        final SpinnerNumberModel diaModel = new SpinnerNumberModel(40,10,150,2);
        final JSpinner diaSpinner = new JSpinner(diaModel);
        final SpinnerNumberModel borderModel = new SpinnerNumberModel(30,10,50,1);
        final JSpinner borderSpinner = new JSpinner(borderModel);
        final SpinnerNumberModel alphaModel = new SpinnerNumberModel(175,0,255,1);
        final JSpinner alphaSpinner = new JSpinner(alphaModel);
        ChangeListener l = new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                JSpinner spinner = (JSpinner)e.getSource();
                if(spinner == padSpinner)
                    osPanel.PAD = padModel.getNumber().intValue();
                if(spinner == diaSpinner)
                    osPanel.DIA = diaModel.getNumber().intValue();
                if(spinner == borderSpinner)
                    osPanel.BORDER = borderModel.getNumber().intValue();
                if(spinner == alphaSpinner)
                    osPanel.alpha = alphaModel.getNumber().intValue();
                osPanel.repaint();
            }
        };
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5,2,5,2);
        addToPanel(new JLabel("PAD"), padSpinner, gbc, l);
        addToPanel(new JLabel("DIA"), diaSpinner, gbc, l);
        addToPanel(new JLabel("BORDER"), borderSpinner, gbc, l);
        addToPanel(new JLabel("alpha"), alphaSpinner, gbc, l);
    }

    private void addToPanel(JLabel label, JSpinner s,
                            GridBagConstraints gbc, ChangeListener l)
    {
        gbc.anchor = gbc.EAST;
        add(label, gbc);
        gbc.anchor = gbc.WEST;
        add(s, gbc);
        s.addChangeListener(l);
    }
}
