package filter;

import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 18, 2005 Time: 11:14:02 AM To change this template use
 * File | Settings | File Templates.
 */
public class CopyrightFilter extends DocumentFilter {

    public void replace(DocumentFilter.FilterBypass fb,
                        int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        if (length > 0) fb.remove(offset, length);
        insertString(fb, offset, string, attr);
    }

    public void insertString(DocumentFilter.FilterBypass fb,
                             int offset, String string, AttributeSet attr) throws BadLocationException {
        fb.insertString(offset, string, attr);
        if (string.length() == 1) {
            Document d = fb.getDocument();
            String text = d.getText(0, d.getLength());
            String pat = "(c)";
            String rep = "©";
            int pos = text.lastIndexOf(pat, offset + 1);
            if (offset + 1 - pos == pat.length()) {
                fb.remove(pos, pat.length());
                fb.insertString(pos, rep, attr);
            }
        }
    }
}
