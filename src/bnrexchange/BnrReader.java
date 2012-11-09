package bnrexchange;

import java.io.InputStream;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import bnrexchange.stax.CurrencyParser;
import bnrexchange.stax.StaxParser;

public class BnrReader {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); 
	private static final String LINK = new Config().getWebUrl();
	private List<Currency> currencies = new ArrayList<Currency>();
	public static BnrReader INSTANCE = new BnrReader();
	// if empty all are needed (read)
	public static List<String> neededCurrencies = Arrays.asList(new String[] {"EUR", "USD", "CAD", "GBP"});	
	
	private BnrReader() {		
	}
	
	private InputStream getInputStream() throws Exception {
		
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyPort", "128");
		System.getProperties().put("proxyHost", "192.168.16.7");
		
		URL url = new URL(LINK);
		return url.openStream();
	}
	
	private void readCurrencies(InputStream is) throws Exception {				
		
		XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
        XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(is);

        StaxParser parser = new StaxParser();
        parser.registerParser("Cube", new CurrencyParser());	
        
        parser.parse(staxXmlReader);                
	}

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}
	
	public static void main(String[] args) {
		try {						
			InputStream is = INSTANCE.getInputStream();			
			INSTANCE.readCurrencies(is);			
			for (Currency c : INSTANCE.getCurrencies()) {
				System.out.println(c.format());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	

}
