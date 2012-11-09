package zoom;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:13:55 PM
 * To change this template use File | Settings | File Templates.
 */
class JvCellFillEdit extends AbstractUndoableEdit
{
    protected JvUndoableTableModel tableModel;
    protected ArrayList<JvCellFill> fills = new ArrayList<JvCellFill>();


    public JvCellFillEdit(JvUndoableTableModel tableModel)
    {
        this.tableModel = tableModel;
    }


    public void addFill(JvCellFill fill)
    {
        fills.add(fill);
    }


    @Override
    public String getPresentationName()
    {
        return "Cell Fill";
    }


    public void fill()
    {
        for (JvCellFill fill : fills)
            fill.doFill(tableModel);
    }


    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();

        for (JvCellFill fill : fills)
            fill.undoFill(tableModel);
    }


    @Override
    public void redo() throws CannotUndoException
    {
        super.redo();

        fill();
    }
}
