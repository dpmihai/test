package test;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Image;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: mihai.panaitescu
 * Date: 09-Dec-2009
 * Time: 13:48:10
 */
public class PdfWithFlashTest {

    public static void main(String arg[]) {

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("tablePDF.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            Font font = FontFactory.getFont(FontFactory.TIMES, 10);
            PdfPCell cell = new PdfPCell(new Phrase("X", font));
            cell.setMinimumHeight(12);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(3);
//            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Y", font));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("T", font));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Movie", font));
            cell.setRotation(90);
            table.addCell(cell);

            VideoCellEvent event = new VideoCellEvent();
            cell = new PdfPCell(new Phrase("Play", font));
            cell.setColspan(2);
            cell.setCellEvent(event);
            cell.setMinimumHeight(200);
            table.addCell(cell);

            document.add(table);
            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // adding flash swf object to pdf
    static class VideoCellEvent implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {

            PdfContentByte cb = canvas[PdfPTable.TEXTCANVAS];
            PdfFileSpecification fs;
            try {
                fs = PdfFileSpecification.fileEmbedded(
                        cb.getPdfWriter(),
                        "D:\\Public\\next-reports\\tutorial\\datasource.swf", "datasource.swf", null
                );

                PdfAnnotation videoAnnotation =
                        PdfAnnotation.createScreen(
                                cb.getPdfWriter(),
                                rect,
                                "TEST Annotation", fs,
                                "application/x-shockwave-flash", true);

                Image image = Image.getInstance("D:\\Public\\next-reports\\src\\images\\splash.png");
                image.setAbsolutePosition(0, 0);
                PdfAppearance app = cb.getPdfWriter().getDirectContent().
                        createAppearance(image.getWidth(), image.getHeight());
                app.addImage(image);

                videoAnnotation.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, app);
                videoAnnotation.setAppearance(PdfAnnotation.APPEARANCE_DOWN, app);
                videoAnnotation.setAppearance(PdfAnnotation.APPEARANCE_ROLLOVER, app);


                videoAnnotation.setFlags(PdfAnnotation.FLAGS_PRINT);

                cb.getPdfWriter().addAnnotation(videoAnnotation);


            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }
}
