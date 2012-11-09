package dbQuery;

/*
 * (c) 2004, Slav Boleslawski
 *
 * Released under terms of the Artistic Licence
 * http://www.opensource.org/licences/artistic-licence.php
 */


import java.sql.*;
import java.util.*;

/**
 * This is a test class that illustrates the use of 
 * <code>DatabaseQuerier.getRecords()</code> method.
 */

public class QuerierTest {
	//Provide your values for all static variables below

    private static final String ODBC_NAME =
            "Driver={Microsoft Access Driver (*.mdb)};" +
            "DBQ=C:\\Documents and Settings\\mihai.panaitescu\\My Documents\\test.mdb;DriverID=22;READONLY=true}";
	private static final String DB_URL = "jdbc:jtds:sqlserver://191.191.21.16:1433/";
	private static final String DB_CATALOG = "testCatalog";
	private static final String DB_USERNAME = "";
	private static final String DB_PASSWORD = "";
	private static final String SQL_QUERY = "SELECT * FROM ACCOUNT_WEB_SEARCH_V";

	private DatabaseQuerier querier = null;

	public QuerierTest() {
		Connection conn = null;
		try {
			//conn = openJdbcConnection(DB_URL, DB_CATALOG, DB_USERNAME, DB_PASSWORD);
			//conn = openOdbcConnection(ODBC_NAME, DB_USERNAME, DB_PASSWORD);
            conn = openOracleConnection();
            querier = new SingleThreadedDatabaseQuerier(conn);
			//querier = new MultiThreadedDatabaseQuerier(conn);
			
			Thread[] threads = new Thread[1];
			for(int i = 0; i < threads.length; i++) {
				threads[i] = new Thread() {
					public void run() {
						String threadName = Thread.currentThread().getName();
						try {
                            List recs = querier.getRecords(SQL_QUERY);
                            System.out.println("recs.size="+recs.size());
                            System.out.println("Thread " + threadName + " completed");
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("\tInterruptedException in thread " +
								threadName);
						}
					}
				};
				threads[i].setName(String.valueOf(i));
			}
			System.out.println("Threads created");
			
			for(int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			System.out.println("Threads started");


            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            System.out.println("Call interrupt ...");

            threads[0].interrupt();
            System.out.println("Thread interrupted");
			
			for(int i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {}
			}
			System.out.println("Threads terminated");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (querier != null) {
				querier.close();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}

    public static Connection openOracleConnection() throws SQLException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(java.lang.ClassNotFoundException e) {
			throw new SQLException("Cannot load database driver");
		}
		String url = "jdbc:oracle:thin:@172.31.0.12:1521:KSI";
		return DriverManager.getConnection(url, "kollecto", "ksi");
	}

    public static Connection openOdbcConnection(String dataSourceName, String username, 
    		String password) throws SQLException {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch(java.lang.ClassNotFoundException e) {
			throw new SQLException("Cannot load database driver");
		}
		String url = "jdbc:odbc:" + dataSourceName;
		return DriverManager.getConnection(url, username, password);
	}
	
	public static Connection openJdbcConnection(String url, String catalog,
			String username, String password) throws SQLException {
		String className = "net.sourceforge.jtds.jdbc.Driver";
		try {
			java.sql.Driver driver = (java.sql.Driver)Class.forName(
				className).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}

		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);
		props.put("SendStringParametersAsUnicode", "false");
		return DriverManager.getConnection(url + catalog,  props);
	}


	public static void main(String[] args) {
		new QuerierTest();
	}

}
