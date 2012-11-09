package dropdown;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: mihai.panaitescu
 * Date: 07-Jan-2010
 * Time: 11:19:16
 */
public class DropDownTest extends JPanel {
    public static void main(String[] args) {
//        final JButton status = new JButton("Color");
//        final JPanel panel = new ColorSelectionPanel();
//        final DropDownComponent dropdown = new DropDownComponent(status, panel);
//        panel.addPropertyChangeListener("selectedColor",
//
//                new PropertyChangeListener() {
//                    public void propertyChange(PropertyChangeEvent evt) {
//                        dropdown.hidePopup();
//                        status.setBackground((Color) evt.getNewValue());
//                    }
//                });
        List<String> list = new ArrayList<String>();
        list.add("Project");
        list.add("Hours");
        list.add("test");
        DropDownListComponent  dropdown = new DropDownListComponent(list);

        JFrame frame = new JFrame("Drop Down Test");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("North", dropdown);
        frame.getContentPane().add("Center", new JLabel("Drop Down Test"));
        frame.pack();
        frame.setSize(300, 300);
        frame.show();

    }
}
