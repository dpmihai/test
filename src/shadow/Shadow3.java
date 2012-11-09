package shadow;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.swing.*;


// Drop Shadow Demo, with layered effect.
// Simon Morris, April 2007.
//
// --
//
// MAJOR FIX ME...
// This demo has one major efficiency flaw: it needs to repaint the whole
// component whenever something is moved.
//
// When a JInternalFrame is moved, Swing repaints the frame at its new 
// location, and any parts of the JDesktopPane which have been uncovered.
// Unfortunately Swing's repaint mechanism doesn't know about the shadow
// which needs to be drawn outside of the moved component's bounds.  This
// could have been solved by using a DropShadowBorder like technique,
// except the JInternalFrame's already have a border (their title bar and
// surrounding drag area) which doesn't work too well as a compound (the
// entire border, including shadows, becomes draggable!)
//
// My solution is rather sloppy: when the desktop is asked to repaint its
// dirty area, it widens the clip rectangle to the full desktop and repaints 
// from scratch all the shadows.   Not very efficient.  An ideal solution
// would be one where painting was handled without the paintImmediately()
// hack to widen the dirty area... but this has proved a little more tricky
// to implement in sympathy with the existing RepaintManager than it sounds.
public class Shadow3
{	private JFrame frame;

	Shadow3()
	{	Runnable r = new Runnable()
		{	public void run() { _build(); }
		};
		try { SwingUtilities.invokeAndWait(r); }
			catch(Exception e) { e.printStackTrace(); }
	}

