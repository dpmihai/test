package test;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfCellWithTableTest {

	public static void main(String[] args) {

		try {
			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document,	new FileOutputStream("tableInCellPDF.pdf"));
			document.open();
			PdfPTable table = new PdfPTable(3);
			//table.setWidthPercentage(100);
			table.setWidths(new float[] {200, 1000, 200});
			
			PdfPCell cell;
			
			Font font = FontFactory.getFont(FontFactory.TIMES, 10);
			cell = new PdfPCell(new Phrase("X", font));
			cell.setMinimumHeight(12);
			cell.setVerticalAlignment(PdfPCell.ALIGN_BASELINE);
			cell.setColspan(3);
			table.addCell(cell);
			

			cell = new PdfPCell(new Phrase("A", font));			
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("B", font));			
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("C", font));			
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("D", font));			
			table.addCell(cell);
			
			PdfPTable innerTable = new PdfPTable(4);
			cell = new PdfPCell(new Phrase("E1", font));				
			innerTable.addCell(cell);
			cell = new PdfPCell(new Phrase("E2", font));			
			innerTable.addCell(cell);
			cell = new PdfPCell(new Phrase("E3", font));			
			innerTable.addCell(cell);
			cell = new PdfPCell(new Phrase("E4", font));			
			innerTable.addCell(cell);			
			cell = new PdfPCell(new Phrase("E5", font));
			cell.setColspan(4);
			innerTable.addCell(cell);
			cell = new PdfPCell(new Phrase("E6", font));
			cell.setColspan(4);
			innerTable.addCell(cell);
			innerTable.setWidths(new float[]{100, 200, 100, 100});
			cell = new PdfPCell(innerTable);		
			cell.setPadding(0);
			cell.setColspan(2);
			table.addCell(cell);
						

			document.add(table);
			document.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
