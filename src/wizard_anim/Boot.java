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


class Boot extends JFrame
{	private final static int PLOT_W = 250;	// Width of each track
	private final static int PLOT_H = 10;	// Height of each track
	private final static int RUNNING_TIME=2500;	// Time to cross window

	private double animPos;					// Current position in animation
	private Plot plot;						// Custom Swing component
	private Path[] paths;					// Paths to guide tracks

	Boot(final Path[] p)
	{	super("Boot");

		paths=p;

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
				long diff = l-startTime;
				animPos = (double)diff/(double)runningTime;

				Boot.this.plot.rebuild();
			}
		};
		timer.scheduleAtFixedRate(tt,20,20);
	}

	// Initialise a GUI to get the paths/velocity stuff.  A simple custom
	// component called Plot on a single panel.
	private void _initGUI()
	{	plot = new Plot();

		//JPanel p = new JPanel(new BorderLayout());
		//p.setBackground(Color.BLACK);
		//p.add(plot,BorderLayout.NORTH);

		this.getContentPane().add(plot);
		this.pack();  this.setVisible(true);
	}

	// Plot draws all the
	private class Plot extends JComponent
	{	private Dimension componentDim;		// Total size of component
		private VolatileImage backImg;		// Double buffer
		private Graphics backG;				// Double buffer graphics

		Plot()
		{	componentDim = new Dimension(PLOT_W,PLOT_H*paths.length);
			backImg = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.createCompatibleVolatileImage(componentDim.width,componentDim.height);
			backG = backImg.getGraphics();
		}
		public Dimension getPreferredSize() { return componentDim; }
		public Dimension getMaximumSize() { return componentDim; }
		public Dimension getMinimumSize() { return componentDim; }

		public void paintComponent(Graphics g)
		{	g.drawImage(backImg,0,0,null);
		}

		public void rebuild()
		{	backG.setColor(Color.BLACK);
			backG.fillRect(0,0,backImg.getWidth(),backImg.getHeight());

			for(int z=0;z<paths.length;z++)
			{	Path path = paths[z];

				double[] d = path.getCoords(animPos);
				if(path instanceof MemoryPath)
				{	MemoryPath mp = (MemoryPath)path;
					for(int i=1-mp.getMemorySize();i<=0;i++)
					{	int col = 0xff0000 + (0x110000*i);
						d = mp.getPreviousCoords(i);
						backG.setColor(new Color(col));
						backG.fillRect((int)(d[0]-2),(int)(d[1]-2) , 5,5);
					}
				}
				else
				{	backG.setColor(Color.RED);
					backG.fillRect((int)(d[0]-2),(int)(d[1]-2) , 5,5);
				}
			}

			this.repaint();
		}
	}

	public static void main(String[] args)
	{	/*SequencedVelocity sv = new SequencedVelocity();
		sv.add(new SoftAccelerationVelocity(),0.5d);
		sv.add(new SoftDecelerationVelocity(),0.5d);*/

		int y=PLOT_H/2;
		Path[] p = new Path[7];
		// Straight line, no frills.
		p[0] = new StraightLinePath(0,y , PLOT_W,y , new ConstantVelocity());
		y+=PLOT_H;
		// Straight line, memory of ten, progressively faster
		p[1] = new MemoryPath
		(	new StraightLinePath(0,y , PLOT_W,y , new SoftAccelerationVelocity()),
			10
		);
		y+=PLOT_H;
		// Drunken line varying by five, memory of ten, progressively slower
		p[2] = new MemoryPath
		(	new DrunkenLinePath(0,y, PLOT_W,y , new SoftDecelerationVelocity() ,5),
			10
		);
		y+=PLOT_H;
		// Straight line, speeds up then slows down
		p[3] = new StraightLinePath
		(	0,y, PLOT_W,y ,
			new SequencedVelocity
			(	new SoftAccelerationVelocity(),
				new SoftDecelerationVelocity()
			)
		);
		y+=PLOT_H;
		// Straight line, slows down then speeds up
		p[4] = new StraightLinePath
		(	0,y, PLOT_W,y ,
			new SequencedVelocity
			(	new SoftDecelerationVelocity(),
				new SoftAccelerationVelocity()
			)
		);
		y+=PLOT_H;

		p[5] = new MemoryPath
		(	new SequencedPath
			(	new StraightLinePath
				(	0,y, PLOT_W,y ,
					new SequencedVelocity
					(	new SoftAccelerationVelocity(),
						new SoftDecelerationVelocity()
					)
				),
				new StraightLinePath
				(	PLOT_W,y, 0,y ,
					new SequencedVelocity
					(	new SoftAccelerationVelocity(),
						new SoftDecelerationVelocity()
					)
				)
			) ,
			10
		);
		y+=PLOT_H;
		// Straight line, gravity with bounce
		p[6] = new StraightLinePath
		(	0,y , PLOT_W,y , new GravityBounceVelocity()
		);
		y+=PLOT_H;


		/*double[] arr = new double[2];
		for(double d=0d ; d<=1.0001d ; d+=0.05d)
		{	for(int i=0;i<p.length;i++)
			{	arr=p[i].getCoords(d,arr);
				System.out.printf("(%3d,%3d)   ",(long)arr[0],(long)arr[1]);
			}
			System.out.println("");
		}*/

		new Boot(p);
	}
}
