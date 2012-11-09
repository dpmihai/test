package filter;

import java.awt.*;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 16, 2005 Time: 2:56:09 PM
 * Updated : January 2006
 * <p/>
 *
 *
 * NumberFilter is a very useful class which allows to enter only numbers, decimal separator and
 * '-' sign inside a text component. The number is formatted on the fly by group separators.
 *
 * It is possible to specify :
 * 1) the maximum number of integer digits
 * 2) the maximum number of fraction digits
 * 3) a low threshold
 * 4) a high threshold
 * 5) a type
 *      POSITIVE for positive numbers or
 *      POSITIVE_NEGATIVE for positive or negative numbers (allowing '-' sign at the first position)
 * 6) locale which will specify decimal and group separators
 *
 * Notes:
 *
 *      1) JFieldText is used to set the caret on text field components which use this filter
 *      2) Take care about what is set on a text field with such a document filter, for example if we
 *         use a text field with 2 decimals and 2 fraction digits and we will try to set with setText()
 *         method a string with more decimals or more fraction digits or not-allowed characters, it
 *         will raise a BadLocationException which is caught by default in setText(), no error will
 *         be shown and the text field will not be updated
 *      3) for input text components
 *              low threshold must be a negative number or zero (if it is for example 5 we won't be able
 *                            to enter from keyboard any number less than 5 -> so we won't be able to
 *                            enter a number like 15, because we cannot enter 1!!)
 *
 *              high threshold must be a postive number or zero (if it is for example -5 we won't be able
 *                            to enter from keyboard any number greater than -5 -> so we won't be able
 *                            to enter a number like -15 because we cannot enter -1!!)
 *
 *              low threshold and high threshold are useful only if we have an interval of values which
 *              contains zero [-a, b]
 *
 *
 * Usage :
 *
 *      NumberFilter nf = new NumberFilter(digits, decimalDigits, txt);
 *      AbstractDocument doc = (AbstractDocument)txt.getDocument();
 *      doc.setDocumentFilter(nf);
 *
 */
public class NumberFilter extends DocumentFilter {

    private char[] chars;                                        // allowed characters
    private char groupingSeparator;
    private char decimalPoint;
    private int limit;                                           // maximum number of integer digits allowed
    private int maxSize;
    private int fractionsDigits;                                 // maximum number of fractional digits allowed
    private int cachedOffset;
    private JTextField txt;
    private static double lowThreshold = -Double.MAX_VALUE;
    private static double highThreshold = Double.MAX_VALUE;
    private static int DEFAULT_LIMIT = 15;                       // default number of integer digits allowed
    private static Locale locale = new Locale("ro", "RO");       // default locale
    private NumberFormat nf = NumberFormat.getInstance(locale);

    private byte type = POSITIVE;
    public static final byte POSITIVE = 1;                       // positive numbers
    public static final byte POSITIVE_NEGATIVE = 2;              // positive and negative numbers


