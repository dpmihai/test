package db.rep.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import db.rep.bean.Field;
import db.rep.bean.Report;
import db.rep.event.ReportEvent;
import db.rep.listener.ReportListener;
import db.rep.table.BaseTable;
import db.rep.table.ComboBoxEditor;
import db.rep.table.FieldsTable;
import db.rep.table.FieldsTableModel;
import db.rep.util.Globals;

/**
 * @author Decebal Suiu
 */
public class FieldsPanel extends JPanel implements ReportListener {

    Report report;
    List fields;
    FieldsTableModel fieldsModel = new FieldsTableModel(new ArrayList());
    BaseTable fieldsTable;
    JScrollPane fieldsPane;

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel pneButtons = new JPanel();
    JButton btnAdd = new JButton();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JButton btnRemove = new JButton();
    JButton btnUp = new JButton();
    JButton btnDown = new JButton();
    JPanel pneFill = new JPanel();

    public FieldsPanel(Report report) {
        this.report = report;
        try {
            jbInit();
            fetchInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FieldsPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        fieldsTable = new FieldsTable(fieldsModel, false);
        fieldsTable.getSelectionModel().addListSelectionListener(new FieldsModelListener());

//        System.out.println("!!!!!!");
//        TableColumn tableColumn = fieldsTable.getColumnModel().getColumn(0);
//        System.out.println("tableColumn=" + tableColumn.getHeaderValue());
//        ComboBoxEditor comboBoxEditor = new ComboBoxEditor(new Vector());
//        tableColumn.setCellEditor(comboBoxEditor);

        // Add table to scroller
        fieldsPane = new JScrollPane(fieldsTable);
        btnAdd.setText("Add");
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addField();
            }

        });
        pneButtons.setLayout(gridBagLayout2);
        btnRemove.setText("Remove");
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeField();
            }

        });
        btnUp.setText("Up");
        btnUp.setEnabled(false);
        btnUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                upField();
            }

        });
        btnDown.setText("Down");
        btnDown.setEnabled(false);
        btnDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                downField();
            }

        });
        this.add(fieldsPane, new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 5), 0, 0));
        this.add(pneButtons, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0
                , GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL,
                new Insets(10, 5, 10, 10), 0, 0));
        pneButtons.add(btnAdd, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0));
        pneButtons.add(btnRemove, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0));
        pneButtons.add(btnUp, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0));
        pneButtons.add(btnDown, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0));
        pneButtons.add(pneFill, new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0
                , GridBagConstraints.SOUTH, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    public void setReport(Report report) {
        this.report = report;
        try {
            fetchInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchInfo() {
        fields = report.getFields();
        fieldsModel = new FieldsTableModel(fields);
        fieldsTable.setModel(fieldsModel);
    }

    public void addField() {
        fieldsModel.addObject(new Field());
    }

    public void removeField() {
        int selectedRow = fieldsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        Field field = (Field) fieldsModel.getObjectForRow(selectedRow);
        fieldsModel.deleteObject(selectedRow);
        report.getFields().remove(field);
        report.fireReportChanged(new ReportEvent(report,ReportEvent.FIELDS_CHANGE ));
    }

    public void upField() {
        int selectedRow = fieldsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
//        dbFieldsModel.m
    }

    public void downField() {
        int selectedRow = fieldsTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
    }

    public void reportChanged(ReportEvent event) {
        System.out.println("FieldsPanel reportChanged");
        ComboBoxEditor cbe = (ComboBoxEditor) fieldsTable.getColumnModel().getColumn(0).getCellEditor();
        System.out.println("cbe="+cbe);
        if (cbe == null) {
            cbe = new ComboBoxEditor(new Vector());
            fieldsTable.setCellEditor(cbe);
        }
        JComboBox cmbTables = (JComboBox)cbe.getComponent();
        Vector selectedTables = new Vector(report.getSelectedColumns().keySet());
        System.out.println("$$$: " + selectedTables);
        System.out.println("$$$: " + report.getSelectedColumns().values());
        ComboBoxModel model = new DefaultComboBoxModel(selectedTables);
        cmbTables.setModel(model);
        if (!isValid()) {
            Globals.showAlarm(this);
        }
    }

    public boolean isValid() {
        return false;
    }

    class FieldsModelListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            int row = fieldsTable.getSelectedRow();

            if (row > 0) {
                btnUp.setEnabled(true);
            } else {
                btnDown.setEnabled(false);
            }

            if (row < (fieldsModel.getRowCount() - 1)) {
                btnDown.setEnabled(true);
            } else {
                btnDown.setEnabled(false);
            }

            if (fieldsModel.getRowCount() != 0) {
                btnRemove.setEnabled(true);
            } else {
                btnRemove.setEnabled(false);
            }
        }

    }

}
