package db.rep.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import db.rep.util.Globals;
import db.ConnectionDialog;
import util.ResizeUtil;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 27, 2005 Time: 10:04:13 AM
 */
public class CreateConnectionAction extends AbstractAction {

    public CreateConnectionAction() {
		putValue(Action.NAME, "Create Connection");
//		Icon icon = ImageIcon(ChangePasswordAction.class.getResource("images/exit.gif"));
//		putValue(Action.SMALL_ICON, icon);
        putValue(Action.MNEMONIC_KEY, new Integer('C'));
		putValue(Action.SHORT_DESCRIPTION, "Create Connection");
		putValue(Action.LONG_DESCRIPTION, "Create Connection");
    }

    public void actionPerformed(ActionEvent e) {
        JFrame mainFrame = Globals.getMainFrame();
        ConnectionDialog cd = new ConnectionDialog(mainFrame, "DB Connect");
        cd.setSize(400,300);
        ResizeUtil.centerComponent(cd, mainFrame);
        cd.setVisible(true);
    }

}
