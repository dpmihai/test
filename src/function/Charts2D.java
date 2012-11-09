package function;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class Charts2D extends JFrame
{
  public Charts2D() {
    super("2D Charts");
    setSize(720, 280);
    getContentPane().setLayout(new GridLayout(1, 3, 10, 0));
    getContentPane().setBackground(Color.white);

    int nData = 8;
    int[] xData = new int[nData];
    int[] yData = new int[nData];
    for (int k=0; k<nData; k++) {
      xData[k] = k;
      yData[k] = (int)(Math.random()*100);
      if (k > 0)
        yData[k] = (yData[k-1] + yData[k])/2;
    }

    JChart2D chart = new JChart2D(
      JChart2D.CHART_LINE, nData, xData,
      yData, "Line Chart");
    chart.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_MITER));
    chart.setLineColor(new Color(0, 128, 128));
    getContentPane().add(chart);

    chart = new JChart2D(JChart2D.CHART_COLUMN,
      nData, xData, yData, "Column Chart");
    GradientPaint gp = new GradientPaint(0, 100,
      Color.white, 0, 300, Color.blue, true);
    chart.setGradient(gp);
    chart.setEffectIndex(JChart2D.EFFECT_GRADIENT);
    chart.setDrawShadow(true);
    getContentPane().add(chart);

    chart = new JChart2D(JChart2D.CHART_PIE, nData, xData,
      yData, "Pie Chart");
    ImageIcon icon = new ImageIcon("hubble.gif");
    chart.setForegroundImage(icon.getImage());
    chart.setEffectIndex(JChart2D.EFFECT_IMAGE);
    chart.setDrawShadow(true);
    getContentPane().add(chart);

    WindowListener wndCloser = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
    addWindowListener(wndCloser);

    setVisible(true);
  }

  public static void main(String argv[]) {
    new Charts2D();
  }
}

class JChart2D extends JPanel
{
  public static final int CHART_LINE = 0;
  public static final int CHART_COLUMN = 1;
  public static final int CHART_PIE = 2;

  public static final int EFFECT_PLAIN = 0;
  public static final int EFFECT_GRADIENT = 1;
  public static final int EFFECT_IMAGE = 2;

  protected int m_chartType = CHART_LINE;
  protected JLabel m_title;
  protected ChartPanel m_chart;

  protected int m_nData;
  protected int[] m_xData;
  protected int[] m_yData;
  protected int m_xMin;
  protected int m_xMax;
  protected int m_yMin;
  protected int m_yMax;
  protected double[] m_pieData;

  protected int  m_effectIndex = EFFECT_PLAIN;
  protected Stroke m_stroke;
  protected GradientPaint m_gradient;
  protected Image  m_foregroundImage;
  protected Color  m_lineColor = Color.black;
  protected Color  m_columnColor = Color.blue;
  protected int  m_columnWidth = 12;
  protected boolean m_drawShadow = false;

  public JChart2D(int type, int nData,
   int[] yData, String text) {
    this(type, nData, null, yData, text);
  }

  public JChart2D(int type, int nData, int[] xData,
   int[] yData, String text) {
    super(new BorderLayout());
    setBackground(Color.white);
    m_title = new JLabel(text, JLabel.CENTER);
    add(m_title, BorderLayout.NORTH);

    m_chartType = type;

    if (xData==null) {
      xData = new int[nData];
      for (int k=0; k<nData; k++)
        xData[k] = k;
    }
    if (yData == null)
      throw new IllegalArgumentException(
      "yData can't be null");
    if (nData > yData.length)
      throw new IllegalArgumentException(
      "Insufficient yData length");
    if (nData > xData.length)
      throw new IllegalArgumentException(
      "Insufficient xData length");
    m_nData = nData;
    m_xData = xData;
    m_yData = yData;

    m_xMin = m_xMax = 0;  // To include 0 into the interval
    m_yMin = m_yMax = 0;
    for (int k=0; k<m_nData; k++) {
      m_xMin = Math.min(m_xMin, m_xData[k]);
      m_xMax = Math.max(m_xMax, m_xData[k]);
      m_yMin = Math.min(m_yMin, m_yData[k]);
      m_yMax = Math.max(m_yMax, m_yData[k]);
    }
    if (m_xMin == m_xMax)
      m_xMax++;
    if (m_yMin == m_yMax)
      m_yMax++;

    if (m_chartType == CHART_PIE) {
      double sum = 0;
      for (int k=0; k<m_nData; k++) {
        m_yData[k] = Math.max(m_yData[k], 0);
        sum += m_yData[k];
      }
      m_pieData = new double[m_nData];
      for (int k=0; k<m_nData; k++)
        m_pieData[k] = m_yData[k]*360.0/sum;
    }

    m_chart = new ChartPanel();
    add(m_chart, BorderLayout.CENTER);
  }

