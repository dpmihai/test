package db.rep.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import db.rep.util.Globals;

/**
 * @author Decebal Suiu
 */
public class ExitAction extends AbstractAction {
    
    public ExitAction() {
		putValue(Action.NAME, "Exit");
//		Icon icon = ImageIcon(ChangePasswordAction.class.getResource("images/exit.gif"));
//		putValue(Action.SMALL_ICON, icon);
        putValue(Action.MNEMONIC_KEY, new Integer('x'));        
		putValue(Action.SHORT_DESCRIPTION, "Exit");
		putValue(Action.LONG_DESCRIPTION, "Exit");
    }

    public void actionPerformed(ActionEvent e) {
        JFrame mainFrame = Globals.getMainFrame();
        if (mainFrame != null) {
            mainFrame.dispose();
        }
        
        System.exit(0);
    }  

}
