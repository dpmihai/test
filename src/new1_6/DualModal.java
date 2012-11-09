package new1_6;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 1, 2007
 * Time: 4:27:22 PM
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DualModal {
    public static void main(String args[]) {
        Runnable runner = new Runnable() {
            public void run() {
                JFrame frame1 = new JFrame("Left");
                JFrame frame2 = new JFrame("Right");
                frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton button1 = new JButton("Left");
                JButton button2 = new JButton("Right");
                frame1.add(button1, BorderLayout.CENTER);
                frame2.add(button2, BorderLayout.CENTER);
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton source = (JButton) e.getSource();
                        String text = getNewText(source);
                        if (!JOptionPane.UNINITIALIZED_VALUE.equals(text) &&
                                text.trim().length() > 0) {
                            source.setText(text);
                        }
                    }
                };
                button1.addActionListener(listener);
                button2.addActionListener(listener);
                frame1.setBounds(100, 100, 200, 200);
                frame1.setVisible(true);
                frame2.setBounds(400, 100, 200, 200);
                frame2.setVisible(true);
            }
        };
        EventQueue.invokeLater(runner);
    }

    private static String getNewText(Component parent) {
        JOptionPane pane = new JOptionPane(
                "New label", JOptionPane.QUESTION_MESSAGE
        );
        pane.setWantsInput(true);
        JDialog dialog = pane.createDialog(parent, "Enter Text");
// Uncomment line and comment out next to see application modal
// dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setVisible(true);
        return (String) pane.getInputValue();
    }
}