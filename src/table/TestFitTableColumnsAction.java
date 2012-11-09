package table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 20, 2006
 * Time: 10:28:50 AM
 */
public class TestFitTableColumnsAction {

    public static void main(String[] args) {

        Object[] colNames = {"Column1", "Columnn2"};
        Object[][] rowData = {{"Long test to see", "Short"}, {"Very long tesst to see", "And"}};

        JTable table = new JTable(rowData, colNames);
        table.registerKeyboardAction(new FitTableColumnsAction()
                , KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK)
                , JComponent.WHEN_FOCUSED);
        JScrollPane scr = new JScrollPane();
        scr.setViewportView(table);
        scr.setPreferredSize(new Dimension(100, 100));
        scr.setMinimumSize(new Dimension(100, 100));

        showFrame(scr);
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

}
