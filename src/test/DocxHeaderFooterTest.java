package test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.docx4j.convert.out.flatOpcXml.FlatOpcXmlCreator;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.utils.BufferUtil;
import org.docx4j.wml.Br;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.SectPr;

/**
 * Create a WordML Pkg and add a header to it. Output is Flat OPC XML. Notice:
 * 1. the Header part 2. the contents of the sectPr element
 *
 * @author jharrop
 *
 */
public class DocxHeaderFooterTest {
	private static WordprocessingMLPackage wordMLPackage;	
	private static ObjectFactory objectFactory = new ObjectFactory();

	public static void main(String[] args) throws Exception {
		wordMLPackage = WordprocessingMLPackage.createPackage();
		// Delete the Styles part, since it clutters up our output
		MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
		Relationship styleRel = mdp.getStyleDefinitionsPart().getSourceRelationships().get(0);
		mdp.getRelationshipsPart().removeRelationship(styleRel);
		// OK, the guts of this sample:
		// The 2 things you need:
		// 1. the Header part
		// 2. an entry in SectPr
		Relationship relationship = createHeaderPart(wordMLPackage);		
		createHeaderReference(wordMLPackage, relationship);
		
		Relationship footerRelationship = createFooterPart(wordMLPackage);
		createFooterReference(wordMLPackage, footerRelationship);
		
		// Display the result as Flat OPC XML
//		FlatOpcXmlCreator worker = new FlatOpcXmlCreator(wordMLPackage);
//		worker.marshal(System.out);
		
		wordMLPackage.getMainDocumentPart().addParagraphOfText("First Page");
		newPage();
		wordMLPackage.getMainDocumentPart().addParagraphOfText("Second Page");
		
		wordMLPackage.save(new File("D:/Public/Documents/NextReports/TestH.docx"));
	}

	public static Relationship createHeaderPart(WordprocessingMLPackage wordprocessingMLPackage) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		Relationship rel = wordprocessingMLPackage.getMainDocumentPart().addTargetPart(headerPart);
		// After addTargetPart, so image can be added properly
		headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, headerPart));
		return rel;
	}

	public static void createHeaderReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = objectFactory.createSectPr();
			wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		HeaderReference headerReference = objectFactory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(headerReference);// add header or
		// footer references
	}

	public static Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage, Part sourcePart) throws Exception {
		Hdr hdr = objectFactory.createHdr();
		File file = new File("D:\\logo.png");
		java.io.InputStream is = new java.io.FileInputStream(file);
		hdr.getContent()
				.add(newImage(wordprocessingMLPackage, sourcePart, BufferUtil.getBytesFromInputStream(is), "filename", "alttext",
						1, 2));
		return hdr;
	}
	
	public static Relationship createFooterPart(WordprocessingMLPackage wordprocessingMLPackage) throws Exception {
		FooterPart footerPart = new FooterPart();
		Relationship rel = wordprocessingMLPackage.getMainDocumentPart().addTargetPart(footerPart);
		// After addTargetPart, so image can be added properly
		footerPart.setJaxbElement(getFtr(wordprocessingMLPackage, footerPart));
		return rel;
	}
	
	public static void createFooterReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = objectFactory.createSectPr();
			wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		FooterReference footerReference = objectFactory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(footerReference);// add header or footer references
	}
	
	public static Ftr getFtr(WordprocessingMLPackage wordprocessingMLPackage, Part sourcePart) throws Exception {
		Ftr ftr = objectFactory.createFtr();
		File file = new File("D:\\soccer_ball.png");
		java.io.InputStream is = new java.io.FileInputStream(file);
		ftr.getContent()
				.add(newImage(wordprocessingMLPackage, sourcePart, BufferUtil.getBytesFromInputStream(is), "filename", "alttext",
						1, 2));
		return ftr;
	}

	// public static P getP() {
	// P headerP = objectFactory.createP();
	// R run1 = objectFactory.createR();
	// Text text = objectFactory.createText();
	// text.setValue("123head123");
	// run1.getRunContent().add(text);
	// headerP.getParagraphContent().add(run1);
	// return headerP;
	// }
	public static org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage, Part sourcePart, byte[] bytes,
			String filenameHint, String altText, int id1, int id2) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, sourcePart, bytes);
		Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, false);
		// Now add the inline in w:p/w:r/w:drawing
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P p = factory.createP();
		org.docx4j.wml.R run = factory.createR();
		p.getContent().add(run);
		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return p;
	}
	
	private static void newPage() {
		Br objBr = new Br();
		objBr.setType(STBrType.PAGE);
		P para = objectFactory.createP();
		para.getContent().add(objBr);
		wordMLPackage.getMainDocumentPart().getContent().add(para);
	}
}
