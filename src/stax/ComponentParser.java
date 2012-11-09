package stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public interface ComponentParser {

    /**
     * Parse some XML data using the supplied stax reader.
     *
     * @param staxXmlReader STAX reader.
     * @throws XMLStreamException
     */
    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException;

}
