/*
 *	Copyright 2005 stat4j.org
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package stax;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Class: XMLExamples Description:
 * 
 * 
 * @author ldabreo
 *  
 */
public class XMLExamples {

  public static void staxExample(File file) throws Exception {

    FileInputStream fis = new FileInputStream(file);

    XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
    XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(fis);

    for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader.next()) {
      if (event == XMLStreamConstants.START_ELEMENT) {
        String element = staxXmlReader.getLocalName();
        // do something with element
      }
    }

  }

  public static void staxLoopExample(File file) throws Exception {

    FileInputStream fis = new FileInputStream(file);

    XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
    XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(fis);

    for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader.next()) {
      switch (event) {
      case XMLStreamConstants.START_DOCUMENT:
        System.out.println("Start document " + staxXmlReader.getLocalName());
        break;
      case XMLStreamConstants.START_ELEMENT:
        System.out.println("Start element " + staxXmlReader.getLocalName());
      	System.out.println("Element text " + staxXmlReader.getElementText());
        break;
      case XMLStreamConstants.END_ELEMENT:
        System.out.println("End element " + staxXmlReader.getLocalName());
        break;
      default:
        break;
      }
    }
  }

  public static void saxExample(File file) throws Exception {

    FileInputStream fis = new FileInputStream(file);

    XMLReader saxXmlReader = XMLReaderFactory.createXMLReader();

    // Create callback handler
    DefaultHandler handler = new DefaultHandler() {
      public void startElement(String uri, String localName, String qName, Attributes attributes) {
        // do something with element
      }
    };

    // register handler
    saxXmlReader.setContentHandler(handler);
    saxXmlReader.setErrorHandler(handler);

    // control passed to parser...
    saxXmlReader.parse(new InputSource(fis));

  }

}