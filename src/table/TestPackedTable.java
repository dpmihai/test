package table;

import java.awt.event.KeyEvent;
import java.awt.*;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 24, 2006
 * Time: 5:40:52 PM
 */
public class TestPackedTable {

    public static void main(String[] args) {

        Object[] colNames = {"Column1", "Columnn2"};
        Object[][] rowData = {{"Long test to see", "Short"}, {"Very long tesst to see", "And"}};

        PackedTable table = new PackedTable(rowData, colNames);

        JScrollPane scr = new JScrollPane();
        scr.setViewportView(table);
        scr.setPreferredSize(new Dimension(100, 100));
        scr.setMinimumSize(new Dimension(100, 100));
        table.pack(TablePacker.ALL_ROWS, true);

        showFrame(scr);
    }

    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}
