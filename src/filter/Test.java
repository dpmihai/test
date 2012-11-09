package filter;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.NumberFormatter;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 17, 2005 Time: 11:29:20 AM To change this template use
 * File | Settings | File Templates.
 */
public class Test {

    public static void format(double t) {
        DecimalFormat form = new DecimalFormat("#0.00");
        System.out.println(form.format(t));
    }


    public static void filter() {
        JTextField tf = new JTextField();
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        doc.setDocumentFilter(new CopyrightFilter());
        showFrame(tf);
    }

    public static void filter2() {
        JFormattedTextField intField = new
                JFormattedTextField(new
                        InternationalFormatter(NumberFormat.getIntegerInstance()) {

                            protected DocumentFilter getDocumentFilter() {
                                return filter;
                            }

                            //private DocumentFilter filter = new IntFilter();
                            private DocumentFilter filter = new PatternFilter("(-)?[\\d]*");
                        });
        //JFormattedTextField intField = new JFormattedTextField();
        //((AbstractDocument)intField.getDocument()).setDocumentFilter(new IntFilter());
        showFrame(intField);
    }

    public static void filter3() {
        // ##-##-#### and all subpatterns
        String pattern = "\\d(\\d(-(\\d(\\d(-(\\d{0,4}))?)?)?)?)?";

        //String pattern =  "\\d(\\d*.?\\d*)?";
        JTextField tf = new JTextField();
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        doc.setDocumentFilter(new PatternFilter(pattern));
        showFrame(tf);
    }

    public static void filterTest() {
        // ##-##-#### and all subpatterns
        String pattern = "\\d(\\d(?)?)?";

        JTextField tf = new JTextField();
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        doc.setDocumentFilter(new PatternFilter(pattern));
        showFrame(tf);
    }

    public static void filter4() {
        JTextField tf = new JTextField();
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        //doc.setDocumentFilter(new SizeFilter(4) );
        doc.setDocumentFilter(new PatternFilter(".{0,4}"));
        showFrame(tf);
    }

    public static void filter6() {

        final JTextField tf = new JTextField();
        final DocumentFilter PATTERN = new PatternFilter("(-)?\\d{0,12}(,\\d{0,2})?");

        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        doc.setDocumentFilter(PATTERN);

        final NumberFormat nf = NumberFormat.getInstance(new Locale("ro", "RO"));
        tf.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    System.out.println("getText=" + tf.getText());
                    Number n = nf.parse(tf.getText());
                    String s = nf.format(n.doubleValue());
                    AbstractDocument doc = (AbstractDocument) tf.getDocument();
                    doc.setDocumentFilter(new PatternFilter(".*"));
                    tf.setText(s);
                    System.out.println(s);
                    doc.setDocumentFilter(PATTERN);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

            }
        });
        showFrame(tf);
    }

     public static void filterN() {
        JTextField tf = new JTextField();
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        //doc.setDocumentFilter(new SizeFilter(4) );
        //String s = "-?(\\d*(,?\\d{0,2})?)?";
        //String s = "-(\\d(\\d*(,?\\d{0,2})?)?)?|\\d(\\d*(,?\\d{0,2})?)?";
         String s = "\\d*";
        doc.setDocumentFilter(new PatternFilter(s));
        showFrame(tf);
    }

    public static void filterFormat() {

        NumberFormat decimalFormat = DecimalFormat.getInstance(new Locale("ro", "RO"));
        decimalFormat.setMaximumFractionDigits(2);
        if (decimalFormat instanceof DecimalFormat) {
            ((DecimalFormat) decimalFormat).setDecimalSeparatorAlwaysShown(true);
        }

        NumberFormatter textFormatter = new NumberFormatter(decimalFormat);

        textFormatter.setOverwriteMode(true);
        textFormatter.setAllowsInvalid(false);
        JFormattedTextField tf = new JFormattedTextField(textFormatter);
        showFrame(tf);
    }


    public static void filter5() {
        // ##/#### and all subpatterns
        String pattern = "0([1-9](/(\\d{0,4})?)?)?|1([0-2](/(\\d{0,4})?)?)?";

        final JTextField tf = new JTextField();
        tf.setToolTipText("MM/YYYY");
        final JButton ok = new JButton("Ok");
        ok.setEnabled(false);
        AbstractDocument doc = (AbstractDocument) tf.getDocument();
        doc.setDocumentFilter(new PatternFilter(pattern));
        tf.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int size = tf.getText().length();
                if (size == 7) {
                    ok.setEnabled(true);
                } else {
                    ok.setEnabled(false);
                }
            }
        });

        showFrame(tf, ok);
    }

    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }

    public static void showFrame(JComponent comp, JButton ok) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.getContentPane().add(ok, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 150);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        //filter5();

//        MYInputDialog d = new MYInputDialog(new JFrame(), "Test", true);
//        d.show();

        filterN();       
    }

}
