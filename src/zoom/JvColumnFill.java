package zoom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:14:27 PM 
 */
class JvColumnFill implements JvCellFill
{
    protected int columnIndex;
    protected int[] rowIndices;
    protected Object[] values;


    public JvColumnFill(JvUndoableTableModel model, int columnIndex, int[] rowIndices)
    {
        this.columnIndex = columnIndex;
        this.rowIndices = rowIndices;

        values = new Object[rowIndices.length];
        for (int i = 0; i < rowIndices.length; i++)
            values[i] = model.getValueAt(rowIndices[i], columnIndex);
    }


    public void doFill(JvUndoableTableModel model)
    {
        for (int i = 1; i < rowIndices.length; i++)
        {
            if (model.isCellEditable(rowIndices[i], columnIndex))
                model.setValueAt(values[0], rowIndices[i], columnIndex, false);
        }
    }


    public void undoFill(JvUndoableTableModel model)
    {
        for (int i = 1; i < rowIndices.length; i++)
        {
            if (model.isCellEditable(rowIndices[i], columnIndex))
                model.setValueAt(values[i], rowIndices[i], columnIndex, false);
        }
    }
}
