package csv_jdbc;

import java.sql.*;
import java.util.Properties;

// http://csvjdbc.sourceforge.net/
public class DemoDriver {
	public static void main(String[] args) {
		try {
			// load the driver into memory
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			
			Properties props = new java.util.Properties();
			props.put("separator", " ");   

			// create a connection. The first command line parameter is assumed to
			// be the directory in which the .csv files are held
			String pathToCSV = "D:/Public/test/src/csv_jdbc";
			Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + pathToCSV, props);

//			// create a Statement object to execute the query with
//			Statement stmt = conn.createStatement();
//
//			// Select the ID and NAME columns from test.csv
//			ResultSet results = stmt.executeQuery("SELECT ID,NAME,EMAIL FROM test");
			
			PreparedStatement cstmt = conn.prepareStatement("SELECT COUNT(*) FROM test");
			ResultSet cresults = cstmt.executeQuery();
			cresults.next();
			int count = cresults.getInt(1);
			System.out.println("count="+count);
			
			PreparedStatement stmt = conn.prepareStatement("SELECT ID,NAME,EMAIL FROM test");
			ResultSet results = stmt.executeQuery();						

			// dump out the results
			while (results.next()) {
				System.out.println("ID= " + results.getString("ID") + 
						"   NAME= " + results.getString("NAME") + 
						"   EMAIL= " + results.getString("EMAIL"));
			}

			// clean up
			results.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}