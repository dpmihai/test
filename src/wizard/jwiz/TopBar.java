package wizard.jwiz;

import wizard_anim.movement.AnimationController;
import wizard_anim.movement.GravityBounceVelocity;
import wizard_anim.movement.MemoryPath;
import wizard_anim.movement.StraightLinePath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;


class TopBar extends JComponent
{	private FontMetrics fontMet;			// Metrics for drawing labels
	private BufferedImage unselectedImg;	// Unselected image buffer
	private BufferedImage selectedImg;		// Selected image buffer

	private Color unselectedLow;			// Lowlight for unselected tab
	private Color unselectedHigh;			// Highlight for unselected tab
	private Color unselectedMid;			// Midpoint for unselected tab
	private Color unselectedText;			// Text for unselected tab
	private Color selectedLow;				// Lowlight for selected tab
	private Color selectedHigh;				// Midpoint for selected tab
	private Color selectedMid;				// Highlight for selected tab
	private Color selectedText;				// Text for selected tab

	private JWizardPane parent;				// Parent component
	private int tab=0;						// Current tab (FIXME: local copy - not good!)
	private int currentX=0;					// Pixel position of bar
	private String[] labels;				// Tab labels
	private Timer timer;					// Animation timer
	private TabTimer ttask;					// Current animation timer task
	private VolatileImage backImg;			// Double buffer
	private Graphics2D backImgG;			// Double buffer graphics

	// Preferred dimensions
	private final static Dimension PREF_DIM = new Dimension(400,30);

	private final static int FRAME_DELAY=20;
	private final static int ANIM_DURATION=1000;


	TopBar(JWizardPane wiz,String[] l)
	{	this.parent=wiz;  this.labels=l;
		this.setOpaque(true);

		// Default colours.
		// FIXME, this is a horrible horrible hack! :(  Colours should be
		// read from usual Swing properties.  And they would be too, if
		// I could have been arsed!
		if(JWizardPane.BLACK)
		{	unselectedLow = new Color(0x000044);
			unselectedMid = new Color(0x330099);
			unselectedHigh = new Color(0xAACCFF);
			unselectedText = Color.WHITE;
			selectedLow = new Color(0xFF2211);
			selectedMid = new Color(0xFF8833);
			selectedHigh = new Color(0xFFFF22);
			selectedText = Color.YELLOW;
//            selectedLow = new Color(80,129,135);
//			selectedMid = new Color(80,129,135);
//			selectedHigh = new Color(234,192,240);
//			selectedText = Color.YELLOW;
        }
		else
		{	unselectedLow = new Color(0xbbccdd);
			unselectedMid = new Color(0x330099);
			unselectedHigh = new Color(0xFFFFFF);
			unselectedText = Color.BLACK;
			selectedLow = new Color(0x6382bd);
			selectedMid = new Color(0xFF8833);
			selectedHigh = new Color(0xFFFFFF);
			selectedText = Color.WHITE;
		}
		// Create a timer instance and something to run on it.
		ttask = new TabTimer();
		timer = new Timer(FRAME_DELAY,ttask);

		// Install a mouse listener so we can report clicks back to the
		// JWizardPane to change tabs
		this.addMouseListener
		(	new MouseAdapter()
			{	public void mousePressed(MouseEvent ev)
				{	int tw=(int)(getSize().getWidth())/labels.length;
					int i=ev.getX()/tw;
					if(i<labels.length && i!=tab)  parent.setTab(i);
				}
			}
		);
	}

	public void setFont(Font f)
	{	super.setFont(f);
		fontMet = null;
	}

	void setTab(int t)
	{	// Kill a current animation
		if(timer.isRunning())  timer.stop();

		// Set new tab pos and start fresh animation
		tab=t;
		ttask.reset();
		timer.start();
	}

	private class TabTimer implements ActionListener
	{	boolean backwards;					// Is the anim moving leftwards?
		MemoryPath path;					// Movement path
		AnimationController anim;			// Controls time

