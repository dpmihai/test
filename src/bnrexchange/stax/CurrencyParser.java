package bnrexchange.stax;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import bnrexchange.BnrReader;
import bnrexchange.Currency;

public class CurrencyParser implements ComponentParser {			

	@Override
	public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {
		
		StaxReader reader = new StaxReader(staxXmlReader);
                
        String dateString = staxXmlReader.getAttributeValue(0);
        Date date;
        try {
			date = BnrReader.sdf.parse(dateString);			
		} catch (ParseException e) {
			throw new XMLStreamException("Could not parse date : " + dateString);			
		}
		
		List<Currency> list = new ArrayList<Currency>();
		while(reader.moveToElement("Rate")) {			
			
			String code = staxXmlReader.getAttributeValue(0);
			
			if ((BnrReader.neededCurrencies.size() == 0) || BnrReader.neededCurrencies.contains(code)) {
			
				int multiplier = 1;
				if (staxXmlReader.getAttributeCount() > 1) {
					multiplier = Integer.parseInt(staxXmlReader.getAttributeValue(1));
				}
				double value = Double.parseDouble(staxXmlReader.getElementText());			
						
				Currency c = new Currency();
				c.setDate(date);
				c.setMultiplier(multiplier);
				c.setName(code);
				c.setValue(value);
				list.add(c);
			}
		}
		BnrReader.INSTANCE.setCurrencies(list);
				
	}
		
}
