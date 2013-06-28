package new1_7;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadUrl {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		System.setProperty("http.proxyHost", "192.168.16.7");
		System.setProperty("http.proxyPort", "128");
				
		try (InputStream in = URI.create("http://ste.hwg.cz/values.xml").toURL().openStream()) {
	        Files.copy(in, Paths.get("D:/values.xml"));
	    }
	}

}
