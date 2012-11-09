package db;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 10:59:19 AM
 */

import java.sql.*;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

public class DBViewer {

    public static final int INFO = 1;
    public static final int TABLES = 2;
    public static final int INDEXES = 4;
    public static final int SUPPORTED_KEYWORDS = 8;

    private String info = "";
    private List tables = new ArrayList();
    private List keywords = new ArrayList();

    public DBViewer(Connection con, int mask) {
        view(con, mask);
    }

    private void view(Connection con, int mask) {

        try {
            DatabaseMetaData dbmd = con.getMetaData();

            if ((mask & INFO) == INFO) {
                StringBuffer sb = new StringBuffer();
                sb.append("Database Product: ").append(dbmd.getDatabaseProductName()).append("\r\n");
                sb.append("Database Product Version: ").append(dbmd.getDatabaseProductVersion()).append("\r\n");
                sb.append("Driver Name: ").append(dbmd.getDriverName()).append("\r\n");
                sb.append("Driver Version: ").append(dbmd.getDriverVersion()).append("\r\n");
                info = sb.toString();
            }

            if ((mask & SUPPORTED_KEYWORDS) == SUPPORTED_KEYWORDS) {
                StringTokenizer st = new StringTokenizer(dbmd.getSQLKeywords(), ",");
                while (st.hasMoreTokens()) {
                    keywords.add(st.nextToken());
                }
            }

            // Get a ResultSet that contains all of the tables in this database
            // We specify a table_type of "TABLE" to prevent seeing system tables,
            // views and so forth
            if ((mask & TABLES) == TABLES) {
                String[] tableTypes = {"TABLE", "VIEW"};
                ResultSet allTables = dbmd.getTables(null, null, null, tableTypes);
                while (allTables.next()) {
                    String table_name = allTables.getString("TABLE_NAME");
                    String table_type = allTables.getString("TABLE_TYPE");

                    if ((mask & INDEXES) == INDEXES) {
                        ResultSet indexList = null;
                        try {
                            // Get a list of all the indexes for this table
                            indexList = dbmd.getIndexInfo(null, null, table_name, false, false);
                            List indexes = new ArrayList();
                            while (indexList.next()) {
                                String index_name = indexList.getString("INDEX_NAME");
                                String column_name = indexList.getString("COLUMN_NAME");
                                if (!index_name.equals("null")) {
                                    Index index = new Index(index_name, column_name);
                                    indexes.add(index);
                                }
                            }
                            Table table = new Table(table_name, table_type, indexes);
                            tables.add(table);

                        } catch (SQLException e) {
                            System.err.println("SQL Exception: " + e.getMessage());
                        } finally {
                            if (indexList != null) {
                                try {
                                    indexList.close();
                                } catch (SQLException e) {
                                    System.err.println("SQL Exception: " + e.getMessage());
                                }
                            }
                        }

                    } else {
                        Table table = new Table(table_name, table_type);
                        tables.add(table);
                    }
                }

                allTables.close();
            }            
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public String getInfo() {
        return info;
    }

    public List getTables() {
        return tables;
    }

    public List getKeywords() {
        return keywords;
    }
}
