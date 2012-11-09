package db.rep.table;

import java.util.Arrays;
import java.util.List;

import db.rep.bean.Field;

/**
 * @author Decebal Suiu
 */
public class FieldsTableModel extends BaseAbstractTableModel {

    static final String[] heading = { "Object",
                                      "Field or Expression",
                                      "Group By" };
    
    public FieldsTableModel(List elements) {
        super(elements);
    }

    protected void initializeColumnNames() {
        columnNames = Arrays.asList(heading);
    }

    public Object getValueAt(int row, int col) {
        Field dbTable = (Field) elements.get(row);

        switch (col) {
            case 0:
                return dbTable.object;
            case 1:
                return dbTable.fieldOrExpression;
            case 2:
                return new Boolean(dbTable.groupBy);                
        }

        return "";
    }
    
    public void setValueAt(Object value, int row, int col) {
        Field field = (Field) elements.get(row);
        
        switch (col) {
        	case 0:
        	    field.object = (String) value;
        	    break;
        	case 1:
        	    field.fieldOrExpression = (String) value;
        	    break;
        	case 2:
        	    field.groupBy = ((Boolean) value).booleanValue();
        	    break;
        	    
        }
    }
    
    public boolean isCellEditable(int row, int col) {
        return true;
    }

}
