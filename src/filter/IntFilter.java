package filter;

import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 18, 2005 Time: 10:51:27 AM To change this template use
 * File | Settings | File Templates.
 */
public class IntFilter extends DocumentFilter {

    // same with PatternFilter ->  [\d-]*
    // or (-)?[\d]* for - only on the start

    public void insertString(DocumentFilter.FilterBypass fb, int offset,
                             String string, AttributeSet attr)
            throws BadLocationException {

        int leng = fb.getDocument().getLength();
        if ((leng == 0) && (string.indexOf("0") == 0)) {
            throw new BadLocationException("Zero not allowed on first position.", offset);
        }

        StringBuffer buffer = new StringBuffer(string);
        for (int i = buffer.length() - 1; i >= 0; i--) {
            char ch = buffer.charAt(i);
            if (!Character.isDigit(ch) && ch != '-') {
                buffer.deleteCharAt(i);
            }
        }
        super.insertString(fb, offset, buffer.toString(), attr);
    }

    public void replace(DocumentFilter.FilterBypass fb,
                        int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        if (length > 0) {
            fb.remove(offset, length);
        }
        insertString(fb, offset, string, attr);
    }

}