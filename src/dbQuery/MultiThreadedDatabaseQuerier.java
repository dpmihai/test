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
 * This class implements a multithreaded version of <code>DatabaseQuerier</code>.
 * The user can change the <code>EXECUTOR_COUNT</code> value for optimal concurrent performance.
 * The class enables concurrent execution of the <code>getRecords()</code> method
 * through implementation of a simple pool of executors.
 * It limits the number of threads executing getRecords() concurrently through
 * a semaphore object.
 */
 
public class MultiThreadedDatabaseQuerier implements DatabaseQuerier {
	private static final int EXECUTOR_COUNT = 2;
	private Connection conn;
	private LinkedList queryExecutors;
	private Semaphore semaphore;
	
	
	private class Semaphore {
		private int count;
		
		public Semaphore(int n) {
			count = n;
		}
		
		public synchronized void acquire() throws InterruptedException {
			while(count == 0) {
				wait();
			}
			count--;
		}
		
		public synchronized void release() {
			count++;
			notify();
		}
	}
		
	
	public MultiThreadedDatabaseQuerier(Connection conn) throws SQLException {
		this.conn = conn;
		queryExecutors = new LinkedList();
		for(int i = 0; i < EXECUTOR_COUNT; i++) {
			queryExecutors.add(new QueryExecutor());
		}
		int maxStatements = conn.getMetaData().getMaxStatements();
		if (maxStatements == 0) {
			maxStatements = Integer.MAX_VALUE;
		}
		semaphore = new Semaphore(maxStatements);
	}
	
	public List getRecords(String query) throws SQLException, InterruptedException {
		Statement statement = null;
		ResultSet rs = null;
		QueryExecutor queryExecutor = null;
		try {
			semaphore.acquire();
			
			statement = conn.createStatement();
			queryExecutor = getQueryExecutor();
			rs = queryExecutor.executeQuery(statement, query);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
					
			List results = new ArrayList();
			while(rs.next()) {
				if (Thread.currentThread().interrupted()) {
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

			if (queryExecutor != null) {
				releaseQueryExecutor(queryExecutor);
			}
			semaphore.release();
		}
	}
	
	public void close() {
		synchronized(queryExecutors) {
			for(Iterator iter = queryExecutors.iterator(); iter.hasNext();) {
				((QueryExecutor)iter.next()).close();
			}
		}
	}
	
	private QueryExecutor getQueryExecutor() throws InterruptedException {
		synchronized(queryExecutors) {
			while(queryExecutors.size() == 0) {
				queryExecutors.wait();
			}
			return (QueryExecutor)queryExecutors.removeFirst();
		}
	}
	
	private void releaseQueryExecutor(QueryExecutor executor) {
		synchronized(queryExecutors) {
			queryExecutors.add(executor);
			queryExecutors.notify();
		}
	}
}
