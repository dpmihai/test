package zoom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:15:08 PM
 * To change this template use File | Settings | File Templates.
 */
interface JvCellFill
{
    public void doFill(JvUndoableTableModel model);
    public void undoFill(JvUndoableTableModel model);
}
