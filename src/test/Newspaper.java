package test;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocumentException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: mihai.panaitescu
 * Date: 26-Mar-2010
 * Time: 11:54:33
 */
public class Newspaper {
    public static final String RESULTPATH = "D:\\Public\\next-reports\\reports\\";
    public static final String RESOURCESPATH = "D:\\Public\\next-reports\\reports\\";
    public static final String NEWSPAPER = RESOURCESPATH + "Example.pdf";

    public static final float LLX1 = 190;
    public static final float LLY1 = 41;
    public static final float URX1 = 320;
    public static final float URY1 = 328;
    public static final float W1 = URX1 - LLX1;
    public static final float H1 = URY1 - LLY1;

    public static final float LLX2 = 328;
    public static final float LLY2 = 41;
    public static final float URX2 = 734;
    public static final float URY2 = 611;
    public static final float W2 = URX2 - LLX2;
    public static final float H2 = URY2 - LLY2;

    public static final String MESSAGE = "Your ad could be here. Contact +32 555 12 34 for more information.";

    public static final String RESULT = RESULTPATH + "newspaper.pdf";

    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader(NEWSPAPER);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));

            PdfAnnotation annotation1 = PdfAnnotation.createText(
                    stamper.getWriter(), new Rectangle(LLX1, LLY1, URX1, URY1),
                    "Advertisement 1", MESSAGE, false, "Insert");
            PdfAppearance ap = stamper.getOverContent(1).createAppearance(W1, H1);
            ap.setRGBColorStroke(0xFF, 0x00, 0x00);
            ap.setLineWidth(3);
            ap.moveTo(0, 0);
            ap.lineTo(W1, H1);
            ap.moveTo(W1, 0);
            ap.lineTo(0, H1);
            ap.stroke();
            annotation1.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, ap);
            stamper.addAnnotation(annotation1, 1);

            PdfAnnotation annotation2 = PdfAnnotation.createText(
                    stamper.getWriter(), new Rectangle(LLX2, LLY2, URX2, URY2),
                    "Advertisement 2", MESSAGE, true, "Insert");
            annotation2.put(PdfName.C, new PdfArray(new float[]{0, 0, 1}));
            stamper.addAnnotation(annotation2, 1);


            stamper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    


}
