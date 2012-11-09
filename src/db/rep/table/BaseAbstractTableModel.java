package db.rep.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author Decebal Suiu
 */
public abstract class BaseAbstractTableModel extends AbstractTableModel {

    protected List elements = new ArrayList();
    protected List columnNames = new ArrayList();
    
    protected TableSorter tableSorter;

    public BaseAbstractTableModel(List elements) {
        if (elements != null && elements.size() > 0) {
            this.elements.addAll(elements);
        }
        initializeColumnNames();
    }

    /**
     * Initializes the column names. This method is called automatically by the
     * constructor.
     */
    protected abstract void initializeColumnNames();

    public Class getColumnClass(int c) {
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object value = getValueAt(i, c);
            if (value != null) {
                return value.getClass();
            }
        }
        return Object.class;
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
        return elements.size();
    }

    public String getColumnName(int col) {
        return (String) columnNames.get(col);
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setRowCount(int rowCount) {
        if (rowCount == 0) {
            elements.clear();
            fireTableDataChanged();
        } else if (rowCount < elements.size()) {
            for (int i = elements.size() - 1; i >= rowCount; i--) {
                elements.remove(i);
            }
            fireTableDataChanged();
        }
    }

    public void setElements(List elemente) {
        this.elements = elemente;
        fireTableDataChanged();
    }

    public List getElements() {
        return new ArrayList(elements);
    }
    
    public void addObject(Object o) {
        elements.add(o);
        fireTableDataChanged();
    }

    public void addObjects(List periods) {
        elements.addAll(periods);
        fireTableDataChanged();
    }
    
    public void updateObject(int row, Object o) {
        if (tableSorter != null) {
            row = tableSorter.modelIndex(row);
        }
        elements.set(row, o);
        fireTableDataChanged();
    }
    
    public void deleteObject(int row) {
        if (tableSorter != null) {
            row = tableSorter.modelIndex(row);
        }
        elements.remove(row);
        fireTableDataChanged();
    }
    
    public Object getObjectForRow(int row) {
        if (tableSorter != null) {
            row = tableSorter.modelIndex(row);
        }
        return elements.get(row);
    }

    public TableSorter getTableSorter() {
        return tableSorter;
    }
    
    public void setTableSorter(TableSorter tableSorter) {
        this.tableSorter = tableSorter;
    }       
    
}