package db.rep.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import db.rep.bean.DBColumn;
import db.rep.bean.DBTable;
import db.rep.bean.Report;
import db.rep.event.ReportEvent;
import db.rep.table.BaseTable;
import db.rep.table.DBColumnsTableModel;
import db.rep.table.DBTablesTableModel;
import db.rep.util.Globals;

/**
 * @author Decebal Suiu
 */
public class ObjectsPanel extends JPanel {

    Report report;
    DBTablesTableModel dbTablesModel = new DBTablesTableModel(new ArrayList());
    DBColumnsTableModel dbColumnsModel = new DBColumnsTableModel(new ArrayList());
    BaseTable dbTables;
    BaseTable dbColumns;
    JScrollPane dbTablesPane;
    JScrollPane dbColumnsPane;

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    Border border1 = BorderFactory.createEtchedBorder(Color.white,
            new Color(148, 145, 140));
    Border border2 = new TitledBorder(border1, "Tables and Views");
    Border border3 = BorderFactory.createEtchedBorder(Color.white,
            new Color(148, 145, 140));
    Border border4 = new TitledBorder(border3, "Columns");
    JPanel pneColumns = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel pneButtons = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    JButton btnDeselectAll = new JButton();
    JButton btnSelectAll = new JButton();

    public ObjectsPanel(Report report) {
        this.report = report;
        try {
            jbInit();
            fetchInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

     public ObjectsPanel() {
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

        dbTables = new BaseTable(dbTablesModel);
        dbColumns = new BaseTable(dbColumnsModel);

        // Add tables to scroller
        dbTablesPane = new JScrollPane(dbTables);
        dbColumnsPane = new JScrollPane(dbColumns);

        ListSelectionModel lsm = dbTables.getSelectionModel();
        lsm.addListSelectionListener(new TableSelectionListener());

        dbTablesPane.setBorder(border2);
//        dbColumnsPane.setBorder(border4);
        pneColumns.setBorder(border4);
        pneColumns.setLayout(gridBagLayout2);
        pneButtons.setLayout(gridBagLayout3);
        btnDeselectAll.setText("Deselect All");
        btnDeselectAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deselectAll();
            }
            
        });
        btnSelectAll.setText("Select All");
        btnSelectAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectAll();
            }
    
        });

        this.add(pneColumns, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 10, 10), 0, 0));
        this.add(dbTablesPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 5, 10), 0, 0));
        pneColumns.add(pneButtons, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        pneButtons.add(btnSelectAll,
                       new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(0, 10, 5, 10), 0, 0));
        pneButtons.add(btnDeselectAll,
                       new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(5, 10, 10, 10), 0, 0));
        pneColumns.add(dbColumnsPane,
                       new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 10, 10, 0), 0, 0));
    }

    private void fetchInfo() {
        HashMap map = report.getDBTables();
        if (map == null) {
            map = new HashMap();
        }
        dbTablesModel = new DBTablesTableModel(new ArrayList(map.values()));
        dbColumnsModel = new DBColumnsTableModel(new ArrayList());
        dbTables.setModel(dbTablesModel);
        dbColumns.setModel(dbColumnsModel);
    }

    public void selectAll() {
        int selectedRow = dbTables.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        DBTable table = (DBTable) dbTablesModel.getObjectForRow(selectedRow);
        List columns = table.columns;
        int n = columns.size();
        for (int i = 0; i < n; i++) {
            ((DBColumn) columns.get(i)).select = true;
        }
        dbColumns.setModel(new DBColumnsTableModel(columns));
        report.fireReportChanged(new ReportEvent(report, ReportEvent.OBJECTS_CHANGE));
    }

    public void deselectAll() {
        int selectedRow = dbTables.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        DBTable table = (DBTable) dbTablesModel.getObjectForRow(selectedRow);
        List columns = table.columns;
        int n = columns.size();
        for (int i = 0; i < n; i++) {
            ((DBColumn) columns.get(i)).select = false;
        }
        dbColumns.setModel(new DBColumnsTableModel(columns));
        report.fireReportChanged(new ReportEvent(report, ReportEvent.OBJECTS_CHANGE));
    }

    class TableSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            // ignore extra message
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            if (lsm.isSelectionEmpty()) {
                System.out.println("No row is selected.");
            } else {
                int selectedRow = lsm.getMinSelectionIndex();
                System.out.println("Row " + selectedRow + " is now selected");

                DBTable table = (DBTable) dbTablesModel.getObjectForRow(selectedRow);
                System.out.println(table);
                List columns = Globals.getReport().getColumns(table.name);
                dbColumnsModel = new DBColumnsTableModel(columns);
                dbColumns.setModel(dbColumnsModel);                
            }
        }

    }

    public void clear() {
        dbTablesModel = new DBTablesTableModel(new ArrayList());
        dbColumnsModel = new DBColumnsTableModel(new ArrayList());
        dbTables.setModel(dbTablesModel);
        dbColumns.setModel(dbColumnsModel);
    }

}
