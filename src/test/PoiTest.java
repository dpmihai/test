package test;

import org.apache.poi.hpsf.UnsupportedVariantTypeException;
import org.apache.poi.hpsf.MutablePropertySet;
import org.apache.poi.hpsf.MutableProperty;
import org.apache.poi.hpsf.Variant;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.MutableSection;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.apache.poi.hpsf.wellknown.PropertyIDMap;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import junit.framework.Assert;

/**
 * User: mihai.panaitescu
 * Date: 26-Nov-2009
 * Time: 12:09:56
 */
public class PoiTest {

    static final String POI_FS = "E:\\TestHPSFWritingFunctionality.xls";

    @Test
    public void testWriteSimplePropertySet()
            throws IOException, UnsupportedVariantTypeException
        {
            final String AUTHOR = "Rainer Klute";
            final String TITLE = "Test Document";

            final File filename = new File(POI_FS);
            filename.deleteOnExit();
            final OutputStream out = new FileOutputStream(filename);
            final POIFSFileSystem poiFs = new POIFSFileSystem();

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet xlsSheet = wb.createSheet("Page 1");
            wb.write(out);

            final MutablePropertySet ps = new MutablePropertySet();
            final MutableSection si = new MutableSection();
            si.setFormatID(SectionIDMap.SUMMARY_INFORMATION_ID);
            ps.getSections().set(0, si);

            final MutableProperty p = new MutableProperty();
            p.setID(PropertyIDMap.PID_AUTHOR);
            p.setType(Variant.VT_LPWSTR);
            p.setValue(AUTHOR);
            si.setProperty(p);
            si.setProperty(PropertyIDMap.PID_TITLE, Variant.VT_LPSTR, TITLE);

            poiFs.createDocument(ps.toInputStream(),
                                 SummaryInformation.DEFAULT_STREAM_NAME);
            poiFs.writeFilesystem(out);
            out.close();

            /* Read the POIFS: */
            final PropertySet[] psa = new PropertySet[1];
            final POIFSReader r = new POIFSReader();
            r.registerListener(new POIFSReaderListener()
                {
                    public void processPOIFSReaderEvent
                        (final POIFSReaderEvent event)
                    {
                        try
                        {
                            psa[0] = PropertySetFactory.create(event.getStream());
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            //fail(org.apache.poi.hpsf.Util.toString(ex));
                        }
                    }

                },
                SummaryInformation.DEFAULT_STREAM_NAME);
            r.read(new FileInputStream(filename));
            Assert.assertNotNull(psa[0]);
            Assert.assertTrue(psa[0].isSummaryInformation());

            final Section s = (Section) (psa[0].getSections().get(0));
            Object p1 = s.getProperty(PropertyIDMap.PID_AUTHOR);
            Object p2 = s.getProperty(PropertyIDMap.PID_TITLE);
            Assert.assertEquals(AUTHOR, p1);
            Assert.assertEquals(TITLE, p2);
        }

}
