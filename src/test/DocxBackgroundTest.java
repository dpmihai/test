package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;

import org.docx4j.jaxb.Context;
import org.docx4j.vml.CTFill;
import org.docx4j.wml.CTBackground;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;

public class DocxBackgroundTest {

	private static WordprocessingMLPackage wordMLPackage;
	private static ObjectFactory factory;

	public static void main(String[] args) {

		try {			

			boolean landscape = false;
			wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			factory = Context.getWmlObjectFactory();
			
					
			MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
			
			mdp.addParagraphOfText("Hello Word!");
			
			byte[] imageBytes = readImage("D:\\Sample.png");
			BinaryPartAbstractImage imagePartBG = BinaryPartAbstractImage.createImagePart(wordMLPackage, imageBytes);
			
			mdp.getContents().setBackground(createBackground(imagePartBG.getRelLast().getId())); 			
			
			wordMLPackage.save(new File("D:/Public/Documents/NextReports/TestBackground.docx"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	private static CTBackground createBackground(String rId) {

		org.docx4j.wml.ObjectFactory wmlObjectFactory = new org.docx4j.wml.ObjectFactory();

		CTBackground background = wmlObjectFactory.createCTBackground();
		background.setColor("FFFFFF");
		org.docx4j.vml.ObjectFactory vmlObjectFactory = new org.docx4j.vml.ObjectFactory();
		// Create object for background (wrapped in JAXBElement)
		org.docx4j.vml.CTBackground background2 = vmlObjectFactory.createCTBackground();
		JAXBElement<org.docx4j.vml.CTBackground> backgroundWrapped = vmlObjectFactory.createBackground(background2);
		background.getAnyAndAny().add(backgroundWrapped);
		background2.setTargetscreensize("1024,768");
		background2.setVmlId("_x0000_s1025");
		background2.setBwmode(org.docx4j.vml.officedrawing.STBWMode.WHITE);
		// Create object for fill
		CTFill fill = vmlObjectFactory.createCTFill();
		background2.setFill(fill);
		fill.setTitle("Alien 1");
		fill.setId(rId);
		fill.setType(org.docx4j.vml.STFillType.FRAME);
		fill.setRecolor(org.docx4j.vml.STTrueFalse.T);

		return background;
	}

}
