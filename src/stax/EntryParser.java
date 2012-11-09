package stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class EntryParser implements ComponentParser {

    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {

        StaxReaderHelper readerHelper = new StaxReaderHelper(staxXmlReader);
        // read title
        readerHelper.moveToElement("title");
        String title = staxXmlReader.getElementText();

        // read link attributes
        readerHelper.moveToElement("link");
        // read href attribute
        String linkHref = staxXmlReader.getAttributeValue(0);

        // read updated
        readerHelper.moveToElement("updated");
        String updated = staxXmlReader.getElementText();

        // read title
        readerHelper.moveToElement("summary");
        String summary = staxXmlReader.getElementText();

        // Do something with the data read from STAX..
    }


}
