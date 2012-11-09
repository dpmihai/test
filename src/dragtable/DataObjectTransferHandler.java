package dragtable;
 
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
 
/**
 * @author Decebal Suiu
 */
public class DataObjectTransferHandler extends TransferHandler {
 
    private static DropHandler dropHandler = new DropHandler();
    
    private DataFlavor dataFlavor;
    private Point dragPoint;
    private Point dropPoint;
    private Component dragComponent;
    private Component dropComponent;
 
    public DataObjectTransferHandler(DataFlavor dataFlavor) {
        super();
        this.dataFlavor = dataFlavor;
    }

    public Point getDropPoint() {
        return dropPoint;
    }
 
    public void setDropPoint(Point dropPoint) {
        this.dropPoint = dropPoint;
    }
 
    public Component getDragComponent() {
        return dragComponent;
    }
 
    public void setDragComponent(Component dragComponent) {
        this.dragComponent = dragComponent;
    }
 
    public Point getDragPoint() {
        return dragPoint;
    }
 
    public void setDragPoint(Point dragPoint) {
        this.dragPoint = dragPoint;
    }
 
    public Component getDropComponent() {
        return dropComponent;
    }
 
    public void setDropComponent(Component dropComponent) {
        this.dropComponent = dropComponent;
    }
 
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY_OR_MOVE;
    }
 
    public boolean canImport(JComponent c, DataFlavor[] transferFlavors) {
        if (c.isEnabled() && (c instanceof JTable)) {
            for (int i = 0; i < transferFlavors.length; i++) {
                if (transferFlavors[i].equals(dataFlavor)) {
                    return true;
                }
            }
        }
        
        return false;
    }
 
    public void exportAsDrag(JComponent comp, InputEvent ev, int action) {
        setDragComponent(comp);
        setDragPoint(((MouseEvent) ev).getPoint());
        super.exportAsDrag(comp, ev, action);
    }
 
    protected Transferable createTransferable(JComponent c) {
        Transferable t = null;
        if (c instanceof JTable) {
            JTable table = (JTable) c;
            int[] selection = table.getSelectedRows();
            Vector selectedRows = new Vector();            
            for (int aSelection : selection) {
                selectedRows.add(((DefaultTableModel) table.getModel()).getDataVector().get(aSelection));
            }
            t = new ListTransferable(selectedRows, dataFlavor);
        }
        
        return t;
    }
 
    public boolean importData(JComponent comp, Transferable t) {
        if (!canImport(comp, t.getTransferDataFlavors())) {
            return false;
        }
        
        try {
            if (getDragComponent() != comp) {
                List list = (List) t.getTransferData(dataFlavor);
                JTable table = (JTable) comp;
                DefaultTableModel model = ((DefaultTableModel) table.getModel());
                int insertRow;
                if (getDropPoint() != null) {
                    insertRow = table.rowAtPoint(getDropPoint());
                } else {
                    insertRow = table.getSelectedRow();
                }
                
                for (int i = 0; i < list.size(); i++) {
                    model.insertRow(insertRow + i, (Vector) list.get(i));
                }
                
                table.getSelectionModel().clearSelection();
                table.getSelectionModel().setSelectionInterval(insertRow, insertRow + list.size() - 1);
                table.requestFocus();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action != DnDConstants.ACTION_MOVE) {
            return;
        }
        
        try {
            List list = (List) data.getTransferData(dataFlavor);
            JTable table = (JTable) source;
            DefaultTableModel model = ((DefaultTableModel) table.getModel());
            if (source != getDropComponent()) {
                int index;
                for (int i = 0; i < list.size(); i++) {
                    index = model.getDataVector().indexOf(list.get(i));
                    model.removeRow(index);
                }
            } else {
                int index;
                int insertRow = table.rowAtPoint(getDropPoint());
                for (int i = 0; i < list.size(); i++) {
                    index = model.getDataVector().indexOf(list.get(i));
                    model.moveRow(index, index, insertRow + i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static DropHandler getDropHandler() {
        return dropHandler;
    }
 
    static class DropHandler implements DropTargetListener, Serializable {
 
        private boolean canImport;
 
        private boolean actionSupported(int action) {
            return (action & (DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK)) !=
                   DnDConstants.ACTION_NONE;
        }
 
        public void dragEnter(DropTargetDragEvent ev) {
            DataFlavor[] flavors = ev.getCurrentDataFlavors();
            JComponent c = (JComponent) ev.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();
 
            if (importer != null && importer.canImport(c, flavors)) {
                canImport = true;
            } else {
                canImport = false;
            }
 
            int dropAction = ev.getDropAction();
 
            if (canImport && actionSupported(dropAction)) {
                ev.acceptDrag(dropAction);
            } else {
                ev.rejectDrag();
            }
        }
 
        public void dragOver(DropTargetDragEvent ev) {
            int dropAction = ev.getDropAction();
            if (canImport && actionSupported(dropAction)) {
                JTable table = (JTable) ev.getDropTargetContext().getComponent();
                int row = table.rowAtPoint(ev.getLocation());
                table.getSelectionModel().setSelectionInterval(row, row);
                ev.acceptDrag(dropAction);
            } else {
                ev.rejectDrag();
            }
        }
 
        public void dragExit(DropTargetEvent ev) {
        }
 
        public void drop(DropTargetDropEvent ev) {
            int dropAction = ev.getDropAction();
            JComponent c = (JComponent) ev.getDropTargetContext().getComponent();
            DataObjectTransferHandler importer = (DataObjectTransferHandler) c.getTransferHandler();
 
            if (canImport && importer != null && actionSupported(dropAction)) {
                ev.acceptDrop(dropAction);
                try {
                    Transferable t = ev.getTransferable();
                    importer.setDropPoint(ev.getLocation());
                    importer.setDropComponent(c);
                    ev.dropComplete(importer.importData(c, t));
                } catch (RuntimeException re) {
                    ev.dropComplete(false);
                }
            } else {
                ev.rejectDrop();
            }
        }
 
        public void dropActionChanged(DropTargetDragEvent ev) {
            int dropAction = ev.getDropAction();
            if (canImport && actionSupported(dropAction)) {
                ev.acceptDrag(dropAction);
            } else {
                ev.rejectDrag();
            }
        }
        
    }
    
}
