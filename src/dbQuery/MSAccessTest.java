package dbQuery;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import craftsman.spy.SpyDriver;

public class MSAccessTest {
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.setProperty("spy.driver", "net.ucanaccess.jdbc.UcanaccessDriver");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			DriverManager.deregisterDriver(drivers.nextElement());
		}
		DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver());
        DriverManager.registerDriver(new SpyDriver());
        System.setProperty("spy.log", "D:/jdbc-spy.log");
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://D:/Temp/access/Test.mdb;showschema=true");
        System.out.println("**** Connected");
        
	}    

}