	private void _build()
	{	frame = new JFrame();
		JDesktopPane desktop = new Shadows();
		desktop.setBackground(new Color(0x88ccff));
		desktop.setDoubleBuffered(false);
		for(int i=0;i<3;i++)
		{	JInternalFrame jf = new JInternalFrame("Frame"+i,true,false,false,false);
			desktop.add(jf);
			jf.setSize(200,150);
			jf.setLocation(i*100,i*40);
			jf.setLayer(2-i);
			jf.setVisible(true);
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(desktop,BorderLayout.CENTER);
		frame.setSize(500,300);
		frame.setVisible(true);
	}

	private class Shadows extends JDesktopPane
	{	private VolatileImage shadowBuff;	// Overlaid shadows
		private Graphics2D sg;				// Graphics context

		// Two modes: soft or solid shadows.
		// Solid shadows are drawn using filled rectangles.  The softer
		// shadows are drawn by pre-generating tiles (see constructor) which
		// are used to create the shadow edge.
		private boolean solidShadowMode=false;

		// For solid shadows the alpha blending can be done as the shadows
		// are constructed (so areas of overlapping shadows are darker) or
		// when then final result is overlaid (so all shadows are of a
		// consistent shade.)
		private boolean progressiveShadowMode=true;

		// The shadow has a base value (a kind of constant overhead which
		// is always added) plus an increment which determines how the shadow
		// grows as it falls over different layers of component.
		private int shadowInc=10;			// Shadow increment (multiplier)
		private int shadowBase=0;			// Shadow minimum
		// For soft shadows we have a maximum size, which acts as a ceiling
		// on how large then shadows are.
		private int shadowMax=30;			// Maximum shadow
		// The alpha-ness  for solid shadows.  This can be added progressively
		// during build, or on the final overlay (see progressiveShadowMode)
		private float alpha=0.125f;			// Shadow transparency

		private BufferedImage[] tiles;		// Tiles for non-solid shadow


		public Shadows()
		{	super();

			// Pre-render five soft shadow tiles: three corners, two edges.
			tiles = new BufferedImage[5];
			for(int i=0;i<tiles.length;i++)
			{	// Create a transparent compatible buffer
				GraphicsConfiguration gc= GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice()
					.getDefaultConfiguration();
				tiles[i] = gc
					.createCompatibleImage(shadowMax,shadowMax,Transparency.TRANSLUCENT);
				// Graphics for new buffer
				Graphics2D g = (Graphics2D)tiles[i].getGraphics();
				g.setColor(Color.BLACK);
				g.setComposite
				(	AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(1f/shadowMax))
				);
				int j;
				int edge=shadowMax/2;
				switch(i)
				{	case 0 :		// Bottom left hand corner
						for(j=0;j<shadowMax;j++)
							g.fillRoundRect(0+j,0-shadowMax+j , shadowMax*2-j*2,shadowMax*2-j*2 , edge,edge);
						break;
					case 1 :		// Bottom edge
						for(j=0;j<shadowMax;j++)
							g.fillRect(0,0 , shadowMax,shadowMax-j);
						break;
					case 2 :		// Bottom right hand corner
						for(j=0;j<shadowMax;j++)
							g.fillRoundRect(0-shadowMax+j,0-shadowMax+j , shadowMax*2-j*2,shadowMax*2-j*2 , edge,edge);
						break;
					case 3 :		// Right edge
						for(j=0;j<shadowMax;j++)
							g.fillRect(0,0 , shadowMax-j,shadowMax);
						break;
					case 4 :		// Top right hand corner
						for(j=0;j<shadowMax;j++)
							g.fillRoundRect(0-shadowMax+j,0+j , shadowMax*2-j*2,shadowMax*2-j*2 , edge,edge);
						break;
				}
			}

		}

		// FIXME: Major problem!!
		// To ensure the shadows are drawn correctly these methods widen
		// the desktop dirty repaint region to include the entire component,
		// thus forcing a complete repaint each update.  NOT GOOD!  Ideally
		// would wouldn't need to override these methods.  (See header.)
		public void paintImmediately(Rectangle r)
		{	super.paintImmediately(new Rectangle(0,0,getWidth(),getHeight()));
		}

		public void paintImmediately(int x,int y,int w,int h)
		{	super.paintImmediately(0,0,getWidth(),getHeight());
		}

		// All 'live' drawing done here to ensure our shadow code gets
		// run last.
		public void paint(Graphics g)
		{	// STEP ONE: Draw the original internal components.
			super.paint(g);
			
			// STEP TWO: Render shadows
			int ww=this.getWidth() , hh=this.getHeight();
			Rectangle r = new Rectangle();

			// Create an transparent buffer to build the shadows
			if
			(	shadowBuff==null ||
				shadowBuff.getWidth()!=ww ||
				shadowBuff.getHeight()!=hh ||
				shadowBuff.validate(getGraphicsConfiguration())==VolatileImage.IMAGE_INCOMPATIBLE
			)
			{   shadowBuff = getGraphicsConfiguration().createCompatibleVolatileImage(ww,hh,Transparency.TRANSLUCENT);
				sg = (Graphics2D)shadowBuff.getGraphics();
			}
			r = g.getClipBounds(r);
			sg.setClip(r.x,r.y,r.width,r.height);
			sg.setComposite(AlphaComposite.Clear);
			sg.fillRect(r.x,r.y,r.width,r.height);

			// Set the composites for both the building and final overlay...
			Composite buildComposite,finalComposite;
			if(solidShadowMode)
			{	// If we want progressive shadows then the shadow drawing is done
				// with alpha blending and the final draw is not.  If we want
				// consistent colour shadows then the shadows are drawn solid and
				// the final draw is blended.
				buildComposite = progressiveShadowMode ?
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha) :
					AlphaComposite.SrcOver;
				finalComposite = progressiveShadowMode ?
					AlphaComposite.SrcOver :
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
			}
			else
			{	// If we're  drawing soft shadows then the alpha on the tiles
				// is already pre-drawn in the tiles, so don't make anything
				// transparent.
				buildComposite = AlphaComposite.SrcOver;
				finalComposite = AlphaComposite.SrcOver;
			}

			// Our children
			JInternalFrame[] frames = this.getAllFrames();
			// FIXME: at this point we should sort/filter the frames,
			//   probably using Container.getComponentZOrder(), to ensure
			//   they are drawn correctly when re-ordered.
			// Shadows in solid black
			sg.setComposite(buildComposite);
			sg.setColor(Color.BLACK);
			// First we need to draw any shadows which fall upon the backdrop,
			// rear [n-1] to front [0] draw the shadow against the desktop
			for(int i=frames.length-1;i>=0;i--)
			{	if(frames[i].isIcon()) continue;
				r = frames[i].getBounds(r);
				int sz=(frames.length-i)*shadowInc+shadowBase;
				if(solidShadowMode) _1(sg,r,sz); else _2(sg,r,sz);
			}
			// Next we draw those shadows which fall inside each component,
			// rear [n-1] to front [0] draw the shadows inside each component
			for(int i=frames.length-1;i>=0;i--)
			{	if(frames[i].isIcon()) continue;
				// The the bounds of the i'th component, and clip to it
				r = frames[i].getBounds(r);
				sg.setClip(r.x , r.y , r.width , r.height);
				// Clear any drawing occupying this rectangle (this is like
				// drawing the component over existing shadows -- except
				// because components are already draw, we 'cut out'
				// their shapes from the shadow buffer...
				sg.setComposite(AlphaComposite.Clear);
				sg.fillRect(r.x , r.y , r.width , r.height);
				sg.setComposite(buildComposite);
				// Now work forward, drawing any shadows which fall within our
				// current component's bounds.
				for(int j=i-1;j>=0;j--)
				{	// Size is the difference between current layer and this one
					int sz=(i-j)*shadowInc+shadowBase;
					// Draw shadow
					r = frames[j].getBounds(r);
					if(solidShadowMode) _1(sg,r,sz); else  _2(sg,r,sz);
				}
			}

			// STEP THREE: Add the shadow to the current drawing
			Graphics2D g2 = (Graphics2D)g;
			Composite comp = g2.getComposite();
			g2.setComposite(finalComposite);
			g2.drawImage(shadowBuff,0,0,null);
			g2.setComposite(comp);
		}

		// Rectangle shadows
		private void _1(Graphics g,Rectangle r,int sz)
		{	g.fillRect(r.x+r.width , r.y+sz , sz , r.height-sz);	// Vertical
			g.fillRect(r.x+sz , r.y+r.height , r.width , sz);		// Horizontal
		}
		// Soft (bitmap tile) shadows
		private void _2(Graphics g,Rectangle r,int sz)
		{	if(sz>shadowMax) sz=shadowMax;
			int x1=r.x+sz , y1=r.y+sz;
			int x2=x1+r.width-shadowMax , y2=y1+r.height-shadowMax;
			// Bottom edge
			g.drawImage(tiles[0] , x1,y2 , null);
			g.drawImage(tiles[1] , x1+shadowMax,y2 , r.width-shadowMax*2,shadowMax , null);
			// Right edge
			g.drawImage(tiles[3] , x2,y1+shadowMax , shadowMax,r.height-shadowMax*2 , null);
			g.drawImage(tiles[4] , x2,y1 , null);
			// Corner
			g.drawImage(tiles[2] , x2,y2 , null);
		}
	}

	public static void main(String[] args)
	{	new Shadow3();
	}
}
