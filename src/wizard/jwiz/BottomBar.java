package wizard.jwiz;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import wizard_anim.movement.AnimationController;
import wizard_anim.movement.StraightLinePath;

class BottomBar extends JPanel
{	private int previousTab=0;				// FIXME: should be in the parent too
	private int tab=0;						// FIXME: redundant local copy

	private JWizardPane parent;				// Parent
	private CardLayout cardLayout;			// Layout manager
	private JButton forwardB,backB;			// Buttons

	private BufferedImage forwardImg;		// Buffer for transition
	private BufferedImage backImg; 			// Buffer for transition
	private VolatileImage backBuffImg;		// Double buffer
	private Graphics2D backBuffImgG;		// Double buffer graphics
	private Timer timer;					// Animation timer
	private TabTimer ttask;					// Current animation timer task

	private final static String MAIN_PAN = "m";
	private final static String ANIM_PAN = "a";

	private final static int FRAME_DELAY=20;
	private final static int ANIM_DURATION=200;


	BottomBar(JWizardPane jwiz)
	{	parent = jwiz;

		// Back button
		backB = new JButton("Back");
		backB.addActionListener
		(	new ActionListener()
			{	public void actionPerformed(ActionEvent ev)
				{	parent.incTab(-1);
				}
			}
		);
		// Forward button
		forwardB = new JButton("Forward");
		forwardB.addActionListener
		(	new ActionListener()
			{	public void actionPerformed(ActionEvent ev)
				{	parent.incTab(+1);
				}
			}
		);

		// Glue the buttons together so that are evenly spaced and
		// right aligned
		JPanel p1 = new JPanel(new GridLayout(1,0 , 5,5));
		p1.add(backB);  p1.add(forwardB);
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel(),BorderLayout.CENTER);
		p2.add(p1,BorderLayout.EAST);

		// Use a card layout, so we can have an hidden anim panel
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		// Add the main panel with the buttons
		this.add(p2,MAIN_PAN);
		this.setOpaque(true);
		// Add the transition animation panel
		this.add(new TransitionPanel(),ANIM_PAN);

		cardLayout.show(this,MAIN_PAN);

		ttask = new TabTimer();
		timer = new Timer(FRAME_DELAY,ttask);
	}

	void setTab(int t)
	{	// Kill a current animation
		if(timer.isRunning())  timer.stop();

		// Set new tab pos and start fresh animation
		previousTab=tab;  tab=t;
		ttask.reset();
		timer.start();
	}


	private class TabTimer implements ActionListener
	{	StraightLinePath path;				// Movement path
		AnimationController anim;			// Controls time
		BufferedImage buttonBuff;			// Moving button image
		Point p1,p2;						// Co-ords of back/forward buttons
		Point finalCoords;					// Final position for moving button

		void reset()
		{	// Capture or set  up graphics buffers, as needed
			backImg = captureImage(backB,backImg);
			forwardImg = captureImage(forwardB,forwardImg);
			Dimension d = BottomBar.this.getSize();
			if(backBuffImg==null || backBuffImg.getWidth()!=d.width || backBuffImg.getHeight()!=d.height)
			{	backBuffImg = JWizardPane.GCON.createCompatibleVolatileImage(d.width,d.height);
				backBuffImgG=(Graphics2D)backBuffImg.getGraphics();
			}
			BottomBar.this.paint(backBuffImgG);

			// Create path, depending upon which button is moving
			p1 = getAbsoluteLocation(backB);
			p2 = getAbsoluteLocation(forwardB);
			if(previousTab<tab)
			{	// Moving ==>
				path = new StraightLinePath(p2.x,p2.y , p1.x,p1.y , JWizardPane.SOFT_VELOCITY);
				buttonBuff=forwardImg;
				finalCoords=p1;
			}
			else
			{	// Moving <==
				path = new StraightLinePath(p1.x,p1.y , p2.x,p2.y , JWizardPane.SOFT_VELOCITY);
				buttonBuff=backImg;
				finalCoords=p2;
			}

			// Start animation
			cardLayout.show(BottomBar.this,ANIM_PAN);
			anim = new AnimationController(ANIM_DURATION);
			anim.start();
		}

		// This timer actually runs on to 2.0d -- you'll note there are
		// three sections for the movement, the fade, and finally clean up.
		public void actionPerformed(ActionEvent ev)
		{	double time = anim.getMoment();
			if(time>=2.0d)
			{	// Last frame?
				timer.stop();
				cardLayout.show(BottomBar.this,MAIN_PAN);
			}
			else if(time>=1.0d)
			{	// Not last frame?  Fading
				Dimension d = BottomBar.this.getSize();
				backBuffImgG.setColor(getBackground());
				backBuffImgG.fillRect(0,0,d.width,d.height);

				backBuffImgG.drawImage(buttonBuff,finalCoords.x,finalCoords.y,null);

				Composite comp = backBuffImgG.getComposite();
				float al = (float)(time-1d);
				if(al>1f)  al=1f;
				AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , al);
				backBuffImgG.setComposite(alpha);
				backBuffImgG.drawImage(backImg,p1.x,p1.y,null);
				backBuffImgG.drawImage(forwardImg,p2.x,p2.y,null);
				backBuffImgG.setComposite(comp);
			}
			else
			{	// Not last frame?  Moving
				Dimension d = BottomBar.this.getSize();
				backBuffImgG.setColor(getBackground());
				backBuffImgG.fillRect(0,0,d.width,d.height);

				double[] p = path.getCoords(time);
				backBuffImgG.drawImage(buttonBuff,(int)p[0],(int)p[1],null);
			}
			BottomBar.this.repaint();
		}
	}

	private class TransitionPanel extends JComponent
	{	public TransitionPanel()
		{	this.setOpaque(true);
		}
		public void paintComponent(Graphics g)
		{	g.drawImage(backBuffImg,0,0,null);
		}
	}

	private BufferedImage captureImage(JComponent c,BufferedImage bi)
	{	Dimension d = c.getSize();
		if(bi==null || bi.getWidth()!=d.width || bi.getHeight()!=d.height)
		{	bi = JWizardPane.GCON.createCompatibleImage(d.width , d.height);
		}
		c.paint(bi.getGraphics());
		return bi;
	}
	private Point getAbsoluteLocation(Component c)
	{	Point result = new Point(0,0);
		while(c!=this)
		{	Point p = c.getLocation();
			result.x+=p.x;  result.y+=p.y;
			c=c.getParent();
		}
		return result;
	}
}
