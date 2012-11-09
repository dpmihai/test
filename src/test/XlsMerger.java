package test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * User: mihai.panaitescu
 * Date: 13-May-2010
 * Time: 11:22:55
 */
public class XlsMerger {

    private Map<HSSFFont, HSSFFont> fontCache = new HashMap<HSSFFont, HSSFFont>();

    public static void main(String[] args) {
        XlsMerger merger = new XlsMerger();
        List<InputStream> list = new ArrayList<InputStream>();
        try {
            // Source
            list.add(new FileInputStream(new File("E:\\Public\\test\\1.xls")));
            list.add(new FileInputStream(new File("E:\\Public\\test\\2.xls")));

            // Resulting
            OutputStream out = new FileOutputStream(new File("E:\\Public\\test\\merge.xls"));

            merger.doMerge(list, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doMerge(List<InputStream> list, OutputStream outputStream)
            throws IOException {

        HSSFWorkbook wbWrite = new HSSFWorkbook();


        int index = 0;
        for (InputStream in : list) {

            index++;
            HSSFWorkbook wbRead = new HSSFWorkbook(in);

            List<HSSFPictureData> pictures = wbRead.getAllPictures();
            List<Integer> indexes = new ArrayList<Integer>();
            for (HSSFPictureData picture : pictures) {
                indexes.add(wbWrite.addPicture(picture.getData(), HSSFWorkbook.PICTURE_TYPE_JPEG));                
            }

            for (int i = 0; i < wbRead.getNumberOfSheets(); i++) {
                HSSFSheet oldSheet = wbRead.getSheetAt(i);
                HSSFSheet newSheet = wbWrite.createSheet(index + "_" + oldSheet.getSheetName());
                for (int j = 0; j <= oldSheet.getLastRowNum(); j++) {
                    HSSFRow oldRow = oldSheet.getRow(j);
                    HSSFRow newRow = newSheet.createRow(j);
                    if (oldRow.getRowStyle() != null) {
                        newRow.setRowStyle(oldRow.getRowStyle());
                    }
                    //HSSFFont newFont = wbWrite.createFont();
                    for (int k = 0; k < oldRow.getLastCellNum(); k++) {
                        HSSFCell oldCell = oldRow.getCell(k);
                        HSSFCell newCell = newRow.createCell(k);
                        newCell.setCellType(oldCell.getCellType());
                        if (HSSFCell.CELL_TYPE_STRING == oldCell.getCellType()) {
                            newCell.setCellValue(oldCell.getStringCellValue());
                        } else {
                            newCell.setCellValue(oldCell.getNumericCellValue());
                        }
                        newCell.setCellStyle(createCellStyle(wbRead, wbWrite, oldCell.getCellStyle()));
                        if (oldCell.getHyperlink() != null) {
                            newCell.setHyperlink(oldCell.getHyperlink());
                        }
                    }

                    System.out.println("** height= "+ oldRow.getHeight());
                    newRow.setHeight(oldRow.getHeight());
                }

                // regions
                for (int r = 0, size = oldSheet.getNumMergedRegions(); r < size; r++) {
                    XlsRegion xlsRegion = new XlsRegion(oldSheet.getMergedRegion(r));
                    CellRangeAddress region = xlsRegion.getCellRangeAddress();
                    //Border border = xlsRegion.getBorder();
                    newSheet.addMergedRegion(region);
                }

                // pictures
                //@todo compute image position??
                //http://old.nabble.com/Copy-images-from-Workbook-to-Workbook-td23032425.html

                HSSFPatriarch newPatriarch = newSheet.createDrawingPatriarch();
                for (int r = 0; r < pictures.size(); r++) {
//                    HSSFClientAnchor newAnchor = new HSSFClientAnchor(0, 0, 0, 0, oldAnchor.getCol1(), oldAnchor.getRow1(),
//                            oldAnchor.getCol2(), oldAnchor.getRow2());
                    HSSFClientAnchor newAnchor = new HSSFClientAnchor(0, 0, 0, 0, (short)0, 0,
                            (short)5, 1);
                    HSSFPicture newPicture = newPatriarch.createPicture(newAnchor, indexes.get(r));
                    newPicture.resize();
                    newAnchor.setAnchorType(2);
                }

            }
        }
        wbWrite.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }

    public HSSFCellStyle createCellStyle(HSSFWorkbook wbRead, HSSFWorkbook wbWrite, HSSFCellStyle oldCellStyle) {
        HSSFCellStyle newCellStyle = wbWrite.createCellStyle();

        HSSFFont oldFont = oldCellStyle.getFont(wbRead);
        HSSFFont newFont = getFont(oldFont);
        if (newFont == null) {
            newFont = wbWrite.createFont();
            newFont.setFontName(oldFont.getFontName());
            newFont.setFontHeightInPoints(oldFont.getFontHeightInPoints());
            newFont.setColor(oldFont.getColor());
            newFont.setBoldweight(oldFont.getBoldweight());
            newFont.setItalic(oldFont.getItalic());
            fontCache.put(oldFont, newFont);
        }        

        //System.out.println("** name="+newFont.getFontName() + " : " + newFont.getFontHeightInPoints());
        newCellStyle.setFont(newFont);

        newCellStyle.setFillPattern(oldCellStyle.getFillPattern());
        newCellStyle.setFillForegroundColor(oldCellStyle.getFillForegroundColor());
        newCellStyle.setFillBackgroundColor(oldCellStyle.getFillBackgroundColor());
        newCellStyle.setAlignment(oldCellStyle.getAlignment());
        newCellStyle.setVerticalAlignment(oldCellStyle.getVerticalAlignment());
        newCellStyle.setBorderLeft(oldCellStyle.getBorderLeft());
        newCellStyle.setBorderRight(oldCellStyle.getBorderRight());
        newCellStyle.setBorderBottom(oldCellStyle.getBorderBottom());
        newCellStyle.setBorderTop(oldCellStyle.getBorderTop());
        newCellStyle.setDataFormat(oldCellStyle.getDataFormat());
        newCellStyle.setWrapText(oldCellStyle.getWrapText());        
        return newCellStyle;
    }

    public class XlsRegion {
        private CellRangeAddress cra;
        //private Border border;

        public XlsRegion(CellRangeAddress cra/*, Border border*/) {
            this.cra = cra;
            //this.border = border;
        }

        public CellRangeAddress getCellRangeAddress() {
            return cra;
        }

//        public Border getBorder() {
//            return border;
//        }
    }

    public boolean isFontCreated(HSSFWorkbook wb, HSSFFont font) {
        int no = wb.getNumberOfFonts();
        for  (short i=0; i<no; i++) {
            HSSFFont f = wb.getFontAt(i);
            if (f.equals(font)) {
                System.out.println("*** found");
                return true;
            }
        }
        return false;
    }

    public HSSFFont getFont(HSSFFont oldFont) {
        for (HSSFFont font : fontCache.keySet()) {
            if (equalsFonts(oldFont, font)) {                
                return fontCache.get(font);
            }
        }
        return null;
    }

    private boolean equalsFonts(HSSFFont oldFont, HSSFFont newFont) {
        return oldFont.getFontName().equals(newFont.getFontName()) &&
              (oldFont.getFontHeightInPoints() == newFont.getFontHeightInPoints()) &&
              (oldFont.getColor() == newFont.getColor()) &&
              (oldFont.getBoldweight() == newFont.getBoldweight()) &&
              (oldFont.getItalic() == newFont.getItalic());  
    }

}
