package bnrexchange.stax;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxReader {
	
	private XMLStreamReader reader;

    public StaxReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    public boolean moveToElement(String target) throws XMLStreamException {
        // If current element is equal to target        
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            if ((event == XMLStreamConstants.START_ELEMENT) && (reader.getLocalName().equals(target))) {
                return true;
            }
        }
        return false;
    }

}
