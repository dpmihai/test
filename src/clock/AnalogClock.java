package clock;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class AnalogClock extends JComponent implements Runnable {

	public AnalogClock() {
		(new Thread(this)).start();
	}

	public void run() {
		try {
			for (;;) {
				Thread.sleep(500);
				repaint();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void paint(Graphics graphics) {
		super.paint(graphics);

		// calculate the time values
		// long t = System.currentTimeMillis();
		long t = (new Date()).getTime();
		// milliseconds in the current minute
		long minute = t % MILLIS_IN_MINUTE;
		// seconds in the current minute
		long secs_min = minute / 1000L;
		// milliseconds in the current hour
		long hour = t % MILLIS_IN_HOUR;
		// milliseconds in the current half day
		long hday = t % MILLIS_IN_HALF_DAY;

		// calculate the clock hand angles
		double sec_angle = -Math.PI / 2 + secs_min * SECS_IN_MINUTE_TO_ANGLE;
		double min_angle = -Math.PI / 2 + hour * MILLIS_IN_HOUR_TO_ANGLE;
		double hour_angle = -Math.PI / 2 + hday * MILLIS_IN_HALF_DAY_TO_ANGLE;

		// Draw the hands
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		size = getSize(size);
		insets = getInsets(insets);
		int radius = Math.min(size.width - insets.left - insets.top, size.height - insets.top - insets.bottom) / 2;
		g.translate((double) size.width / 2D, (double) size.height / 2D);

		// draw the seconds
		g.setColor(Color.red);
		g.setStroke(SEC_STROKE);
		g.rotate(sec_angle);
		g.drawLine(0, 0, radius - 6, 0);
		g.rotate(-sec_angle);

		// draw the minutes
		g.setColor(Color.black);
		g.setStroke(MIN_STROKE);
		g.rotate(min_angle);
		g.drawLine(0, 0, radius - 10, 0);
		g.rotate(-min_angle);

		// draw the hours
		g.setColor(Color.black);
		g.setStroke(HOUR_STROKE);
		g.rotate(hour_angle);
		g.drawLine(0, 0, radius / 2, 0);
		g.rotate(-hour_angle);

		// draw the perimeter
		g.setColor(Color.darkGray);
		g.drawOval(-radius + 2, -radius + 2, 2 * radius - 4, 2 * radius - 4);
	}

	private static long MILLIS_IN_MINUTE = 60L * 1000L;
	private static long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60L;
	private static long MILLIS_IN_HALF_DAY = MILLIS_IN_HOUR * 12L;

	private static double SECS_IN_MINUTE_TO_ANGLE = (2 * Math.PI / 60L);
	private static double MILLIS_IN_HOUR_TO_ANGLE = (2 * Math.PI / MILLIS_IN_HOUR);
	private static double MILLIS_IN_HALF_DAY_TO_ANGLE = (2 * Math.PI / MILLIS_IN_HALF_DAY);

	private static Stroke SEC_STROKE = new BasicStroke();
	private static Stroke MIN_STROKE = new BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static Stroke HOUR_STROKE = MIN_STROKE;

	private Dimension size = null;
	private Insets insets = new Insets(0, 0, 0, 0);

	public static void main(String[] args) {
		JFrame f = new JFrame("Clock");
		AnalogClock clock = new AnalogClock();
		f.getContentPane().add(clock);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setBounds(50, 50, 200, 200);
		f.setVisible(true);
	}

}
