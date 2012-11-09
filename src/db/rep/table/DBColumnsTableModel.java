package db.rep.table;

import java.util.Arrays;
import java.util.List;

import db.rep.bean.DBColumn;

/**
 * @author Decebal Suiu
 */
public class DBColumnsTableModel extends BaseAbstractTableModel {

    static final String[] heading = { "Name",
                                      "Alias",
                                      "Select" };
    
    public DBColumnsTableModel(List elements) {
        super(elements);
    }

    protected void initializeColumnNames() {
        columnNames = Arrays.asList(heading);
    }

    public Object getValueAt(int row, int col) {
        DBColumn dbColumn = (DBColumn) elements.get(row);

        switch (col) {
            case 0:
                return dbColumn.name;
            case 1:
                return dbColumn.alias;
            case 2:
                return new Boolean(dbColumn.select);
        }

        return "";
    }
    
    public void setValueAt(Object value, int row, int col) {
        switch (col) {
        	case 1: {
        	    DBColumn dbColumn = (DBColumn) elements.get(row);
        	    dbColumn.alias = (String) value;
        	    return;
        	}
            case 2: {
                DBColumn dbColumn = (DBColumn) elements.get(row); 
                dbColumn.select = ((Boolean) value).booleanValue();                
                return;
            }
        }
    }
    
    public boolean isCellEditable(int row, int col) {
        switch (col) {
        	case 0:
        	    return false;
        	case 1:
        	    return true;
        	case 2:
        	    return true;
        }
        
        return false;
    }
    
}
