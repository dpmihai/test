package db.rep;

import java.awt.*;
import javax.swing.*;

import db.rep.action.CreateConnectionAction;
import db.rep.action.DisconnectAction;
import db.rep.action.ExitAction;
import db.rep.action.FetchAction;
import db.rep.util.Globals;
import db.rep.view.FieldsPanel;
import db.rep.view.ObjectsPanel;
import db.rep.view.PreviewPanel;
import db.rep.view.RelationsPanel;

/**
 * @author Decebal Suiu
 */
public class MainFrame extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();

    // menu
    private JMenu mnuReport = new JMenu();
    private JMenu mnuHelp = new JMenu();

    // menu items
    private JMenuItem mnuItemFetch = new JMenuItem(new FetchAction());
    private JMenuItem mnuItemExit = new JMenuItem(new ExitAction());
    private JMenuItem mnuItemConnect = new JMenuItem(new CreateConnectionAction());
    private JMenuItem mnuItemDisconnect = new JMenuItem(new DisconnectAction());

    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel lblImage = new JLabel();

    public MainFrame(String title) {
        super(title);
        try {
//            setBackgroundImage();

            jbInit();
//            getContentPane().add(new PneCeDetails(this));
            validate();
            this.setLocationRelativeTo(null);
            Globals.setMainFrame(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setJMenuBar(menuBar);

        // menus stuff
        mnuReport.setText("Report");
        mnuReport.setMnemonic('R');
        mnuReport.add(mnuItemConnect);
        mnuReport.add(mnuItemDisconnect);
        mnuReport.add(mnuItemFetch);
        mnuReport.add(mnuItemExit);
        mnuHelp.setText("Help");
        mnuHelp.setMnemonic('H');

        // menu items stuff
        menuBar.add(mnuReport);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(mnuHelp);

//        Report report = new Report();
//        Globals.setReport(report);
        JTabbedPane reportPane = new JTabbedPane();
        Globals.setReportPane(reportPane);
        ObjectsPanel op = new ObjectsPanel();
        reportPane.addTab("Objects", op);
        RelationsPanel rp = new RelationsPanel();
        reportPane.addTab("Relations", rp);
        FieldsPanel fieldsPanel = new FieldsPanel();
//        report.addReportListener(fieldsPanel);
        reportPane.addTab("Fields", fieldsPanel);
        PreviewPanel previewPanel = new PreviewPanel();
//        report.addReportListener(previewPanel);
        reportPane.addTab("Preview", previewPanel);
        reportPane.setVisible(true);

        getContentPane().add(reportPane);
        getContentPane().validate();

        // tool bar stuff
//        toolBar.add(new ExitAction());
    }

    private void setBackgroundImage() {
        ImageIcon image = new ImageIcon(MainFrame.class.getResource("background.gif"));
        this.getContentPane().setLayout(gridBagLayout1);
//        this.setResizable(false);
        lblImage.setHorizontalAlignment(0);
        lblImage.setIcon(image);
        lblImage.setOpaque(true);
        this.getContentPane().add(lblImage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }

}
