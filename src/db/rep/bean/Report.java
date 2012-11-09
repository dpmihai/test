package db.rep.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import db.rep.event.ReportEvent;
import db.rep.listener.ReportListener;
import db.rep.util.Globals;

/**
 * @author Decebal Suiu
 */
public class Report {

    private HashMap dbTables;
    private List fields;
    private List listeners;

    public Report() {
        listeners = new ArrayList();
    }

    public HashMap getDBTables() {
        if (dbTables == null) {
            dbTables = new HashMap();

            try {
                Connection conn = Globals.getDbConnection();
                if (conn == null) {
                    return null;
                }
                DatabaseMetaData dmtd = conn.getMetaData();
                String[] types = {"TABLE", "VIEW"};
                ResultSet rsTables = dmtd.getTables(null, null, null, types);

                while (rsTables.next()) {
                    String tableName = rsTables.getString("TABLE_NAME");
                    String tableType = rsTables.getString("TABLE_TYPE");
                    DBTable dbTable = new DBTable();
                    dbTable.name = tableName;
                    dbTable.type = tableType;

                    // add columns for current table
                    List columns = new ArrayList();
//                    ResultSet rsColumns = dmtd.getColumns(null, null, tableName, null);
//
//                    while (rsColumns.next()) {
//                        String columnName = rsColumns.getString("COLUMN_NAME");
//                        DBColumn dbColumn = new DBColumn();
//                        dbColumn.name = columnName;
//                        columns.add(dbColumn);
//                    }
//
//                    rsColumns.close();

                    dbTable.columns = columns;

                    dbTables.put(dbTable.name, dbTable);
                }

                rsTables.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dbTables;
    }

    public List getColumns(String tableName) {
        List result = new ArrayList();
        if (dbTables == null) {
            return result;
        }
        DBTable dbTable = (DBTable) dbTables.get(tableName);
        if ((dbTable.columns == null) || (dbTable.columns.size() == 0) )
        {
            try {
                Connection conn = Globals.getDbConnection();
                if (conn == null) {
                    return null;
                }
                DatabaseMetaData dmtd = conn.getMetaData();
                ResultSet rsColumns = dmtd.getColumns(null, null, tableName, null);
                while (rsColumns.next()) {
                    String columnName = rsColumns.getString("COLUMN_NAME");
                    DBColumn dbColumn = new DBColumn();
                    dbColumn.name = columnName;
                    dbTable.columns.add(dbColumn);
                }

                rsColumns.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbTable.columns;
    }

    public HashMap getSelectedColumns() {
        HashMap selectedColumns = new HashMap();

        Iterator it = getDBTables().values().iterator();
        while (it.hasNext()) {
            DBTable table = (DBTable) it.next();
            String tableName = table.name;
            List columns = table.columns;
            int n = columns.size();
            for (int i = 0; i < n; i++) {
                DBColumn column = (DBColumn) columns.get(i);
                if (column.select) {
                    if (!selectedColumns.containsKey(tableName)) {
                        selectedColumns.put(tableName, new ArrayList());
                    }
                    List tmp = (List) selectedColumns.get(tableName);
                    tmp.add(column);
                    selectedColumns.put(tableName, tmp);
                }
            }
        }

        return selectedColumns;
    }

    public List getSelectedColumns(String tableName) {
        return (List) getSelectedColumns().get(tableName);
    }

    public List getSelectedColumnNames(String tableName) {
        List selectedColumns = getSelectedColumns(tableName);
        if (selectedColumns == null) {
            return new ArrayList();
        }

        List selectedColumnNames = new ArrayList();

        Iterator it = selectedColumns.iterator();
        while (it.hasNext()) {
            selectedColumnNames.add(((DBColumn) it.next()).name);
        }

        return selectedColumnNames;
    }

    public HashMap getVisibleColumns() {
        HashMap visibleColumns = new HashMap();

        HashMap tables = getDBTables();
        Iterator it = tables.keySet().iterator();
        while (it.hasNext()) {
            String tableName = (String) it.next();
            List columns = (List) tables.get(tableName);
            int n = columns.size();
            for (int i = 0; i < n; i++) {
                DBColumn column = (DBColumn) columns.get(i);
                if (column.visible) {
                    if (!visibleColumns.containsKey(tableName)) {
                        visibleColumns.put(tableName, new ArrayList());
                    }
                    List tmp = (List) visibleColumns.get(tableName);
                    tmp.add(column);
                    visibleColumns.put(tableName, tmp);
                }
            }
        }

        return visibleColumns;
    }

    public List getFields() {
        if (fields == null) {
            fields = new ArrayList();
        }

        return fields;
    }

    public void addReportListener(ReportListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeReportListener(ReportListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void fireReportChanged(ReportEvent event) {
        for (int i = 0; i < listeners.size(); i++) {
            ((ReportListener) listeners.get(i)).reportChanged(event);
        }
    }
}
