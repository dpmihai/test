package filter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Oct 20, 2005 Time: 5:05:52 PM
 */
public class TextFieldUtilities {

    /** Set number of digits and decimal digits which can be entered
     *  inside a text field
     *  Numbers can be only positive.
     *
     * @param digits number of digits
     * @param decimalDigits number of decimal digits
     * @param txt text field
     *
     */
    public static void setDigits(int digits, int decimalDigits, JTextField txt) {
        NumberFilter nf = new NumberFilter(digits, decimalDigits, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set number of digits and decimal digits which can be entered
     *  inside a text field
     *  Numbers can be positive or negative.
     *
     * @param digits number of digits
     * @param decimalDigits number of decimal digits
     * @param txt text field
     *
     */
    public static void setDigitsPN(int digits, int decimalDigits, JTextField txt) {
        NumberFilter nf = new NumberFilter(digits, decimalDigits, NumberFilter.POSITIVE_NEGATIVE, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set number of digits and decimal digits which can be entered
     *  inside a text field
     *  Numbers can be positive or negative. Numbers can be only between lowThreshold and highThreshold
     *
     * @param digits number of digits
     * @param decimalDigits number of decimal digits
     * @param lowThreshold number cannot be less than low threshold
     * @param highThreshold number cannot be greater than high threshold
     * @param txt text field
     *
     */
    public static void setDigitsPN(int digits, int decimalDigits, double lowThreshold, double highThreshold, JTextField txt) {
        NumberFilter nf = new NumberFilter(digits, decimalDigits, lowThreshold, highThreshold, NumberFilter.POSITIVE_NEGATIVE, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set number of decimal digits which can be entered
     *  inside a text field
     *  Numbers can be only positive.
     *
     * @param decimalDigits number of decimal digits
     * @param txt text field
     *
     */
    public static void setDecimalDigits(int decimalDigits, JTextField txt) {
        NumberFilter nf = new NumberFilter(12, decimalDigits, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set number of decimal digits which can be entered
     *  inside a text field
     *  Numbers can be positive or negative.
     *
     * @param decimalDigits number of decimal digits
     * @param txt text field
     *
     */
    public static void setDecimalDigitsPN(int decimalDigits, JTextField txt) {
        NumberFilter nf = new NumberFilter(12, decimalDigits, NumberFilter.POSITIVE_NEGATIVE, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /**
     * Set number of integer digits which can be entered inside a text field
     * Numbers can be only positive.
     *
     * @param digits number of decimal digits
     * @param txt           text field
     */
    public static void setIntegerDigits(int digits, JTextField txt) {
        NumberFilter nf = new NumberFilter(digits, txt);
        AbstractDocument doc = (AbstractDocument) txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /**
     * Set number of integer digits which can be entered inside a text field
     * Numbers can be positive or negative.
     *
     * @param digits number of decimal digits
     * @param txt           text field
     */
    public static void setIntegerDigitsPN(int digits, JTextField txt) {
        NumberFilter nf = new NumberFilter(digits, NumberFilter.POSITIVE_NEGATIVE, txt);
        AbstractDocument doc = (AbstractDocument) txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set to enter only digits inside a text field, no decimals allowed
     *  Numbers can be only positive.
     *
     * @param txt text field
     *
     */
    public static void setDigits(JTextField txt) {
        NumberFilter nf = new NumberFilter(12, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Set to enter only digits inside a text field, no decimals allowed
     *  Numbers can be positive or negative.
     *
     * @param txt text field
     *
     */
    public static void setDigitsPN(JTextField txt) {
        NumberFilter nf = new NumberFilter(12, NumberFilter.POSITIVE_NEGATIVE, txt);
        AbstractDocument doc = (AbstractDocument)txt.getDocument();
        doc.setDocumentFilter(nf);
    }

    /** Reset a text field
     * @param txt text field
     */
    public static void reset(JTextField txt) {
        Document doc = txt.getDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
        
}