    /**
     * Constructor
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, byte type, Locale locale, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale);
        this.txt = txt;
    }

    /**
     * Constructor
     * POSITIVE numbers
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, Locale locale, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, byte type, Locale locale) {

        if (limit <= 0) {
            throw new IllegalArgumentException("Number of integer digits must be at least 1.");
        }

        if (fractionsDigits < 0) {
            throw new IllegalArgumentException("Number of fractional digits must be positive");
        }

        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null.");
        }

        if ((type != POSITIVE) && (type != POSITIVE_NEGATIVE)) {
            throw new IllegalArgumentException("Invalid type parameter.");
        }

        this.type = type;
        this.locale = locale;
        nf = NumberFormat.getInstance(locale);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
        this.limit = limit;
        this.fractionsDigits = fractionsDigits;
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        int noSeparators;
        if (limit % 3 == 0) {
            noSeparators = limit / 3 - 1;
        } else {
            noSeparators = limit / 3;
        }
        maxSize = limit + noSeparators; // add number of group separators
        nf.setMaximumFractionDigits(fractionsDigits);
        groupingSeparator = dfs.getGroupingSeparator();
        decimalPoint = dfs.getDecimalSeparator();
        if (fractionsDigits == 0) {
            if (this.type == POSITIVE) {
                chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            } else {
                chars = new char[]{'-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            }
        } else {
            if (this.type == POSITIVE) {
                chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', decimalPoint};
            } else {
                chars = new char[]{'-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', decimalPoint};
            }
        }
    }

    /**
     * Constructor
     * POSITIVE numbers
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param locale locale
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, Locale locale) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, byte type, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     */
     public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold, byte type) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     */
     public NumberFilter(int limit, int fractionsDigits, double lowThreshold, double highThreshold) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, double lowThreshold, double highThreshold, byte type, Locale locale, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, double lowThreshold, double highThreshold, Locale locale, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param txt text field component (needed for updating caret position)
     */
     public NumberFilter(int limit, double lowThreshold, double highThreshold, byte type, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param txt text field component (needed for updating caret position)
     */
     public NumberFilter(int limit, double lowThreshold, double highThreshold, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param type POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, byte type, Locale locale, JTextField txt) {
       this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, int fractionsDigits, Locale locale, JTextField txt) {
       this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use no fraction digits
     * @param limit         maximum number of integer digits allowed
     * @param lowThreshold  low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param locale        locale
     */
    public NumberFilter(int limit, double lowThreshold, double highThreshold, byte type, Locale locale) {
        this(limit, 0, lowThreshold, highThreshold, type, locale);
    }


    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param locale locale
     */
     public NumberFilter(int limit, double lowThreshold, double highThreshold, Locale locale) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     */
    public NumberFilter(int limit, int fractionsDigits, byte type, Locale locale) {
       this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param locale locale
     */
    public NumberFilter(int limit, int fractionsDigits, Locale locale) {
       this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     */
     public NumberFilter(int limit, double lowThreshold, double highThreshold, byte type) {
        this(limit, 0, lowThreshold, highThreshold, type, locale);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param lowThreshold low threshold (number cannot be less than low threshold)
     * @param highThreshold high threshold (number cannot be greater than high threshold)
     */
     public NumberFilter(int limit, double lowThreshold, double highThreshold) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param txt text field component (needed for updating caret position)
     */
     public NumberFilter(int limit, int fractionsDigits, byte type, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param txt text field component (needed for updating caret position)
     */
     public NumberFilter(int limit, int fractionsDigits, JTextField txt) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     */
     public NumberFilter(int limit, int fractionsDigits, byte type) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, type, locale);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param fractionsDigits maximum number of fractional digits allowed
     */
     public NumberFilter(int limit, int fractionsDigits) {
        this(limit, fractionsDigits, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, byte type, Locale locale, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, Locale locale, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     */
    public NumberFilter(int limit, byte type, Locale locale) {
        this(limit, 0, lowThreshold, highThreshold, type, locale);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * @param limit maximum number of integer digits allowed
     * @param locale locale
     */
    public NumberFilter(int limit, Locale locale) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale);
    }

    /**
     * Constructor
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, byte type, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(int limit, JTextField txt) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     */
    public NumberFilter(int limit, byte type) {
        this(limit, 0, lowThreshold, highThreshold, type, locale);
    }


    /**
     * Constructor
     * POSITIVE numbers
     * Use no fraction digits
     * Use RO locale
     * @param limit maximum number of integer digits allowed
     */
    public NumberFilter(int limit) {
        this(limit, 0, lowThreshold, highThreshold, POSITIVE, locale);
    }

     /**
     * Constructor
     * Use DEFAULT_LIMIT integer digits
     * Use no fraction digits
     * Use RO locale
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(byte type, JTextField txt) {
        this(DEFAULT_LIMIT, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use DEFAULT_LIMIT integer digits
     * Use no fraction digits
     * Use RO locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(JTextField txt) {
        this(DEFAULT_LIMIT, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }

    /**
     * Constructor
     * Use DEFAULT_LIMIT integer digits
     * Use no fraction digits
     * @param type          POSITIVE or POSITIVE_NEGATIVE
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(byte type, Locale locale, JTextField txt) {
        this(DEFAULT_LIMIT, 0, lowThreshold, highThreshold, type, locale, txt);
    }

    /**
     * Constructor
     * POSITIVE numbers
     * Use DEFAULT_LIMIT integer digits
     * Use no fraction digits
     * @param locale locale
     * @param txt text field component (needed for updating caret position)
     */
    public NumberFilter(Locale locale, JTextField txt) {
        this(DEFAULT_LIMIT, 0, lowThreshold, highThreshold, POSITIVE, locale, txt);
    }


    // This method is called when characters are inserted into the document
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String str,
                             AttributeSet attr) throws BadLocationException {
        int len = fb.getDocument().getLength();
        cachedOffset = offset;
        replace(fb, 0, len, str, attr);
    }

    // This method is called when characters are removed from the document
    public void remove(FilterBypass fb, int offset, int length) throws
            BadLocationException {
        int len = fb.getDocument().getLength();
        cachedOffset = offset;
        String text = fb.getDocument().getText(0, len);
        String start = text.substring(0, offset);
        String s = start + text.substring(offset + length);

        double d = 0;
        // if last character is "-", we do not have a number for parsing
        // this is necessary so the "-" character not be deleted when it is lonely
        if (!s.equals("-")) {
            try {
                d = nf.parse(getWithoutSeparator(s)).doubleValue();
            } catch (ParseException nfe) {
                // can appear if we remove all digits
                fb.replace(0, len, "", null);
                return;
            }

            if ((d < lowThreshold) || (d > highThreshold)) {
                throw new BadLocationException("Number exceeds threshold interval", offset);
            }
        }

        String str;
        if (!s.equals("-")) {
            str = nf.format(d);
        } else {
            str = s;
        }

        // test to see that after removing the decimal separator, not to have a bigger number of integer digits than
        // LIMIT
        int index = str.indexOf(decimalPoint);
        if (index == -1) {
            int k = getWithoutSeparator(str).length();
            if (str.startsWith("-")) {
                k--;
            }
            if (k > limit) {
                throw new BadLocationException("Deleting decimal separator impossible", offset);
            }
        }

        String end;
        if (s.endsWith(String.valueOf(decimalPoint))) {
            end = String.valueOf(decimalPoint);
        } else {
            end = toAdd(s);
        }
        str += end;

        int oldNo = getDecimalPoints(text, offset);
        int newNo = getDecimalPoints(str, offset);
        int delta = oldNo - newNo;

        fb.replace(0, len, str, null);
        if (offset < len) {
            if (txt != null) {
                txt.setCaretPosition(offset - delta);
            }
        }
    }

    // This method is called when characters in the document are replace with other characters
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
                        String str, AttributeSet attrs) throws BadLocationException {

        cachedOffset = offset;
        if (!verify(str)) return;

        // testing minus sign : allow it only on the first position
        int leng = fb.getDocument().getLength();
        String t = fb.getDocument().getText(0, leng);
        if (t.substring(0, offset).length() == 0) {
            int i= str.indexOf('-');
            if (i != -1) {
                if (i > 0) {
                    throw new BadLocationException("Minus sign not allowed here.", offset);
                }
            }
        } else {
            if (str.indexOf('-') != -1) {
               throw new BadLocationException("Minus sign not allowed here.", offset);
            }
        }


        /************** REPLACE ***********************/
        // length > 0 means a replace
        // insert also uses this method (length = 0)
        if (length > 0) {
            // test replace is possible
            // do not exceed decimal part or fractional part!
            int len = fb.getDocument().getLength();
            String text = fb.getDocument().getText(0, len);
            text = text.substring(0, offset) + str + text.substring(offset+length);
            String clean = getWithoutSeparator(text);
            int pos = clean.indexOf(decimalPoint);
            String dec = "";
            String frac = "";
            if (pos >= 0) {
                dec = clean.substring(0, pos);
                frac = clean.substring(pos);
            } else {
                dec = clean;
            }
            if (dec.length() > limit) {
               throw new BadLocationException("New characters exceeds max size of decimal part", offset);
            } else if (frac.length() > fractionsDigits+1) {
                throw new BadLocationException("New characters exceeds max size of fractional part", offset);
            }
            fb.replace(offset, length, str, attrs);
        }

        int len = fb.getDocument().getLength();
        String s = fb.getDocument().getText(0, len);
        int i = s.indexOf(decimalPoint);
        boolean nDigits = false;

        if (i != -1) {
            // do not allow two decimal points
            if (str.equals(String.valueOf(decimalPoint))) {
                return;
            }
            // we have two fraction digits
            if ((i + fractionsDigits + 1) == s.length()) {
                nDigits = true;
            }
        }

        String text;
        if (length > 0) {
            text = s; //replace
        } else {
            text = s.substring(0, offset) + str + s.substring(offset); // insert
        }
        
        if (length == 0) {
            // test for insert not to enter other fractional digit after the maximum number
            // of fractional digits
            // without this code when we enter 9 on the last fract. digit, entering other digit will
            // make the number to be rounded!
            int p = text.indexOf(decimalPoint);
            if (p > 0) {
                // do not take into consideration other fraction digit
               if (text.substring(p).length() > fractionsDigits+1) {
                   text = text.substring(0, text.length()-1);
               }
            }
        }

        double d = 0;
        // if only minus sign is present, we do not have a number for parsing
        if (!text.equals("-")) {
            try {
                d = nf.parse(getWithoutSeparator(text)).doubleValue();
            } catch (ParseException nfe) {
                // can appear if we enter multiple points!
                return;
            }
        }

        if ((d < lowThreshold) || (d > highThreshold)) {
            throw new BadLocationException("Number exceeds threshold interval", offset);
        }

        // keep the point to be shown
        boolean decimal = false;
        if (str.indexOf(decimalPoint) != -1) {
            decimal = true;
        }

        // if only minus sign is present
        if (text.equals("-")) {
            str = "-";
        } else {
            str = nf.format(d);
        }
        if (decimal) {
            if (str.indexOf(decimalPoint) == -1) {
                str += decimalPoint;
            }
        }

        String add = toAdd(text);
        // this test was needed because when we pasted 0,00
        // we obtained in textfield 0,,00
        if (str.endsWith(String.valueOf(decimalPoint)) && add.startsWith(String.valueOf(decimalPoint))) {
            str += add.substring(1);
        } else {
            str += add;
        }

        int newLength = str.length();
        int index = str.indexOf(decimalPoint);
        boolean withFractionDigits = false;
        if (index != -1) {
            withFractionDigits = true;
        }


        int totalSize = maxSize;
        // for negative numbers we allow another character ("-")
        if(t.startsWith("-") || str.startsWith("-")) {
            totalSize += 1;
        }
        // for fraction digits we allow another (fractionDigits+1) characters
        if (withFractionDigits) {
            totalSize += (fractionsDigits + 1);
        }

        if (newLength <= totalSize) {
            // if we have fractionDigits digits after decimal point we do not allow to enter other digits
            // without this code, if we enter a digit after fractionDigits digits, the number will be rounded!
            if (withFractionDigits) {
                if (nDigits && (index + fractionsDigits + 1) == str.length()) {
                    // insert after decimal point
                    if (offset > index) {
                        throw new BadLocationException("New characters exceeds max size of document", offset);
                    }
                } else {
                }
            }

            fb.replace(0, len, str, attrs);
            if (offset < len - 1) {
                int oldNo = getDecimalPoints(s, offset);
                int newNo = getDecimalPoints(str, offset);
                int pos = offset + 1 + (newNo - oldNo);
                if (txt != null) {
                    txt.setCaretPosition(pos);
                }
            }
        } else {
            throw new BadLocationException("New characters exceeds max size of document", offset);
        }
    }

    private boolean verify(String s) {
        char[] sc = s.toCharArray();
        for (int i = 0, size = sc.length; i < size; i++) {
            for (int j = 0; j < chars.length; j++) {
                if (sc[i] == chars[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private String addSeparator(String s) {
        int len = s.length();
        StringBuffer reverse = new StringBuffer(s).reverse();
        StringBuffer result = new StringBuffer();

        // after every 3 characters add a separator
        int j = 0;
        for (int i = 0; (i + 3) < len; j = i + 3, i = i + 3) {
            result.append(reverse.substring(i, i + 3));
            result.append(String.valueOf(groupingSeparator));
        }

        // add last characters
        if (j <= len) {
            result.append(reverse.substring(j));
        }

        return result.reverse().toString();
    }

    // after a format ending zeros and maybe the decimalPoint are removed
    // this method obtain these characters if any
    private String toAdd(String s) {

        int index = s.indexOf(decimalPoint);
        if (index == -1) {
            return "";
        }

        s = s.substring(index);

        if (s.length() > fractionsDigits + 1) {
            s = s.substring(0, fractionsDigits + 1);
        } else if (!s.endsWith("0")) {
            return "";
        }

        StringBuffer sb = new StringBuffer(s);
        StringBuffer reverse = sb.reverse();
        StringBuffer r = new StringBuffer();
        for (int i = 0, size = reverse.length(); i < size; i++) {
            char c = sb.charAt(i);
            if ((c == decimalPoint) || (c == '0')) {
                r.append(c);
            } else {
                break;
            }
        }

        String result = r.reverse().toString();

        int allLen = s.length();
        int len = result.length();
        if ((allLen > fractionsDigits + 1)) {
            result = result.substring(0, fractionsDigits + 1 - allLen + len);
        }

        return result;
    }

    public String getWithoutSeparator(String s) {
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s, String.valueOf(groupingSeparator));
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        final JTextField txt = new JTextField();
        txt.setMinimumSize(new Dimension(100, 20));
        NumberFilter nf = new NumberFilter(8, 2, NumberFilter.POSITIVE_NEGATIVE, txt);
        ((AbstractDocument) txt.getDocument()).setDocumentFilter(nf);

        frame.getContentPane().add(txt, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }

    public int getOffset() {
        return cachedOffset;
    }

    private int getDecimalPoints(String s, int offset) {
        int size = s.length();
        if (offset >= size) {
            offset = size;
        }
        String st = s.substring(0, offset);
        int result = 0;
        for (int i = 0, len = st.length(); i < len; i++) {
            char c = st.charAt(i);
            if (c == groupingSeparator) {
                result++;
            }
        }
        return result;
    }

}
