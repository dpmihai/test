package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class XlsEdit {
	
	private Map<HSSFFont, HSSFFont> fontCache = new HashMap<HSSFFont, HSSFFont>();
	private static File editFile = new File("D:\\Public\\test\\edit.xls");
	private static File outFile = new File("D:\\Public\\test\\edit2.xls");
	private static int sheetNo = 2;
	
	public void edit(File inFile, int sheetNo, File outFile) throws IOException {
											
		InputStream is = new FileInputStream(inFile);
		HSSFWorkbook wbRead = new HSSFWorkbook(is);
		
		// modify
        HSSFSheet sheet = wbRead.getSheetAt(sheetNo-1);        
        Row row = sheet.createRow(0);      
        Cell cell = row.createCell(0);
        cell.setCellValue("Blahblah");   

        is.close();
        
        // write
        FileOutputStream fos =new FileOutputStream(outFile);
        wbRead.write(fos);
        fos.close();
	}
	
	
	public static void main(String[] args) {
		XlsEdit xe = new XlsEdit();
		try {			
			xe.edit(editFile, sheetNo, outFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
