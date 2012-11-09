package stax;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


public class StaxWriterHelper {

    private static final String NEW_LINE = "\r\n";
    //private static final String TAB = "\t";
    private static final String TAB = "  ";
    private int tabs = 0;

    private XMLStreamWriter writer;

    public StaxWriterHelper(XMLStreamWriter writer) {
        this.writer = writer;
    }

    public void writeElement(String elementName, String value) throws XMLStreamException {
        writer.writeCharacters(NEW_LINE);
        writeTabs();
        writer.writeStartElement(elementName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    public void writeStartElement(String elementName) throws XMLStreamException {
        writer.writeCharacters(NEW_LINE);
        writeTabs();
        writer.writeStartElement(elementName);
        tabs++;
    }

    public void writeEndElement() throws XMLStreamException {
        tabs--;
        writer.writeCharacters(NEW_LINE);
        writeTabs();
        writer.writeEndElement();
    }

    public void writeEndElement(boolean newLine) throws XMLStreamException {
        tabs--;
        if (newLine) {
            writer.writeCharacters(NEW_LINE);
            writeTabs();
        }
        writer.writeEndElement();        
    }

    private void writeTabs() throws XMLStreamException {
        for (int i = 1; i <= tabs; i++) {
            writer.writeCharacters(TAB);
        }
    }

    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        writeTabs();
        writer.writeStartDocument(encoding, version);
        tabs++;
    }

    public void writeEndDocument() throws XMLStreamException {
        tabs--;
        writeTabs();
        writer.writeEndDocument();
    }

    public void writeAttribute(String name, String value) throws XMLStreamException {
        writer.writeAttribute(name, value);
    }

    public void writeNamespace(String name, String value) throws XMLStreamException {
        writer.writeNamespace(name, value);
    }

    public void close() throws XMLStreamException {
        writer.flush();
        writer.close();
    }

    public void writeCharacters(String text) throws XMLStreamException {
        writer.writeCharacters(text);
    }

}

