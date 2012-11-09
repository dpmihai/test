package bnrexchange.stax;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxParser implements ComponentParser {

    private Map<String, ComponentParser> delegates;

    public StaxParser() {
        delegates = new HashMap<String, ComponentParser>();
    }

    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {

        for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                String element = staxXmlReader.getLocalName();
                // If a Component Parser is registered that can handle
                // this element delegate to parser...
                if (delegates.containsKey(element)) {
                    ComponentParser parser = (ComponentParser) delegates.get(element);
                    parser.parse(staxXmlReader);
                }
            }
        }
    }

    public void registerParser(String name, ComponentParser parser) {
        delegates.put(name, parser);
    }

}