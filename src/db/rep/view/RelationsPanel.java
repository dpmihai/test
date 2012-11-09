package db.rep.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.rep.bean.Report;
import db.rep.table.BaseTable;
import db.rep.table.DBRelationsTableModel;

/**
 * @author Decebal Suiu
 */
public class RelationsPanel extends JPanel {

    Report report;
    List relations;
    DBRelationsTableModel dbRelationsModel = new DBRelationsTableModel(new ArrayList());
    BaseTable dbRelations;
    JScrollPane dbRelationsPane;

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public RelationsPanel(Report report) {
        this.report = report;
        try {
            jbInit();
            fetchInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RelationsPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setReport(Report report) {
        this.report = report;
        try {
            fetchInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        dbRelations = new BaseTable(dbRelationsModel);

        // Add table to scroller
        dbRelationsPane = new JScrollPane(dbRelations);
        this.add(dbRelationsPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(10, 10, 10, 10), 0, 0));
    }

    private void fetchInfo() {
        relations = new ArrayList();
        dbRelationsModel = new DBRelationsTableModel(relations);
        dbRelations.setModel(dbRelationsModel);
    }

}
