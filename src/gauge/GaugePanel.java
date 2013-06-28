package gauge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GaugePanel extends JPanel {		
	
	private int width = 300;
	private Color color;
	private Color background = Color.WHITE;
	private int gap = 10;
	private int arcWidth = 20;
	private int footerHeight = 20;	
	private float stroke = 1f;
	
	private boolean showMinMax = true;
	private double minValue = 0;
	private double maxValue = 100;
	private double value = 0;
	private String valueLabel = ""; // "per hour", "average"
	private String unit = "";       // measure unit (°C, m/s, kg),  %
	private String title = "";      // "Visitors", "Errors"
			
	public GaugePanel(int width, Color color) {
		super();
		this.width = width;
		this.color = color;		
	}
	
	public GaugePanel(int width, Color color, int minValue, int maxValue) {
		this(width, "", color, minValue, maxValue);
	}
	
	public GaugePanel(int width, String title, Color color, int minValue, int maxValue) {
		this(width, title, color, minValue, maxValue, "");
	}
	
	public GaugePanel(int width, String title, Color color, int minValue, int maxValue, String valueLabel) {
		this(width, title, color, minValue, maxValue, valueLabel, "", Color.WHITE, true);
	}
	
	public GaugePanel(int width, String title, Color color, int minValue, int maxValue, String valueLabel, String unit, Color background, boolean showMinMax) {
		super();
		this.width = width;		
		this.title = title;
		this.color = color;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.valueLabel = valueLabel;
		this.unit = unit;		
		this.background = background;		
		this.showMinMax = showMinMax;
		
		addComponentListener(new ResizeListener());
	}

//	protected void paintComponent(Graphics g) {				
//		
//		Graphics2D g2 = (Graphics2D) g;
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);				
//
//		// for resize
//		gap = getHeight() / 10;		
//		int vWidth = getWidth() > width ? getWidth() : width;		
//		//int vWidth =  width;
//		
//		arcWidth = vWidth / 5;
//		int vHeight = vWidth - 2 * gap - footerHeight;				
//		
//		g2.setPaint(background);
//		Rectangle rectangle = new Rectangle(0, 0, getWidth(), getHeight());
//		g2.fill(rectangle);					
//		
//		// draw initial background color
//		Arc2D externalArc = new Arc2D.Double(gap, gap, vWidth - 2*gap, vHeight, 0, 180, Arc2D.OPEN);
//		Arc2D innerArc = new Arc2D.Double(gap + arcWidth, gap + arcWidth, vWidth - 2*gap - 2* arcWidth, vHeight - 2*arcWidth, 0, 180, Arc2D.OPEN);	
//		Area i1 = new Area(externalArc);
//		Area i2 = new Area(innerArc);
//		i1.subtract(i2);
//		g2.setPaint(new Color(240, 240, 240));		
//		g2.fill(i1);
//				
//		// draw background color depending on gauge's value
//		double val = value;
//		if (value < minValue) {
//			val = minValue;
//		}
//		if (value > maxValue) {
//			val = maxValue;
//		}
//		double range = Math.abs(maxValue-minValue);
//		double delta = Math.abs(minValue - val);
//		double f =  (range - delta) /range;
//		double angle =  180 * f ;			
//		Arc2D externalArcColor = new Arc2D.Double(gap, gap, vWidth - 2*gap, vHeight, angle, 180 - angle, Arc2D.PIE);
//		Arc2D innerArcColor = new Arc2D.Double(gap + arcWidth, gap + arcWidth, vWidth - 2*gap - 2* arcWidth, vHeight - 2*arcWidth, 0, 180, Arc2D.PIE);
//		Area a1 = new Area(externalArcColor);
//		Area a2 = new Area(innerArcColor);
//		a1.subtract(a2);		
//		g2.setPaint(new GradientPaint(0, vHeight, Color.WHITE, vWidth - 2*gap, vHeight - 2*gap, color));		
//		g2.fill(a1);
//		
//		// draw arcs & base lines
//		g2.setPaint(Color.GRAY);	
//		g2.setStroke(new BasicStroke(stroke));
//		g2.draw(externalArc);				
//		g2.draw(innerArc);		
//		g2.drawLine(gap, vWidth/2 -10, gap + arcWidth, vWidth/2 - 10);
//		g2.drawLine(vWidth - gap - arcWidth, vWidth/2 - 10, vWidth - gap, vWidth/2 - 10);		
//		
//		if (showMinMax) {
//			Font font = new Font(g2.getFont().getFontName(), g2.getFont().getStyle(), vHeight / 14);
//			g2.setFont(font);
//			String smin;
//			if (hasNoDecimals(minValue)) {
//				smin = String.valueOf((int) minValue);
//			} else {
//				smin = String.valueOf(minValue);
//			}
//			if ((unit != null) && !unit.isEmpty()) {
//				smin = smin + unit;
//			}
//			int swidth = g2.getFontMetrics().stringWidth(smin);
//			g2.drawString(smin, gap + arcWidth / 2 - swidth / 2, vHeight / 2 + vHeight / 14 + gap + 2);
//
//			String smax;
//			if (hasNoDecimals(maxValue)) {
//				smax = String.valueOf((int) maxValue);
//			} else {
//				smax = String.valueOf(maxValue);
//			}
//
//			if ((unit != null) && !unit.isEmpty()) {
//				smax = smax + unit;
//			}
//			swidth = g2.getFontMetrics().stringWidth(smax);
//			g2.drawString(smax, vWidth - gap - swidth / 2 - arcWidth / 2, vHeight / 2 + vHeight / 14 + gap + 2);
//		}
//		
//		if ((valueLabel != null) && !valueLabel.isEmpty()) {
//			Font font = new Font(g2.getFont().getFontName(), g2.getFont().getStyle(), vHeight/14); 
//			g2.setFont(font);
//			int swidth = g2.getFontMetrics().stringWidth(valueLabel);
//			g2.setPaint(Color.GRAY);
//			g2.drawString(valueLabel, vWidth/2 - swidth/2 , vHeight/2 + vHeight/14 + gap );
//		}
//		
//		Font font = new Font(g2.getFont().getFontName(), Font.BOLD, vHeight/8); 
//		g2.setFont(font);
//		g2.setPaint(Color.BLACK);
//		String svalue;
//		if (hasNoDecimals(value)) {
//			svalue = String.valueOf((int)value);
//		} else {
//			svalue = String.valueOf(value);
//		}
//		if ((unit != null) && !unit.isEmpty()) {
//			svalue = svalue + unit;
//		}
//		int swidth = g2.getFontMetrics().stringWidth(svalue);
//		g2.drawString(svalue, vWidth/2 - swidth/2 , vHeight/2 + gap );
//		
//		if ((title != null) && !title.isEmpty()) {
//			font = new Font(g2.getFont().getFontName(), Font.BOLD, vHeight/12); 
//			g2.setFont(font);
//			swidth = g2.getFontMetrics().stringWidth(title);
//			g2.drawString(title, vWidth/2 - swidth/2 , 3* gap / 2 + g2.getFont().getSize() + vHeight/12 );
//		}				
//	}
	