		// Called when the animation needs to restart
		void reset()
		{	// Work out where the bar should stop (pixels)
			int tw=(int)(getSize().getWidth())/labels.length;
			int startX=currentX , endX=tw*(tab+1);
			backwards=(startX>endX);
			// WANT THE ORIGINAL BEHAVIOUR?  Swap the comments over on the
			// StraightLinePath's below to switch off the bounce.
			path = new MemoryPath
			(	//new StraightLinePath(currentX,0,endX,0 , JWizardPane.SOFT_VELOCITY) ,
				new StraightLinePath(currentX,0,endX,0 , new GravityBounceVelocity()) ,
				11
			);
			anim = new AnimationController(ANIM_DURATION);
			anim.start();
		}

		// Called each loop through the animation
		public void actionPerformed(ActionEvent ev)
		{	double time = anim.getMoment();
			if(time>=1.0d)
			{	// Last frame?
				// Size of each tab multipled by new tab pos to
				// find the end pixel width of the bar.
				int tw=(int)(getSize().getWidth())/labels.length;
				currentX=tw*(tab+1);

				// HOWEVER, we don't want the animation to stop immediately
				// because we still have a trail to collapse, so carry on
				// collecting co-ords for 20% overtime.

				// FIXME: movement API should have a better way of doing this!
				// Seriously!!  Eg: a StationaryPath for pauses, weighted in a
				// SequencedPath for only the last part of the movement(???)

				// FIXME: there should be a better way of chewing through the
				// remaining 'trails' in a memory path without having to make
				// bogus getCoords() calls.  advanceBuffer() perhaps???
				path.getCoords(0.99999d);
				if(time>=1.2d)
				{	// -----End timer
					timer.stop();
					path.reset();
				}
			}
			else
			{	// Not last frame?
				double d[] = path.getCoords(time);
				currentX=(int)d[0];
			}
			TopBar.this._paintComponent();
			TopBar.this.repaint();
		}
	}

	public void paintComponent(Graphics g)
	{	if(_invalid())  _paintComponent();
		g.drawImage(backImg,0,0,null);
	}

