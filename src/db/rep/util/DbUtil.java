package db.rep.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A collection of JDBC helper methods. This class is thread safe.
 *
 * @author Decebal Suiu
 */
public final class DbUtil {

    /**
     * Close a <code>Connection</code>, avoid closing if null.
     */
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * Close a <code>ResultSet</code>, avoid closing if null.
     */
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * Close a <code>Statement</code>, avoid closing if null.
     */
    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * Close a <code>Connection</code>, avoid closing if null and hide
     * any SQLExceptions that occur.
     */
    public static void closeQuietly(Connection conn) {
        try {
            close(conn);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Close a <code>Connection</code>, <code>Statement</code> and
     * <code>ResultSet</code>.  Avoid closing if null and hide any
     * SQLExceptions that occur.
     */
    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        try {
            closeQuietly(rs);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }
    }

    /**
     * Close a <code>Connection</code>, <code>Statement</code>
     * Avoid closing if null and hide any SQLExceptions that occur.
     */
    public static void closeQuietly(Connection conn, Statement stmt) {
        try {
            closeQuietly(stmt);
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Close a <code>ResultSet</code>, avoid closing if null and hide any
     * SQLExceptions that occur.
     */
    public static void closeQuietly(ResultSet rs) {
        try {
            close(rs);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Close a <code>Statement</code>, avoid closing if null and hide
     * any SQLExceptions that occur.
     */
    public static void closeQuietly(Statement stmt) {
        try {
            close(stmt);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Commits a <code>Connection</code> then closes it, avoid closing if null.
     */
    public static void commitAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.commit();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * Commits a <code>Connection</code> then closes it, avoid closing if null
     * and hide any SQLExceptions that occur.
     */
    public static void commitAndCloseQuietly(Connection conn) {
        try {
            commitAndClose(conn);
        } catch (SQLException e) {
            // quiet
        }
    }

    /**
     * Loads and registers a database driver class.
     * If this succeeds, it returns true, else it returns false.
     */
    public static boolean loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName).newInstance();
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IllegalAccessException e) {
            // Constructor is private, OK for DriverManager contract
            return true;
        } catch (InstantiationException e) {
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    public static void printStackTrace(SQLException e) {
        printStackTrace(e, new PrintWriter(System.err));
    }

    public static void printStackTrace(SQLException e, PrintWriter pw) {
        SQLException next = e;
        while (next != null) {
            next.printStackTrace(pw);
            next = next.getNextException();
            if (next != null) {
                pw.println("Next SQLException:");
            }
        }
    }

    public static void printWarnings(Connection conn) {
        printWarnings(conn, new PrintWriter(System.err));
    }

    public static void printWarnings(Connection conn, PrintWriter pw) {
        if (conn != null) {
            try {
                printStackTrace(conn.getWarnings(), pw);
            } catch (SQLException e) {
                printStackTrace(e, pw);
            }
        }
    }

    /**
     * Rollback any changes made on the given connection.
     * @param conn The database Connection to rollback.  A null value is legal.
     * @throws SQLException
     */
    public static void rollback(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    /**
     * Performs a rollback on the <code>Connection</code> then closes it,
     * avoid closing if null.
     */
    public static void rollbackAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * Performs a rollback on the <code>Connection</code> then closes it,
     * avoid closing if null and hide any SQLExceptions that occur.
     */
    public static void rollbackAndCloseQuietly(Connection conn) {
        try {
            rollbackAndClose(conn);
        } catch (SQLException e) {
            // quiet
        }
    }

}
