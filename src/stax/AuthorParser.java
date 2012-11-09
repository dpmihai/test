package stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class AuthorParser implements ComponentParser {


    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {

        StaxReaderHelper readerHelper = new StaxReaderHelper(staxXmlReader);
        // read name
        readerHelper.moveToElement("name");
        String name = staxXmlReader.getElementText();

        // read email
        readerHelper.moveToElement("email");
        String email = staxXmlReader.getElementText();

        // Do something with author data...
    }


}
