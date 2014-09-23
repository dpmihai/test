package test;

import java.math.BigInteger;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTTblPrBase.TblStyle;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Tr;
import org.docx4j.wml.TrPr;

public class DocxNestedTest {
      
       static WordprocessingMLPackage wordMLPackage =null;
       static ObjectFactory factory = new ObjectFactory();
        
       protected static void addTc(Tr tr, String text, int tableWidth) {
                 Tc tc = factory.createTc();
                 TcPr tcPr = new TcPr();
                 TblWidth width = new TblWidth();
                 width.setType("dxa");
                 width.setW(new BigInteger(String.valueOf(tableWidth)));
                 tcPr.setTcW(width);
//                 CTVerticalJc vAlign = new CTVerticalJc();
//                 vAlign.setVal(STVerticalJc.CENTER);
//                 tcPr.setVAlign(vAlign);
                 tc.setTcPr(tcPr);
                 tc.getContent().add( wordMLPackage.getMainDocumentPart().createParagraphOfText(text) );
                 tr.getContent().add( tc );
              }
      
        public static void main(String[] args) throws Exception {
              
                System.out.println( "Creating package..");
                wordMLPackage = WordprocessingMLPackage.createPackage();
               
                Tbl tblCredProg = factory.createTbl();
                TblPr tblPr = new TblPr();

                TblStyle tblStyle = new TblStyle();
                tblStyle.setVal("TableGrid");
               
                tblPr.setTblStyle(tblStyle);
                tblCredProg.setTblPr(tblPr);
              
                TblWidth width = new TblWidth();
                width.setType("auto");
                width.setW(new BigInteger("0"));
                tblPr.setTblW(width);
               
                // create row 1
                Tr tr = factory.createTr();
               
                // col 1 of row 1
                addTc(tr , "A", 1000);
                //col 2 of row 1
                addTc(tr , "B", 3192);

                tblCredProg.getContent().add(tr);
              
                // create row 2
                Tr tr2 = factory.createTr();
                 
                TrPr pr = new TrPr();
                tr2.setTrPr(pr);
               
                // col 1 of row 2
               
                   Tc tc1 = factory.createTc();
                   TcPr tcPr = new TcPr();
                        TblWidth widtha = new TblWidth();
                        widtha.setType("auto");
                        widtha.setW(new BigInteger("0"));
                        tcPr.setTcW(widtha);                       
                        
                        tc1.setTcPr(tcPr);
                        tc1.getContent().add(createInnerTable());
                        //The following is important or you may get a corrupted docx file
                        tc1.getContent().add( wordMLPackage.getMainDocumentPart().createParagraphOfText(""));
                   tr2.getContent().add( tc1 );
                  
                   tblCredProg.getContent().add(tr2);
                  
                   //col2 of row 2
                   addTc(tr2 , "C", 3192);
                  
                   wordMLPackage.getMainDocumentPart().addObject(tblCredProg);
                // Now save it
                wordMLPackage.save(new java.io.File("D:/Public/Documents/NextReports/Test2.docx") );
              
                System.out.println("Done.");
                              
        }
        
        private static Tbl createInnerTable() {
        	Tbl tbl = factory.createTbl();
            TblPr tblPr = new TblPr();
            tbl.setTblPr(tblPr);
            
            Tr tr = factory.createTr();            
            addTc(tr , "1", 500);            
            addTc(tr , "2", 500);
            tbl.getContent().add(tr);
            
            Tr tr2 = factory.createTr();            
            addTc(tr2 , "3", 500);            
            addTc(tr2 , "4", 500);
            tbl.getContent().add(tr2);
            
            return tbl;
        }
       
             
      
    
}
