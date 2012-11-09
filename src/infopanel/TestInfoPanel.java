package infopanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 2:51:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestInfoPanel {

    public static void main (String[] args) {
        JOptionPane pane =
            new JOptionPane ("The action you have chosen to perform\n is " +
                             "not recommended.", JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog (null, "Warning");
        Container grabbedContent = dialog.getContentPane( );
        JTextArea area =
            new JTextArea ("No, seriously dude, you are about to totally "+
                           "bake your computer, if not your entire " +
                           "network, if you don't bail right now. Think " +
                           "I'm kidding? Would I go to such lengths to " +
                           "provide such an elaborate warning message if " +
                           "I were kidding? No, no, wait… you know " +
                           "what? Go ahead. Click OK and blow everything " +
                           "to kingdom come. See if I care.",
                           5, 40);
        area.setLineWrap (true);
        area.setWrapStyleWord (true);

        JScrollPane scroller =
            new JScrollPane (area,
                             ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        InfoPanel mip = new InfoPanel (grabbedContent, scroller);
        dialog.setContentPane (mip);
        dialog.pack( );
        dialog.setVisible(true);
        // dialog blocks on setVisible (JOptionPane makes it modal)
        System.exit(0);
    }
}
