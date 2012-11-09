package test;

import org.apache.poi.hssf.usermodel.*;

import java.io.*;


/**
 * User: mihai.panaitescu
 * Date: 02-Dec-2009
 * Time: 10:51:30
 */
public class PoiImage {
    public static void main(String[] args) {
        int col = 1;
        int row = 1;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet testsheet = wb.createSheet("test");
        System.out.println("The work book is created");
        testsheet.setMargin((short)0, 100);

        try {
            FileOutputStream fos = new FileOutputStream("E:\\sample.xls");
            System.out.println("File sample.xls is created");
            FileInputStream fis = new FileInputStream("E:\\Public\\Documents\\NextReports\\Images\\info.png");
            ByteArrayOutputStream img_bytes = new ByteArrayOutputStream();
            int b;
            while ((b = fis.read()) != -1) {
                img_bytes.write(b);
            }
            fis.close();

            /*  dx1 - the x coordinate within the first cell.
                dy1 - the y coordinate within the first cell.
                dx2 - the x coordinate within the second cell.
                dy2 - the y coordinate within the second cell.
                col1 - the column (0 based) of the first cell.
                row1 - the row (0 based) of the first cell.
                col2 - the column (0 based) of the second cell.
                row2 - the row (0 based) of the second cell.
            */
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) col, row, (short) (col+2), (row+2));
            int index = wb.addPicture(img_bytes.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
            HSSFSheet sheet = wb.getSheet("test");
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor, index);
            // 0 = Move and size with Cells
            // 2 = Move but don't size with cells
            // 3 = Don't move or size with cells.
            anchor.setAnchorType(2);
            
            wb.write(fos);
            System.out.println("Writing data to the xls file");
            fos.close();
            System.out.println("File closed");
        }
        catch (IOException ioe) {
            System.out.println("Hi ! You got an exception. " + ioe.getMessage());
        }
    }
}//end of class MyWorkBook
