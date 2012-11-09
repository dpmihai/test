package zoom;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:16:23 PM
 * To change this template use File | Settings | File Templates.
 */
class JvRedoAction extends AbstractAction
{
    protected final UndoManager manager;


    public JvRedoAction(UndoManager manager)
    {
        this.manager = manager;
    }


    public void actionPerformed(ActionEvent e)
    {
        try
        {
            manager.redo();
        }
        catch (CannotRedoException ex)
        {
            ex.printStackTrace();
        }
    }
}