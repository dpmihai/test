package filter;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 18, 2005 Time: 11:22:26 AM To change this template use
 * File | Settings | File Templates.
 */

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PatternFilter extends DocumentFilter {

    private Pattern pattern;

    /**
     * Constructor
     * Useful for every kind of input validation !
     * This is the insert pattern
     * If we use the filter for text components, the pattern must contain all subpatterns so we can
     * enter characters into that component!
     * @param pat pattern string
     */
    public PatternFilter(String pat) {
        pattern = Pattern.compile(pat);
    }

    /**
     * Invoked prior to insertion of text into the
     * specified Document
     *
     * @param fb FilterBypass that can be used to mutate Document
     * @param offset  the offset into the document to insert the content >= 0.
     *    All positions that track change at or after the given location
     *    will move.
     * @param string the string to insert
     * @param attr      the attributes to associate with the inserted
     *   content.  This may be null if there are no attributes.
     * @exception BadLocationException  the given insert position is not a
     *   valid position within the document
     */
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        replace(fb, offset, 0, string, attr);
    }

    /**
     * Invoked prior to removal of the specified region in the
     * specified Document.
     *
     * @param fb FilterBypass that can be used to mutate Document
     * @param offset the offset from the beginning >= 0
     * @param length the number of characters to remove >= 0
     * @exception BadLocationException  some portion of the removal range
     *   was not a valid part of the document.  The location in the exception
     *   is the first bad position encountered.
     */
    public void replace(FilterBypass fb, int offset,
                        int length, String string, AttributeSet attr) throws
            BadLocationException {

        System.out.println("replace");
        //if (length > 0) fb.remove(offset, length);
        String s = "";
        System.out.println(">>offset="+offset);
        System.out.println(">>length="+fb.getDocument().getLength());
        System.out.println(">>string="+string);
        if (offset < fb.getDocument().getLength()-1) {
           s = fb.getDocument().getText(offset, fb.getDocument().getLength());
        }
        System.out.println(">>s="+s);
        String newStr = fb.getDocument().getText(0, offset) + string + s;
        System.out.println("s="+s);
        System.out.println("offset="+offset);
        System.out.println(newStr);
        Matcher m = pattern.matcher(newStr);
        if (m.matches()) {
            fb.replace(0, fb.getDocument().getLength(), newStr, attr);
        } else {
            System.out.println("bad");
        }

    }
}
