package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.docProps.core.CoreProperties;
import org.docx4j.docProps.core.dc.elements.SimpleLiteral;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.Body;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.CTTblPrBase.TblStyle;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.SectPr.PgMar;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcMar;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner.GridSpan;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.TcPrInner.VMerge;
import org.docx4j.wml.Text;
import org.docx4j.wml.TextDirection;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.DocPropsCorePart;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;

// http://blog.iprofs.nl/2012/09/06/creating-word-documents-with-docx4j/
//
//
// jars
//
//  docx4j-3.1.0.jar
//  jaxb-xmldsig-core-1.0.0.jar
//  slf4j-api-1.7.7.jar
//  slf4j-simple-1.7.7.jar
//  xalan-2.7.1.jar
//  xmlgraphics-commons-1.5.jar
//  commons-io-1.3.1.jar

// set column width does not work on Word 2007 (not tested in Word 2010)
public class DocxTest {

	private static WordprocessingMLPackage wordMLPackage;
	private static ObjectFactory factory;

	public static void main(String[] args) {

		try {
			// wordMLPackage = WordprocessingMLPackage.createPackage();
			// wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");
			// wordMLPackage.save(new File("D:/Test.docx"));

			boolean landscape = false;
			wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			factory = Context.getWmlObjectFactory();
			
			// page margins			   
			Body body = wordMLPackage.getMainDocumentPart().getJaxbElement().getBody();
			PageDimensions page = new PageDimensions();
			PgMar pgMar = page.getPgMar();      			
			pgMar.setBottom(BigInteger.valueOf(1000));
			pgMar.setTop(BigInteger.valueOf(1000));
			pgMar.setLeft(BigInteger.valueOf(1000));
			pgMar.setRight(BigInteger.valueOf(1000));			
			SectPr sectPr = factory.createSectPr();   
			body.setSectPr(sectPr);                           
			sectPr.setPgMar(pgMar);  
			
			addMetadata();

			Tbl table = createTableWithContent();						
			//addBorders(table);						
			wordMLPackage.getMainDocumentPart().addObject(table);
			
			newPage();
			wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");

			wordMLPackage.save(new File("D:/Public/Documents/NextReports/Test.docx"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Tbl createTableWithContent() {
		Tbl table = factory.createTbl();
				
		// for TEST
		// this adds borders to all cells
		TblPr tblPr = new TblPr();
		TblStyle tblStyle = new TblStyle();
		tblStyle.setVal("TableGrid");
		tblPr.setTblStyle(tblStyle);
		table.setTblPr(tblPr);

		Tr tableRow = factory.createTr();
		
		DocxStyle defStyle = new DocxStyle();
		defStyle.setBold(false);
		defStyle.setItalic(false);
		defStyle.setUnderline(false);
		defStyle.setHorizAlignment(JcEnumeration.CENTER);
		
		DocxStyle style = new DocxStyle();
		style.setBold(true);
		style.setItalic(true);
		style.setUnderline(true);
		style.setFontSize("40");
		style.setFontColor("FF0000");
		style.setFontFamily("Book Antiqua");
		style.setTop(300);
		style.setBackground("CCFFCC");		
		style.setVerticalAlignment(STVerticalJc.CENTER);
		style.setHorizAlignment(JcEnumeration.CENTER);
		style.setBorderTop(true);
		style.setBorderBottom(true);
		style.setNoWrap(true);
		style.setTextDirection("tbRl");  // btLr or tbRl
		addTableCell(tableRow, "Field 1 is very long to show in a cell", 2500, style, 1, null); // inches*72
		addTableCell(tableRow, "Field 2", 3500, defStyle, 1, "restart");
		addTableCell(tableRow, "Field 3", 1500, defStyle, 1, "restart");
		
		table.getContent().add(tableRow);
		
		tableRow = factory.createTr();
		addTableCell(tableRow, "PreInterval", 3500, defStyle, 1, null);
		addTableCell(tableRow, "", 3500, defStyle, 1, "");
		addTableCell(tableRow, "", 1500, defStyle, 1, "");
		table.getContent().add(tableRow);

		tableRow = factory.createTr();
		addTableCell(tableRow, "Interval", 3500, defStyle, 1, null);
		addTableCell(tableRow, "", 3500, defStyle, 1, "close");
		addTableCell(tableRow, "", 1500, defStyle, 1, "close");
		table.getContent().add(tableRow);
					
		tableRow = factory.createTr();
		addTableCell(tableRow, "Time", 2500, defStyle, 1, null);
		addTableCell(tableRow, "Unit", 3500, defStyle, 1, null);
		//addTableCell(tableRow, "<html><b><font color=\"red\">HTML Text<br>Line2</font></b></html>", 1500, defStyle, 1, null);
		addTableCell(tableRow, "<html><b><font size=\"4\" color=\"blue\">HTML TEXT</font></b></html>", 1500, defStyle, 1, null);		
		table.getContent().add(tableRow);

		tableRow = factory.createTr();
		addTableCell(tableRow, "Merged horizontally", 0, defStyle, 3, null);
		table.getContent().add(tableRow);

		String filenameHint = null;
		String altText = null;
		int id1 = 0;
		int id2 = 1;
		byte[] bytes = readImage("D:\\Public\\Documents\\NextReports\\logo.jpg");
		P pImage;
		try {
			pImage = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2, 6000);
			tableRow = factory.createTr();
			addTableCell(tableRow, pImage, 6000, defStyle, 3, null);
			table.getContent().add(tableRow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bytes = readImage("D:\\Public\\Documents\\NextReports\\d_center.png");		
		try {
			pImage = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2, 6000);
			tableRow = factory.createTr();
			addTableCell(tableRow, pImage, 6000, defStyle, 3, null);
			table.getContent().add(tableRow);
		} catch (Exception e) {
			e.printStackTrace();
		}				
		
		// add 40 Rows
		for (int i=0; i< 40; i++) {
			tableRow = factory.createTr();
			addTableCell(tableRow, "Cell " + (i+1), 2500, defStyle, 1, null);
			addTableCell(tableRow, "Second Cell" + (i+1), 3500, defStyle, 1, null);
			addTableCell(tableRow, "Third Cell" + (i+1), 1500, defStyle, 1, null);
			table.getContent().add(tableRow);
		}
		
		
		// Create hyperlink
		Hyperlink link = newHyperlink(wordMLPackage.getMainDocumentPart(), "NextReports Site", "http://www.next-reports.com");
		tableRow = factory.createTr();
		addTableCell(tableRow, link, 6000, style, 3, null);		
		table.getContent().add(tableRow);

		return table;
	}
	
	private static void addTableCell(Tr tableRow, Hyperlink link, int width, DocxStyle style, int horizontalMergedCells, String verticalMergedVal) {
		P paragraph = factory.createP();
		paragraph.getContent().add(link);
		addTableCell(tableRow, paragraph, width, style, horizontalMergedCells, verticalMergedVal);
	}

			
	private static void addTableCell(Tr tableRow, P image, int width, DocxStyle style, int horizontalMergedCells, String verticalMergedVal) {
		Tc tableCell = factory.createTc();				
		addImageCellStyle(tableCell, image, style);				
		setCellWidth(tableCell, width);				
		setCellVMerge(tableCell, verticalMergedVal);				
		setCellHMerge(tableCell, horizontalMergedCells);							
		tableRow.getContent().add(tableCell);
	}
	
	private static void addTableCell(Tr tableRow, String content, int width, DocxStyle style, int horizontalMergedCells, String verticalMergedVal) {
		Tc tableCell = factory.createTc();				
		addCellStyle(tableCell, content, style);		 		
		setCellWidth(tableCell, width);				
		setCellVMerge(tableCell, verticalMergedVal);				
		setCellHMerge(tableCell, horizontalMergedCells);
		if (style.isNoWrap()) {
			setCellNoWrap(tableCell);
		}		
		tableRow.getContent().add(tableCell);
	}

	private static void addBorders(Tbl table) {
		CTBorder border = new CTBorder();
		// border.setColor("auto");
		border.setColor("0000FF");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);

		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		table.getTblPr().setTblBorders(borders);
	}
	
	private static void setTextDirection(Tc tableCell, String dir) {
		if (dir != null) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
			TextDirection td = new TextDirection();
			td.setVal(dir);
			tableCellProperties.setTextDirection(td);
		}
	}
	
	private static void setCellBorders(Tc tableCell, boolean borderTop, boolean borderRight, boolean borderBottom, boolean borderLeft) {
				
		TcPr tableCellProperties = tableCell.getTcPr();
		if (tableCellProperties == null) {
			tableCellProperties = new TcPr();
			tableCell.setTcPr(tableCellProperties);
		}
		
		CTBorder border = new CTBorder();
		// border.setColor("auto");
		border.setColor("0000FF");
		border.setSz(new BigInteger("20"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);			
		
		TcBorders borders = new TcBorders();
		if (borderBottom) {
			borders.setBottom(border);
		} 
		if (borderTop) {
			borders.setTop(border);
		} 
		if (borderLeft) {
			borders.setLeft(border);
		}
		if (borderRight) {
			borders.setRight(border);
		}
		tableCellProperties.setTcBorders(borders);		
	}

	private static void setCellWidth(Tc tableCell, int width) {
		if (width > 0) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
			TblWidth tableWidth = new TblWidth();
			tableWidth.setType("dxa");
			tableWidth.setW(BigInteger.valueOf(width));
			tableCellProperties.setTcW(tableWidth);
		}
	}
	
	private static void setCellNoWrap(Tc tableCell) {
		TcPr tableCellProperties = tableCell.getTcPr();
		if (tableCellProperties == null) {
			tableCellProperties = new TcPr();
			tableCell.setTcPr(tableCellProperties);
		}
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		tableCellProperties.setNoWrap(b);		
	}

	private static void setCellVMerge(Tc tableCell, String mergeVal) {
		if (mergeVal != null) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
			VMerge merge = new VMerge();
			if (!"close".equals(mergeVal)) {
				merge.setVal(mergeVal);
			}
			tableCellProperties.setVMerge(merge);
		}
	}
	
	private static void setCellHMerge(Tc tableCell, int horizontalMergedCells) {
		if (horizontalMergedCells > 1) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
	
			GridSpan gridSpan = new GridSpan();
			gridSpan.setVal(new BigInteger(String.valueOf(horizontalMergedCells)));
	
			tableCellProperties.setGridSpan(gridSpan);
			tableCell.setTcPr(tableCellProperties);
		}				
	}	
	
