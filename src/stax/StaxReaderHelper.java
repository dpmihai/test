package stax;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 3, 2006
 * Time: 2:32:55 PM
 */
public class StaxReaderHelper {

    private XMLStreamReader reader;

    public StaxReaderHelper(XMLStreamReader reader) {
        this.reader = reader;
    }

    public boolean moveToElement(String target) throws XMLStreamException {
        // If current element is equal to target
        String readElement = null;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            if ((event == XMLStreamConstants.START_ELEMENT) && (reader.getLocalName().equals(target))) {
                return true;
            }
        }
        return false;
    }
}
