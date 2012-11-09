package rtf;


import com.lowagie.text.*;
import com.lowagie.text.List;
import com.lowagie.text.Rectangle;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.table.RtfCell;
import com.lowagie.text.rtf.table.RtfBorder;
import com.lowagie.text.rtf.table.RtfBorderGroup;
import com.lowagie.text.rtf.table.RtfTable;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.field.RtfTableOfContents;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooterGroup;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;

import java.io.FileOutputStream;
import java.awt.*;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: 23-Feb-2009
 * Time: 17:14:00
 */
public class RtfText {

    /**
     * Creates 2 RTF files with the same content, but using different Writers.
     *
     * @param args no arguments needed
     */

    public static void main(String[] args) throws Exception {
        System.out.println("Test Suite");

        Document doc = new Document();
        RtfWriter2 writer2 = RtfWriter2.getInstance(doc,
                new FileOutputStream("testNew.rtf"));
        RtfWriter2.getInstance(doc, new FileOutputStream("testOld.rtf"));

        writer2.setAutogenerateTOCEntries(true);

        // header
        Table headerTable = new Table(1);
        Cell pageNumberCell = new Cell();
        pageNumberCell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        pageNumberCell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        pageNumberCell.add(new Phrase("Page "));
        pageNumberCell.add(new RtfPageNumber());
        headerTable.addCell(pageNumberCell);
        HeaderFooter header = new RtfHeaderFooter(headerTable);

        //footer
        Table footerTable = new Table(1);
        Cell footerCell = new Cell();
        footerCell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        footerCell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        footerCell.add(new Phrase("- " + Calendar.getInstance().getTime() + " -"));
        footerCell.setBorderWidth(0);
        footerTable.addCell(footerCell);
        RtfHeaderFooterGroup footer = new RtfHeaderFooterGroup();
        RtfHeaderFooter hf = new RtfHeaderFooter(footerTable);
        footer.setHeaderFooter(hf, com.lowagie.text.rtf.headerfooter.RtfHeaderFooter.DISPLAY_ALL_PAGES);



//        RtfHeaderFooterGroup footer = new RtfHeaderFooterGroup();
//        footer
//                .setHeaderFooter(
//                        new RtfHeaderFooter(new Phrase(
//                                "This is the footer on the title page")),
//                        com.lowagie.text.rtf.headerfooter.RtfHeaderFooter.DISPLAY_FIRST_PAGE);
//        footer
//                .setHeaderFooter(
//                        new RtfHeaderFooter(new Phrase(
//                                "This is a left side page")),
//                        com.lowagie.text.rtf.headerfooter.RtfHeaderFooter.DISPLAY_LEFT_PAGES);
//        footer
//                .setHeaderFooter(
//                        new RtfHeaderFooter(new Phrase(
//                                "This is a right side page")),
//                        com.lowagie.text.rtf.headerfooter.RtfHeaderFooter.DISPLAY_RIGHT_PAGES);

        doc.setHeader(header);
        doc.setFooter(footer);

        doc.open();

        Paragraph p = new Paragraph();
        p.add(new RtfTableOfContents("UPDATE ME!"));
        doc.add(p);

        p = new Paragraph("", new RtfFont("Staccato222 BT"));
        p.add(new Chunk("Hello! "));
        p.add(new Chunk("How do you do?"));
        doc.add(p);
        p.setAlignment(Element.ALIGN_RIGHT);
        doc.add(p);

        Anchor a = new Anchor("http://www.uni-klu.ac.at");
        a.setReference("http://www.uni-klu.ac.at");
        doc.add(a);

//		Image img = Image.getInstance("pngnow.png");
//		doc.add(new Chunk(img, 0, 0));
//		doc.add(new Annotation("Mark", "This works!"));

        Chunk c = new Chunk("");
        c.setNewPage();
        doc.add(c);

        List subList = new List(true, 40);
        subList.add(new ListItem("Sub list 1"));
        subList.add(new ListItem("Sub list 2"));

        List list = new List(true, 20);
        list.add(new ListItem("Test line 1"));
        list
                .add(new ListItem(
                        "Test line 2 - This is a really long test line to test that linebreaks are working the way they are supposed to work so a really really long line of drivel is required"));
        list.add(subList);
        list.add(new ListItem("Test line 3 - \t\u20ac\t 60,-"));
        doc.add(list);

        list = new List(false, 20);
        list.add(new ListItem("Bullet"));
        list.add(new ListItem("Another one"));
        doc.add(list);

        doc.newPage();

        Chapter chapter = new Chapter(new Paragraph("This is a Chapter"), 1);
        chapter.add(new Paragraph(
                "This is some text that belongs to this chapter."));
        chapter.add(new Paragraph("A second paragraph. What a surprise."));

        Section section = chapter.addSection(new Paragraph(
                "This is a subsection"));
        section.add(new Paragraph("Text in the subsection."));

        doc.add(chapter);

        com.lowagie.text.rtf.field.RtfTOCEntry rtfTOC = new com.lowagie.text.rtf.field.RtfTOCEntry(
                "Table Test");
        doc.add(rtfTOC);

        int totalColumns = 3;
        float[] headerwidths = new float[totalColumns];
        Table table = new Table(totalColumns);
        float size = 100f / totalColumns;
        for (int i = 0; i < totalColumns; i++) {
            headerwidths[i] = size;
        }
        //table.setSpaceInsideCell(2);
        table.setAlignment(Element.ALIGN_LEFT);
        //table.setSpaceBetweenCells(2);
        table.setWidths(headerwidths);
        //table.setPadding(10);


        Cell emptyCell = new Cell("");

        Cell cellLeft = new Cell("Left Alignment");        
        cellLeft.setHorizontalAlignment(Element.ALIGN_LEFT);
        Cell cellCenter = new Cell("Center Alignment");
        cellCenter.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell cellRight = new Cell("Right Alignment");
        cellRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //cellRight.setBackgroundColor(Color.RED);
//        cellRight.setBorderWidthBottom(20);
//        cellRight.setBorderWidthTop(10);

        table.addCell(cellLeft);
        table.addCell(cellCenter);
        table.addCell(cellRight);

        Cell cellSpanHoriz = new Cell("This Cell spans two columns");
        cellSpanHoriz.setColspan(2);
        table.addCell(cellSpanHoriz);
        table.addCell(emptyCell);

        Cell cellSpanVert = new Cell("This Cell spans two rows");
        cellSpanVert.setRowspan(2);
        table.addCell(emptyCell);
        table.addCell(cellSpanVert);
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);

