package toolbar;

import javax.swing.*;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 11:49:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        //frameanimation.getContentPane().setLayout(new GridBagLayout());
        //frameanimation.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
        //        GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");

        JToolBar toolBar = new JToolBar(" - Toolbar - ");

        JLabel label = new JLabel("Font");
        toolBar.add(label);
        String fonts[ ] = {"Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput"};
        toolBar.addSeparator();
        JComboBox combo = new JComboBox(fonts);
        combo.setPreferredSize(new Dimension(100, 20));
        toolBar.add(combo);
        toolBar.setMaximumSize(toolBar.getSize(  ));
        toolBar.setUI((BasicToolBarUI)AquaToolBarUI.createUI(null));
        //toolBar.setUI(new CustomToolBarUI());

        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.setVisible(true);
    }
}
