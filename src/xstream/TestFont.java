package xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.awt.*;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 29, 2007
 * Time: 9:27:38 AM
 */
public class TestFont {

    public static void main(String[] args) {

        String version = System.getProperty("java.version");
        System.out.println("java=" + version);

        Font font = new Font("sansserif", Font.BOLD, 32);
        System.out.println("font =" +font);

        XStream xstream = new XStream(new DomDriver());
        Writer writer = new StringWriter();

        xstream.toXML(font, writer);
        String s = ((StringWriter)writer).getBuffer().toString();
        try {
            System.out.println(s);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font font2 = (Font)xstream.fromXML(s);
        System.out.println("font2=" + font2);
    }
}
