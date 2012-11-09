package zoom;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:16:52 PM
 * To change this template use File | Settings | File Templates.
 */
class JvUndoAction extends AbstractAction
{
    protected final UndoManager manager;


    public JvUndoAction(UndoManager manager)
    {
        this.manager = manager;
    }


    public void actionPerformed(ActionEvent e)
    {
        try
        {
            manager.undo();
        }
        catch (CannotUndoException ex)
        {
            ex.printStackTrace();
        }
    }
}