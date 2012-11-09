package roundbutton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

public class OnOffButton extends JToggleButton implements ActionListener, Runnable, MouseMotionListener, MouseListener,
		HierarchyListener {
	
	Image buttonImage = new ImageIcon(OnOffButton.class.getResource("button.png")).getImage();
	Image bgImage = new ImageIcon(OnOffButton.class.getResource("bg.png")).getImage();

	int buttonX;
	int deltaX = -1;
	// boolean threadStop;
	boolean selected;
	boolean drag;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		// threadStop = true;
		new Thread(this).start();
	}

	public OnOffButton() {
		this.addActionListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addHierarchyListener(this);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// for antialiasing geometric shapes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // for antialiasing text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // to go for quality over speed
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null, null);
		g2.drawImage(buttonImage, buttonX, 0, getWidth() / 2, this.getHeight(), null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!drag) {
			selected = !selected;
			// threadStop = true;
			new Thread(this).start();
		}
	}

	@Override
	public void run() {
		// while (true) {
		// if (threadStop) {
		if (this.isSelected()) {
			for (; buttonX < this.getWidth() / 2; buttonX++) {
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} else {
			for (; buttonX > 0; buttonX--) {
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		repaint();
		// threadStop = false;
		// }
		// }
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		drag = true;
		// threadStop = false;
		if (deltaX == -1) {
			deltaX = evt.getX() - buttonX;
		}

		buttonX = evt.getX() - deltaX;

		if (buttonX < 0) {
			buttonX = 0;
		}
		if (buttonX > this.getWidth() / 2) {
			buttonX = this.getWidth() / 2;
		}

		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// threadStop = false;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		deltaX = -1;
		if (drag) {
			if (buttonX < this.getWidth() / 4) {
				this.setSelected(false);
			} else {
				this.setSelected(true);
			}
		}
		drag = false;
	}

	@Override
	public void hierarchyChanged(HierarchyEvent arg0) {
		new Thread(this).start();
	}
	
	public static void main(String[] args) {
		OnOffButton button = new OnOffButton();
		button.setBackground(Color.green);
		button.setPreferredSize(new Dimension(50,18));

		// Create a frame in which to show the button.
		JFrame frame = new JFrame();		
		frame.getContentPane().add(button);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setSize(150, 150);
		frame.setVisible(true);
	}
}
