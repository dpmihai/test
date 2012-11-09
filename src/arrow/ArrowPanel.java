package arrow;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 3, 2005 Time: 4:39:00 PM
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * ArrowPanel.java
 * <p/>
 * An example of how to use Java2D to create an arrow icon.
 *
 * @author Jon Lipsky (jon.lipsky@elevenworks.com)
 */
public class ArrowPanel extends JPanel
{
	private int inset = 10;
	private Color shapeColor = new Color(125,50,50);

	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int vWidth = getWidth();
		int vHeight = getHeight();

		// Calculate the size of the button
		int vShapeHeight = vHeight - (inset * 2);
		int vShapeWidth = vWidth - (inset * 2);

		BufferedImage vBuffer = new BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D bg = vBuffer.createGraphics();
		bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Paint the background of the button
		bg.setColor(Color.WHITE);
		bg.fillRect(0, 0, vWidth, vHeight);

		// Create the arrow shape
		Polygon vShape = createShape(vShapeWidth, vShapeHeight);

		// Translate our coordinates based on the insets we want
		bg.translate(inset, inset);

		// Create the gradient paint for the background of the arrow
		Color vGradientStartColor = shapeColor.brighter().brighter();
		Color vGradientEndColor = vGradientStartColor.brighter().brighter().brighter().brighter();
		Paint vPaint = new GradientPaint(inset, 0, vGradientStartColor, vShapeWidth, 0, vGradientEndColor, false);
		bg.setPaint(vPaint);

		// Fill in the background of the arrow
		bg.fill(vShape);

		// Create the stroke for the outline of the arrow
		bg.setColor(shapeColor);
		int vStrokeSize = (int) (.05 * vShapeWidth);
		bg.setStroke(new BasicStroke(vStrokeSize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		// Draw the outline of the arrow
		bg.draw(vShape);

		// Create a smaller arrow
		int vInsetSize = (int) (.06 * vShapeWidth);
		Polygon vSmallerShape = createSmallerPolygon(vShape, vInsetSize);

		bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f));

		// Fill the smaller arrow
		bg.setColor(shapeColor.brighter().brighter());
		bg.fill(vSmallerShape);

		// Draw the circle "highlight" inside of the smaller arrow
		bg.setClip(vSmallerShape);
		vGradientStartColor = shapeColor.brighter().brighter();
		vGradientEndColor = shapeColor;
		vPaint = new GradientPaint(0, 0, vGradientStartColor, vShapeWidth, vShapeHeight, vGradientEndColor, false);
		bg.setPaint(vPaint);
		bg.fillOval(0-inset-(vShapeWidth/3),0-inset+(vShapeHeight/4), (int)(vShapeWidth*1.05), (int)(vShapeHeight*1.5));

		g2.drawImage(vBuffer, 0, 0, null);
	}


	private Polygon createShape(int aWidth, int aHeight)
	{
		int vHorizontalInset = (int) (aWidth * .25);
		int vVerticalInset = (int) (aHeight * .35);
		int vHorizontalCenter = (int) aWidth / 2;

		Polygon vPolygon = new Polygon();
		vPolygon.addPoint(vHorizontalInset, 0);
		vPolygon.addPoint(vHorizontalInset, vVerticalInset);
		vPolygon.addPoint(0, vVerticalInset);
		vPolygon.addPoint(vHorizontalCenter, aHeight);
		vPolygon.addPoint(aWidth, vVerticalInset);
		vPolygon.addPoint(aWidth - vHorizontalInset, vVerticalInset);
		vPolygon.addPoint(aWidth - vHorizontalInset, 0);

		return vPolygon;
	}

	private Polygon createSmallerPolygon(Polygon aPolygon, int aInset)
	{
		Rectangle vBounds = aPolygon.getBounds();

		int vHorizontalCenter = vBounds.width / 2;
		int vVerticalCenter = vBounds.height / 2;

		Polygon vPolygon = new Polygon(aPolygon.xpoints, aPolygon.ypoints, aPolygon.npoints);

		for (int i = 0; i < vPolygon.xpoints.length; i++)
		{
			int x = vPolygon.xpoints[i];

			if (x < vHorizontalCenter)
			{
				x += aInset;
			}
			else if (x > vHorizontalCenter)
			{
				x -= aInset;
			}

			vPolygon.xpoints[i] = x;
		}

		for (int i = 0; i < vPolygon.ypoints.length; i++)
		{
			int y = vPolygon.ypoints[i];

			if (y < vVerticalCenter)
			{
				y += aInset;
			}
			else if (y > vVerticalCenter)
			{
				y -= aInset;
			}

			vPolygon.ypoints[i] = y;
		}


		int aLen1 = vHorizontalCenter - aPolygon.xpoints[2];
		int aLen2 = aPolygon.ypoints[3] - aPolygon.ypoints[1];

		float vRatio = (float) aLen1 / (float) aLen2;

		vPolygon.ypoints[3] -= (int) (aInset * vRatio);

		int vNewVerticalLen = vPolygon.ypoints[3] - vPolygon.ypoints[1];
		int vNewHorizontalLen = (int) (vRatio * vNewVerticalLen);
		vPolygon.xpoints[2] = (int) (vHorizontalCenter - vNewHorizontalLen);
		vPolygon.xpoints[4] = (int) (vHorizontalCenter + vNewHorizontalLen);

		return vPolygon;
	}

	// --------------------------------------------------------------------------
	// Utility Methods
	// --------------------------------------------------------------------------

	/**
	 * A main method to test the panel.
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame vFrame = new JFrame(ArrowPanel.class.getName());
		vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vFrame.setSize(150, 175);
		vFrame.getContentPane().add(new ArrowPanel());
		vFrame.setTitle("Arrow");
		vFrame.show();
	}
}