  public void setEffectIndex(int effectIndex) {
    m_effectIndex = effectIndex;
    repaint();
  }

  public int getEffectIndex() { return m_effectIndex; }

  public void setStroke(Stroke stroke) {
    m_stroke = stroke;
    m_chart.repaint();
  }

  public void setForegroundImage(Image img) {
    m_foregroundImage = img;
    repaint();
  }

  public Image getForegroundImage() { return m_foregroundImage; }

  public Stroke getStroke() { return m_stroke; }

  public void setGradient(GradientPaint gradient) {
    m_gradient = gradient;
    repaint();
  }

  public GradientPaint getGradient() { return m_gradient; }

  public void setColumnWidth(int columnWidth) {
    m_columnWidth = columnWidth;
    m_chart.calcDimensions();
    m_chart.repaint();
  }

  public int getColumnWidth() { return m_columnWidth; }

  public void setColumnColor(Color c) {
    m_columnColor = c;
    m_chart.repaint();
  }

  public Color getColumnColor() { return m_columnColor; }

  public void setLineColor(Color c) {
    m_lineColor = c;
    m_chart.repaint();
  }

  public Color getLineColor() { return m_lineColor; }

  public void setDrawShadow(boolean drawShadow) {
    m_drawShadow = drawShadow;
    m_chart.repaint();
  }

  public boolean getDrawShadow() { return m_drawShadow; }

  class ChartPanel extends JComponent
  {
    int m_xMargin = 5;
    int m_yMargin = 5;
    int m_pieGap = 10;
    int space = 100;

    int m_x;
    int m_y;
    int m_w;
    int m_h;

