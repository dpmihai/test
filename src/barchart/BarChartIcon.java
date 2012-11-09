package barchart;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 13, 2008
 * Time: 5:12:58 PM
 */


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.Icon;

/**
 * @author thierryl
 */
public class BarChartIcon implements Icon {
	/** Offset */
	private int mOffset;
	/** Icon width */
	private int mMaxWidth;
	/** Icon Height */
	private int mHeight;
	/** Minimum Value */
	private double mMin;
	/** Maximum Value */
	private double mMax;
	/** Value to be displayed */
	private double mValue;
	/** Zero Value */
	private double mZero;
	/** Default Color */
	private Color mFillColor = Color.BLUE;
	/** Default Number Format */
	private Format mFormater = NumberFormat.getNumberInstance();
	private int mWidth;

	/**
	 * Default constructor diplaying a Barchart with <code>Color.BLUE</code>
	 * @param iWidth Icon Width
	 * @param iHeight Icon Height
	 * @param iOffset Offset
	 * @param iMin Minimum Value
	 * @param iMax Maximum Value
	 * @param iValue Value
	 * @param iZero Zero Value
	 */
	public BarChartIcon(
		int iWidth, int iHeight, int iOffset,
		double iMin, double iMax, double iValue, double iZero) {
		mMaxWidth = iWidth;
		mHeight = iHeight;
		mMin = iMin;
		mMax = iMax;
		mValue = iValue;
		mOffset = iOffset;
		mZero = iZero;
		mWidth = (int)
			(((Math.max(iValue - iZero, iZero) - iMin) / (iMax - iMin))
					* iWidth);
	}
	/**
	 * Color Constructor
	 * @param iWidth Icon Width
	 * @param iHeight Icon Height
	 * @param iOffset Offset
	 * @param iMin Minimum Value
	 * @param iMax Maximum Value
	 * @param iValue Value
	 * @param iZero Zero Value
	 * @param iFillColor Used to fill the Bar
	 */
	public BarChartIcon(
		int iWidth, int iHeight, int iOffset,
		double iMin,
		double iMax,
		double iValue,
		double iZero, Color iFillColor) {
		this(iWidth, iHeight, iOffset, iMin, iMax, iValue, iZero);
		mFillColor = iFillColor;
	}
	/**
	 * Formater setter if set to null no text is displayed
	 * @param iFormater Format the Double if set to null no text is displayed
	 */
	public void setFormater(Format iFormater) {
		mFormater = iFormater;
	}

	/**
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return mHeight;
	}

	/**
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return mWidth;
	}

	/**
	 * @see javax.swing.Icon
	 * #paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	public void paintIcon(Component iComp, Graphics oGraphics, int iX, int iY) {
		double lVirtualWidth = mMax - mMin;
		int lZeroX =
			(int) (((mZero - mMin) / lVirtualWidth)
					* (mMaxWidth) - mOffset);
		int lBarWidth =
			(int) Math.abs(((mValue - mZero) / lVirtualWidth)
							* (mMaxWidth - 2 * mOffset));
		oGraphics.setColor(mFillColor);
//		Color lGradColor1 = mFillColor;
//		Color lGradColor2 =
//			new Color(lGradColor1.getRed(),
//						lGradColor1.getGreen(),
//						lGradColor1.getBlue(),
//						lGradColor1.getAlpha() / 3);
		int lX = lZeroX + mOffset;
		int lY = iY + mOffset;
		int lWidth = lBarWidth - mOffset;
		int lHeight = getIconHeight() - 2 * mOffset;
//		GradientPaint lPaint =
//			new GradientPaint(
//				lX,
//				lY,
//				lGradColor1,
//				lZeroX + lWidth,
//				lHeight,
//				lGradColor2, true);
//		GradientPaint lReversePaint =
//			new GradientPaint(
//				lX,
//				lY,
//				lGradColor2,
//				lZeroX + lBarWidth,
//				lHeight,
//				lGradColor1, true);

		if (mValue > mZero) {
//			((Graphics2D) oGraphics).setPaint(lPaint);
			oGraphics.fillRect(
						lX,
						lY,
						lBarWidth,
						lHeight);
//			((Graphics2D) oGraphics).setPaint(lReversePaint);
			oGraphics.drawRect(
						lX,
						lY,
						lBarWidth,
						lHeight);
		} else {
			lX = lZeroX - lWidth;
//			((Graphics2D) oGraphics).setPaint(lPaint);
			oGraphics.fillRect(
						lX,
						lY,
						lWidth,
						lHeight);
//			((Graphics2D) oGraphics).setPaint(lReversePaint);
			oGraphics.drawRect(
						lX,
						lY,
						lWidth,
						lHeight);
		}
		if (mFormater != null) {
			String lText = null;
			try {
				lText = mFormater.format(new Double(mValue));
			} catch (IllegalArgumentException lExce) {
				mFillColor = Color.RED;
				lText = "#BAD FORMAT EXCEPTION: " + mValue;
			}
			((Graphics2D) oGraphics).setPaint(Color.BLACK);
			oGraphics.setXORMode(Color.WHITE);

			if (lText != null) {
				int lTextWidth =
					((Graphics2D) oGraphics).
						getFontMetrics().stringWidth(lText);
				//Stay in Icon Width
				int lStartText = 0;
				if (lTextWidth < mMaxWidth) {
					lStartText = lZeroX + 10 + lTextWidth > mMaxWidth
						? mMaxWidth - 10 - lTextWidth
						: lZeroX + 10;
				}
				oGraphics.drawString(
					lText,
					lStartText,
					lY + lHeight - mOffset);
					//(getIconHeight() -  mOffset) - lFontAdjustement / 2);
			}
		}
	}

}
