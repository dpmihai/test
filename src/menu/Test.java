package menu;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 28, 2006
 * Time: 10:49:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        //frameanimation.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
        //        GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");

        ToolTipManager.sharedInstance().setInitialDelay(2000);
        JMenuBar mb = new JMenuBar();

        JMenuItem[] all = new JMenuItem[6];
        all[0] = new JMenuItem("First lonhdsad asdasdfdsfsdfsdfsfsfsf");
        all[0].setToolTipText("Help First");
        all[1] = new JMenuItem("Second");
        all[1].setToolTipText("Help Second");
        all[2] = new JMenuItem("Third");
        all[2].setToolTipText("Help Third");
        all[3] = new JMenuItem("Fourth");
        all[3].setToolTipText("Help Fourth");
        all[4] = new JMenuItem("Fifth");
        all[4].setToolTipText("Help Fifth");
        all[5] = new JMenuItem("Sixth");
        all[5].setToolTipText("Help Sixth");

        JMenuExpand menu = new JMenuExpand("File");

        menu.add(all[0], true);
        menu.add(all[1], false);
        menu.add(all[2], false);
        menu.add(all[3], false);
        menu.add(all[4], true);
        menu.add(all[5], false);
        mb.add(menu);
        frame.setJMenuBar(mb);
        //frameanimation.pack();
        frame.setVisible(true);
    }
}
