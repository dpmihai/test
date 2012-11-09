package wizard;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.io.IOException;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;


// Possibly the hackiest PLaF code in the world :)
public class WizSynthPainter extends SynthPainter
{	private static BufferedImage scrollBarNorthImg;
	private static BufferedImage scrollBarEastImg;
	private static BufferedImage scrollBarSouthImg;
	private static BufferedImage scrollBarWestImg;
	private final static GraphicsConfiguration GCON; // Used to create compat. buffers

	static
	{	try
		{	scrollBarNorthImg = ImageIO.read( WizSynthPainter.class.getResourceAsStream("scrollbar_n.png") );
			scrollBarEastImg = ImageIO.read( WizSynthPainter.class.getResourceAsStream("scrollbar_e.png") );
			scrollBarSouthImg = ImageIO.read( WizSynthPainter.class.getResourceAsStream("scrollbar_s.png") );
			scrollBarWestImg = ImageIO.read( WizSynthPainter.class.getResourceAsStream("scrollbar_w.png") );
		}catch(IOException e) { e.printStackTrace(); }

		// Handy for getting compat./volatile buffers.
		GCON = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration();
	}

	public void paintButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	Graphics2D g2 = (Graphics2D)g;

		VolatileImage img = GCON.createCompatibleVolatileImage(w,h,Transparency.TRANSLUCENT);
		Graphics2D imgG = (Graphics2D)img.getGraphics();
		imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);

		SynthStyle ss = context.getStyle();
		boolean selected = (context.getComponentState()&SynthConstants.PRESSED)>0;
		Color sHiCol = (Color)ss.get(context, "selectedHigh");
		Color sLoCol = (Color)ss.get(context, "selectedLow");
		Color uHiCol = (Color)ss.get(context, "unselectedHigh");
		Color uLoCol = (Color)ss.get(context, "unselectedLow");

		_buttonShade(imgG,x,y,w,h,0,sHiCol,sLoCol);
		g.drawImage(img,x,y,null);
		if(!selected)
		{	_buttonShade(imgG,x,y,w,h,2,uHiCol,uLoCol);
			g.drawImage(img,x,y,null);
		}
	}
	private void _buttonShade(Graphics2D g2,int x,int y,int w,int h,int border,Color hiCol,Color loCol)
	{	int x2=x+border , y2=y+border , w2=w-border*2 , h2=h-border*2;

		// Clear the image to transparent
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0,0,w,h);
		// Draw mask - rounded ends
		g2.setComposite(AlphaComposite.Src);
		g2.setColor(Color.WHITE);
		g2.fillOval(x2,y2 , h2,h2);  g2.fillOval(x2+w2-h2,y2,h2,h2);
		g2.fillRect(x2+h2/2,y2 , w2-h2,h2);

		// Now draw into that mask
		g2.setComposite(AlphaComposite.SrcAtop);
		int th=h/4;
		g2.setPaint(new GradientPaint(0,0,loCol , 0,th,hiCol));
		g2.fillRect(0,0,w,th);
		g2.setPaint(new GradientPaint(0,4,hiCol , 0,h-th-4,loCol));
		g2.fillRect(0,th,w,h-th);
	}

	/*public void paintPanelBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	Graphics2D g2 = (Graphics2D)g;
		SynthStyle ss = context.getStyle();
		Color c1 = (Color)ss.get(context,"panel1");
		Color c2 = (Color)ss.get(context,"panel2");
		Color c3 = (Color)ss.get(context,"panel3");
		Color c4 = (Color)ss.get(context,"panel4");

		int th=h/4;
		g2.setPaint(new GradientPaint(0,0,c1 , 0,th,c2));
		g2.fillRect(0,0,w,th);
		g2.setPaint(new GradientPaint(0,th,c2 , 0,th*2,c1));
		g2.fillRect(0,th,w,th);
		g2.setPaint(new GradientPaint(0,th*2,c3 , 0,h,c4));
		g2.fillRect(0,th*2,w,h-th*2);
	}*/


	public void paintArrowButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintArrowButtonForeground(SynthContext context, Graphics g, int x, int y, int w, int h,int dir)
	{	BufferedImage arrow=null;
		switch(dir)
		{	case SwingConstants.NORTH :	arrow=scrollBarNorthImg;  break;
			case SwingConstants.EAST : 	arrow=scrollBarEastImg;  break;
			case SwingConstants.SOUTH : arrow=scrollBarSouthImg;  break;
			case SwingConstants.WEST : 	arrow=scrollBarWestImg;  break;
		}
		int xo=(w-arrow.getWidth())/2;
		int yo=(h-arrow.getHeight())/2;
		int ww=w-xo*2 , hh=h-yo*2;
		g.drawImage(arrow,x+xo,y+yo,ww,hh,null);

	}
	public void paintCheckBoxBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintComboBoxBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintFileChooserBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintListBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	SynthStyle ss = context.getStyle();
		Color col = (Color)ss.get(context, "unselectedLow");
		g.setColor(col);  g.fillRect(x,y,w,h);

	}
	public void paintMenuBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintScrollBarThumbBackground(SynthContext context, Graphics g, int x, int y, int w, int h,int orientation)
	{	SynthStyle ss = context.getStyle();
		Color col = (Color)ss.get(context, "unselectedText");
		g.setColor(col);  g.drawRoundRect(x+2,y+2,w-5,h-5,4,4);
	}
	public void paintScrollBarTrackBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintScrollPaneBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}
	public void paintTextFieldBackground(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	_paintBorder(context,g,x,y,w,h);
	}

	private void _paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h)
	{	SynthStyle ss = context.getStyle();

		Color col = (Color)ss.get(context, "unselectedLow");
		g.setColor(col);  g.fillRoundRect(x,y,w-1,h-1,8,8);

		boolean selected = (context.getComponentState()&SynthConstants.FOCUSED)>0;
		col = (Color)ss.get(context, "unselectedMid");
		g.setColor(col);  g.drawRoundRect(x,y,w-1,h-1,8,8);
	}
}