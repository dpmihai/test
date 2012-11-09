package toolbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 7, 2006
 * Time: 11:48:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestToolbar {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");

        JToolBar toolBar = new JToolBar(" - Toolbar - ");
        toolBar.addSeparator();
        toolBar.add(new JButton(new ToolbarButtonAction("Test1")));
        toolBar.add(new JButton(new ToolbarButtonAction("Test2")));
        toolBar.add(new JButton(new ToolbarButtonAction("Test3")));
        toolBar.addSeparator();
        toolBar.add(new JButton(new ToolbarButtonAction("Test4")));
        ToolbarUtil.setButtonsPreferredSize(toolBar, new Dimension(50,20));
        toolBar.setMaximumSize(toolBar.getSize());

        frame.getContentPane().add(ToolbarUtil.createExtendedToolBar(toolBar), BorderLayout.NORTH);
        frame.setVisible(true);
    }
}
