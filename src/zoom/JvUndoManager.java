package zoom;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:17:38 PM
 * To change this template use File | Settings | File Templates.
 */
class JvUndoManager extends UndoManager
{
    protected Action undoAction;
    protected Action redoAction;


    public JvUndoManager()
    {
        this.undoAction = new JvUndoAction(this);
        this.redoAction = new JvRedoAction(this);

        synchronizeActions();           // to set initial names
    }


    public Action getUndoAction()
    {
        return undoAction;
    }


    public Action getRedoAction()
    {
        return redoAction;
    }


    @Override
    public boolean addEdit(UndoableEdit anEdit)
    {
        try
        {
            return super.addEdit(anEdit);
        }
        finally
        {
            synchronizeActions();
        }
    }


    @Override
    protected void undoTo(UndoableEdit edit) throws CannotUndoException
    {
        try
        {
            super.undoTo(edit);
        }
        finally
        {
            synchronizeActions();
        }
    }


    @Override
    protected void redoTo(UndoableEdit edit) throws CannotRedoException
    {
        try
        {
            super.redoTo(edit);
        }
        finally
        {
            synchronizeActions();
        }
    }


    protected void synchronizeActions()
    {
        undoAction.setEnabled(canUndo());
        undoAction.putValue(Action.NAME, getUndoPresentationName());

        redoAction.setEnabled(canRedo());
        redoAction.putValue(Action.NAME, getRedoPresentationName());
    }
}
