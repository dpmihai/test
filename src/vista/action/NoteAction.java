package vista.action;

import vista.VistaAction;
import vista.VistaButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 10, 2007
 * Time: 4:28:49 PM
 */
public class NoteAction extends VistaAction {

    public NoteAction() {
        putValue(Action.NAME, "Mark a note about it");
        putValue(Action.SMALL_ICON, new ImageIcon(VistaButton.class.getResource("note.png")));
        putValue(Action.MNEMONIC_KEY, new Integer('M'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M,
                KeyEvent.CTRL_DOWN_MASK));
        putValue(Action.SHORT_DESCRIPTION, "Mark a note");
        putValue(Action.LONG_DESCRIPTION, "Mark a note");
    }

    @Override
    public void perform() {
        try {
            System.out.println("start mark ....");
            Thread.sleep(2000);
            System.out.println("end mark ....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