	private void _paintComponent()
	{	// Get our font metrics if necessary
		if(fontMet==null)
		{	super.setFont(getFont().deriveFont(Font.BOLD));
			fontMet = getFontMetrics(getFont());
		}

		// If the component size has changed, rebuild the buffers
		Dimension d=getSize();
		int w=(int)d.getWidth() , h=(int)d.getHeight();
		if(_invalid())
		{	// Double buffer image
			// FIXME: someone on java.net said the app crashed when dragged
			//   between displays.  I need a fresh VolatileImage each time?
			//   (I doubt you can move VI's between displays!  The AWT team
			//   aren't miracle workers :)
			backImg = JWizardPane.GCON.createCompatibleVolatileImage(w,h);
			backImgG = (Graphics2D)backImg.getGraphics();
			backImgG.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			/* // Smooth rendering of selected tab box, below
			backImgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);*/
			// Unselected bar image
			unselectedImg = _drawBar(w,h, unselectedHigh, unselectedMid, unselectedLow, unselectedText);
			// Selected bar image
			selectedImg = _drawBar(w,h, selectedHigh, selectedMid, selectedLow, selectedText);
			// Dimensions may have changed, update bar pixel pos
			currentX=(w/labels.length)*(tab+1);
		}

		// Draw the selected and unselected bars
		int tw=w/labels.length;
		backImgG.setClip(0,0,currentX,h);
		backImgG.drawImage(selectedImg,0,0,null);
		backImgG.setClip(currentX,0,w-currentX,h);
		backImgG.drawImage(unselectedImg,0,0,null);
		// Draw faded selected bar if retracting
		if(ttask.backwards)
		{	// Moving <==
			int memSz = ttask.path.getBufferSize();
			if(memSz>0)
			{	// Set 25% opaque rendering
				float al=0.25f;
				Composite comp = backImgG.getComposite();
				backImgG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,al));
				// For each co-ord in buffer, plot a rectange from end of
				// solid bar (currentX) to previous locations.  The transparent
				// rectangles build up to make the area gradually more opaque.
				for(int i=1;i<memSz;i+=2)
				{	int prevX = (int)ttask.path.getPreviousCoords(0-i)[0];
					int ww=(prevX-currentX);
					backImgG.setClip(currentX,0,ww,h);
					backImgG.drawImage(selectedImg,0,0,null);
				}
				backImgG.setComposite(comp);
			}
		}
		/* // Selected tab box
		backImgG.setClip(0,0 , w,h);
		backImgG.setColor(selectedText);
		backImgG.drawRoundRect(tw*tab+4,4 , tw-9,h-9, h-8,h-8);*/
	}

	private boolean _invalid()
	{	Dimension d=this.getSize();
		return
		(	backImg==null ||
			d.width!=backImg.getWidth() ||
			d.height!=backImg.getHeight()
		);
	}

	private BufferedImage _drawBar(int w,int h,Color hi,Color md,Color lo,Color tx)
	{	int tw=(int)w/labels.length;

		// Create a buffer which supports variable alpha
		BufferedImage im = JWizardPane.GCON.createCompatibleImage(w,h,Transparency.TRANSLUCENT);
		Graphics2D g2 = (Graphics2D)im.getGraphics();

		int oldW=w;
		w=tw*labels.length;

		// Clear the image to transparent
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0,0,w,h);
		// Draw mask - rounded ends
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillOval(0,0 , h,h);  g2.fillOval(w-h,0,h,h);
		g2.fillRect(h/2,0 , w-h,h);

		// Any drawing only occures within the area we just drew
		// Paint background
		g2.setComposite(AlphaComposite.SrcAtop);
		_drawRect(g2,0,w,hi,md,lo);
		g2.setPaint(null);

		// Paint separators between tabs, b+w lines are 1/8 alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,0.125f));
		g2.setColor(Color.WHITE);
		for(int i=1;i<labels.length;i++)  g2.drawLine(tw*i+1,0 , tw*i+1,h);
		g2.setColor(Color.BLACK);
		for(int i=1;i<labels.length;i++)  g2.drawLine(tw*i-1,0 , tw*i-1,h);
		g2.setComposite(AlphaComposite.SrcAtop);

		// Paint labels
		g2.setColor(tx);  g2.setFont(this.getFont());
		for(int i=0;i<labels.length;i++)
			_drawLabel(g2,labels[i],i*tw,0,tw,h);

		// Fill surround with background
		g2.setComposite(AlphaComposite.DstAtop);
		//g2.setColor(Color.BLACK);
		g2.setColor(getBackground());
		g2.fillRect(0,0,oldW,h);

		return im;
	}
	private void _drawRect(Graphics2D g2,int x,int w,Color hiCol,Color mdCol,Color loCol)
	{	Dimension d = getSize();

		// Divide height into quarters
		int h=(int)d.getHeight();
		int th=h/4;

		// Paint shaded area
		g2.setPaint(new GradientPaint(0,0,loCol , 0,th,hiCol));
		g2.fillRect(x,0,w,th);
		g2.setPaint(new GradientPaint(0,4,hiCol , 0,h-th-4,loCol));
		g2.fillRect(x,th,w,h-th);
	}
	/*private void _drawRect(Graphics2D g2,int x,int w,Color hiCol,Color mdCol,Color loCol)
	{	Dimension d = getSize();

		int h=(int)d.getHeight();
		int th1=h/4;
		int th2=th1*3;

		g2.setPaint(new GradientPaint(0,0,loCol , 0,th1,hiCol));
		g2.fillRect(x,0,w,th1);
		g2.setPaint(new GradientPaint(0,th1,hiCol , 0,th2,mdCol));
		g2.fillRect(x,th1,w,th2-th1);
		g2.setPaint(new GradientPaint(0,th2,mdCol , 0,h,loCol));
		g2.fillRect(x,th2,w,h-th2);
	}*/
	private void _drawLabel(Graphics2D g2,String l,int x,int y,int w,int h)
	{	Rectangle2D r2 = fontMet.getStringBounds(l,g2);
		int xx=(int)(w-r2.getWidth())/2;
		int yy=(int)(h-r2.getHeight())/2;
		g2.drawString(l,x+xx,y+yy+fontMet.getMaxAscent());
		//g2.drawRect(x+1,y+1,w-3,h-3);
	}

	// Return our preferred size to container
	public Dimension getPreferredSize() { return PREF_DIM; }
	public Dimension getMaximumSize() { return PREF_DIM; }
	public Dimension getMinimumSize() { return PREF_DIM; }
}
