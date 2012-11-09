package db.rep.table;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.*;

import db.rep.util.Show;

/**
 * @author Decebal Suiu
 */
public class BaseTable extends JTable {

    private boolean sortable = true;

    public BaseTable() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        customizeHeader();

        setDefaultRenderer(Date.class, DATE_RENDERER);
    }

    public BaseTable(BaseAbstractTableModel model, boolean sortable) {
        this();
        this.sortable = sortable;
        setModel(model);
    }

    public BaseTable(AbstractTableModel model, boolean sortable) {
        this();
        this.sortable = sortable;
        setModel(model);
    }

    public BaseTable(BaseAbstractTableModel model) {
        this(model, true);
    }

    public BaseTable(AbstractTableModel model) {
        this(model, true);
    }

//    private boolean mouseDraggedOverHeader;
    private void customizeHeader() {
        JTableHeader header = getTableHeader();
        header.addMouseListener(new TableMouseAdapter());
//        header.addMouseMotionListener(new MouseMotionAdapter() {
//
//            public void mouseDragged(MouseEvent e) {
//                mouseDraggedOverHeader = true;
//            }
//
//        });
        header.setReorderingAllowed(false);
    }

    private int[] columnsWidth;
    
    /**
     * Sets the preferred columns' width for this table. These sizes are taken
     * into consideration <strong>only if</strong> they are not found in the
     * application's preferences.
     * <p>
     * Note: This method should be called <i>before</i> <code>setModel</code>
     * @param dim the preferred widths
     */
    public void setColumnsWidth(int[] dim) {
        columnsWidth = dim;
    }

    /**
     * Sets the number of rows in the table to <code>rowCount</code>
     *
     * @param rowCount the new number of rows in the table
     */
    public void setRowCount(int rowCount) {
        TableModel model = getModel();
        if (model instanceof TableSorter) {
            model = ((TableSorter) model).getTableModel();
        }
        ((BaseAbstractTableModel) model).setRowCount(rowCount);
    }

    public void setElements(List elements) {
        TableModel model = getModel();
        if (model instanceof TableSorter) {
            model = ((TableSorter) model).getTableModel();
        }
        ((BaseAbstractTableModel) model).setElements(elements);
    }

    public List getElements() {
        TableModel model = getModel();
        if (model instanceof TableSorter) {
            model = ((TableSorter) model).getTableModel();
        }
        return ((BaseAbstractTableModel) model).getElements();
    }

    public String getToolTipText(MouseEvent event) {
        Point point = event.getPoint();
        Object value = getValueAt(rowAtPoint(point), columnAtPoint(point));
        if (value != null) {
            return value.toString();
        }
        return super.getToolTipText(event);
    }

    public void setModel(TableModel dataModel) {
        if (sortable) {
            TableSorter sorter = new TableSorter(dataModel);
            super.setModel(sorter);
            sorter.setTableHeader(getTableHeader());
        } else {
            if (dataModel != null) {
                super.setModel(dataModel);
            }
        }

        if (!(dataModel instanceof DefaultTableModel)) {
            loadColumnsWidths();
        }
    }

    private void loadColumnsWidths() {
        TableModel tblModel = getModel();
        if (tblModel instanceof TableSorter) {
            tblModel = ((TableSorter) tblModel).getTableModel();
        }

        //int[] colWidths = Globals.getPreferencesManager().loadColumnsWidths(tblModel.getClass());
//        int[] colWidths = new int[tblModel.getColumnCount()];
//        for (int i = 0; i < tblModel.getColumnCount(); i++) {
//            int defaultWidth = columnsWidth != null ? columnsWidth.length > i ? columnsWidth[i] : 100 : 100;
//            int colWidth = colWidths != null ? colWidths.length > i ? colWidths[i] : defaultWidth : defaultWidth;
//            getColumnModel().getColumn(i).setPreferredWidth(colWidth);
//        }

        // todo remove this when using preferences
        try {
            if (columnsWidth != null) {
                for (int i = 0; i < tblModel.getColumnCount(); i++) {
                    getColumnModel().getColumn(i)
                        .setPreferredWidth(columnsWidth[i]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            // ignore
        }
    }

    public void setRendererForColumn(int column, TableCellRenderer renderer) {
        getColumnModel().getColumn(column).setCellRenderer(renderer);
    }

    /**
     * If the ancestor of this table is a dialog, let the user dispose of it
     * with the ESC key
     */
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
            return super.processKeyBinding(ks, e, condition, pressed);
        }

        // the dialog should close ONLY if no cell was editing
        boolean wasEditing = isEditing();
        super.processKeyBinding(ks, e, condition, pressed);
        return wasEditing;
    }

    protected class TableMouseAdapter extends MouseAdapter {

        private boolean popupShown;

        private void showPopup(int x, int y) {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new AbstractAction("Copy") {
                public void actionPerformed(ActionEvent e) {
                    copy();
                }
            });
            popup.show(BaseTable.this, x, y);
        }

        private void copy() {
            int rowCount = getRowCount();
            int colCount = getColumnCount();
            StringBuffer buf = new StringBuffer();
            for (int col = 0; col < colCount; col++) {
                buf.append(getColumnName(col) + "\t");
            }
            buf.append("\n");
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object o = getValueAt(row, col);
                    if (o != null) {
                        buf.append(o);
                    }
                    buf.append("\t");
                }
                buf.append("\n");
            }
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TableContentsTransferable(buf.toString()), null);
        }

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e.getX(), e.getY());
                popupShown = true;
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                if (!popupShown) {
                    showPopup(e.getX(), e.getY());
                    popupShown = false;
                }
            }
