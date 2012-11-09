package dbQuery;

/*
 * (c) 2004, Slav Boleslawski
 *
 * Released under terms of the Artistic Licence
 * http://www.opensource.org/licences/artistic-licence.php
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a singlethreaded version of <code>DatabaseQuerier</code> by
 * synchronizing access to the <code>getRecords()</code> method.
 */

public class SingleThreadedDatabaseQuerier implements DatabaseQuerier {
	private Connection conn;
	private QueryExecutor queryExecutor;	
	
	public SingleThreadedDatabaseQuerier(Connection conn) throws SQLException {
		this.conn = conn;
		queryExecutor = new QueryExecutor();
	}
	
	public synchronized List getRecords(String query) throws SQLException, InterruptedException {
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = conn.createStatement();
			rs = queryExecutor.executeQuery(statement, query);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

            System.out.println(">>>HERE");
            List results = new ArrayList();
			while(rs.next()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				
				List row = new ArrayList(columnCount);
				for(int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				results.add(row);
			}
			return results;
		} finally {
			statement.cancel();
			if (rs != null)
				rs.close();
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	public void close() {
		queryExecutor.close();
	}
}
