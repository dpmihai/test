package wizard.jwiz;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import wizard_anim.movement.SequencedVelocity;
import wizard_anim.movement.SoftAccelerationVelocity;
import wizard_anim.movement.SoftDecelerationVelocity;
import wizard_anim.movement.Velocity;


public class JWizardPane extends JPanel
{	private int tab;						// Current tab
	private int tabSize;					// Size of tab
	private TopBar topBar;					// Fancy animated top bar
	private MainPanel mainPanel;			// Main tabbed panel
	private BottomBar bottomBar;			// Fancy animated bottom bar
	private JButton forwardB,backB;			// Navigation

	final static Velocity SOFT_VELOCITY;	// A soft in/out movement
	final static GraphicsConfiguration GCON; // Used to create compat. buffers

	// Set this to false if you want the Ocean PLaF, or true for the
	// Black PLaF.
	public final static boolean BLACK=true;	// FIXME: major major plaf hack!


	static
	{	// As velocities are reuable, we only need one of these for all the
		// soft in/out paths.
		SOFT_VELOCITY = new SequencedVelocity
		(	new SoftAccelerationVelocity(),
			new SoftDecelerationVelocity()
		);
		// Handy for getting compat./volatile buffers.
		GCON = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration();
	}


	public JWizardPane(String[] labels,JComponent[] panels)
	{	tab=0;  tabSize=panels.length;

		// Build top, middle and bottom sections
		topBar = new TopBar(this,labels);
		mainPanel = new MainPanel(labels,panels);
		bottomBar = new BottomBar(this);

		// Construct component, bar at the head, navigation at the foot, and
		// the actual panels in the middle.
		this.setOpaque(true);
		this.setLayout(new BorderLayout(10,10));
		this.add(topBar,BorderLayout.NORTH);
		this.add(mainPanel,BorderLayout.CENTER);
		this.add(bottomBar,BorderLayout.SOUTH);
	}

	// Locked to serialise changing of tabs
	public synchronized void setTab(int t)
	{	tab=t;
		topBar.setTab(t);		// FIXME: strictly speaking this should be
		mainPanel.setTab(t);	// done with events fired to the three
		bottomBar.setTab(t);	// components as listeners -- Quick hack!
	}
	void incTab(int i)
	{	int t=tab;
		t+=i;
		if(t>=0 && t<tabSize)  setTab(t);
	}
}
