package filter;

import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class SizeFilter extends DocumentFilter {
    int maxCharacters;
    boolean DEBUG = false;

    // same with PatternFilter with .{0,<maxCharacters>}

    public SizeFilter() {

    }

    public SizeFilter(int maxChars) {
        maxCharacters = maxChars;
    }

    public void insertString(FilterBypass fb, int offs,
                             String str, AttributeSet a)
            throws BadLocationException {
        if (DEBUG) {
            System.out.println("in DocumentSizeFilter's insertString method");
        }

        //This rejects the entire insertion if it would make
        //the contents too long. Another option would be
        //to truncate the inserted string so the contents
        //would be exactly maxCharacters in length.
        if ((fb.getDocument().getLength() + str.length()) <= maxCharacters)
            super.insertString(fb, offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

    public void replace(FilterBypass fb, int offs,
                        int length,
                        String str, AttributeSet a)
            throws BadLocationException {
        if (DEBUG) {
            System.out.println("in DocumentSizeFilter's replace method");
        }
        //This rejects the entire replacement if it would make
        //the contents too long. Another option would be
        //to truncate the replacement string so the contents
        //would be exactly maxCharacters in length.
        if ((fb.getDocument().getLength() + str.length()
                - length) <= maxCharacters)
            super.replace(fb, offs, length, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

}