    ChartPanel() {
      enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

    protected void processComponentEvent(ComponentEvent e) {
      calcDimensions();
    }

    public void calcDimensions() {
      Dimension d = getSize();
      m_x = m_xMargin;
      m_y = m_yMargin;
      m_w = d.width-2*m_xMargin;
      m_h = d.height-2*m_yMargin;
      if (m_chartType == CHART_COLUMN) {
        m_x += m_columnWidth/2;
        m_w -= m_columnWidth;
      }
    }

    public int xChartToScreen(int x) {
      return m_x + (x-m_xMin)*m_w/(m_xMax-m_xMin);
    }

    public int yChartToScreen(int y) {
      return m_y + (m_yMax-y)*m_h/(m_yMax-m_yMin);
    }

    public void paintComponent(Graphics g) {
      int x0 = 0;
      int y0 = 0;
      if (m_chartType != CHART_PIE) {
        g.setColor(Color.black);
        x0 = xChartToScreen(0);
        g.drawLine(x0, m_y , x0, m_y+m_h);
        y0 = yChartToScreen(0);
        g.drawLine(m_x, y0, m_x+m_w, y0);
      }

      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

      if (m_stroke != null)
        g2.setStroke(m_stroke);

      GeneralPath path = new GeneralPath();
      switch (m_chartType) {
      case CHART_LINE:
        g2.setColor(m_lineColor);
        path.moveTo(xChartToScreen(m_xData[0]),
          yChartToScreen(m_yData[0]));
        for (int k=1; k<m_nData; k++)
          path.lineTo(xChartToScreen(m_xData[k]),
            yChartToScreen(m_yData[k]));
        g2.draw(path);
        break;

      case CHART_COLUMN:
        for (int k=0; k<m_nData; k++) {
          m_xMax ++;
          int x = xChartToScreen(m_xData[k]);
          int w = m_columnWidth;
          int y1 = yChartToScreen(m_yData[k]);
          int y = Math.min(y0, y1);
          int h = Math.abs(y1 - y0);
          Shape rc = new Rectangle2D.Double(x, y, w, h);
          path.append(rc, false);
          m_xMax --;
        }

        if (m_drawShadow) {
          AffineTransform s0 = new AffineTransform(
            1.0, 0.0, 0.0, -1.0, x0, y0);
          s0.concatenate(AffineTransform.getScaleInstance(
            1.0, 0.5));
          s0.concatenate(AffineTransform.getShearInstance(
            0.5, 0.0));
          s0.concatenate(new AffineTransform(
            1.0, 0.0, 0.0, -1.0, -x0, y0));
          g2.setColor(Color.gray);
          Shape shadow = s0.createTransformedShape(path);
          g2.fill(shadow);
        }

        if (m_effectIndex==EFFECT_GRADIENT &&
          m_gradient != null) {
          g2.setPaint(m_gradient);
          g2.fill(path);
        }
        else if (m_effectIndex==EFFECT_IMAGE &&
          m_foregroundImage != null)
          fillByImage(g2, path, 0);
        else {
          g2.setColor(m_columnColor);
          g2.fill(path);
        }
        g2.setColor(m_lineColor);
        g2.draw(path);
        break;

      case CHART_PIE:
        double start = 0.0;
        double finish = 0.0;
        int ww = m_w - 2*m_pieGap;
        int hh = m_h - 2*m_pieGap;
        if (m_drawShadow) {
          ww -= m_pieGap;
          hh -= m_pieGap;
        }

        for (int k=0; k<m_nData; k++) {
          finish = start+m_pieData[k];
          double f1 = Math.min(90-start, 90-finish);
          double f2 = Math.max(90-start, 90-finish);
          Shape shp = new Arc2D.Double(m_x, m_y, ww, hh,
            f1, f2-f1, Arc2D.PIE);
          double f = (f1 + f2)/2*Math.PI/180;
          AffineTransform s1 = AffineTransform.
            getTranslateInstance(m_pieGap*Math.cos(f),
            -m_pieGap*Math.sin(f));
          s1.translate(m_pieGap, m_pieGap);
          Shape piece = s1.createTransformedShape(shp);
          path.append(piece, false);
          start = finish;
        }

        if (m_drawShadow) {
          AffineTransform s0 = AffineTransform.
            getTranslateInstance(m_pieGap, m_pieGap);
          g2.setColor(Color.gray);
          Shape shadow = s0.createTransformedShape(path);
          g2.fill(shadow);
        }

        if (m_effectIndex==EFFECT_GRADIENT && m_gradient != null) {
          g2.setPaint(m_gradient);
          g2.fill(path);
        }
        else if (m_effectIndex==EFFECT_IMAGE &&
         m_foregroundImage != null)
          fillByImage(g2, path, 0);
        else {
          g2.setColor(m_columnColor);
          g2.fill(path);
        }

        g2.setColor(m_lineColor);
        g2.draw(path);
        break;
      }
    }

    protected void fillByImage(Graphics2D g2,
     Shape shape, int xOffset) {
      if (m_foregroundImage == null)
        return;
      int wImg = m_foregroundImage.getWidth(this);
      int hImg = m_foregroundImage.getHeight(this);
      if (wImg <=0 || hImg <= 0)
        return;
      g2.setClip(shape);
      Rectangle bounds = shape.getBounds();
      for (int xx = bounds.x+xOffset;
       xx < bounds.x+bounds.width; xx += wImg)
        for (int yy = bounds.y; yy < bounds.y+bounds.height;
         yy += hImg)
          g2.drawImage(m_foregroundImage, xx, yy, this);
    }
  }
}
