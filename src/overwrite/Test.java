package overwrite;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 1, 2005 Time: 10:58:32 AM
 */
public class Test {

    public static void main(String[] args) {
        OverwritePane op = new OverwritePane();
        JScrollPane scr = new JScrollPane();
        scr.setPreferredSize(new Dimension(200, 300));
        scr.setViewportView(op);
        showFrame(scr);
    }

    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 200);
        frame.setTitle("Test");
        frame.pack();
        frame.setVisible(true);
    }
}
