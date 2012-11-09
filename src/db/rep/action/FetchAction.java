package db.rep.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import db.rep.util.Globals;
import db.rep.view.ObjectsPanel;
import db.rep.view.RelationsPanel;
import db.rep.view.FieldsPanel;
import db.rep.view.PreviewPanel;
import db.rep.bean.Report;

/**
 * @author Decebal Suiu
 */
public class FetchAction extends AbstractAction {

    public FetchAction() {
        putValue(Action.NAME, "Fetch");
//		Icon icon = ImageIcon(ChangePasswordAction.class.getResource("images/exit.gif"));
//		putValue(Action.SMALL_ICON, icon);
        putValue(Action.MNEMONIC_KEY, new Integer('F'));
        putValue(Action.SHORT_DESCRIPTION, "Fetch");
        putValue(Action.LONG_DESCRIPTION, "Fetch");
    }

    public void actionPerformed(ActionEvent e) {
        JFrame mainFrame = Globals.getMainFrame();
        if (mainFrame == null) {
            return;
        }

        Report report = new Report();
        Globals.setReport(report);
        JTabbedPane tab = Globals.getReportPane();
        ObjectsPanel op = (ObjectsPanel)tab.getComponentAt(0);
        op.setReport(report);

        RelationsPanel rp = (RelationsPanel)tab.getComponentAt(1);
        rp.setReport(report);

        FieldsPanel fp = (FieldsPanel)tab.getComponentAt(2);
        fp.setReport(report);
        report.addReportListener(fp);

        PreviewPanel pp = (PreviewPanel)tab.getComponentAt(3);
        pp.setReport(report);
        report.addReportListener(pp);
    }
}


