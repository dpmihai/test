package xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.asf.reporter.Report;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 13, 2006
 * Time: 4:48:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        XStream xstream = new XStream(new DomDriver());
        FileInputStream fis = null;
        try {


//            Writer writer = new FileWriter("Person.xml");
//            Person p = new Person();
//            p.setName("Ion");
//            p.setAddress("BDIon");
//            xstream.toXML(p, writer);
//            writer.close();

            File f = new File(".");
            System.out.println("path="+f.getAbsolutePath());
            fis = new FileInputStream("WorkedHours.report");
            Report p = (Report)xstream.fromXML(fis);
            System.out.println(p);
        } catch (Exception e1) {
            e1.printStackTrace();

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
