package barchart;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 13, 2008
 * Time: 5:12:09 PM
 */


import db.rep.table.TableSorter;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * <pre>
 * -----------------------------------------------------
 * |           |XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
 * -----------------------------------------------------
 * -----------------------------------------------------
 * |           |XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|  		|
 * -----------------------------------------------------
 * -----------------------------------------------------
 * |XXXXXXXXXXX|										|
 * -----------------------------------------------------
 * -----------------------------------------------------
 * |           |XXXXXXXXXXXXXXXXXXXX|					|
 * -----------------------------------------------------
 * Min         Zero               Value             	Max
 * </pre>
 *
 * This Renderer listen to the TableModel of the JTable to know if the
 * Min/Max informations have changed. TableModel is retreived thanks to the
 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent
 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
 *
 * @author thierryl
 */
public class BarChartCellRenderer extends DefaultTableCellRenderer
										implements TableModelListener {



	/** Array of Colors used
	 * For now the only used color is BLUE*/
	private static final Color[] COLORS =
		new Color[] {new Color(0x002F60)};
	/**
	 * Small structure holding 2 double the Min and the Max. This information
	 * is stored in cache for each column.
	 *
	 */
	private static final class MinMaxPair {
		/** Min Value */
		private double mMin = 0;
		/** Maximum Value */
		private double mMax = Double.MIN_VALUE;

		private int mLabelPreferredWidth = 0;

		private double mLabelPreferredValue = 0;

	}
	/** Renderer needs informations on the Table Model but renderer should not
	 * be the one keeping the tablemodel alive in memory the Table is
	 * the responsible for this.*/
	private WeakReference mReferencedModel = null;
	/** Cache holding MinMax Column information, this cache is reseted
	 * When the Column have changed
	 */
	private Map mMinMaxMap = new HashMap();

	private Map mColumnClassMap = new HashMap();
	/**
	 * Cache storing NumberFormat default for column
	 * First the format is asked to this cache if null then it is asked to the
	 * Format by Type Cache
	 */
	private Map mFormatByColmumnMap = new HashMap();
	/**
	 * Initialized in Constructor Recognized types are :
	 * Percent, Double, Float, Integer and Long
	 */
	private Map mFormatByTypeMap = new HashMap();

	private boolean mRenderingEnabled = true;
	private boolean mNormalizedByColumn = true;


	/**
	 * Method Computing Column Min Max Values to set the correct Scale
	 * @param iColumn Column to parse
	 */
	private void resetMinMax(int iColumn, Class iColumnClass) {
		TableModel lModel = (TableModel) mReferencedModel.get();
		if (lModel == null) {
			return;
		}
		mColumnClassMap.put(new Integer(iColumn), iColumnClass);
		MinMaxPair lPair = new MinMaxPair();
		int lRowCount = lModel.getRowCount();
		for (int i = 0; i < lRowCount; i++) {
			Object lObject =  lModel.getValueAt(i, iColumn);
			if (lObject == null) {
				continue;
			}
			try {
				double lValue = getDoubleFromValue(lObject);
				if (lValue < lPair.mMin) {
					lPair.mMin = lValue;
				}
				if (lValue > lPair.mMax) {
					lPair.mMax = lValue;
				}
				int lPreferredWidth =
					getPreferredWidthForValue(lValue, iColumn, iColumnClass);
				if (lPair.mLabelPreferredWidth < lPreferredWidth) {
					lPair.mLabelPreferredWidth = lPreferredWidth;
					lPair.mLabelPreferredValue = lValue;
				}
			} catch (NumberFormatException lExce) {
				continue;
			} catch (ClassCastException lExce) {
				continue;
			}

		}
		setText("");
		mMinMaxMap.put(new Integer(iColumn), lPair);
	}

	private int getPreferredWidthForValue(
			double iValue,
			int iColumnIndex,
			Class iColumnClass) {
		Format lFormat = getFormat(new Integer(iColumnIndex), iColumnClass);
		JLabel lLabel = new JLabel();
		lLabel.setFont(getFont());
		lLabel.setText(lFormat == null
				? Double.toString(iValue)
				: lFormat.format(new Double(iValue)));
		return lLabel.getPreferredSize().width;
	}


	private static double getDoubleFromValue(Object iObject) {
		try {
			double lValue = 0;
			if (iObject == null) {
				return lValue;
			}
			if (iObject instanceof String) {
				String lString = (String) iObject;
				if (lString.length() > 0) {
					lValue = Double.parseDouble(lString);
				}
			} else {
				lValue = ((Number) iObject).doubleValue();
			}
			return lValue;
		} catch (ClassCastException lExce) {
			return 0;
		} catch (NumberFormatException lExce) {
			return 0;
		}
	}


	/**
	 * Set the NumberFormat to be Used
	 * @see NumberFormat#getInstance()
	 * @see NumberFormat#getPercentInstance()
	 * @param iFormater new NumberFormat if set to null Numbers
	 * won't be displayed
	 * @param iColumn Colmumn index for this formatter
	 */
	public void setFormater(Format iFormater, int iColumn) {
		mFormatByColmumnMap.put(new Integer(iColumn), iFormater);
	}
	/**
	 * Clear chache of association between Column index and Formatter
	 */
	public void resetColumnFormater() {
		mFormatByColmumnMap.clear();
	}

	/**
	 * Define the formater used to Format the given type.
	 * Default Values are defined in Constructor with the given Locale
	 * for Integer, Long, Percent, Double and Float.
	 * @param iFormater <code>NumberFormat</code> to use to parse values of
	 * the given type
	 * @param iClass given type
	 */
	public void setDefaultFormater(NumberFormat iFormater, Class iClass) {
		mFormatByTypeMap.put(iClass, iFormater);
	}

	public BarChartCellRenderer() {
		this(Locale.getDefault());
	}
	/**
	 * Default Constructor, build a BarCharRenderer for a Column in a Table
	 * @param iLocale Locale used to Format Numbers
	 */
	public BarChartCellRenderer(Locale iLocale) {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
		NumberFormat lIntegerFormat = NumberFormat.getInstance(iLocale);
		setDefaultFormater(lIntegerFormat, Integer.class);
		setDefaultFormater(lIntegerFormat, Long.class);
		NumberFormat lNumberFormat = NumberFormat.getNumberInstance(iLocale);
		setDefaultFormater(lNumberFormat, Double.class);
		setDefaultFormater(lNumberFormat, Float.class);
	}
	/**
	 * Used every Time the getTableCellRendererConponent method is called
	 * in order to update Model information. If the Model is a TableSorter
	 * TableModel informations will be recover from his underlying TableModel.
	 * If TableModel is to be updated Listeners will be removed and added
	 * as needed.
	 * @param iModel Model to update
	 */
	private void newTableModel(TableModel iModel) {
		if (iModel == null) {
			return;
		}
		if (iModel instanceof TableSorter) {
			iModel = ((TableSorter) iModel).getTableModel();
		}
		if (mReferencedModel != null) {
			TableModel lModel = (TableModel) mReferencedModel.get();
			//Nothing changed
			if (lModel == iModel) {
				return;
			}

			if (lModel != null) {
				lModel.removeTableModelListener(this);
			}
		}
		mReferencedModel = new WeakReference(iModel);
		iModel.addTableModelListener(this);
		mMinMaxMap.clear();
	}

	private Format getFormat(Integer iColumn, Class iColumnClass) {
		// Check if a format has been defined for this Column
		Format lFormatter = (Format) mFormatByColmumnMap.get(iColumn);
		if (lFormatter == null) {
			//If no format has been defined for this column check for the class
			lFormatter = (NumberFormat) mFormatByTypeMap.get(iColumnClass);
		}
		return lFormatter;
	}
	/**
	 * @see javax.swing.table.TableCellRenderer
	 * #getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
	 *  boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(
		JTable iTable,
		Object iValue,
		boolean iIsSelected,
		boolean iHasFocus,
		int iRow,
		int iColumn) {
		TableModel lModel = iTable.getModel();
		newTableModel(lModel);
		int lModelColumn = iTable.convertColumnIndexToModel(iColumn);
		Integer lColumn = new Integer(lModelColumn);
		Class lColumnClass = (Class) mColumnClassMap.get(lColumn);
		if (lColumnClass == null) {
			lColumnClass = iTable.getColumnClass(iColumn);
		}
		if (!mMinMaxMap.containsKey(lColumn)) {
			resetMinMax(lModelColumn, lColumnClass);
		}
		Format lFormatter = getFormat(lColumn, lColumnClass);
		Component lComp = super.getTableCellRendererComponent(
							iTable,
							iValue,
							iIsSelected,
							iHasFocus,
							iRow,
							iColumn);

		if (lComp instanceof JLabel) {
			double lValue = getDoubleFromValue(iValue);
			int lColumnWidth =
				iTable.getColumnModel().getColumn(iColumn).getWidth();
			int lRowHeight = iTable.getRowHeight(iRow);
			JLabel lLabel = (JLabel) lComp;
			if (iValue == null) {
				lLabel.setText("");
				lLabel.setIcon(null);
				return lLabel;
			}
			if (isRenderingEnabled()) {
				MinMaxPair lMinMaxPair =
					(MinMaxPair) mMinMaxMap.get(lColumn);
				double lMin = lMinMaxPair.mMin;
				double lMax = lMinMaxPair.mMax;
				if (lFormatter instanceof NumberFormat) {
					NumberFormat lNumberFormat = (NumberFormat) lFormatter;
					Class lClass = iTable.getColumnClass(iColumn);
					if (lClass == Double.class) {
						lNumberFormat.setMaximumFractionDigits(3);
						lNumberFormat.setMinimumFractionDigits(0);
					} else if (lClass == Integer.class) {
						lNumberFormat.setParseIntegerOnly(true);
					}
				}

				if (!isNormalizedByColumn()) {
					lMin = getMinimumRenderedColumns(lColumnClass);
					lMax = getMaximumRenderedColumns(lColumnClass);
				}
				int lLabelWidth =
					((MinMaxPair) mMinMaxMap.get(lColumn)).mLabelPreferredWidth;
				lLabelWidth += getIconTextGap() * 2;
				BarChartIcon lIcon =
					new BarChartIcon(
						lColumnWidth - lLabelWidth,
						lRowHeight,
						2,
						lMin,
						lMax,
						lValue,
						0,
						COLORS[lModelColumn % COLORS.length]);

				lIcon.setFormater(null);
				lLabel.setText(lFormatter.format(new Double(lValue)));
				lLabel.setIcon(lIcon);
			} else {
				lLabel.setText(lFormatter.format(new Double(lValue)));
				lLabel.setIcon(null);
			}
		}
		return lComp;
	}

	private double getMaximumRenderedColumns(Class iClass) {
		Iterator lIter = mMinMaxMap.keySet().iterator();
		double lRes = Double.MIN_VALUE;
		while(lIter.hasNext()) {
			Integer lColumn = (Integer) lIter.next();
			Class lColumnClass = (Class) mColumnClassMap.get(lColumn);
			if (lColumnClass.equals(iClass)) {
				lRes = Math.max(
						((MinMaxPair) mMinMaxMap.get(lColumn)).mMax,
						lRes);
			}
		}
		return lRes;
	}
	private double getMinimumRenderedColumns(Class iClass) {
		Iterator lIter = mMinMaxMap.keySet().iterator();
		double lRes = Double.MAX_VALUE;
		while(lIter.hasNext()) {
			Integer lColumn = (Integer) lIter.next();
			Class lColumnClass = (Class) mColumnClassMap.get(lColumn);
			if (lColumnClass.equals(iClass)) {
				lRes = Math.min(
						((MinMaxPair) mMinMaxMap.get(lColumn)).mMin,
						lRes);
			}
		}
		return lRes;
	}
	/**
	 * @see javax.swing.event.TableModelListener
	 * #tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent iEvt) {
		if (iEvt.getColumn() == TableModelEvent.ALL_COLUMNS) {
			mMinMaxMap.clear();
		} else {
			mMinMaxMap.remove(new Integer(iEvt.getColumn()));
		}
	}

	public void setRenderingEnabled(boolean iEnabled) {
		mRenderingEnabled = iEnabled;
	}

	public boolean isRenderingEnabled() {
		return mRenderingEnabled;
	}

	public void setNormalizedByColumn(boolean iNormalized) {
		mNormalizedByColumn = iNormalized;
	}

	public boolean isNormalizedByColumn() {
		return mNormalizedByColumn;
	}

	public void setFont(Font iFont) {
		Font lFont = getFont();
		if (lFont == iFont) {
			return;
		}
		if (lFont != null && lFont.equals(iFont)) {
			return;
		}
		//Font has changed update the mLabelPrefferedSize
		super.setFont(iFont);
		if (mMinMaxMap == null) {
			return;
		}
		Set lSet = mMinMaxMap.keySet();
		Iterator lIter = lSet.iterator();
		while (lIter.hasNext()) {
			Integer lColumnIndex = (Integer) lIter.next();
			Class lColumnClass = (Class) mColumnClassMap.get(lColumnIndex);
			MinMaxPair lPair = (MinMaxPair) mMinMaxMap.get(lColumnIndex);
			lPair.mLabelPreferredWidth =
				getPreferredWidthForValue(
						lPair.mLabelPreferredValue,
						lColumnIndex.intValue(),
						lColumnClass);
		}

	}

}