	private static void setCellColor(Tc tableCell, String color) {
		if (color != null) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
			CTShd shd = new CTShd();
			shd.setFill(color);
			tableCellProperties.setShd(shd);
		}
	}

	private static void setCellMargins(Tc tableCell, int top, int right, int bottom, int left) {
		TcPr tableCellProperties = tableCell.getTcPr();
		if (tableCellProperties == null) {
			tableCellProperties = new TcPr();
			tableCell.setTcPr(tableCellProperties);
		}
		TcMar margins = new TcMar();

		if (bottom > 0) {
			TblWidth bW = new TblWidth();
			bW.setType("dxa");
			bW.setW(BigInteger.valueOf(bottom));
			margins.setBottom(bW);
		}

		if (top  > 0) {
			TblWidth tW = new TblWidth();
			tW.setType("dxa");
			tW.setW(BigInteger.valueOf(top));
			margins.setTop(tW);
		}

		if (left > 0) {
			TblWidth lW = new TblWidth();
			lW.setType("dxa");
			lW.setW(BigInteger.valueOf(left));
			margins.setLeft(lW);
		}

		if (right > 0) {
			TblWidth rW = new TblWidth();
			rW.setType("dxa");
			rW.setW(BigInteger.valueOf(right));
			margins.setRight(rW);
		}

		tableCellProperties.setTcMar(margins);
	}

	private static void setVerticalAlignment(Tc tableCell, STVerticalJc align) {
		if (align != null) {
			TcPr tableCellProperties = tableCell.getTcPr();
			if (tableCellProperties == null) {
				tableCellProperties = new TcPr();
				tableCell.setTcPr(tableCellProperties);
			}
	
			CTVerticalJc valign = new CTVerticalJc();
			valign.setVal(align);
	
			tableCellProperties.setVAlign(valign);
		}
	}
	
	private static void addImageCellStyle(Tc tableCell, P image, DocxStyle style) {		
		setCellMargins(tableCell, style.getTop(), style.getRight(), style.getBottom(), style.getLeft());
		setCellColor(tableCell,  style.getBackground());
		setVerticalAlignment(tableCell, style.getVerticalAlignment());		
		setHorizontalAlignment(image, style.getHorizAlignment());
		setCellBorders(tableCell, style.isBorderTop(), style.isBorderRight(), style.isBorderBottom(), style.isBorderLeft());
		tableCell.getContent().add(image);
	}

	/**
	 * This is where we add the actual styling information. In order to do this
	 * we first create a paragraph. Then we create a text with the content of
	 * the cell as the value. Thirdly, we create a so-called run, which is a
	 * container for one or more pieces of text having the same set of
	 * properties, and add the text to it. We then add the run to the content of
	 * the paragraph. So far what we've done still doesn't add any styling. To
	 * accomplish that, we'll create run properties and add the styling to it.
	 * These run properties are then added to the run. Finally the paragraph is
	 * added to the content of the table cell.
	 */
	private static void addCellStyle(Tc tableCell, String content, DocxStyle style) {
		if (style != null) {
			
			// inner html text
			if (content.startsWith("<html>")) {
				try {
					wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Html, content.getBytes(), tableCell);
					P paragraph = factory.createP();
					tableCell.getContent().add(paragraph);
				} catch (Docx4JException e) {					
					e.printStackTrace();
				}
				return;
			}
			
			P paragraph = factory.createP();
	
			Text text = factory.createText();
			text.setValue(content);
	
			R run = factory.createR();
			run.getContent().add(text);
	
			paragraph.getContent().add(run);
			
			setHorizontalAlignment(paragraph, style.getHorizAlignment());
					
			RPr runProperties = factory.createRPr();
			
			if (style.isBold()) {
				addBoldStyle(runProperties);
			}
			if (style.isItalic()) {
				addItalicStyle(runProperties);
			}
			if (style.isUnderline()) {
				addUnderlineStyle(runProperties);
			}
			
			setFontSize(runProperties, style.getFontSize());				
			setFontColor(runProperties, style.getFontColor());				
			setFontFamily(runProperties, style.getFontFamily());
					
			setCellMargins(tableCell, style.getTop(), style.getRight(), style.getBottom(), style.getLeft());
			setCellColor(tableCell, style.getBackground());
			setVerticalAlignment(tableCell, style.getVerticalAlignment());
			
			setCellBorders(tableCell, style.isBorderTop(), style.isBorderRight(), style.isBorderBottom(), style.isBorderLeft());
			setTextDirection(tableCell, style.getTextDirection());
	
			run.setRPr(runProperties);
	
			tableCell.getContent().add(paragraph);
		}
	}

	/**
	 * In this method we're going to add the font size information to the run
	 * properties. First we'll create a half-point measurement. Then we'll set
	 * the fontSize as the value of this measurement. Finally we'll set the
	 * non-complex and complex script font sizes, sz and szCs respectively.
	 */
	private static void setFontSize(RPr runProperties, String fontSize) {
		if (fontSize != null && !fontSize.isEmpty()) {
			HpsMeasure size = new HpsMeasure();
			size.setVal(new BigInteger(fontSize));
			runProperties.setSz(size);
			runProperties.setSzCs(size);
		}
	}

	private static void setFontFamily(RPr runProperties, String fontFamily) {
		if (fontFamily != null) {
			RFonts rf = runProperties.getRFonts();
			if (rf == null) {
				rf = new RFonts();
				runProperties.setRFonts(rf);
			}
			rf.setAscii(fontFamily);
		}
	}

	private static void setFontColor(RPr runProperties, String color) {
		if (color != null) {
			Color c = new Color();
			c.setVal(color);
			runProperties.setColor(c);
		}	
	}
	
	private static void setHorizontalAlignment(P paragraph, JcEnumeration hAlign) {
		if (hAlign != null) {
			PPr pprop = new PPr();
			Jc align = new Jc();
			align.setVal(hAlign);
			pprop.setJc(align);
			paragraph.setPPr(pprop);
		}
	}

	/**
	 * In this method we'll add the bold property to the run properties.
	 * BooleanDefaultTrue is the Docx4j object for the b property. Technically
	 * we wouldn't have to set the value to true, as this is the default.
	 */
	private static void addBoldStyle(RPr runProperties) {
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setB(b);
	}

	private static void addItalicStyle(RPr runProperties) {
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		b.setVal(true);
		runProperties.setI(b);
	}

	private static void addUnderlineStyle(RPr runProperties) {
		U val = new U();
		val.setVal(UnderlineEnumeration.SINGLE);
		runProperties.setU(val);
	}

	/**
	 * Create image, specifying width in twips
	 */
	public static P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText, int id1,
			int id2, long cx) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
		Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, cx, false);
		// Now add the inline in w:p/w:r/w:drawing
		ObjectFactory factory = Context.getWmlObjectFactory();
		P p = factory.createP();
		R run = factory.createR();
		p.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return p;
	}

	private static byte[] readImage(String path) {
		// The image to add
		File file = new File(path);
		// Our utility method wants that as a byte array
		InputStream is = null;
		byte[] bytes = new byte[0];
		try {
			is = new FileInputStream(file);
			long length = file.length();
			// You cannot create an array using a long type.
			// It needs to be an int type.
			if (length > Integer.MAX_VALUE) {
				System.out.println("File too large!!");
			}
			bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				System.out.println("Could not completely read file " + file.getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
	public static Hyperlink newHyperlink(MainDocumentPart mdp, String text, String url) {
		try {
			// We need to add a relationship to word/_rels/document.xml.rels
			// but since its external, we don't use the usual 
			// wordMLPackage.getMainDocumentPart().addTargetPart mechanism
			org.docx4j.relationships.ObjectFactory factory = new org.docx4j.relationships.ObjectFactory();
			org.docx4j.relationships.Relationship rel = factory.createRelationship();
			rel.setType(Namespaces.HYPERLINK);
			rel.setTarget(url);
			rel.setTargetMode("External");
			mdp.getRelationshipsPart().addRelationship(rel);
			// addRelationship sets the rel's @Id
			String hpl = "<w:hyperlink r:id=\"" + rel.getId()
					+ "\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
					+ "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" >" + "<w:r>" + "<w:rPr>"
					+ "<w:rStyle w:val=\"Hyperlink\" />" + 
					"</w:rPr>" + "<w:t>" + text + "</w:t>" + "</w:r>" + "</w:hyperlink>";
			return (Hyperlink) XmlUtils.unmarshalString(hpl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static void addMetadata() {
		DocPropsCorePart docPropsCorePart = wordMLPackage.getDocPropsCorePart();
		CoreProperties coreProps = (CoreProperties) docPropsCorePart.getJaxbElement();
		org.docx4j.docProps.core.ObjectFactory CorePropsfactory = new org.docx4j.docProps.core.ObjectFactory();
		org.docx4j.docProps.core.dc.elements.ObjectFactory dcElfactory = new org.docx4j.docProps.core.dc.elements.ObjectFactory();
		
		SimpleLiteral desc = dcElfactory.createSimpleLiteral();
		desc.getContent().add("This file was generated by NextReports");
		coreProps.setDescription(dcElfactory.createDescription(desc));
		
		SimpleLiteral title = dcElfactory.createSimpleLiteral();
		title.getContent().add("NextReports Document");
		coreProps.setTitle(dcElfactory.createTitle(title));
		
		SimpleLiteral author = dcElfactory.createSimpleLiteral();
		author.getContent().add("Mihai Dinca-Panaitescu");
		coreProps.setCreator(author);
		
		SimpleLiteral subject = dcElfactory.createSimpleLiteral();
		subject.getContent().add("Created by NextReports Designer 7.3");
		coreProps.setSubject(subject);
				
		coreProps.setKeywords("Version 7.3");
	}
	
	private static void newPage() {
		Br objBr = new Br();
		objBr.setType(STBrType.PAGE);
		P para = factory.createP();
		para.getContent().add(objBr);
		wordMLPackage.getMainDocumentPart().getContent().add(para);
	}
		

}
