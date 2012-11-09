package validation;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 10:56:44 AM
 */
public class Test {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        final JTextField textField = new JTextField();
        int len = 3;

        final AbstractValidator v = new LengthValidator(new JDialog(), textField, "Field length less than " + len +" .", len);
        textField.setInputVerifier(v);
        textField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                v.verify(textField);
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                v.verify(textField);
            }
            public void componentResized(ComponentEvent e) {
                v.verify(textField);
            }
        });

        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(textField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 200);
        frame.setTitle("Test");
        frame.pack();
        frame.setVisible(true);


    }
}
