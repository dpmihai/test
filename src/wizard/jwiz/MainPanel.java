package wizard.jwiz;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import wizard_anim.movement.AnimationController;
import wizard_anim.movement.Path;
import wizard_anim.movement.StraightLinePath;


class MainPanel extends JPanel
{	private String[] labels;				// Tab labels
	private JComponent[] components;		// Components for tab
	private CardLayout cardLayout;			// Layout manager
	private BufferedImage outgoingImg;		// Panel going out
	private BufferedImage incomingImg;		// Panel coming in
	private VolatileImage backImg;			// Transition buffer (dbuff)
	private Graphics2D backImgG;			// Transition buffer graphics
	private TransitionPanel transPanel;		// Transition panel for animation

	private int previousTab;				// Used to determine direction
	private int tab=0;						// Current tab (FIXME: local copy - naughty!)
	private Timer timer;					// Swing timer
	private TabTimer ttask;					// Current animation timer task

	private final static String TRANSITION_CARD = "[trans]";

	private final static int FRAME_DELAY=20;
	private final static int ANIM_DURATION=500;


	MainPanel(String[] l,JComponent[] c)
	{	labels=l;  components=c;
		this.setOpaque(true);

		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		// Add components to card
		for(int i=0;i<components.length;i++)
			this.add(components[i],labels[i]);
		// Add additional transition card
		transPanel = new TransitionPanel();
		this.add(transPanel,TRANSITION_CARD);

		cardLayout.show(this,labels[tab]);

		ttask = new TabTimer();
		timer = new Timer(FRAME_DELAY,ttask);
	}

	void setTab(int t)
	{	// If in the middle of an animation, stop
		if(timer.isRunning())  timer.stop();
		// Set new tab pos and start fresh animation
		previousTab=tab;  tab=t;
		ttask.reset();  timer.start();
	}


	private class TabTimer implements ActionListener
	{	Path path;							// Movement path
		AnimationController anim;			// Animation timer

		void reset()
		{	// Get the width and height of the component
			Dimension d=getSize();
			final int w=(int)d.getWidth() , h=(int)d.getHeight();

			// Create a path and animation controller
			// JUDDER MODE?  Switch the comments on the path instantiations
			path = new StraightLinePath(0,0,w,0 , JWizardPane.SOFT_VELOCITY);
			//path = new movement.DrunkenLinePath(0,0,w,0 , JWizardPane.SOFT_VELOCITY ,50);
			anim = new AnimationController(ANIM_DURATION);

			// Create a snapshot of the incoming and outgoing panels for
			// the purposes of animation.
			outgoingImg = JWizardPane.GCON.createCompatibleImage(w,h);
			components[previousTab].paint(outgoingImg.getGraphics());
			incomingImg = JWizardPane.GCON.createCompatibleImage(w,h);
			components[tab].paint(incomingImg.getGraphics());

			// The back buffer is our double buffer image.
			backImg = JWizardPane.GCON.createCompatibleVolatileImage(w,h);
			backImgG = (Graphics2D)backImg.getGraphics();
			backImgG.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			backImgG.drawImage(outgoingImg,0,0,null);

			// Flip the transition pane to the front, so we can run the
			// animation.
			cardLayout.show(MainPanel.this,TRANSITION_CARD);

			// And mark the start of the animation...
			anim.start();
		}

		public void actionPerformed(ActionEvent ev)
		{	double time=anim.getMoment();
			if(time>=1.0d)
			{	// Finished animation?  Draw the final frame, end the timer
				// and flip the required panel to the front on the card layout.
				backImgG.drawImage(incomingImg,0,0,null);
				timer.stop();
				cardLayout.show(MainPanel.this,labels[tab]);
			}
			else
			{	// Update animation
				Composite comp = backImgG.getComposite();

				// Clear the component
				Dimension sz = MainPanel.this.getSize();
				backImgG.setColor(MainPanel.this.getBackground());
				backImgG.fillRect(0,0,sz.width,sz.height);

				// Current x position
				double[] coords = path.getCoords(time);
				int x=(int)coords[0];
				int y=(int)coords[1];

				// Start/end x positions
				boolean backwards = (tab<previousTab);
				int x1=(backwards) ? 0-x : x;
				int x2=(backwards) ? x-sz.width: sz.width-x;

				// Outgoing, fading out over time
				AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1f-(float)time);
				backImgG.setComposite(alpha);
				backImgG.drawImage(outgoingImg,x1,y,null);

				// Incoming, fading in over time
				alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , (float)time);
				backImgG.setComposite(alpha);
				backImgG.drawImage(incomingImg,x2,y,null);

				// Reset Graphics
				backImgG.setComposite(comp);
			}
			MainPanel.this.transPanel.repaint();
		}
	}

	private class TransitionPanel extends JComponent
	{	public TransitionPanel()
		{	this.setOpaque(true);
		}
		public void paintComponent(Graphics g)
		{	g.drawImage(backImg,0,0,null);
		}
	}

}
