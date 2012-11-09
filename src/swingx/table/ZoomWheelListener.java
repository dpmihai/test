package swingx.table;


import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ZoomWheelListener extends MouseAdapter implements MouseWheelListener, ListSelectionListener {
	
	private static final int DEFAULT_ROW_HEIGHT = 16;
	/**
	 * Default Rendered class in JTable
	 * @see JTable#createDefaultRenderers()
	 */
	private static Class[] DEFAULT_RENDERED_CLASS = {
        Object.class,
        Number.class,
        Float.class,
        Double.class,
        Date.class,
        Icon.class,
        ImageIcon.class,
        Boolean.class};
	
	private static final class ZoomTableModelListener 
										implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent iEvt) {
			Object lSource = iEvt.getSource();
			if (lSource instanceof JTable) {
				JTable lTable = (JTable) lSource;
				installZoomRenderer(lTable);
			}
		}
		
	}
	
	
	private static final class ZoomRenderer implements TableCellRenderer {

		private TableCellRenderer mRendererDelegate;
		
		private ZoomRenderer(TableCellRenderer iRenderer) {
			mRendererDelegate = iRenderer;
		}
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component lComp = mRendererDelegate.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			JComponent lJComp = (JComponent) lComp;
			Font lFont = lJComp.getFont();
			int lRowHeight = table.getRowHeight(row);
			if (table.getRowHeight(row) == DEFAULT_ROW_HEIGHT) {
				Font lDefaultFont = UIManager.getFont("Table.font");
				if (lDefaultFont != null) {
					lJComp.setFont(lDefaultFont);
					return lJComp;
				}
			}
			Font lZoomFont = lFont.deriveFont((float) (lRowHeight - 4));
			lJComp.setFont(lZoomFont);
			return lJComp;
		
		}
		
	}
	

	
	public static void installZoom(JTable oTable) {
		ZoomWheelListener lZoom = new ZoomWheelListener(oTable);
		oTable.addMouseWheelListener(lZoom);
		oTable.addMouseListener(lZoom);
		oTable.getSelectionModel().addListSelectionListener(lZoom);
		oTable.addPropertyChangeListener("model", new ZoomTableModelListener());
		installZoomRenderer(oTable);
	}
	
	public static void installZoomRenderer(JTable iTable) {
		
		for (int i = 0; i < iTable.getColumnCount(); i++) {
			TableColumn lTableColumn = iTable.getColumnModel().getColumn(i);
			TableCellRenderer lRenderer = lTableColumn.getCellRenderer();
			if (lRenderer != null && !(lRenderer instanceof ZoomRenderer)) {
				lTableColumn.setCellRenderer(new ZoomRenderer(lRenderer));
			}
			Class lClass = iTable.getColumnClass(i);
			lRenderer = iTable.getDefaultRenderer(lClass);
			if (lRenderer != null && !(lRenderer instanceof ZoomRenderer)) {
				iTable.setDefaultRenderer(lClass, new ZoomRenderer(lRenderer));
			}
		}
		for (int i = 0; i < DEFAULT_RENDERED_CLASS.length; i++) {
			TableCellRenderer lRenderer = 
				iTable.getDefaultRenderer(DEFAULT_RENDERED_CLASS[i]);
			if (lRenderer != null && !(lRenderer instanceof ZoomRenderer)) {
				iTable.setDefaultRenderer(
						DEFAULT_RENDERED_CLASS[i], 
						new ZoomRenderer(lRenderer));
			}
		}
	}
	
	public static void unInstallZoom(JTable oTable) {
		MouseWheelListener[] lWheelListeners = 
			oTable.getMouseWheelListeners();
		for (int i = 0; i < lWheelListeners.length; i++) {
			if (lWheelListeners[i] instanceof ZoomWheelListener) {
				oTable.removeMouseWheelListener(lWheelListeners[i]);
			}
		}
		MouseListener[] lMouseListeners = 
			oTable.getMouseListeners();
		for (int i = 0; i < lMouseListeners.length; i++) {
			if (lMouseListeners[i] instanceof ZoomWheelListener) {
				oTable.removeMouseListener(lMouseListeners[i]);
			}
		}
		PropertyChangeListener[] lZoomModelListeners = 
			oTable.getPropertyChangeListeners("model");
		for (int i = 0; i < lZoomModelListeners.length; i++) {
			if (lZoomModelListeners[i] instanceof ZoomTableModelListener) {
				oTable.removePropertyChangeListener(
						"model", 
						lZoomModelListeners[i]);
			}
		}
	}


	private JTable mTable;
	
	public ZoomWheelListener(JTable iTable) {
		mTable = iTable;
		
	}
	
	private void zoom(JTable iTable) {
		
	}
	
	private void restoreDefaultFont(JTable iTable) {
		Font lFont = UIManager.getFont("Table.font");
		iTable.setRowHeight(DEFAULT_ROW_HEIGHT);
		if (lFont != null) {
			iTable.setFont(lFont);
		}
	}
	
	public void mouseClicked(MouseEvent iEvent) {
		if (iEvent.getClickCount() == 2
				&& iEvent.isControlDown()
				&& iEvent.getButton() == MouseEvent.BUTTON2) {
			try {
				JTable lTable = (JTable) iEvent.getSource();
				restoreDefaultFont(lTable);
				lTable.setShowGrid(true);
			} catch (ClassCastException lExce) { }
		}
	}
	
	
	
	public void mouseWheelMoved(MouseWheelEvent iEvent) {
		Object lSource = iEvent.getSource();
		if (lSource instanceof JTable) {
			JTable lTable = (JTable) lSource;
			if (iEvent.isControlDown()) {
				lTable.setShowGrid(false);
				if (iEvent.isShiftDown()) {
					//Just zoom on Selected rows
					int[] lSelectedRows = lTable.getSelectedRows();
					for (int i = 0; i < lSelectedRows.length; i++) {
						int lRowHeight = lTable.getRowHeight(lSelectedRows[i]);
						lRowHeight += iEvent.getWheelRotation();
						if (lRowHeight > 2) {
							lTable.setRowHeight(lSelectedRows[i], lRowHeight);
						}
					}
				} else {
					//Apply Zoom on the entire Table
					int lRowHeight = lTable.getRowHeight();
					lRowHeight += iEvent.getWheelRotation();
					if (lRowHeight > 2) {
						lTable.setRowHeight(lRowHeight);
					}
				}
			} else {
				//Forward the MouseWheelEvent to the parent
				lTable.getParent().dispatchEvent(iEvent);
			}
		}
	}

	
	public void valueChanged(ListSelectionEvent iEvent) {
//		int[] lSelectedRows = mTable.getSelectedRows();
//		if (lSelectedRows == null) {
//			return;
//		}
//		mTable.setRowHeight(mTable.getRowHeight()); //reset the row height model
//		for (int i = 0; i < lSelectedRows.length; i++) {
//			int lRowHeight = mTable.getRowHeight(lSelectedRows[i]);
//			if (lRowHeight < DEFAULT_ROW_HEIGHT) {
//				mTable.setRowHeight(lSelectedRows[i], DEFAULT_ROW_HEIGHT);
//			}
//		}
	}
}
