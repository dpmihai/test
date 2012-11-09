package collapsible;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 11:29:30 AM
 */
public class Test {

    public static void main(String[] args) {

        JPanel panel = new JPanel();       
        JTextField txtField = new JTextField();
        txtField.setPreferredSize(new Dimension(150, 25));
        txtField.setMinimumSize(new Dimension(150, 25));
        panel.add(txtField);
        CollapsiblePanel cp = new CollapsiblePanel(panel);


        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(cp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 200);
        frame.setTitle("Test");
        frame.pack();
        frame.setVisible(true);
    }
}
