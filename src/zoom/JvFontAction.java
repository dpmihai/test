package zoom;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:08:28 PM
 */
class JvFontAction extends AbstractAction
{
    protected JComponent component;
    protected UndoManager undoManager;
    protected String fontSpec;


    public JvFontAction(JComponent component, UndoManager undoManager, String fontSpec)
    {
        super("Font " + fontSpec);
        this.component = component;
        this.undoManager = undoManager;
        this.fontSpec = fontSpec.replace(" ", "-");
    }


    public void actionPerformed(ActionEvent event)
    {
        Font oldFont = component.getFont();
        Font font = Font.decode(fontSpec);

        JvFontEdit edit = new JvFontEdit(component, oldFont, font);
        edit.doit();
        UndoableEditEvent editEvent = new UndoableEditEvent(component, edit);
        undoManager.undoableEditHappened(editEvent);
    }
}