package bnrexchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SqlWriter {
	
	public static void main(String[] args) {
		
		Connection con = null;
		try {
					  
		  SqlWriter writer = new SqlWriter();
			 
	      con = writer.createConnection();     	     
	      
	      Currency test = new Currency();
	      test.setDate(new Date());
	      test.setName("EUR");
	      test.setValue(4.1207);
	      
	      writer.insertCurrency(con, test);
	      
	    } catch (Exception e) {
	    	e.printStackTrace();	       	      
	    } finally {
	    	if (con != null) {
	    		try {
					con.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
	    	}
	    }
	    	  
	} 
	
	private Connection createConnection() throws Exception {
		Config config = new Config(); 
		Class.forName("com.mysql.jdbc.Driver"); 	       	       
	    return DriverManager.getConnection(config.getDatabaseUrl(), config.getDatabaseUser(), config.getDatabasePassword());  	    		    
	}
	
	private void insertCurrency(Connection con, Currency currency) throws Exception {
		
		String sql = "INSERT ron_values(code, value, exchange_date) VALUES(?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql);	      
		ps.setString(1, currency.getName());
		ps.setDouble(2, currency.getValue());
		ps.setDate(3, new java.sql.Date(currency.getDate().getTime()));
		   
		ps.executeUpdate();
		      				  		
	}
	
	private void insertCurrencies(Connection con, List<Currency> currencies) throws Exception {
		for (Currency currency : currencies) {
			insertCurrency(con, currency);
		}
	}
	

}
