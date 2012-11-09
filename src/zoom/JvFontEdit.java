package zoom;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:09:17 PM
 */
class JvFontEdit extends AbstractUndoableEdit
{
    protected JComponent component;
    protected Font oldValue;
    protected Font newValue;


    public JvFontEdit(JComponent component, Font oldValue, Font newValue)
    {
        this.component = component;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    @Override
    public String getPresentationName()
    {
        return "Font";
    }


    @Override
    public String getUndoPresentationName()
    {
        return super.getUndoPresentationName() + ' ' + getFontSpec(oldValue);
    }


    @Override
    public String getRedoPresentationName()
    {
        return super.getRedoPresentationName() + ' ' + getFontSpec(newValue);
    }


    protected String getFontSpec(Font font)
    {
        return font.getFontName();
    }


    public void doit()
    {
        setFont(newValue);
    }


    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();

        setFont(oldValue);
    }


    @Override
    public void redo() throws CannotUndoException
    {
        super.redo();

        setFont(newValue);
    }


    protected void setFont(Font font)
    {
        component.setFont(font);
        if (component instanceof JTable)
            ((JTable) component).getTableHeader().setFont(font);
    }
}