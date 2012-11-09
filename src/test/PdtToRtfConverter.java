package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class PdtToRtfConverter {

	public static void main(String[] args) throws IOException {
		try {
			Document document = new Document();
			document.open();
			PdfReader reader = new PdfReader("C:\\Users\\mihai.panaitescu\\.nextreports-5.3\\reports\\t.pdf");
			PdfDictionary dictionary = reader.getPageN(1);
			PRIndirectReference reference = (PRIndirectReference) dictionary.get(PdfName.CONTENTS);
			PRStream stream = (PRStream) PdfReader.getPdfObject(reference);
			byte[] bytes = PdfReader.getStreamBytes(stream);
			PRTokeniser tokenizer = new PRTokeniser(bytes);
			FileOutputStream fos = new FileOutputStream("C:\\Users\\mihai.panaitescu\\.nextreports-5.3\\reports\\t.rtf");
			StringBuffer buffer = new StringBuffer();
			while (tokenizer.nextToken()) {
				if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
					buffer.append(tokenizer.getStringValue());
				}
			}
			String test = buffer.toString();
			StringReader stReader = new StringReader(test);
			int t;
			while ((t = stReader.read()) > 0)
				fos.write(t);
			document.close();
			System.out.println("Converted Successfully");
		} catch (Exception e) {
		}
	}
}