package filter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.text.AbstractDocument;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Mar 14, 2005 Time: 1:06:14 PM To change this template use File
 * | Settings | File Templates.
 */
public class InputDialog extends JDialog {

    private final String OK = "Ok";
    private final String CANCEL = "Cancel";

    final private JTextField txt = new JTextField();;
    private JLabel lbl;
    private JButton btnOk = new JButton(OK);;
    private JButton btnCancel = new JButton(CANCEL);;
    private String lblText;
    private String pattern;
    private int length;

    private Dimension dim = new Dimension(200, 100);

    /**
     * Constructor : An input dialog with label, text field, ok and cancel buttons
     * Ok button will become active only if the input is matched by the specified
     * pattern and it is fully constructed (has the length specified) 
     *
     * @param parent parent frameanimation
     * @param title dialog title
     * @param lblText label text
     * @param pattern pattern used for input validation
     * @param length length of input characters
     * @param modal true for modal dialog
     */
    public InputDialog(Frame parent, String title, String lblText, String pattern, int length, boolean modal) {
        super(parent, title, modal);
        this.lblText = lblText;
        this.pattern = pattern;
        this.length = length;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        lbl = new JLabel(lblText);

        btnOk.setEnabled(false);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText("");
                dispose();
            }
        });
        AbstractDocument doc = (AbstractDocument) txt.getDocument();
        doc.setDocumentFilter(new PatternFilter(pattern));
        txt.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int size = txt.getText().length();
                if (size == length) {
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setEnabled(false);
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });

        this.setSize(dim);
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(lbl, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
        this.getContentPane().add(txt, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(btnOk);
        btnPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        btnPanel.add(btnCancel);
        btnPanel.add(Box.createHorizontalGlue());

        this.getContentPane().add(btnPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    }

    /**
     * Get input
     * @return input
     */
    public String getInput() {
        return txt.getText();
    }

    /**
     * Get preferred size
     * @return preferred size
     */
    public Dimension getPreferredSize() {
        return dim;
    }

    /** Get minimum size
     * @return minimum size
     */
    public Dimension getMinimumSize() {
        return dim;
    }

    /**
     * Resize dialog
     */
    private void resize() {
        Dimension d = getSize();
        Dimension min = getMinimumSize();
        if (d.width < min.width) {
            d.width = min.width;
        }
        if (d.height < min.height) {
            d.height = min.height;
        }
        setSize(d);
    }
}