        Cell cellSpanHorizVert = new Cell(
                "This Cell spans both two columns and two rows");
        cellSpanHorizVert.setColspan(2);
        cellSpanHorizVert.setRowspan(2);

        table.addCell(emptyCell);
        table.addCell(cellSpanHorizVert);
        table.addCell(emptyCell);

        RtfCell cellDotted = new RtfCell("Dotted border");
        cellDotted.setBorders(new RtfBorderGroup(Rectangle.BOX,
                RtfBorder.BORDER_DOTTED, 1, new Color(0, 0, 0)));
        RtfCell cellEmbossed = new RtfCell("Embossed border");
        cellEmbossed.setBorders(new RtfBorderGroup(Rectangle.BOX,
                RtfBorder.BORDER_EMBOSS, 1, new Color(0, 0, 0)));

        RtfFont font = new RtfFont("Times New Roman", 12);
        font.setColor(Color.RED);
        font.setStyle(com.lowagie.text.Font.BOLD);
        RtfCell cellNoBorder = new RtfCell(new Phrase("No border", font));
        //cellNoBorder.setBorderWidth(0);
        //cellNoBorder.setBorders(new RtfBorderGroup());
        cellNoBorder.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNoBorder.setVerticalAlignment(Element.ALIGN_CENTER);
        cellNoBorder.setBorders(new RtfBorderGroup(Rectangle.BOTTOM | Rectangle.RIGHT,
                RtfBorder.BORDER_SINGLE, 2, new Color(0, 0, 0)));
        //cellNoBorder.setUseBorderPadding(true);        


        table.addCell(cellDotted);
        table.addCell(cellEmbossed);
        table.addCell(cellNoBorder);
        for (int i = 0; i < 100; i++) {
            table.addCell(emptyCell);
            table.addCell(emptyCell);
            table.addCell(emptyCell);
        }

        doc.add(table);

        for (int i = 0; i < 300; i++) {
            doc.add(new Paragraph(
                    "Dummy line to get multi-page document. Line "
                            + (i + 1)));
        }

        doc.close();
    }
}
