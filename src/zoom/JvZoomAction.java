package zoom;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:11:44 PM
 * To change this template use File | Settings | File Templates.
 */
class JvZoomAction extends AbstractAction
{
    protected JvTable table;
    protected UndoManager undoManager;
    protected float scaleFactor;


    public JvZoomAction(JvTable table, UndoManager undoManager, int percent)
    {
        super("Zoom " + percent + "%");
        this.table = table;
        this.undoManager = undoManager;
        scaleFactor = percent / 100.0f;
    }


    public void actionPerformed(ActionEvent event)
    {
        float oldScaleFactor = table.getZoom();
        if (oldScaleFactor == scaleFactor)
            return;

        JvTableZoomEdit edit = new JvTableZoomEdit(table, oldScaleFactor, scaleFactor);
        edit.doit();
        UndoableEditEvent editEvent = new UndoableEditEvent(table, edit);
        undoManager.undoableEditHappened(editEvent);
    }
}



