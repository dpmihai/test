package stax;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 28, 2007
 * Time: 10:47:15 AM
 */
public class FontAttributesParser implements ComponentParser {


    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {

        StaxReaderHelper readerHelper = new StaxReaderHelper(staxXmlReader);

//        while (true) {
//            boolean found = readerHelper.moveToElement("attributes");
//            if (!found) {
//                break;
//            }

            while (true) {
                boolean exist = readerHelper.moveToElement("entry");
                if (!exist) {
                    break;
                }
                readerHelper.moveToElement("awt-text-attribute");
                System.out.println("attr=" + staxXmlReader.getElementText());
            }

            System.out.println("-------------------");
            System.out.println("attr");
        //}


    }


}
