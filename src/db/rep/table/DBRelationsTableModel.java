package db.rep.table;

import java.util.Arrays;
import java.util.List;

import db.rep.bean.DBRelation;

/**
 * @author Decebal Suiu
 */
public class DBRelationsTableModel extends BaseAbstractTableModel {

    static final String[] heading = { "Object left",
                                      "Field left",
                                      "Object right",
                                      "Field right",                                      
                                      "Join" };
    
    public DBRelationsTableModel(List elements) {
        super(elements);
    }

    protected void initializeColumnNames() {
        columnNames = Arrays.asList(heading);
    }

    // Returns value of cell(row,col)
    public Object getValueAt(int row, int col) {
        DBRelation dbRelation = (DBRelation) elements.get(row);

        switch (col) {
            case 0:
                return dbRelation.objectLeft;
            case 1:
                return dbRelation.fieldLeft;
            case 2:
                return dbRelation.objectRight;
            case 3:
                return dbRelation.fieldRight;
            case 4:
                return dbRelation.join;
        }

        return "";
    }
    
}
