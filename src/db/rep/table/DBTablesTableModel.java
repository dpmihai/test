package db.rep.table;

import java.util.Arrays;
import java.util.List;

import db.rep.bean.DBTable;

/**
 * @author Decebal Suiu
 */
public class DBTablesTableModel extends BaseAbstractTableModel {

    static final String[] heading = { "Name",
                                      "Alias",
                                      "Type" };
    
    public DBTablesTableModel(List elements) {
        super(elements);
    }

    protected void initializeColumnNames() {
        columnNames = Arrays.asList(heading);
    }

    public Object getValueAt(int row, int col) {
        DBTable dbTable = (DBTable) elements.get(row);

        switch (col) {
            case 0:
                return dbTable.name;
            case 1:
                return dbTable.alias;
            case 2:
                return dbTable.type;
        }

        return "";
    }
    
    public void setValueAt(Object value, int row, int column) {
        if (column == 1) {
            DBTable dbTable = (DBTable) elements.get(row);
            dbTable.alias = (String) value;                        
        }
    }
    
    public boolean isCellEditable(int row, int col) {
        switch (col) {
        	case 0:
        	    return false;
        	case 1:
        	    return true;
        	case 2:
        	    return false;
        }
        
        return false;
    }
    
}
