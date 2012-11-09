package vista.action;

import vista.VistaButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 4:14:25 PM
 */
public class CancelAction extends AbstractAction {

    public CancelAction() {
        putValue(Action.NAME, "Don't save");
        putValue(Action.SMALL_ICON, new ImageIcon(VistaButton.class.getResource("cancel.png")));
        putValue(Action.MNEMONIC_KEY, new Integer('D'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D,
                KeyEvent.CTRL_DOWN_MASK));
        putValue(Action.SHORT_DESCRIPTION, "Cancel");
        putValue(Action.LONG_DESCRIPTION, "Cancel");
    }

    public void actionPerformed(ActionEvent ev) {
        System.out.println("Cancel");
    }

}
