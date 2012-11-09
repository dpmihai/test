package zoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:13:10 PM
 * To change this template use File | Settings | File Templates.
 */
class JvFillDownAction extends AbstractAction
{
    protected JTable table;


    public JvFillDownAction(JTable table)
    {
        super("Fill Down");
        this.table = table;

        // As far as I can tell, in Excel the "Fill Down" action is always
        // enabled.
        setEnabled(true);

        KeyStroke accel = KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accel);

        table.getInputMap().put(accel, "fillDown");
        table.getActionMap().put("fillDown", this);
    }


    public void actionPerformed(ActionEvent event)
    {
        // Cell selection in JTable looks like a hack.  Basically it looks like
        // the union of column and row selection.
        int[] rowIndices = table.getSelectedRows();
        if (rowIndices.length == 0)
            return;

        JvUndoableTableModel model = (JvUndoableTableModel) table.getModel();
        JvCellFillEdit fillEdit = new JvCellFillEdit(model);

        for (int columnIndex : table.getSelectedColumns())
        {
            columnIndex = table.convertColumnIndexToModel(columnIndex);
            JvColumnFill fill = new JvColumnFill(model, columnIndex, rowIndices);
            fillEdit.addFill(fill);
        }

        fillEdit.fill();
        model.fireUndoableEdit(fillEdit);
    }
}
