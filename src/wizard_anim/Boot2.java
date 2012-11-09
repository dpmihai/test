package wizard_anim;

import wizard_anim.movement.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


class Boot2 extends JFrame
{	private final static int PLOT_W = 500;	// Width of each track
	private final static int PLOT_H = 350;	// Height of each track
	private final static int RUNNING_TIME=2500;	// Time to cross window

	private long diff;
	private double animPos;					// Current position in animation
	private Plot plot;						// Custom Swing component
	private Path path;						// Paths to guide tracks

	Boot2(final Path p)
	{	super("Boot2");

		path=p;

		// Create GUI
		try
		{	Runnable r = new Runnable()
			{	public void run() { _initGUI(); }
			};
			SwingUtilities.invokeAndWait(r);
		}catch(Exception e) { e.printStackTrace(); }

		// Create timer for animation
		Timer timer = new Timer();
		TimerTask tt = new TimerTask()
		{	private long startTime=0;
			private long runningTime=RUNNING_TIME;

			public void run()
			{	long l=System.currentTimeMillis();

				// If over runningTime, start again
				if(l-startTime>runningTime)  startTime=l;

				// Work out how much time has elapsed since start, and
				// what proportion that is of the total running time, then
				// set the animation position accordingly and redraw the
				// animation
				diff = l-startTime;
				animPos = (double)diff/(double)runningTime;

				Boot2.this.plot.rebuild();
			}
		};
		timer.scheduleAtFixedRate(tt,20,20);
	}

	// Initialise a GUI to get the paths/velocity stuff.  A simple custom
	// component called Plot on a single panel.
	private void _initGUI()
	{	plot = new Plot();
		this.getContentPane().add(plot);
		this.pack();  this.setVisible(true);
	}

	// Plot draws all the
	private class Plot extends JComponent
	{	private Dimension componentDim;		// Total size of component
		private VolatileImage backImg;		// Double buffer
		private Graphics backG;				// Double buffer graphics

		Plot()
		{	componentDim = new Dimension(PLOT_W,PLOT_H);
			backImg = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.createCompatibleVolatileImage(componentDim.width,componentDim.height);
			backG = backImg.getGraphics();
			backG.setColor(Color.BLACK);
			backG.fillRect(0,0 , componentDim.width,componentDim.height);
		}
		public Dimension getPreferredSize() { return componentDim; }
		public Dimension getMaximumSize() { return componentDim; }
		public Dimension getMinimumSize() { return componentDim; }

		public void paintComponent(Graphics g)
		{	g.drawImage(backImg,0,0,null);
		}

		public void rebuild()
		{	double[] d = path.getCoords(animPos);
			backG.setColor(Color.GREEN);
			backG.fillRect((int)diff/5,(int)(d[1]-1) , 3,3);
			this.repaint();
		}
	}

	public static void main(String[] args)
	{	// Straight line, gravity with bounce
		Path p = new StraightLinePath
		(	0,0 , 0,PLOT_H , new GravityBounceVelocity()
		);

		new Boot2(p);
	}
}
