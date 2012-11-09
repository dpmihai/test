package table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 12, 2007
 * Time: 4:37:24 PM
 */
public class TestEditor {
    public static void main(String[] args) {

        DefaultTableModel model = new DefaultTableModel() {
            // This method returns the Class object of the first
            // cell in specified column in the table model.
            // Unless this method is overridden, all values are
            // assumed to be the type Object.
            public Class getColumnClass(int mColIndex) {
                int rowIndex = 0;
                Object o = getValueAt(rowIndex, mColIndex);
                if (o == null) {
                    return Object.class;
                } else {
                    return o.getClass();
                }
            }
        };
        JTable table = new JTable(model);
        JScrollPane scr = new JScrollPane(table);

        model.addColumn("Color", new Object[]{Color.red});
        model.addColumn("Text", new Object[]{"Function"});
        model.addRow(new Object[]{Color.green, "MIN"});
        model.addRow(new Object[]{Color.blue, "MAX"});

        TableColumn column1 = table.getColumn(table.getColumnName(1));
        column1.setCellEditor(new StringActionTableCellEditor(new DefaultCellEditor(new JTextField())));

        showFrame(scr);
    }

    public static void showFrame(JComponent comp) {
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new GridBagLayout());
            frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 100);
            frame.setLocation(400, 400);
            frame.setTitle("Test");
            //frameanimation.pack();
            frame.setVisible(true);
        }

}
