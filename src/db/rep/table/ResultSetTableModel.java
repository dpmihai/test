package db.rep.table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/** 
 * @author Decebal Suiu
 */
public class ResultSetTableModel extends BaseAbstractTableModel {
    
    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;

    private int numberOfRows;

    // keep track of database connection status
    private boolean connectedToDatabase = false;

    /**
     * Initialize resultSet and obtain its meta data object;
     * determine number of rows.
     */ 
    public ResultSetTableModel(Connection conn, String query) throws SQLException {
        super(new ArrayList());
        
        this.conn = conn;

        // create Statement to query database
        statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);

        // update database connection status
        connectedToDatabase = true;

        // set query and execute it
        setQuery(query);
    }

    public Class getColumnClass(int col) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        
        // determine Java class of column
        try {
            String className = metaData.getColumnClassName(col + 1);
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Object.class;
    }

    /**
     * Get number of columns in ResultSet.
     */
    public int getColumnCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        
        // determine number of columns
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get name of a particular column in ResultSet. 
     */
    public String getColumnName(int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine column name
        try {
            return metaData.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Return number of rows in ResultSet.
     */ 
    public int getRowCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        return numberOfRows;
    }

    /**
     * Obtain value in particular row and column.
     */ 
    public Object getValueAt(int row, int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }


        // obtain a value at specified ResultSet row and column
        try {
            resultSet.absolute(row + 1);            
            return resultSet.getObject(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Set new database query string.
     */ 
    public void setQuery(String query) throws SQLException, IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // specify query and execute it
        resultSet = statement.executeQuery(query);

        // obtain meta data for ResultSet
        metaData = resultSet.getMetaData();

        // determine number of rows in ResultSet
        resultSet.last(); // move to last row
        numberOfRows = resultSet.getRow(); // get row number

        // notify JTable that model has changed
        fireTableStructureChanged();
    }

    /**
     * Close Statement and Connection.
     */ 
    public void disconnectFromDatabase() {
        try {
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // update database connection status            
            connectedToDatabase = false;
        }
    }

    protected void initializeColumnNames() {
        columnNames = new ArrayList();
    }
    
}