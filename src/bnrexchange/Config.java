package bnrexchange;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class Config {
			
	private static Properties properties;
	
	public Config() {
		loadProperties();
	}
	
	private void loadProperties() {
		if (properties == null) {			
			try {
				properties = new Properties();
				InputStream is = this.getClass().getResourceAsStream("/rate.properties");						
		        properties.load(is);			        
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
	}	
	
	public String getDatabaseUrl() {
		return properties.getProperty("database.url");
	}

	public String getDatabaseUser() {
		return properties.getProperty("database.user");
	}
	
	public String getDatabasePassword() {
		return properties.getProperty("database.password");
	}
	
	public String getWebUrl() {		
		return properties.getProperty("web.url");
	}

}
