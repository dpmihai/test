package zoom;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:12:31 PM
 * To change this template use File | Settings | File Templates.
 */
class JvTableZoomEdit extends AbstractUndoableEdit
{
    protected JvTable table;
    protected float oldValue;
    protected float newValue;


    public JvTableZoomEdit(JvTable table, float oldValue, float newValue)
    {
        this.table = table;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    @Override
    public String getPresentationName()
    {
        return "Zoom";
    }


    public void doit()
    {
        table.setZoom(newValue);
    }


    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();

        table.setZoom(oldValue);
    }


    @Override
    public void redo() throws CannotUndoException
    {
        super.redo();

        table.setZoom(newValue);
    }
}
