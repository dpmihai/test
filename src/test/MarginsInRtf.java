package test;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.table.RtfCell;

public class MarginsInRtf {
	
	public static void main(String[] args) throws Exception {
		FileOutputStream outputStream = new FileOutputStream("E:\\test.rtf");
		Document document = new Document();
        RtfWriter2 writer = RtfWriter2.getInstance(document, outputStream);
        document.open();

        int totalColumns = 2;
        Table datatable = new Table(totalColumns);                
        datatable.setWidth(100);        
        datatable.setPadding(2);
        datatable.addCell(new RtfCell("test"));
        datatable.addCell(new RtfCell("test2"));
        datatable.addCell(new RtfCell("test3"));
        datatable.addCell(new RtfCell("test4"));
        
        document.setMargins(100, 50, 20, 10);

        document.add(datatable);
        outputStream.flush();
        document.close();
        outputStream.close();
	}

}