protected void paintComponent(Graphics g) {				
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);				

		int canWidth = getWidth();
		int canHeight = getHeight();
		int size = canHeight;
		if (10*canWidth/11 <= canHeight) {
		    size = 10*canWidth/11;
		}
				
		int arcWidth = size/2; 
		int x = canWidth/2;
		int y = 8*size/11; 	
		//int radix = size/2 + y;
		
		g2.setPaint(background);
		Rectangle rectangle = new Rectangle(5, 5, getWidth()-10, getHeight()-10);		
		g2.fill(rectangle);			
		
		g2.setPaint(Color.BLACK);
		g2.draw(rectangle);
					
		// draw initial background color
		Arc2D externalArc = new Arc2D.Double(x-size/2, y-size/2, size, size, 0, 180, Arc2D.OPEN);		
		Arc2D innerArc = new Arc2D.Double(x-size/2 + arcWidth/2, y-size/2 + arcWidth/2, size - arcWidth, size - arcWidth, 0, 180, Arc2D.OPEN);	
		Area i1 = new Area(externalArc);
		Area i2 = new Area(innerArc);
		i1.subtract(i2);
		g2.setPaint(new Color(240, 240, 240));		
		g2.fill(i1);
				
		// draw background color depending on gauge's value
		double val = value;
		if (value < minValue) {
			val = minValue;
		}
		if (value > maxValue) {
			val = maxValue;
		}
		double range = Math.abs(maxValue-minValue);
		double delta = Math.abs(minValue - val);
		double f =  (range - delta) /range;
		double angle =  180 * f ;			
		Arc2D externalArcColor = new Arc2D.Double(x-size/2, y-size/2, size, size, angle, 180 - angle, Arc2D.PIE);
		Arc2D innerArcColor = new Arc2D.Double(x-size/2 + arcWidth/2, y-size/2 + arcWidth/2, size - arcWidth, size - arcWidth, 0, 180, Arc2D.PIE);
		Area a1 = new Area(externalArcColor);
		Area a2 = new Area(innerArcColor);
		a1.subtract(a2);		
		g2.setPaint(new GradientPaint(x-size/2, y-size/2, Color.WHITE, x+size/2, y-size/2, color));		
		g2.fill(a1);
						
		// clear any glitches under inner arc
		Arc2D ia = new Arc2D.Double(x-size/2 + arcWidth/2, y-size/2 + arcWidth/2, size - arcWidth, size - arcWidth, 0, 360, Arc2D.PIE);		
		Area ia2 = new Area(ia);				
		g2.setPaint(background);		
		g2.fill(ia2);
		
		// draw arcs & base lines
		g2.setPaint(Color.GRAY);	
		g2.setStroke(new BasicStroke(stroke));
		g2.draw(externalArc);				
		g2.draw(innerArc);		
		g2.drawLine(x-size/2, y, x-size/2 + arcWidth/2, y);
		g2.drawLine(x+size/2 - arcWidth/2, y, x+size/2, y);		
		
		if (showMinMax) {
			Font font = new Font(g2.getFont().getFontName(), g2.getFont().getStyle(), size / 11);
			g2.setFont(font);
			String smin;
			if (hasNoDecimals(minValue)) {
				smin = String.valueOf((int) minValue);
			} else {
				smin = String.valueOf(minValue);
			}
			if ((unit != null) && !unit.isEmpty()) {
				smin = smin + unit;
			}
			int swidth = g2.getFontMetrics().stringWidth(smin);
			g2.drawString(smin, x-size/2 + arcWidth / 4 - swidth / 2, y + size/11);

			String smax;
			if (hasNoDecimals(maxValue)) {
				smax = String.valueOf((int) maxValue);
			} else {
				smax = String.valueOf(maxValue);
			}

			if ((unit != null) && !unit.isEmpty()) {
				smax = smax + unit;
			}
			swidth = g2.getFontMetrics().stringWidth(smax);
			g2.drawString(smax, x + size/2 - arcWidth/4 - swidth / 2 , y + size/11);
		}
		
		if ((valueLabel != null) && !valueLabel.isEmpty()) {
			Font font = new Font(g2.getFont().getFontName(), g2.getFont().getStyle(), size/12); 
			g2.setFont(font);
			int swidth = g2.getFontMetrics().stringWidth(valueLabel);
			g2.setPaint(Color.GRAY);
			g2.drawString(valueLabel, x - swidth/2 , y + size/12);
		}
		
		Font font = new Font(g2.getFont().getFontName(), Font.BOLD, size/8); 
		g2.setFont(font);
		g2.setPaint(Color.BLACK);
		String svalue;
		if (hasNoDecimals(value)) {
			svalue = String.valueOf((int)value);
		} else {
			svalue = String.valueOf(value);
		}
		if ((unit != null) && !unit.isEmpty()) {
			svalue = svalue + unit;
		}
		int swidth = g2.getFontMetrics().stringWidth(svalue);
		g2.drawString(svalue, x - swidth/2 , y );
		
		if ((title != null) && !title.isEmpty()) {
			font = new Font(g2.getFont().getFontName(), Font.BOLD, size/11); 
			g2.setFont(font);
			swidth = g2.getFontMetrics().stringWidth(title);
			g2.drawString(title, x - swidth/2 , y - 7*size/11 + arcWidth/2 + size/11 );
		}				
	}
				
	private boolean hasNoDecimals(double d) {
		return ((int)d == d);
	}
	
	public void setValue(double value) {	
		this.value = value;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {					
					repaint();				
				}			
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void animate() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				double val = minValue;
				double setValue = value;
				double step;	
				if ((setValue > -1) && (setValue < 1)) {
					step = 0.1;
				} else {
					step = (int) (Math.abs(value - minValue) / 10);
				}
				while (val <= setValue) {
					setValue(val);					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					val = getDouble2(val + step);
				}
				setValue(setValue);		
				
			}
			
		};
		new Thread(r).start();
	}
	
	private double getDouble2(double d) {
		int i = (int)(d * 100);
		return (double)i / 100;
	}
	
	public static void main(String[] args) {
				
		JFrame vFrame = new JFrame(GaugePanel.class.getName());
		vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		//GaugePanel panel =  new GaugePanel(230, "Visitors", Color.BLUE, 0, 150, "per minute");
		GaugePanel panel = new GaugePanel(250, "Temperature", Color.ORANGE, -20, 40, "average", "°C", new Color(236,233,216), true);
	    panel.setValue(28);	
	    Dimension dim = new Dimension(200, 120);
	    panel.setPreferredSize(dim);	
	    vFrame.getContentPane().add(panel);	
	    vFrame.pack();
		vFrame.setVisible(true);		
		
//		int width = 230;
//		Dimension dim = new Dimension(230, 180);
//		
//		GaugePanel[] panels = new GaugePanel[6];
//						
//		panels[0] = new GaugePanel(width, "Visitors", Color.BLUE, 0, 150, "per minute");
//		panels[0].setValue(71);			
//		
//		panels[1] = new GaugePanel(width, "Temperature", Color.ORANGE, -40, 60, "", "°C", new Color(230, 244, 247), true);
//		panels[1].setValue(-10);
//		
//		panels[2] = new GaugePanel(width, "System Indicator", Color.RED, -1, 1, "average", "T", new Color(235, 253, 228), true);
//		panels[2].setValue(0.89);
//		
//		panels[3] = new GaugePanel(width, "Memory", Color.MAGENTA, 0, 100, "", "%", Color.WHITE, false);
//		panels[3].setValue(80);
//		
//		panels[4] = new GaugePanel(width, "Efficiency", Color.YELLOW, 0, 100, "weekly", "%", new Color(228, 237, 204), false);
//		panels[4].setValue(72.39);
//		
//		panels[5] = new GaugePanel(width, "Calls",  new Color(204, 102, 102), 0, 1000, "per day", "",  new Color(249, 239, 217), true);
//		panels[5].setValue(583);
//		
//		vFrame.getContentPane().setLayout(new GridLayout(2, 3));
//		for (GaugePanel panel : panels) {
//			panel.setPreferredSize(dim);								
//			vFrame.getContentPane().add(panel);		
//		}
//		vFrame.pack();
//		vFrame.setVisible(true);		
//		
//		for (GaugePanel panel : panels) {
//			panel.animate();
//		}
		
	}
	
	public class ResizeListener extends ComponentAdapter {

		  @Override
		  public void componentResized(ComponentEvent evt) {
			  //System.out.println("repaint");
		     repaint();
		  }

		}

}
