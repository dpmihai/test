package db.rep.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import db.rep.bean.Report;
import db.rep.event.ReportEvent;
import db.rep.listener.ReportListener;
import db.rep.table.BaseTable;
import db.rep.table.ResultSetTableModel;
import db.rep.util.Globals;
import db.rep.util.SqlGenerator;

/**
 * @author Decebal Suiu
 */
public class PreviewPanel extends JPanel implements ReportListener {

    Report report;

    JTextArea taSql;
    JScrollPane sqlPane;
    JScrollPane dbResultsPane;
    JButton btnShowResults;

    ResultSetTableModel rsTablesModel;
    BaseTable rsTable;

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    Border border1 = BorderFactory.createEtchedBorder(Color.white,
            new Color(148, 145, 140));
    Border border2 = new TitledBorder(border1, "Sql");
    Border border3 = BorderFactory.createEtchedBorder(Color.white,
            new Color(148, 145, 140));
    Border border4 = new TitledBorder(border3, "Results");

    public PreviewPanel(Report report) {
        this.report = report;
        try {
            fetchInfo();
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

    public PreviewPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        rsTable = new BaseTable();
        dbResultsPane = new JScrollPane(rsTable);
        dbResultsPane.setPreferredSize(new Dimension(300, 300));

        taSql = new JTextArea();
        taSql.setRows(5);
        taSql.setEditable(false);
        taSql.setLineWrap(true);
        taSql.setWrapStyleWord(true);
        sqlPane = new JScrollPane(taSql);
        sqlPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sqlPane.setBorder(border2);
        dbResultsPane.setBorder(border4);

        btnShowResults = new JButton("Show Results");
        btnShowResults.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showResults();
            }

        });

        this.add(btnShowResults, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 5, 10), 0, 0));
        this.add(dbResultsPane, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
                , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                new Insets(5, 10, 10, 10), 0, 0));
        this.add(sqlPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 5, 10), 0,
                0));
    }

    private void fetchInfo() {
    }

    public void showResults() {
        String sql = taSql.getText();
        try {
            rsTablesModel = new ResultSetTableModel(Globals.getDbConnection(), sql);
            rsTable.setModel(rsTablesModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportChanged(ReportEvent event) {
        if (event.getChange() == ReportEvent.FIELDS_CHANGE) {
            String sql = new SqlGenerator(report).generateSql();
            System.out.println("sql: " + sql);
            taSql.setText(sql);
        }
    }

}
