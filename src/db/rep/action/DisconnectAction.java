package db.rep.action;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

import db.rep.util.Globals;
import db.rep.view.ObjectsPanel;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 27, 2005 Time: 10:15:31 AM
 */
public class DisconnectAction  extends AbstractAction {

    public DisconnectAction() {
		putValue(Action.NAME, "Disconnect");
//		Icon icon = ImageIcon(ChangePasswordAction.class.getResource("images/exit.gif"));
//		putValue(Action.SMALL_ICON, icon);
        putValue(Action.MNEMONIC_KEY, new Integer('D'));
		putValue(Action.SHORT_DESCRIPTION, "Disconnect");
		putValue(Action.LONG_DESCRIPTION, "Disconnect");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            Connection con = Globals.getDbConnection();
            if (con != null) {
                con.close();
            }
            JTabbedPane tab = Globals.getReportPane();
            ObjectsPanel op = (ObjectsPanel)tab.getComponentAt(0);
            op.clear();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
