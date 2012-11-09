package vista.action;

import vista.VistaAction;
import vista.VistaButton;
import wait.UIActivator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 3:52:17 PM
 */
public class SaveAction extends VistaAction {
    
    public SaveAction() {
        putValue(Action.NAME, "Save");
        putValue(Action.SMALL_ICON, new ImageIcon(VistaButton.class.getResource("save.png")));
        putValue(Action.MNEMONIC_KEY, new Integer('S'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK));
        putValue(Action.SHORT_DESCRIPTION, "Save");
        putValue(Action.LONG_DESCRIPTION, "Save");
    }


    @Override
    public void perform() {        
        try {
            System.out.println("start save ....");
            Thread.sleep(3000);
            System.out.println("end save ....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
