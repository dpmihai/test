package roundbutton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;

public class XrButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 9032198251140247116L;

	String text;
	boolean mouseIn = false;

	public XrButton(String s) {
		super(s);
		text = s;
		setBorderPainted(false);
		addMouseListener(this);
		setContentAreaFilled(false);
	}

	@Override
	protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        Shape firstClip = g.getClip();
        RoundRectangle2D rect = new RoundRectangle2D.Double();
        double arc = Math.ceil(getSize().getHeight()/3);
        rect.setRoundRect(0, 0, Math.ceil(getSize().getWidth()), Math.ceil(getSize().getHeight()), arc, arc);
        Area secondClip = new Area(getBounds());
        secondClip.subtract(new Area(rect));
        Area finalClip = new Area(firstClip);
        finalClip.subtract(secondClip);
        g2.setClip(finalClip);
        super.paintComponent(g2);
        Color[] gradients;
        if(getModel().isRollover())
        {
            gradients = new Color[] { new Color(184, 207, 229), new Color(122, 138, 153), new Color(184, 207, 229) };
        }
        else
        {
            gradients = new Color[] { new Color(122, 138, 153) };
        }
        for(int i = 0; i < gradients.length; i++)
        {
            arc -= 2;
            g2.setColor(gradients[i]);
            g2.drawRoundRect(i+1, i+1, (int)Math.ceil(getSize().getWidth()-2)-(i*2), (int)Math.ceil(getSize().getHeight()-2)-(i*2), (int)arc, (int)arc);
        }
    }


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());

		XrButton xrButton = new XrButton("XrButton");
		JButton jButton = new JButton("JButton");

		frame.getContentPane().add(xrButton);

		frame.getContentPane().add(jButton);

		frame.setSize(150, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
	}

	public void mouseExited(MouseEvent e) {
		mouseIn = false;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
