package robot;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 5, 2006
 * Time: 2:52:10 PM
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MouseWrap extends JFrame {
	protected static final GraphicsDevice SCREEN = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	protected static final int WIDTH = SCREEN.getDisplayMode().getWidth();
	protected static final int HEIGHT = SCREEN.getDisplayMode().getHeight();
	protected static final int CENTER_X = WIDTH>>1;
	protected static final int CENTER_Y = HEIGHT>>1;
	protected Robot robot;
	protected JLabel labelX,labelY;
	protected int mouseX,mouseY,wrapX,wrapY;
	public MouseWrap() {
		super("MouseWrap");
		robot = null;
		try {
			robot = new Robot(SCREEN);
		}
		catch (AWTException e) {
			e.printStackTrace();
			System.exit(1);
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(WIDTH,HEIGHT));
		setResizable(false);
		setUndecorated(true);
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.CENTER));
		labelX = new JLabel("X offset: 0");
		labelY = new JLabel("Y offset: 0");
		p.add(labelX);
		p.add(new JLabel("|"));
		p.add(labelY);
		setLocation(CENTER_X-(getWidth()>>1),CENTER_Y-(getHeight()>>1));
		mouseX = CENTER_X;
		mouseY = CENTER_Y;
		wrapX = 0;
		wrapY = 0;
		robot.mouseMove(CENTER_X,CENTER_Y);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		getContentPane().add(p,BorderLayout.CENTER);
		setVisible(true);
		try {
			SCREEN.setFullScreenWindow(this);
			loop();
		}
		finally {
			SCREEN.setFullScreenWindow(null);
		}
	}
	protected void loop() {
		while (true) {
			requestFocus();
			repaint();
			Thread.yield();
		}
	}
	public void processEvent(AWTEvent e) {
		if (e.getSource() == robot) { // ignore events from robot
			robot.setAutoWaitForIdle(true);
			super.processEvent(e);
		}
		else if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent)e;
			if (me.getID() == MouseEvent.MOUSE_MOVED) {
				int x = me.getX();
				int y = me.getY();
				int newx = x,newy = y;
				if (x <= 0) {
					newx = WIDTH+x;
					wrapX--;
				}
				if (x >= WIDTH-1) {
					newx = x-WIDTH;
					wrapX++;
				}
				if (y <= 0) {
					newy = WIDTH+y;
					wrapY--;
				}
				if (y >= HEIGHT-1) {
					newy = y-WIDTH;
					wrapY++;
				}
				robot.mouseMove(newx,newy);
				int xoffset = CENTER_X+WIDTH*wrapX+newx;
				int yoffset = CENTER_X+HEIGHT*wrapY+newy;
				labelX.setText("X Offset: "+xoffset);
				labelY.setText("Y Offset: "+yoffset);
			}
		}
		else if (e instanceof KeyEvent) {
			System.exit(0);
		}
		super.processEvent(e);
	}
	public static void main(String args[]) {
		new MouseWrap();
	}
}