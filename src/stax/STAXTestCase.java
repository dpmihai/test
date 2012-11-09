package stax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.stream.*;

import junit.framework.TestCase;


public class STAXTestCase extends TestCase {

    public STAXTestCase(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    public STAXTestCase() {
        super();
    }

    public void testReadAtomFeedFile() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("atom.xml");

        XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
        XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(in);

        StaxParser parser = new StaxParser();
        parser.registerParser("author", new AuthorParser());
        parser.registerParser("entry", new EntryParser());

        parser.parse(staxXmlReader);

    }


    public void testReadReportFile() throws Exception {
        //InputStream in = this.getClass().getResourceAsStream("atom.xml");
        InputStream in = this.getClass().getResourceAsStream("Java1_5.report");

        XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
        XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(in);

        StaxParser parser = new StaxParser();
        parser.registerParser("attributes", new FontAttributesParser());

        parser.parse(staxXmlReader);

    }

    public void test() throws Exception {

        InputStream in = this.getClass().getResourceAsStream("Java1_5.report");

        XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
        XMLStreamReader reader = (XMLStreamReader) factory.createXMLStreamReader(in);

        StaxParser parser = new StaxParser();
        String readElement = null;

        File file = new File("Java1_5_new.report");
        FileOutputStream out = new FileOutputStream(file);
        String now = new SimpleDateFormat().format(new Date(System.currentTimeMillis()));
        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter staxWriter = outFactory.createXMLStreamWriter(out);
        StaxWriterHelper writeHelper = new StaxWriterHelper(staxWriter);

        boolean hasOneAttribute = false;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            if ((event == XMLStreamConstants.START_ELEMENT) || (event == XMLStreamConstants.END_ELEMENT)) {
                String elementName = reader.getLocalName();
                int attrs = 0;
                if (event == XMLStreamConstants.START_ELEMENT) {
                    attrs = reader.getAttributeCount();
                }
                if (elementName.equals("attributes")) {
                    while (true) {
                        event = reader.next();
                        if (event == XMLStreamConstants.END_DOCUMENT) {
                            break;
                        }
                        if (event == XMLStreamConstants.END_ELEMENT) {
                            elementName = reader.getLocalName();
                            if (elementName.equals("attributes")) {
                                System.out.println("****end attributes");
                                break;
                            }
                        }
                    }
                } else {
                    if (event == XMLStreamConstants.START_ELEMENT) {
                        writeHelper.writeStartElement(elementName);
                        for (int i=0; i<attrs; i++) {
                            String value = reader.getAttributeValue(i);
                            String name = reader.getAttributeName(i).getLocalPart();
                            writeHelper.writeAttribute(name, value);
                        }
                        if (attrs <= 1) {
                            hasOneAttribute = true;
                        }
                    } else if (event == XMLStreamConstants.END_ELEMENT) {
                        System.out.println(">> end : " + hasOneAttribute + "  -> " + elementName);
                        if (hasOneAttribute) {
                            writeHelper.writeEndElement(false);
                        } else {
                            writeHelper.writeEndElement(true);
                        }
                        hasOneAttribute = false;
                    }
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                writeHelper.writeCharacters(reader.getText());
            } else {
                System.out.println("--> not used event= " + event);
            }
        }

        writeHelper.close();

    }

    public void testWriteAtomFeedFile() throws Exception {
        File file = new File("atomoutput.xml");
        FileOutputStream out = new FileOutputStream(file);
        String now = new SimpleDateFormat().format(new Date(System.currentTimeMillis()));
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter staxWriter = factory.createXMLStreamWriter(out);

        StaxWriterHelper writeHelper = new StaxWriterHelper(staxWriter);

        writeHelper.writeStartDocument("UTF-8", "1.0");
        // feed
        writeHelper.writeStartElement("feed");
        writeHelper.writeNamespace("", "http://www.w3.org/2005/Atom");

        // title
        writeHelper.writeElement("title", "Simple Atom Feed File");
        // subtitle
        writeHelper.writeElement("subtitle", "Using STAX to read feed files");
        // link
        writeHelper.writeStartElement("link");
        writeHelper.writeAttribute("href", "http://example.org/");
        writeHelper.writeEndElement();
        // updated
        writeHelper.writeElement("updated", now);
        // author
        writeHelper.writeStartElement("author");
        writeHelper.writeElement("name", "Feed Author");
        writeHelper.writeElement("email", "doofus@feed.com");
        writeHelper.writeEndElement();
        // entry
        writeHelper.writeStartElement("entry");
        writeHelper.writeElement("title", "STAX parsing is simple");
        writeHelper.writeStartElement("link");
        writeHelper.writeAttribute("href", "http://www.devx.com/");
        writeHelper.writeEndElement();
        writeHelper.writeElement("updated", now);
        writeHelper.writeElement("summary", "Lean how to use STAX");
        writeHelper.writeEndElement();

        writeHelper.writeEndElement(); // end feed
        writeHelper.writeEndDocument();
        writeHelper.close();
    }


}