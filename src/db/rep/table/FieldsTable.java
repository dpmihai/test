package db.rep.table;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;

import db.rep.util.Globals;
import db.rep.bean.Field;
import db.rep.bean.Report;
import db.rep.event.ReportEvent;

/**
 * @author Decebal Suiu
 */
public class FieldsTable extends BaseTable {

    public FieldsTable() {
    }

    public FieldsTable(AbstractTableModel model) {
        super(model);
        init();
    }

    public FieldsTable(AbstractTableModel model, boolean sortable) {
        super(model, sortable);
        init();
    }

    public FieldsTable(BaseAbstractTableModel model) {
        super(model);
        init();
    }

    public FieldsTable(BaseAbstractTableModel model, boolean sortable) {
        super(model, sortable);
        init();
    }

    private void init() {
        int rows = this.getRowCount();
        int cols = this.getColumnCount();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                getCellEditor(i, j);
            }
        }
    }


    public TableCellEditor getCellEditor(int row, int col) {
        TableCellEditor tmpEditor = null;
        String selectedTable = (String) this.getValueAt(row, 0);
        if (col == 0) {
            ComboBoxEditor cbe = new ComboBoxEditor(new Vector());
            JComboBox cmbTables = (JComboBox) cbe.getComponent();
            Vector selectedTables = new Vector(Globals.getReport().getSelectedColumns().keySet());
            System.out.println("$$$ cell editor : " + selectedTables);
            ComboBoxModel model = new DefaultComboBoxModel(selectedTables);
            cmbTables.setModel(model);
            return cbe;
        } else if (col == 1) {
            List selectedColumns = (List) Globals.getReport().getSelectedColumnNames(selectedTable);
            System.out.println("columns = " + selectedColumns);
            ComboBoxEditor comboBoxEditor = new ComboBoxEditor(new Vector(selectedColumns));
            comboBoxEditor.addCellEditorListener(new CellEditorListener() {

                public void editingStopped(ChangeEvent e) {
                    int row = getSelectedRow();
                    if (row == -1) {
                        return;
                    }
                    System.out.println(getModel());
                    FieldsTableModel model = (FieldsTableModel) getModel();
                    String selectedColumn = (String) getValueAt(row, 1);
                    if (selectedColumn == null) {
                        return;
                    }
                    System.out.println("selectedColumn: " + selectedColumn);
                    Field field = (Field) model.getObjectForRow(row);
                    System.out.println("field: " + field);
                    Report report = Globals.getReport();
                    report.getFields().add(field);
                    report.fireReportChanged(new ReportEvent(report, ReportEvent.FIELDS_CHANGE));
                }

                public void editingCanceled(ChangeEvent e) {
                }

            });
            return comboBoxEditor;
        }

        return super.getCellEditor(row, col);
    }

}
