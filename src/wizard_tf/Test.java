package wizard_tf;

import wizard.BootGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 11:29:03 AM
 */
public class Test {

    public static void main(String[] args) {

        List<WPanel> panels = new ArrayList<WPanel>();
		panels.add(new WPanel("Company details", BootGUI._panel1(BootGUI.COMPONENTS_1)));
		panels.add(new WPanel("Install directory", BootGUI._panel1(BootGUI.COMPONENTS_2)));
		panels.add(new WPanel("Components", BootGUI._panel1(BootGUI.COMPONENTS_3)));
		panels.add(new WPanel("Install", BootGUI._panel1(BootGUI.COMPONENTS_4)));

        JWizard wizard = new JWizard(panels);


        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());

        frame.add(wizard, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        

        frame.pack();
        frame.setVisible(true);

    }
}
