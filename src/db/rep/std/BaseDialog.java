package db.rep.std;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import db.rep.util.Show;

/**
 * @author Decebal Suiu
 */
public class BaseDialog extends JDialog {

    {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Show.pack(BaseDialog.this);
            }
        });
    }

    public BaseDialog() throws HeadlessException {
    }

    public BaseDialog(Dialog owner) throws HeadlessException {
        super(owner);
    }

    public BaseDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public BaseDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public BaseDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public BaseDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
    }

    public BaseDialog(Frame owner) throws HeadlessException {
        super(owner);
    }

    public BaseDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public BaseDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public BaseDialog(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
    }

    public BaseDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    /**
     * Disposes of this dialog when the user presses ESC
     */
    protected JRootPane createRootPane() {
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = super.createRootPane();
        rootPane.getActionMap().put(action, action);
        rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, action);
        return rootPane;
    }

    public void show() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().downFocusCycle(getContentPane());
        super.show();
    }

}
