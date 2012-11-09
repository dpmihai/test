package test;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfImageTest {
	
	public static void main(String arg[]) {

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("tablePDF.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            
            byte[] imageBytes = getImage();
            Image pdfImage = Image.getInstance(imageBytes);           
            System.out.println(pdfImage + " : " + imageBytes.length );
            float factor = 72f * 50 / (getDPI() * 100);
            pdfImage.scaleAbsolute(259 * factor, 28 * factor);
            PdfPCell cell = new PdfPCell(pdfImage);
            cell.setColspan(3);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setUseDescender(true);
            cell.setMinimumHeight(12);              
            table.addCell(cell);

            Font font = FontFactory.getFont(FontFactory.TIMES, 10);
            cell = new PdfPCell(new Phrase("X", font));
            cell.setMinimumHeight(12);
            cell.setVerticalAlignment(PdfPCell.ALIGN_BASELINE);
            cell.setColspan(3);
//            cell.setRowspan(2);
            table.addCell(cell);

//            cell = new PdfPCell(new Phrase("Y", font));
//            table.addCell(cell);

            cell = new PdfPCell(new Phrase("T", font));
            cell.setColspan(3);
            table.addCell(cell);                       

            document.add(table);
            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public static byte[] getImage() throws IOException {        
        InputStream is = new FileInputStream("D:\\Public\\next-reports\\output\\Demo\\Reports\\logo.png");              
        ByteArrayOutputStream img_bytes = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = is.read()) != -1) {
                img_bytes.write(b);
            }
        } finally {
            is.close();
        }
        return img_bytes.toByteArray();
    }
	
	 protected static int getDPI() {
	        return GraphicsEnvironment.isHeadless() ? 96 : Toolkit.getDefaultToolkit().getScreenResolution();
	    }

}
