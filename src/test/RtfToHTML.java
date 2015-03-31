package test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

public class RtfToHTML {
	
	public static void main(String[] args) {
		String rtfText = "{\\rtf1\\ansi" +
						"\n{\\fonttbl\\f0\\fnil Monospaced;\\f1\\fnil Dialog;}" +
						"\n{\\stylesheet{\\s2\\f1\\fs24\\i0\\b0\\cf0 default;}{\\s1\\li0\\ri0\\fi0\\sbasedon2 default;}}" +
						"\n\\margl1273\\margr1273\\margt1417\\margb1134" +
						"\n\\s1\\li0\\ri0\\fi0\\qc\\sa0\\sb0\\f1\\fs24\\i0\\b0\\ul0\\sl0\\cf0 centered\\par" +
						"\n\\ql left\\par" +
						"\n\\qr right\\par" +
						"\n\\pard\\ql\\par" +
						"\n\\li0\\ri0\\fi0\\ul0\\sl0\\par" +
						"}";
		try {
			System.out.println(rtfToHtml(new StringReader(rtfText)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String rtfToHtml(Reader rtf) throws IOException {
		JEditorPane p = new JEditorPane();
		p.setContentType("text/rtf");
		EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
		try {
			kitRtf.read(rtf, p.getDocument(), 0);
			kitRtf = null;
			EditorKit kitHtml = p.getEditorKitForContentType("text/html");
			Writer writer = new StringWriter();
			kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
			return writer.toString();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

}
