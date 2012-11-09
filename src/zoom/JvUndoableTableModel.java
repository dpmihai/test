package zoom;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:18:53 PM
 * To change this template use File | Settings | File Templates.
 */
class JvUndoableTableModel extends DefaultTableModel
{
    public JvUndoableTableModel(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }


    public Class getColumnClass(int column)
    {
        if (column >= 0 && column < getColumnCount())
            return getValueAt(0, column).getClass();

        return Object.class;
    }



    @Override
    public void setValueAt(Object value, int row, int column)
    {
        setValueAt(value, row, column, true);
    }


    public void setValueAt(Object value, int row, int column, boolean undoable)
    {
        UndoableEditListener[] listeners = getListeners(UndoableEditListener.class);
        if (undoable == false || listeners == null)
        {
            super.setValueAt(value, row, column);
            return;
        }


        Object oldValue = getValueAt(row, column);
        super.setValueAt(value, row, column);
        JvCellEdit cellEdit = new JvCellEdit(this, oldValue, value, row, column);
        fireUndoableEdit(listeners, cellEdit);
    }


    public void addUndoableEditListener(UndoableEditListener listener)
    {
        listenerList.add(UndoableEditListener.class, listener);
    }


    public void fireUndoableEdit(UndoableEdit edit)
    {
        UndoableEditListener[] listeners = getListeners(UndoableEditListener.class);
        fireUndoableEdit(listeners, edit);
    }


    protected void fireUndoableEdit(UndoableEditListener[] listeners, UndoableEdit edit)
    {
        UndoableEditEvent editEvent = new UndoableEditEvent(this, edit);
        for (UndoableEditListener listener : listeners)
            listener.undoableEditHappened(editEvent);
    }
}
