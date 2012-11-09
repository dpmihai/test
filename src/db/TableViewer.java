package db;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 26, 2005 Time: 11:52:40 AM
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TableViewer {

  private List columnNames = new ArrayList();
  private List columnTypes = new ArrayList();
  private List data = new ArrayList();

  public TableViewer(Connection con, String table) {
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM "+ table);
      fetch(rs);
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
    }
  }

  public TableViewer(Connection con, String table, String[] columns) {
    try {
      Statement stmt = con.createStatement();
      StringBuffer sb = new StringBuffer();
      sb.append("SELECT ");
      for (int i=0, size=columns.length; i<size; i++) {
          sb.append(columns[i]);
          if (i < size-1) {
              sb.append(", ");
          }
      }
      sb.append(" FROM ").append(table);
      ResultSet rs = stmt.executeQuery(sb.toString());
      fetch(rs);
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
    }
  }

  private void fetch(ResultSet rs) throws SQLException {
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      for(int col = 1; col <= columnCount; col++) {
        columnNames.add(rsmd.getColumnLabel(col));
        columnTypes.add(rsmd.getColumnTypeName(col));
      }

      while(rs.next()) {
        List row = new ArrayList();
        for(int col = 1; col <= columnCount; col++) {
          row.add(rs.getString(col));
        }
          data.add(row);
      }
  }



  public List getColumnNames() {
      return columnNames;
  }

  public List getColumnTypes() {
      return columnTypes;
  }

  public List getData() {
      return data;
  }
}