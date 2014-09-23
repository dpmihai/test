package test;

import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTSimpleField;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.SectPr;
import org.docx4j.model.structure.SectionWrapper;

// imports skipped

public class DocxPageNoTest {
       

        private static org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();

        private static P makePageBr() throws Exception {
                P p = factory.createP();
                R r = factory.createR();
                Br br = factory.createBr();
                br.setType(STBrType.PAGE);
                r.getContent().add(br);
                p.getContent().add(r);
                return p;
        }

        public static P makeParagraph(Part part, String text) {
                P p = factory.createP();
                R r = factory.createR();
                p.getContent().add(r);
                org.docx4j.wml.Text t = factory.createText();
                r.getContent().add(t);
                t.setValue(text);
                return p;
        }

        public static Ftr getFtr() throws Exception
           {
              // AddPage Numbers
              CTSimpleField pgnum = factory.createCTSimpleField();
              pgnum.setInstr(" PAGE \\* MERGEFORMAT ");
              RPr RPr = factory.createRPr();
               RPr.setNoProof(new BooleanDefaultTrue());
               PPr ppr = factory.createPPr();
               Jc jc = factory.createJc();
               jc.setVal(JcEnumeration.CENTER);
               ppr.setJc(jc);
               PPrBase.Spacing pprbase = factory.createPPrBaseSpacing();
               pprbase.setBefore(BigInteger.valueOf(240));
               pprbase.setAfter(BigInteger.valueOf(0));
               ppr.setSpacing(pprbase);
             
               R run = factory.createR();
               run.getContent().add(RPr);
               pgnum.getContent().add(run);

               JAXBElement<CTSimpleField> fldSimple = factory.createPFldSimple(pgnum);
               P para = factory.createP();
               para.getContent().add(fldSimple);
               para.setPPr(ppr);  
               // Now add our paragraph to the footer
               Ftr ftr = factory.createFtr();
               ftr.getContent().add(para);
              return ftr;
           }
       
        public static void main(String[] args) throws Exception {

                WordprocessingMLPackage pkg = WordprocessingMLPackage.createPackage();
                MainDocumentPart main_part = pkg.getMainDocumentPart();

                main_part.addStyledParagraphOfText("Normal", "Cover page (TODO)");

//                HeaderPart cover_hdr_part = new HeaderPart(new PartName(
//                                "/word/cover-header.xml")), content_hdr_part = new HeaderPart(
//                                new PartName("/word/content-header.xml"));
//                pkg.getParts().put(cover_hdr_part);
//                pkg.getParts().put(content_hdr_part);
//
//                Hdr cover_hdr = factory.createHdr(), content_hdr = factory.createHdr();
//
//                // Bind the header JAXB elements as representing their header parts
//                cover_hdr_part.setJaxbElement(cover_hdr);
//                content_hdr_part.setJaxbElement(content_hdr);
//
//                // Add the reference to both header parts to the Main Document Part
//                Relationship cover_hdr_rel = main_part.addTargetPart(cover_hdr_part);
//                Relationship content_hdr_rel = main_part
//                                .addTargetPart(content_hdr_part);
//
//                cover_hdr.getContent().add(
//                                makeParagraph(cover_hdr_part, "Cover header text"));
//                content_hdr.getContent().add(
//                                makeParagraph(content_hdr_part, "Content header text"));

               
               
               
               
                //DO FOOTER PART NOW ***********************************************************************
               
                FooterPart cover_ftr_part = new FooterPart(new PartName(
                                "/word/cover-footer.xml")), content_ftr_part = new FooterPart(
                                new PartName("/word/content-footer.xml"));
               
                pkg.getParts().put(cover_ftr_part);
                pkg.getParts().put(content_ftr_part);

                //Ftr cover_ftr = factory.createFtr(), content_ftr = factory.createFtr();
                //page number test

                Ftr cover_ftr = getFtr();
                Ftr content_ftr = getFtr();


                // Bind the header JAXB elements as representing their header parts
                cover_ftr_part.setJaxbElement(cover_ftr);
                content_ftr_part.setJaxbElement(content_ftr);

                // Add the reference to both header parts to the Main Document Part
                Relationship cover_ftr_rel = main_part.addTargetPart(cover_ftr_part);
                Relationship content_ftr_rel = main_part
                                .addTargetPart(content_ftr_part);

               
//                cover_ftr.getContent().add(
//                                makeParagraph(cover_ftr_part, "Cover footer text"));
//                content_ftr.getContent().add(
//                                makeParagraph(content_hdr_part, "Content footer text"));
               
               
                //PUT THE DOCUMENT TOGETHER

               
                List<SectionWrapper> sections = pkg.getDocumentModel().getSections();

                // Get last section SectPr and create a new one if it doesn't exist
                SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
                if (sectPr == null) {
                        sectPr = factory.createSectPr();
                        pkg.getMainDocumentPart().addObject(sectPr);
                        sections.get(sections.size() - 1).setSectPr(sectPr);
                }

//                // link cover and content headers
//                HeaderReference hdr_ref; // this variable is reused
//
//                hdr_ref = factory.createHeaderReference();
//                hdr_ref.setId(cover_hdr_rel.getId());
//                hdr_ref.setType(HdrFtrRef.FIRST);
//                sectPr.getEGHdrFtrReferences().add(hdr_ref);
//
//                hdr_ref = factory.createHeaderReference();
//                hdr_ref.setId(content_hdr_rel.getId());
//                hdr_ref.setType(HdrFtrRef.DEFAULT);
//                sectPr.getEGHdrFtrReferences().add(hdr_ref);
//               
//                BooleanDefaultTrue boolanDefaultTrue = new BooleanDefaultTrue();
//                sectPr.setTitlePg(boolanDefaultTrue);
               

                // link cover and content footers
                FooterReference ftr_ref; // this variable is reused

                ftr_ref = factory.createFooterReference();
                ftr_ref.setId(cover_ftr_rel.getId());
                ftr_ref.setType(HdrFtrRef.FIRST);
                sectPr.getEGHdrFtrReferences().add(ftr_ref);

                ftr_ref = factory.createFooterReference();
                ftr_ref.setId(content_ftr_rel.getId());
                ftr_ref.setType(HdrFtrRef.DEFAULT);
                sectPr.getEGHdrFtrReferences().add(ftr_ref);
               
               
                main_part.addObject(makePageBr());
                // end cover page

                main_part.addStyledParagraphOfText("Heading1", "Overview");
                main_part.addObject(makePageBr());
                main_part.addStyledParagraphOfText("Normal", "Second page");
                main_part.addObject(makePageBr());
               
                pkg.save(new java.io.File("D:/Public/Documents/NextReports/header_test.docx"));
        }
}

