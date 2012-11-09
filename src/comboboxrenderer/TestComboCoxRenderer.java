package comboboxrenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2006
 * Time: 5:09:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestComboCoxRenderer extends ComboBoxRenderer{

    public String getRenderedString(Object object) {
        String s = " ";
        try {
            TestObject ar = (TestObject) object;
            if (ar.getId() == null) {
                s = " ";
            } else {
                s = ar.getName();
            }
            setToolTipText(s);
        } catch (ClassCastException ex) {
            // ok, the combo has a prototype display value, which is String
            s = " ";
        }
        return s;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");

        TestObject obj1 = new TestObject(1, "unu");
        TestObject obj2 = new TestObject(2, "doi");
        JComboBox cmb =new JComboBox();
        cmb.addItem(obj1);
        cmb.addItem(obj2);
        cmb.setRenderer(new TestComboCoxRenderer());
        frame.getContentPane().add(cmb, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

        frame.setVisible(true);
    }
}

class TestObject {
    private Integer id;
    private String name;

    public TestObject() {
    }

    public TestObject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