//            else {
//                if (mouseDraggedOverHeader) {
//                    mouseDraggedOverHeader = false;
//
//                    storeColumnsWidths();
//                }
//            }
        }
    }
//
//    private void storeColumnsWidths() {
//        TableModel tblModel = getModel();
//        if (tblModel instanceof TableSorter) {
//            tblModel = ((TableSorter) tblModel).getTableModel();
//        }
//
//        int[] colWidths = new int[getColumnCount()];
//        for (int i = 0; i < getColumnCount(); i++) {
//            colWidths[i] = getColumnModel().getColumn(i).getWidth();
//        }
//        //Globals.getPreferencesManager().storeColumnsWidths(tblModel.getClass(), colWidths);
//    }

    protected class TableContentsTransferable implements Transferable {

        private String data;

        public TableContentsTransferable(String data) {
            this.data = data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.stringFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.stringFlavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!flavor.equals(DataFlavor.stringFlavor)) {
                return null;
            }
            return data;
        }

    }

    /**
     * Formats dates as DD/MM/YYYY
     */
    public final static TableCellRenderer DATE_RENDERER = new DefaultTableCellRenderer() {

        public void setValue(Object value) {
            setText(Show.asDMY((Date) value));
        }

    };

    /**
     * Formats dates as DD/MM/YYYY HH:MM
     */
    public final static TableCellRenderer FULL_DATE_RENDERER = new DefaultTableCellRenderer() {

        public void setValue(Object value) {
            setText(Show.asDMYHM((Date) value));
        }
    };

    /**
     * Formats doubles with thousands separator
     */
    public final static TableCellRenderer DOUBLE_RENDERER = new DefaultTableCellRenderer() {
        {
            setHorizontalAlignment(RIGHT);
        }

        public void setValue(Object value) {
            String text = "";
            if (value != null) {
                text = Show.asNr1000((Double) value);
            }
            setText(text);
        }
    };

    /**
     * Formats doubles without thousands separator
     */
    public final static TableCellRenderer SIMPLE_DOUBLE_RENDERER = new DefaultTableCellRenderer() {
        {
            setHorizontalAlignment(RIGHT);
        }

        public void setValue(Object value) {
            String text = "";
            if (value != null) {
                text = Show.asNr1000(((Double) value).doubleValue(), false);
            }
            setText(text);
        }
    };

}
