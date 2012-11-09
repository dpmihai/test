package gradient;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Circle extends JPanel {

	private int x = 10;
	private int d = 3;
	private int radius = 30;
	private Color color = Color.RED.darker();

	private Ellipse2D.Double circle = new Ellipse2D.Double(x, x, x + radius, x
			+ radius);
	private Ellipse2D.Double border = new Ellipse2D.Double(x - d, x - d, x
			+ radius + 2 * d, x + radius + 2 * d);

	
	private GradientPaint gradient = new GradientPaint(15, 15, Color.WHITE, 40,
			40, color, true); // true means to repeat pattern

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		drawGradientCircle(g2d);

	}

	protected void drawGradientCircle(Graphics2D g2d) {
		g2d.setPaint(gradient);
		g2d.fill(circle);
		g2d.setPaint(Color.gray);
		g2d.draw(circle);
		g2d.setPaint(Color.gray);
		g2d.draw(border);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new Circle());
		f.setSize(400, 300);
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
