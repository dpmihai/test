package test;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.rtf.RtfWriter2;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.EventListener;

/**
 * User: mihai.panaitescu
 * Date: 20-May-2010
 * Time: 12:06:34
 */
public class RtfMerger {

    public static void main(String[] args) {
         List<InputStream> list = new ArrayList<InputStream>();
        try {
            // Source pdfs
            list.add(new FileInputStream(new File("E:\\Public\\test\\1.rtf")));
            list.add(new FileInputStream(new File("E:\\Public\\test\\2.rtf")));

            // Resulting pdf
            OutputStream out = new FileOutputStream(new File("E:\\Public\\test\\merge.rtf"));

            doMerge(list, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Merge multiple pdf into one pdf
     *
     * @param list
     *            of pdf input stream
     * @param outputStream
     *            output file output stream
     * @throws com.lowagie.text.DocumentException
     * @throws IOException
     */
    public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        RtfWriter2 writer = RtfWriter2.getInstance(document, outputStream);
        document.open();

        int index = 0;
        for (InputStream in : list) {            
            writer.importRtfDocument(in, new EventListener[]{});
            if (index < list.size() - 1) {
                document.newPage();
            }
            index++;
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }
}
